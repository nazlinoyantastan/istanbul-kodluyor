package com.tobeto.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.product.request.AddProductRequestDTO;
import com.tobeto.dto.product.request.DeleteProductRequestDTO;
import com.tobeto.dto.product.request.EditProductRequestDTO;
import com.tobeto.dto.product.response.GetAllProductsResponseDTO;
import com.tobeto.dto.shelfProduct.response.GetAllProductsFromShelvesResponseDTO;
import com.tobeto.entities.warehouse.Product;
import com.tobeto.service.ProductService;

@RestController
@RequestMapping("/api/v1") // -> api/v1/product
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@GetMapping("/getAllProductsFromShelves")
	public List<GetAllProductsFromShelvesResponseDTO> getAllProductsFromShelves() {
		return productService.getAllProductsFromShelves();
	}

	@GetMapping("/getAllProducts")
	public ResponseEntity<List<GetAllProductsResponseDTO>> getAllProducts() {
		List<Product> allProducts = productService.getAllProducts();
		List<GetAllProductsResponseDTO> allProductsDTO = new ArrayList<>();
		allProducts.forEach(product -> {
			allProductsDTO.add(responseMapper.map(product, GetAllProductsResponseDTO.class));
		});
		return ResponseEntity.ok(allProductsDTO);
	}

	@PostMapping("/addProduct")
	public SuccessResponseDTO addProduct(@RequestBody AddProductRequestDTO addProductRequestDTO) {
		Product product = requestMapper.map(addProductRequestDTO, Product.class);
		productService.addProduct(product);
		return new SuccessResponseDTO("Product created!");
	}

	@PostMapping("/deleteProduct")
	public SuccessResponseDTO deleteProduct(@RequestBody DeleteProductRequestDTO deleteProductRequestDTO) {
		productService.deleteProduct(deleteProductRequestDTO.getId());
		return new SuccessResponseDTO("Product deleted successfuly!");
	}

	@PostMapping("/updateProduct")
	public SuccessResponseDTO editProduct(@RequestBody EditProductRequestDTO editProductRequestDTO) {
		Product product = requestMapper.map(editProductRequestDTO, Product.class);
		productService.updateProduct(product);
		return new SuccessResponseDTO("Product updated!");
	}

	@GetMapping("/reportProduct")
	public ResponseEntity<byte[]> generateProductReportAndPDF() {
		ByteArrayOutputStream out = productService.transferProductsToReportAndGeneratePDFAllProducts();
		byte[] pdfBytes = out.toByteArray();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		// Here you have to set the actual filename of your pdf
		String filename = "product_report.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> respons = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
		return respons;

	}

	@GetMapping("/reportProductWarningCount")
	public ResponseEntity<byte[]> transferProductsToReportAndGeneratePDFWarningCount() {
		ByteArrayOutputStream out = productService.transferProductsToReportAndGeneratePDFWarningCount();
		byte[] pdfBytes = out.toByteArray();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		// Here you have to set the actual filename of your pdf
		String filename = "product_min_report.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> respons = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
		return respons;

	}

}