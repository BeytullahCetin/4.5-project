package com.turkcell.product_service.infrastructure;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * ProductEntity - JPA Entity
 * Infrastructure katmanında veritabanı tablosunu temsil eder
 * Domain entity'den JPA entity'ye dönüşüm için kullanılır
 */
@Entity
@Table(name = "products")
public class ProductEntity {

	@Id
	@Column(name = "id", columnDefinition = "UUID")
	private UUID id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "description", nullable = false, length = 1000)
	private String description;

	@Column(name = "price_amount", nullable = false, precision = 19, scale = 2)
	private BigDecimal priceAmount;

	@Column(name = "price_currency", nullable = false, length = 3)
	private String priceCurrency;

	@Column(name = "stock_quantity", nullable = false)
	private Integer stockQuantity;

	@Column(name = "created_at", nullable = false)
	private java.time.LocalDateTime createdAt;

	@Column(name = "updated_at")
	private java.time.LocalDateTime updatedAt;

	// Default constructor for JPA
	protected ProductEntity() {
	}

	// Constructor for creating new entity
	public ProductEntity(UUID id, String name, String description,
			BigDecimal priceAmount, String priceCurrency,
			Integer stockQuantity) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.priceAmount = priceAmount;
		this.priceCurrency = priceCurrency;
		this.stockQuantity = stockQuantity;
		this.createdAt = java.time.LocalDateTime.now();
		this.updatedAt = java.time.LocalDateTime.now();
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPriceAmount() {
		return priceAmount;
	}

	public void setPriceAmount(BigDecimal priceAmount) {
		this.priceAmount = priceAmount;
	}

	public String getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public java.time.LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(java.time.LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public java.time.LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * Entity'yi güncellerken updatedAt alanını otomatik olarak günceller
	 */
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = java.time.LocalDateTime.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductEntity that = (ProductEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "ProductEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", priceAmount=" + priceAmount +
				", priceCurrency='" + priceCurrency + '\'' +
				", stockQuantity=" + stockQuantity +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				'}';
	}
}
