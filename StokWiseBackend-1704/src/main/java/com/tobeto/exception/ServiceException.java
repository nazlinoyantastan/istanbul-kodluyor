package com.tobeto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -309140238380688123L;

	public static enum ERROR_CODES {
		PRODUCT_NOT_FOUND(1, "Product not found."), SHELF_NOT_FOUND(2, "Shelf not found."),

		NOT_ENOUGH_SHELF(3, "Not enough shelf."), NOT_ENOUGH_SPACE_SHELF(4, "Not enough space on the shelf."),

		SHELF_HAS_PRODUCTS(5, "Shelf has products."),
		SET_SHELF_COUNT(6, "You cannot set capacity less than the number of products in the shelf."),
		SHELF_CAPACITY(7, "You cannot set capacity less than 50 and bigger than 200"),
		PRODUCT_STOCK_ERROR(8, "Product unit in stock less than entry count."),

		USER_NOT_FOUND(9, "User not found."),
		NOT_ENOUGH_STOCK(10, "Cannot select more products than the quantity in stock."),
		CATEGORY_NOT_FOUND(11, "Category not found."),
		PRODUCT_QUANTİTY_EROR(12, "Product quantity is not zero, cannot delete the product. ");

		private int code;
		private String message;

		private ERROR_CODES(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

	}

	private int code;
	private String message;

	public ServiceException(ERROR_CODES errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	private String errorCode;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}