package com.tobeto.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tobeto.dto.shelfProduct.response.GetAllProductsFromShelvesResponseDTO;
import com.tobeto.entities.warehouse.Category;
import com.tobeto.entities.warehouse.Product;
import com.tobeto.entities.warehouse.ShelfProduct;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.warehouse.CategoryRepository;
import com.tobeto.repository.warehouse.ProductRepository;
import com.tobeto.repository.warehouse.ShelfProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ShelfProductRepository shelfProductRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<GetAllProductsFromShelvesResponseDTO> getAllProductsFromShelves() {
		List<Object[]> products = shelfProductRepository.getAllProductsFromShelves();
		List<GetAllProductsFromShelvesResponseDTO> allProducts = new ArrayList<>();

		for (Object[] product : products) {
			UUID productId = (UUID) product[0]; // Ürün ID'si
			long unitInStock = (long) product[1]; // Ürün adedi
			String name = (String) product[2]; // Ürün adı

			GetAllProductsFromShelvesResponseDTO allProduct = new GetAllProductsFromShelvesResponseDTO(productId,
					unitInStock, name);
			allProducts.add(allProduct);
		}

		return allProducts;
	}

	public Product addProduct(Product product) {
		Optional<Category> oCategory = categoryRepository.findById(product.getCategory().getId());
		if (oCategory.isPresent()) {
			product.setCategory(oCategory.get());
		}

		return productRepository.save(product);
	}

	public Product updateProduct(Product product) {
//		Optional<Category> oCategory = categoryRepository
//				.findById(product.getCategory().getId());
//		if(oCategory.isPresent()) {
//			product.setCategory(oCategory.get());
//		}

		Optional<Product> oProduct = productRepository.findById(product.getId());
		if (oProduct.isPresent()) {
			product.setCategory(oProduct.get().getCategory());
		}
		return productRepository.save(product);
	}

	public void deleteProduct(UUID id) {
		productRepository.deleteById(id);
	}

	///////////////// BURADAN SONRASI EKLENDİ

	public Product getProduct(UUID productId) { // getFruit
		Optional<Product> oProduct = productRepository.findById(productId);
		Product product = null;
		if (oProduct.isPresent()) {
			product = oProduct.get();
		} else {
			// product bulunamadı. hata ver
			throw new ServiceException(ERROR_CODES.PRODUCT_NOT_FOUND);
		}
		return product;
	}

	// Yeni metod: Stok miktarını arttır
	public void increaseProductStock(UUID productId, int count) {
		Product product = getProduct(productId);
		int currentStock = product.getUnitInStock();

		// Güncel stok miktarından azaltılacak miktarı çıkar
		int newStock = currentStock + count;

		// Negatif stok miktarını önlemek için kontrol
		if (newStock < 0) {
			throw new ServiceException(ERROR_CODES.NOT_ENOUGH_SHELF); // hata
																		// kodundan
																		// emin
																		// değilim
		}

		// Yeni stok miktarını güncelle
		product.setUnitInStock(newStock);
		productRepository.save(product);
	}

	// depodan ürün gönderimi için aşağıdaki kısımları yazdım

	// öncelikle gelen id li ürünün raflarda olup olmadığını
	// kontrol eden metodu yazdım

	private ShelfProduct getProductFromShelf(UUID productId) {
		Optional<ShelfProduct> oProduct = shelfProductRepository.findFirstByProductId(productId);
		ShelfProduct product = null;
		if (oProduct.isPresent()) {
			product = oProduct.get();
		} else {
			// product bulunamadı. hata ver
			throw new ServiceException(ERROR_CODES.PRODUCT_NOT_FOUND);
		}
		return product;
	}

	// Yeni metod: Stok miktarını azaltır.
	public void decreaseProductStock(UUID productId, int count) {
		Product product = getProduct(productId);
		int currentStock = product.getUnitInStock();
		int currentQuantity = product.getQuantity();

		// Güncel stok miktarına azaltılacak miktarı çıkar
		int newStock = currentStock - count;
		int newQuantity = currentQuantity - count;

		// Negatif stok miktarını önlemek için kontrol
		if (newStock < 0) {
			throw new ServiceException(ERROR_CODES.NOT_ENOUGH_STOCK);
		}

		// Yeni stok miktarını güncelle
		product.setUnitInStock(newStock);
		product.setQuantity(newQuantity);
		productRepository.save(product);
	}

	@Transactional
	public void dispatchProduct(UUID productId, int count) {

		ShelfProduct product = getProductFromShelf(productId);
		// increaseProductStock(productId, count); -> decrese

		Optional<ShelfProduct> oShelf = shelfProductRepository.findByProductIdNotFull(productId);
		if (oShelf.isPresent()) {

			// yarı dolu raf bulundu.
			// gönderim öncelikli olarak bu raf içinden yapılacak.

			ShelfProduct shelf = oShelf.get();
			int dispatchCount = count;
			int productCount = shelfProductRepository.findProductCountByShelfIdAndProductId(shelf.getShelf().getId(),
					productId);

			if (dispatchCount > productCount) {

				dispatchCount = productCount;

			}

			shelf.setProductCount(productCount - dispatchCount);
			if (shelf.getProductCount() == 0) {

				// bu raftaki bu ürün bitti

				shelfProductRepository.deleteProductFromShelf(shelf.getShelf().getId(), productId);

			}

			decreaseProductStock(productId, dispatchCount);
			shelfProductRepository.save(shelf);
			count -= dispatchCount;

		}

		// gönderim yapılacak product kaldıysa diğer tam dolu raflardan gönderim
		// devam edecek

		if (count > 0) {

			dispatchFromFullShelf(count, product);
			decreaseProductStock(productId, count);

		}

	}

	private void dispatchFromFullShelf(int count, ShelfProduct shelfProduct) {

		List<ShelfProduct> fullShelves = shelfProductRepository
				.findByProductIdAndProductCountGreaterThan(shelfProduct.getProduct().getId(), 0);

		int nextFullShelf = fullShelves.size() - 1;

		while (count > 0) {

			if (nextFullShelf < 0) {

				throw new ServiceException(ERROR_CODES.PRODUCT_NOT_FOUND);

			}

			ShelfProduct shelf = fullShelves.get(nextFullShelf);

			int dispatchAmount = count;
			int shelfProductCount = shelfProductRepository
					.findProductCountByShelfIdAndProductId(shelf.getShelf().getId(), shelfProduct.getProduct().getId());
			if (dispatchAmount > shelfProductCount) {
				dispatchAmount = shelfProductCount;
			}

			shelf.setProductCount(shelfProductCount - dispatchAmount);
			if (shelf.getProductCount() == 0) {
				// bu raftaki bu ürün bitti

				shelfProductRepository.deleteProductFromShelf(shelf.getShelf().getId(),
						shelfProduct.getProduct().getId());

			}

			shelfProductRepository.save(shelf);
			count -= dispatchAmount;
			nextFullShelf--;

		}

	}

	@Transactional
	public ByteArrayOutputStream transferProductsToReportAndGeneratePDFAllProducts() {
		List<Product> products = productRepository.findAll();
		return generatePDF(products);
	}

	@Transactional
	public ByteArrayOutputStream transferProductsToReportAndGeneratePDFWarningCount() {
		List<Product> products = productRepository.findAll();
		List<Product> warningProducts = new ArrayList<>();
		for (Product product : products) {
			if (product.getMinimumCount() >= product.getQuantity()) {
				warningProducts.add(product);
			}
		}
		return generatePDF(warningProducts);
	}

	private ByteArrayOutputStream generatePDF(List<Product> products) {
		Document document = new Document();
		try {
//			// Masaüstü dizin yolunu alın
//			String desktopPath = System.getProperty("user.home") + "/Desktop/";
//
//			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//			String fileName = "product_report_" + timestamp + ".pdf";
//
//			// Dosya yolunu oluşturun
//			String filePath = desktopPath + fileName;
//
//			PdfWriter.getInstance(document, new FileOutputStream(filePath));

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.setPageSize(PageSize.A4.rotate());
			document.open();
			// Maksimum 30 ürün içeren tabloları tutmak için bir liste oluşturun
			List<PdfPTable> tables = new ArrayList<>();

			PdfPTable table = null;
			int count = 0;

			for (Product product : products) {
				// Her 23 ürün için yeni bir tablo oluşturun
				if (count % 15 == 0) {
					if (table != null) {
						tables.add(table);
					}
					table = new PdfPTable(7);
					table.setWidthPercentage(100);
					table.addCell("Product Name");
					table.addCell("Category");
					table.addCell("Price");
					table.addCell("Quantity");
					table.addCell("Unit In Stock");
					table.addCell("Minimum Count");
					table.addCell("Description");
				}

				// Her hücre için bir PdfPCell oluşturun ve padding ekleyin
				PdfPCell cell = new PdfPCell(new Phrase(turkishCharConvert(product.getName())));
				System.out.println(product.getName());
				cell.setPadding(5); // Padding ayarı
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(turkishCharConvert(product.getCategory().getName())));
				cell.setPadding(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(Double.toString(product.getPrice())));
				cell.setPadding(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(Integer.toString(product.getQuantity())));
				cell.setPadding(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(Integer.toString(product.getUnitInStock())));
				cell.setPadding(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(Integer.toString(product.getMinimumCount())));
				cell.setPadding(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(turkishCharConvert(product.getDescription())));
				cell.setPadding(5);
				table.addCell(cell);

				count++;
			}

			// Son tabloyu listeye ekleyin
			if (table != null) {
				tables.add(table);
			}

			// Tüm tabloları dokümana ekleyin
			for (PdfPTable t : tables) {
				document.add(t);
				// Yeni bir sayfa ekleyin
				document.newPage();
			}

			return byteArrayOutputStream;
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return null;
	}

	private String turkishCharConvert(String val) {
		return val.replace("ı", "i").replace("ş", "s").replace("ö", "o").replace("ğ", "g").replace("ü", "u")
				.replace("ç", "c").replace("Ğ", "G").replace("Ü", "U").replace("Ş", "S").replace("İ", "I")
				.replace("Ö", "O").replace("Ç", "C");
	}

}