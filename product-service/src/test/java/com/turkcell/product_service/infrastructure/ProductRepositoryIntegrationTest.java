package com.turkcell.product_service.infrastructure;

import com.turkcell.product_service.domain.entities.Product;
import com.turkcell.product_service.domain.valueobjects.Currency;
import com.turkcell.product_service.domain.valueobjects.Price;
import com.turkcell.product_service.domain.valueobjects.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProductRepositoryIntegrationTest - PostgreSQL ile Integration Test
 * Gerçek veritabanı ile çalışan test sınıfı
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductRepositoryIntegrationTest {

	@Autowired
	private ProductRepositoryImpl productRepository;

	@Autowired
	private ProductJpaRepository jpaRepository;

	@BeforeEach
	void setUp() {
		// Her test öncesi veritabanını temizle
		jpaRepository.deleteAll();
	}

	@Test
	void testSaveProduct() {
		// Given
		Product product = Product.create(
				"Test Laptop",
				"Test Gaming Laptop",
				new Price(new BigDecimal("15000.00"), Currency.TRY),
				new Stock(10));

		// When
		Product savedProduct = productRepository.save(product);

		// Then
		assertNotNull(savedProduct);
		assertNotNull(savedProduct.getId());
		assertEquals("Test Laptop", savedProduct.getName());
		assertEquals("Test Gaming Laptop", savedProduct.getDescription());
		assertEquals(new BigDecimal("15000.00"), savedProduct.getPrice().getAmount());
		assertEquals(Currency.TRY, savedProduct.getPrice().getCurrency());
		assertEquals(10, savedProduct.getStock().getQuantity());
	}

	@Test
	void testFindById() {
		// Given
		Product product = Product.create(
				"Test Mouse",
				"Test Gaming Mouse",
				new Price(new BigDecimal("500.00"), Currency.TRY),
				new Stock(50));
		Product savedProduct = productRepository.save(product);

		// When
		Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

		// Then
		assertTrue(foundProduct.isPresent());
		assertEquals(savedProduct.getId(), foundProduct.get().getId());
		assertEquals("Test Mouse", foundProduct.get().getName());
	}

	@Test
	void testFindAll() {
		// Given
		Product product1 = Product.create("Product 1", "Description 1",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product product2 = Product.create("Product 2", "Description 2",
				new Price(new BigDecimal("200.00"), Currency.TRY), new Stock(20));

		productRepository.save(product1);
		productRepository.save(product2);

		// When
		List<Product> allProducts = productRepository.findAll();

		// Then
		assertEquals(2, allProducts.size());
	}

	@Test
	void testFindByNameContaining() {
		// Given
		Product laptop = Product.create("Gaming Laptop", "High-end laptop",
				new Price(new BigDecimal("15000.00"), Currency.TRY), new Stock(5));
		Product mouse = Product.create("Gaming Mouse", "High-precision mouse",
				new Price(new BigDecimal("500.00"), Currency.TRY), new Stock(20));

		productRepository.save(laptop);
		productRepository.save(mouse);

		// When
		List<Product> gamingProducts = productRepository.findByNameContaining("Gaming");

		// Then
		assertEquals(2, gamingProducts.size());
	}

	@Test
	void testFindInStockProducts() {
		// Given
		Product inStock = Product.create("In Stock Product", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product outOfStock = Product.create("Out of Stock Product", "Description",
				new Price(new BigDecimal("200.00"), Currency.TRY), new Stock(0));

		productRepository.save(inStock);
		productRepository.save(outOfStock);

		// When
		List<Product> inStockProducts = productRepository.findInStockProducts();

		// Then
		assertEquals(1, inStockProducts.size());
		assertEquals("In Stock Product", inStockProducts.get(0).getName());
	}

	@Test
	void testFindOutOfStockProducts() {
		// Given
		Product inStock = Product.create("In Stock Product", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product outOfStock = Product.create("Out of Stock Product", "Description",
				new Price(new BigDecimal("200.00"), Currency.TRY), new Stock(0));

		productRepository.save(inStock);
		productRepository.save(outOfStock);

		// When
		List<Product> outOfStockProducts = productRepository.findOutOfStockProducts();

		// Then
		assertEquals(1, outOfStockProducts.size());
		assertEquals("Out of Stock Product", outOfStockProducts.get(0).getName());
	}

	@Test
	void testFindByPriceRange() {
		// Given
		Product cheap = Product.create("Cheap Product", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product expensive = Product.create("Expensive Product", "Description",
				new Price(new BigDecimal("1000.00"), Currency.TRY), new Stock(5));
		Product veryExpensive = Product.create("Very Expensive Product", "Description",
				new Price(new BigDecimal("5000.00"), Currency.TRY), new Stock(2));

		productRepository.save(cheap);
		productRepository.save(expensive);
		productRepository.save(veryExpensive);

		// When
		List<Product> midRangeProducts = productRepository.findByPriceRange(500.0, 2000.0);

		// Then
		assertEquals(1, midRangeProducts.size());
		assertEquals("Expensive Product", midRangeProducts.get(0).getName());
	}

	@Test
	void testDeleteById() {
		// Given
		Product product = Product.create("To Delete", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product savedProduct = productRepository.save(product);

		// When
		productRepository.deleteById(savedProduct.getId());

		// Then
		assertFalse(productRepository.existsById(savedProduct.getId()));
		assertEquals(0, productRepository.count());
	}

	@Test
	void testExistsById() {
		// Given
		Product product = Product.create("Test Product", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product savedProduct = productRepository.save(product);

		// When & Then
		assertTrue(productRepository.existsById(savedProduct.getId()));
		assertFalse(productRepository.existsById(Product.ProductId.generate()));
	}

	@Test
	void testCount() {
		// Given
		Product product1 = Product.create("Product 1", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product product2 = Product.create("Product 2", "Description",
				new Price(new BigDecimal("200.00"), Currency.TRY), new Stock(20));

		productRepository.save(product1);
		productRepository.save(product2);

		// When
		long count = productRepository.count();

		// Then
		assertEquals(2, count);
	}

	@Test
	void testCountInStockProducts() {
		// Given
		Product inStock1 = Product.create("In Stock 1", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product inStock2 = Product.create("In Stock 2", "Description",
				new Price(new BigDecimal("200.00"), Currency.TRY), new Stock(20));
		Product outOfStock = Product.create("Out of Stock", "Description",
				new Price(new BigDecimal("300.00"), Currency.TRY), new Stock(0));

		productRepository.save(inStock1);
		productRepository.save(inStock2);
		productRepository.save(outOfStock);

		// When
		long inStockCount = productRepository.countInStockProducts();

		// Then
		assertEquals(2, inStockCount);
	}

	@Test
	void testUpdateProduct() {
		// Given
		Product product = Product.create("Original Name", "Original Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product savedProduct = productRepository.save(product);

		// When
		savedProduct.updateProduct("Updated Name", "Updated Description");
		savedProduct.updatePrice(new Price(new BigDecimal("150.00"), Currency.TRY));
		Product updatedProduct = productRepository.save(savedProduct);

		// Then
		assertEquals("Updated Name", updatedProduct.getName());
		assertEquals("Updated Description", updatedProduct.getDescription());
		assertEquals(new BigDecimal("150.00"), updatedProduct.getPrice().getAmount());
	}

	@Test
	void testStockOperations() {
		// Given
		Product product = Product.create("Test Product", "Description",
				new Price(new BigDecimal("100.00"), Currency.TRY), new Stock(10));
		Product savedProduct = productRepository.save(product);

		// When - Reduce stock
		savedProduct.reduceStock(3);
		Product reducedStockProduct = productRepository.save(savedProduct);

		// Then
		assertEquals(7, reducedStockProduct.getStock().getQuantity());

		// When - Add stock
		reducedStockProduct.addStock(5);
		Product addedStockProduct = productRepository.save(reducedStockProduct);

		// Then
		assertEquals(12, addedStockProduct.getStock().getQuantity());
	}
}
