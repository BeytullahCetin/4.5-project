package com.turkcell.product_service.infrastructure;

import com.turkcell.product_service.domain.entities.Product;
import com.turkcell.product_service.domain.valueobjects.Currency;
import com.turkcell.product_service.domain.valueobjects.Price;
import com.turkcell.product_service.domain.valueobjects.Stock;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductMapper - Domain ↔ Infrastructure dönüşümleri
 * Domain entity'leri ile JPA entity'leri arasında dönüşüm yapar
 * Static metodlar kullanarak utility sınıfı olarak tasarlanmıştır
 */
public class ProductMapper {

	/**
	 * Domain Product'ı JPA ProductEntity'ye dönüştürür
	 * 
	 * @param product Domain Product
	 * @return JPA ProductEntity
	 */
	public static ProductEntity toEntity(Product product) {
		if (product == null) {
			return null;
		}

		ProductEntity entity = new ProductEntity();
		entity.setId(product.getId().getValue());
		entity.setName(product.getName());
		entity.setDescription(product.getDescription());
		entity.setPriceAmount(product.getPrice().getAmount());
		entity.setPriceCurrency(product.getPrice().getCurrency().getCode());
		entity.setStockQuantity(product.getStock().getQuantity());
		entity.setCreatedAt(java.time.LocalDateTime.now());
		entity.setUpdatedAt(java.time.LocalDateTime.now());

		return entity;
	}

	/**
	 * JPA ProductEntity'yi Domain Product'a dönüştürür
	 * 
	 * @param entity JPA ProductEntity
	 * @return Domain Product
	 */
	public static Product toDomain(ProductEntity entity) {
		if (entity == null) {
			return null;
		}

		// ProductId oluştur
		Product.ProductId productId = Product.ProductId.fromUUID(entity.getId());

		// Price oluştur
		Currency currency = Currency.fromCode(entity.getPriceCurrency());
		Price price = new Price(entity.getPriceAmount(), currency);

		// Stock oluştur
		Stock stock = new Stock(entity.getStockQuantity());

		// Domain Product'ı reconstruct et
		return Product.reconstruct(
				productId,
				entity.getName(),
				entity.getDescription(),
				price,
				stock);
	}

	/**
	 * Domain Product listesini JPA ProductEntity listesine dönüştürür
	 * 
	 * @param products Domain Product listesi
	 * @return JPA ProductEntity listesi
	 */
	public static List<ProductEntity> toEntityList(List<Product> products) {
		if (products == null) {
			return null;
		}

		return products.stream()
				.map(ProductMapper::toEntity)
				.collect(Collectors.toList());
	}

	/**
	 * JPA ProductEntity listesini Domain Product listesine dönüştürür
	 * 
	 * @param entities JPA ProductEntity listesi
	 * @return Domain Product listesi
	 */
	public static List<Product> toDomainList(List<ProductEntity> entities) {
		if (entities == null) {
			return null;
		}

		return entities.stream()
				.map(ProductMapper::toDomain)
				.collect(Collectors.toList());
	}

	/**
	 * Mevcut ProductEntity'yi Domain Product ile günceller
	 * ID ve timestamp'ler korunur, sadece business data güncellenir
	 * 
	 * @param entity  Güncellenecek JPA ProductEntity
	 * @param product Güncel Domain Product
	 * @return Güncellenmiş JPA ProductEntity
	 */
	public static ProductEntity updateEntity(ProductEntity entity, Product product) {
		if (entity == null || product == null) {
			return entity;
		}

		// ID değiştirilmez
		entity.setName(product.getName());
		entity.setDescription(product.getDescription());
		entity.setPriceAmount(product.getPrice().getAmount());
		entity.setPriceCurrency(product.getPrice().getCurrency().getCode());
		entity.setStockQuantity(product.getStock().getQuantity());
		entity.setUpdatedAt(java.time.LocalDateTime.now());

		return entity;
	}

	/**
	 * Domain Product'ın güncellenmiş halini mevcut ProductEntity'ye uygular
	 * Bu metod, repository'de update işlemleri için kullanılır
	 * 
	 * @param existingEntity Mevcut JPA ProductEntity
	 * @param updatedProduct Güncellenmiş Domain Product
	 */
	public static void applyDomainChanges(ProductEntity existingEntity, Product updatedProduct) {
		if (existingEntity == null || updatedProduct == null) {
			return;
		}

		existingEntity.setName(updatedProduct.getName());
		existingEntity.setDescription(updatedProduct.getDescription());
		existingEntity.setPriceAmount(updatedProduct.getPrice().getAmount());
		existingEntity.setPriceCurrency(updatedProduct.getPrice().getCurrency().getCode());
		existingEntity.setStockQuantity(updatedProduct.getStock().getQuantity());
		existingEntity.setUpdatedAt(java.time.LocalDateTime.now());
	}
}
