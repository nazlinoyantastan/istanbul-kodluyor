package com.tobeto.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
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
		return productRepository.findAllActive();
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

		Optional<Product> oProduct = productRepository.findById(product.getId());
		if (oProduct.isPresent()) {
			product.setCategory(oProduct.get().getCategory());
		}
		return productRepository.save(product);
	}

	@Transactional
	public void deleteProduct(UUID id) {

		Optional<Product> productOptional = productRepository.findById(id);

		if (productOptional.isPresent()) {
			Product product = productOptional.get();
			if (product.getQuantity() == 0) {

				productRepository.softDeleteById(id);
			} else {
				throw new ServiceException(ERROR_CODES.PRODUCT_QUANTİTY_EROR);
			}
		}
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
		return generatePDF(products, "All Products Report", "All products in the inventory");
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

//		// Uyarı ürünleri listesi boşsa, özel bir mesaj içeren PDF oluştur
//		if (warningProducts.isEmpty()) {
//			return generateEmptyPDF("Minimum Count altında ürün yoktur");
//		}

//		return generatePDF(warningProducts, "Low Stock Alert Report");

		// Uyarı ürünleri listesi boşsa, özel bir mesaj içeren PDF oluştur
		String message = "There Are No Products Under The Minimum Count";
		String title = "Low Stock Alert Report";
		if (warningProducts.isEmpty()) {
			return generateEmptyPDF(message);
		}

		return generatePDF(warningProducts, title, "Products under the minimum count");

	}

	private ByteArrayOutputStream generatePDF(List<Product> products, String title, String message) {
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

			// Tarihi oluşturun
			String dateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

			// Maksimum 30 ürün içeren tabloları tutmak için bir liste oluşturun
			List<PdfPTable> tables = new ArrayList<>();

			PdfPTable table = null;
			int count = 0;

			for (Product product : products) {
				// Her 15 ürün için yeni bir tablo oluşturun
				if (count % 4 == 0) {
					if (table != null) {
						tables.add(table);
					}
					table = new PdfPTable(7); // 7 sütun
					table.setWidthPercentage(100);
					table.setSpacingBefore(10); // Tablo öncesi boşluk
					table.setSpacingAfter(10); // Tablo sonrası boşluk

					// Tablo başlığını ekleyin
					PdfPCell titleCell = new PdfPCell(
							new Phrase(title, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
					titleCell.setColspan(7);
					titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					titleCell.setBorder(Rectangle.NO_BORDER);
					titleCell.setPadding(8); // Başlık için padding
					table.addCell(titleCell);

					// Tarih hücresini ekleyin
					PdfPCell dateCell = new PdfPCell(new Phrase("Report Date: " + dateStr));
					dateCell.setColspan(7);
					dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					dateCell.setBorder(Rectangle.NO_BORDER);
					dateCell.setPadding(5);
					table.addCell(dateCell);

					// Sütun başlıklarını ekleyin
					String[] headers = { "Product Name", "Category", "Price", "Quantity", "Unit In Stock", "Min. Count",
							"Description" };
					for (String header : headers) {
						PdfPCell headerCell = new PdfPCell(
								new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
						headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						headerCell.setPadding(8);
						table.addCell(headerCell);
					}
				}

				// Ürün bilgilerini ekleyin
				PdfPCell cell = new PdfPCell(new Phrase(turkishCharConvert(product.getName())));
				cell.setPadding(5); // Padding ayarı
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(turkishCharConvert(product.getCategory().getName())));
				cell.setPadding(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("$ " + Double.toString(product.getPrice())));
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

	private ByteArrayOutputStream generateEmptyPDF(String message) {
		Document document = new Document();
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.setPageSize(PageSize.A4.rotate());
			document.open();

			String dateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

			PdfPTable table = null;

			table = new PdfPTable(7); // 7 sütun
			table.setWidthPercentage(100);
			table.setSpacingBefore(10); // Tablo öncesi boşluk

			// Tarih hücresini ekleyin
			PdfPCell dateCell = new PdfPCell(new Phrase("Report Date: " + dateStr));
			dateCell.setColspan(7);
			dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dateCell.setBorder(Rectangle.NO_BORDER);
			dateCell.setPadding(5);
			table.addCell(dateCell);

			// Mesajı içeren bir paragraf oluştur
			Paragraph paragraph = new Paragraph(message, new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL));
			paragraph.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph);

			document.add(table);

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