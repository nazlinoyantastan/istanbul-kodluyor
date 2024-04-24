package com.tobeto.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.product.ProductDTO;
import com.tobeto.dto.product.request.DispatchProductRequestDTO;
import com.tobeto.dto.product.request.EntryProductRequestDTO;
import com.tobeto.dto.product.response.ProductResponseDTO;
import com.tobeto.dto.shelf.ShelfDTO;
import com.tobeto.dto.shelf.request.AddShelfRequestDTO;
import com.tobeto.dto.shelf.request.TableShelfRequestDTO;
import com.tobeto.dto.shelf.response.TableShelfResponseDTO;
import com.tobeto.dto.shelfProduct.request.DeleteShelfProductRequestDTO;
import com.tobeto.dto.shelfProduct.request.UpdateShelfProductRequestDTO;
import com.tobeto.entities.warehouse.Product;
import com.tobeto.entities.warehouse.Shelf;
import com.tobeto.entities.warehouse.ShelfProduct;
import com.tobeto.exception.ServiceException;
import com.tobeto.service.ProductService;
import com.tobeto.service.ShelfService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/v1")
public class ShelfController {

	@Autowired
	private ShelfService shelfService;

	@Autowired
	private ProductService productService;

	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@PostMapping("/addShelf")
	public SuccessResponseDTO addShelf(@RequestBody AddShelfRequestDTO addShelfRequestDTO) {
		Shelf shelf = requestMapper.map(addShelfRequestDTO, Shelf.class);
		shelfService.addShelf(shelf);
		return new SuccessResponseDTO("Shelf created!");
	}

	@GetMapping("/getAllShelves")
	public List<ShelfDTO> getAllShelves() {
		List<Shelf> shelves = shelfService.getAllShelves();
		List<ShelfDTO> shelfDTOs = new ArrayList<>();

		for (Shelf shelf : shelves) {
			ShelfDTO shelfDTO = new ShelfDTO();
			shelfDTO.setId(shelf.getId());
			shelfDTO.setCapacity(shelf.getCapacity());

			List<ShelfProduct> shelfProducts = shelf.getShelfProducts();
			List<ProductDTO> productDTOs = new ArrayList<>();

			for (ShelfProduct shelfProduct : shelfProducts) {
				ProductDTO productDTO = new ProductDTO();
				Product product = shelfProduct.getProduct();
				productDTO.setName(product.getName());
				productDTO.setCategory(product.getCategory().getName());
				productDTO.setCount(shelfProduct.getProductCount());
				productDTOs.add(productDTO);
			}

			shelfDTO.setProducts(productDTOs);
			shelfDTOs.add(shelfDTO);
		}

		return shelfDTOs;
	}

	@PostMapping("/deleteShelf")
	public SuccessResponseDTO deleteShelf(@RequestBody DeleteShelfProductRequestDTO deleteShelfProductRequestDTO) {
		shelfService.deleteShelf(deleteShelfProductRequestDTO.getId());
		return new SuccessResponseDTO("Shelf deleted successfully!");
	}

	@PostMapping("/editShelf")
	public SuccessResponseDTO updateShelf(@RequestBody UpdateShelfProductRequestDTO updateShelfProductRequestDTO) {
		shelfService.updateShelf(updateShelfProductRequestDTO.getId(), updateShelfProductRequestDTO.getCapacity());
		return new SuccessResponseDTO("Shelf updated!");
	}

	@PostMapping("/entryProduct")
	public ResponseEntity<String> entryProduct(@RequestBody EntryProductRequestDTO request) {
		try {
			shelfService.entryProduct(request.getProductId(), request.getCount());
			return ResponseEntity.ok("Product added successfully on the shelf!");
		} catch (ServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// ÜRÜN ÇIKIŞI
	@PostMapping("/dispatchProduct")
	public ResponseEntity<String> dispatchProduct(@RequestBody DispatchProductRequestDTO dto) {
		try {
			productService.dispatchProduct(dto.getProductId(), dto.getCount());
			return ResponseEntity.ok("Product dispatch successfully!");
		} catch (ServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Transactional
	@PostMapping("/getAllProductsFromShelf")
	public List<ProductResponseDTO> getAllProductsFromShelf(@RequestBody TableShelfRequestDTO dto) {

		List<ShelfProduct> shelfProducts = shelfService.getAllProductsFromShelf(dto.getId());
		return shelfProducts.stream().map(sp -> {
			ProductResponseDTO productDTO = new ProductResponseDTO();
			productDTO.setId(sp.getProduct().getId());
			productDTO.setCount(sp.getProductCount());
			productDTO.setName(sp.getProduct().getName());
			return productDTO;
		}).toList();

	}

	@Transactional
	@GetMapping("/getAllTableShelves")
	public List<TableShelfResponseDTO> getAllTableShelves() {
		List<Shelf> shelves = shelfService.getAllShelves();
		return shelves.stream().map(sh -> {
			TableShelfResponseDTO dto = new TableShelfResponseDTO();
			dto.setId(sh.getId());
			dto.setCapacity(sh.getCapacity());
			int sumProductCount = sh.getShelfProducts().stream().mapToInt(sp -> sp.getProductCount()).sum();
			dto.setProductCount(sumProductCount);
			if (sh.getShelfProducts().size() > 0) {
				dto.setProductCategory(sh.getShelfProducts().get(0).getProduct().getCategory().getName());
			}
			return dto;
		}).toList();
	}

}