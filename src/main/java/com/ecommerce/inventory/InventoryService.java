package com.ecommerce.inventory;

import java.util.List;

import com.ecommerce.models.Product;

public class InventoryService {
	// Add Product
	public void addProduct(String name, String description, double price, int quantity) {
		// write your logic here
	}

	// Get Products Sorted By Price
	public List<Product> getProductsSortedByPrice() {
		// write your logic here
		return null;
	}

	// Retrieve Products Below Specific Price
	public List<Product> getProductsBelowPrice(double priceLimit) {
		// write your logic here
		return null;
	}

	// Update Product
	public void updateProduct(String id, double newPrice, int newQuantity) {
		// write your logic here
	}

	// Delete Product
	public void deleteProduct(String id) {
		// write your logic here
	}

	// Calculate Total Inventory Value
	public double calculateTotalInventoryValue() {
		// write your logic here
		return 0;
	}
}