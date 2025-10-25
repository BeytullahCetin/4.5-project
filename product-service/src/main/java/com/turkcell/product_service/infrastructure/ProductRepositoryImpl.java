package com.turkcell.product_service.infrastructure;

import com.turkcell.product_service.domain.entities.Product;
import com.turkcell.product_service.domain.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ProductRepositoryImpl - Repository Implementation
 * Infrastructure katmanında ProductRepository interface'ini implement eder
 * PostgreSQL veritabanı ile çalışır
 * 
 * Bu implementasyon, gerçek PostgreSQL veritabanı kullanarak
 * veri kalıcılığını sağlar
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

	@Autowired
	private ProductJpaRepository jpaRepository;

	@Override
	public Product save(Product product) {
		if (product == null) {
			throw new IllegalArgumentException("Kaydedilecek ürün null olamaz");
		}

		ProductEntity entity = ProductMapper.toEntity(product);
		ProductEntity savedEntity = jpaRepository.save(entity);

		// Kaydedilen domain product'ı döndür
		return ProductMapper.toDomain(savedEntity);
	}

	@Override
	public Optional<Product> findById(Product.ProductId id) {
		if (id == null) {
			return Optional.empty();
		}

		Optional<ProductEntity> entity = jpaRepository.findById(id.getValue());
		return entity.map(ProductMapper::toDomain);
	}

	@Override
	public List<Product> findAll() {
		return jpaRepository.findAll()
				.stream()
				.map(ProductMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findByNameContaining(String name) {
		if (name == null || name.trim().isEmpty()) {
			return Collections.emptyList();
		}

		return jpaRepository.findByNameContainingIgnoreCase(name)
				.stream()
				.map(ProductMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findInStockProducts() {
		return jpaRepository.findInStockProducts()
				.stream()
				.map(ProductMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findOutOfStockProducts() {
		return jpaRepository.findOutOfStockProducts()
				.stream()
				.map(ProductMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
			throw new IllegalArgumentException("Geçersiz fiyat aralığı");
		}

		return jpaRepository.findByPriceRange(minPrice, maxPrice)
				.stream()
				.map(ProductMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Product.ProductId id) {
		if (id == null) {
			throw new IllegalArgumentException("Silinecek ürün ID'si null olamaz");
		}

		jpaRepository.deleteById(id.getValue());
	}

	@Override
	public boolean existsById(Product.ProductId id) {
		if (id == null) {
			return false;
		}

		return jpaRepository.existsById(id.getValue());
	}

	@Override
	public long count() {
		return jpaRepository.count();
	}

	@Override
	public long countInStockProducts() {
		return jpaRepository.countInStockProducts();
	}

	/**
	 * Repository'deki tüm verileri temizler
	 * Test amaçlı kullanılabilir
	 */
	public void clearAll() {
		jpaRepository.deleteAll();
	}

	/**
	 * Repository'deki veri sayısını döndürür
	 * Debug amaçlı kullanılabilir
	 */
	public long getStoreSize() {
		return jpaRepository.count();
	}

	/**
	 * Belirli bir ID'ye sahip entity'nin var olup olmadığını kontrol eder
	 * Internal kullanım için
	 */
	public boolean containsEntity(UUID id) {
		return jpaRepository.existsById(id);
	}

	/**
	 * Repository'deki tüm entity'leri döndürür
	 * Debug ve test amaçlı kullanılabilir
	 */
	public List<ProductEntity> getAllEntities() {
		return jpaRepository.findAll();
	}
}
