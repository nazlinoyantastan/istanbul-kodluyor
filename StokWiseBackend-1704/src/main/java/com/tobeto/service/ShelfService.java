package com.tobeto.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entities.warehouse.Category;
import com.tobeto.entities.warehouse.Product;
import com.tobeto.entities.warehouse.Shelf;
import com.tobeto.entities.warehouse.ShelfProduct;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.warehouse.ShelfProductRepository;
import com.tobeto.repository.warehouse.ShelfRepository;

import jakarta.transaction.Transactional;

@Service
public class ShelfService {

	@Autowired
	private ShelfRepository shelfRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private ShelfProductRepository shelfProductRepository;

	public List<Shelf> getAllShelves() {
		return shelfRepository.findAll();
	}

	public void addShelf(Shelf shelf) {
		if (shelf.getCapacity() > 50 && shelf.getCapacity() <= 200) {
			shelfRepository.save(shelf);
		} else {
			throw new ServiceException(ERROR_CODES.SHELF_CAPACITY);
		}
	}

	public void updateShelf(int id, int capacity) {
		Optional<Shelf> optionalShelf = shelfRepository.findById(id);
		if (optionalShelf.isPresent()) {
			Shelf shelf = optionalShelf.get();
			List<ShelfProduct> shelfProducts = shelfProductRepository.findByShelfId(id);
			int totalProductCount = shelfProducts.stream().mapToInt(ShelfProduct::getProductCount).sum();
			if (capacity < totalProductCount) {
				throw new ServiceException(ERROR_CODES.SET_SHELF_COUNT);
			} else {
				shelf.setCapacity(capacity);
				shelfRepository.save(shelf);
			}
		}
	}

	public void deleteShelf(int id) {
		List<ShelfProduct> shelfProducts = shelfProductRepository.findByShelfId(id);
		int totalCount = 0;
		for (ShelfProduct shelfProduct : shelfProducts) {
			totalCount += shelfProduct.getProductCount();
		}
		if (totalCount > 0) {
			throw new ServiceException(ERROR_CODES.SHELF_HAS_PRODUCTS);
		} else {
			shelfRepository.deleteById(id);
		}
	}

	@Transactional
	public void entryProduct(int productId, int count) { // acceptFruit
		Product product = productService.getProduct(productId);

		if ((product.getQuantity() - product.getUnitInStock()) >= count) {

			// Ürünü raflara eklerken stok miktarını arttır
			productService.increaseProductStock(productId, count);

			Category category = product.getCategory();
			List<Shelf> shelves = shelfRepository.findByShelfProductsProductCategoryId(category.getId());

			List<Shelf> halfShelves = shelves.stream().filter(s -> {
				int shelfCapacity = s.getCapacity();
				int totalProductCount = s.getShelfProducts().stream()
						.flatMapToInt(shelf -> IntStream.of(shelf.getProductCount())).sum();
				return totalProductCount < shelfCapacity;
			}).sorted((shelf1, shelf2) -> {
				int shelfSpace1 = getShelfSpace(shelf1);
				int shelfSpace2 = getShelfSpace(shelf2);
				return shelfSpace1 > shelfSpace2 ? 1 : -1;
			}).toList();

			for (Shelf shelf : halfShelves) {
				count = fillShelf(productId, count, product, shelf);
				if (count == 0) {
					break;
				}
			}

			if (count > 0) {
				entryShelf(count, product);
			}
		} else {
			throw new ServiceException(ERROR_CODES.NOT_ENOUGH_SPACE_SHELF);
		}

	}

	public int getShelfSpace(Shelf shelf1) {
		int shelfCapacity1 = shelf1.getCapacity();
		int totalProductCount1 = shelf1.getShelfProducts().stream()
				.flatMapToInt(shelf -> IntStream.of(shelf.getProductCount())).sum();
		int shelfSpace1 = shelfCapacity1 - totalProductCount1;
		return shelfSpace1;
	}

	public int fillShelf(int productId, int count, Product product, Shelf shelf) {
		int addAmount = count;
		int totalProductCount = shelf.getShelfProducts().stream()
				.flatMapToInt(shelfP -> IntStream.of(shelfP.getProductCount())).sum();
		int emptyPart = shelf.getCapacity() - totalProductCount;
		if (addAmount > emptyPart) {
			addAmount = emptyPart;
		}
		Optional<ShelfProduct> oShelfProduct = shelf.getShelfProducts().stream()
				.filter(shelfP -> shelfP.getProduct().getId() == productId).findFirst();
		if (oShelfProduct.isPresent()) {
			ShelfProduct sProduct = oShelfProduct.get();
			sProduct.setProductCount(sProduct.getProductCount() + addAmount);
			shelfProductRepository.save(sProduct);
		} else {
			ShelfProduct sProduct = ShelfProduct.builder().productCount(addAmount).product(product).shelf(shelf)
					.build();
			shelfProductRepository.save(sProduct);
		}
		count -= addAmount;
		return count;
	}

	public void entryShelf(int count, Product product) {
		List<Integer> emptyShelfIds = shelfRepository.findEmptyShelves();
		Collections.sort(emptyShelfIds);

		int firstEntry = 0;
		while (count > 0) {
			if (firstEntry >= emptyShelfIds.size()) {
				throw new ServiceException(ERROR_CODES.NOT_ENOUGH_SHELF);
			}
			int shelfId = emptyShelfIds.get(firstEntry);
			Optional<Shelf> optionalShelf = shelfRepository.findById(shelfId);
			if (optionalShelf.isPresent()) {
				Shelf shelf = optionalShelf.get();
				int addAmount = count;
				if (addAmount > shelf.getCapacity()) {
					addAmount = shelf.getCapacity();
				}
				ShelfProduct sProduct = ShelfProduct.builder().productCount(addAmount).product(product).shelf(shelf)
						.build();
				shelfProductRepository.save(sProduct);
				count -= addAmount;
				firstEntry++;
			}
		}
	}

	public List<ShelfProduct> getAllProductsFromShelf(int shelfId) {
		return shelfProductRepository.findByShelfId(shelfId);
	}

}