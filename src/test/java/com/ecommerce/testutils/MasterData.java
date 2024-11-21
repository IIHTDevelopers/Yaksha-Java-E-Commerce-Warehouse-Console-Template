package com.ecommerce.testutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ecommerce.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MasterData {

	private static final Random random = new Random();

	// Generate a single mock Product
	public static Product getProduct() {
		return new Product(generateRandomId(), "Mock Product " + random.nextInt(100),
				"Mock Description " + random.nextInt(100), generateRandomPrice(), generateRandomQuantity());
	}

	// Generate a list of mock Products
	public static List<Product> getProductList(int count) {
		List<Product> products = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			products.add(getProduct());
		}
		return products;
	}

	// Utility method to convert an object to JSON string
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			final String jsonContent = mapper.writeValueAsString(obj);

			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Helper method to generate a random product ID
	private static String generateRandomId() {
		return "PROD-" + random.nextInt(1000);
	}

	// Helper method to generate a random price
	private static double generateRandomPrice() {
		return random.nextDouble() * 1000; // Random price between 0 and 1000
	}

	// Helper method to generate a random quantity
	private static int generateRandomQuantity() {
		return random.nextInt(100) + 1; // Random quantity between 1 and 100
	}
}
