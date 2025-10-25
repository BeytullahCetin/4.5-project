package com.turkcell.product_service.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * ProductJpaRepository - JPA Repository Interface
 * Spring Data JPA ile veritabanı işlemlerini yönetir
 * PostgreSQL veritabanı ile çalışır
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

	/**
	 * Ürün adına göre arama yapar (case-insensitive)
	 * 
	 * @param name Aranacak ürün adı
	 * @return Bulunan ürünler
	 */
	@Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<ProductEntity> findByNameContainingIgnoreCase(@Param("name") String name);

	/**
	 * Stokta olan ürünleri getirir
	 * 
	 * @return Stokta olan ürünler
	 */
	@Query("SELECT p FROM ProductEntity p WHERE p.stockQuantity > 0")
	List<ProductEntity> findInStockProducts();

	/**
	 * Stokta olmayan ürünleri getirir
	 * 
	 * @return Stokta olmayan ürünler
	 */
	@Query("SELECT p FROM ProductEntity p WHERE p.stockQuantity = 0")
	List<ProductEntity> findOutOfStockProducts();

	/**
	 * Belirli fiyat aralığındaki ürünleri getirir
	 * 
	 * @param minPrice Minimum fiyat
	 * @param maxPrice Maksimum fiyat
	 * @return Fiyat aralığındaki ürünler
	 */
	@Query("SELECT p FROM ProductEntity p WHERE p.priceAmount BETWEEN :minPrice AND :maxPrice")
	List<ProductEntity> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

	/**
	 * Stokta olan ürün sayısını döner
	 * 
	 * @return Stokta olan ürün sayısı
	 */
	@Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.stockQuantity > 0")
	long countInStockProducts();

	/**
	 * Belirli para birimindeki ürünleri getirir
	 * 
	 * @param currency Para birimi kodu
	 * @return Belirtilen para birimindeki ürünler
	 */
	@Query("SELECT p FROM ProductEntity p WHERE p.priceCurrency = :currency")
	List<ProductEntity> findByCurrency(@Param("currency") String currency);

	/**
	 * Minimum stok miktarının altındaki ürünleri getirir
	 * 
	 * @param minStock Minimum stok miktarı
	 * @return Düşük stoklu ürünler
	 */
	@Query("SELECT p FROM ProductEntity p WHERE p.stockQuantity < :minStock")
	List<ProductEntity> findLowStockProducts(@Param("minStock") Integer minStock);

	/**
	 * Ürün adına göre tam eşleşme arar
	 * 
	 * @param name Tam ürün adı
	 * @return Bulunan ürün (varsa)
	 */
	@Query("SELECT p FROM ProductEntity p WHERE p.name = :name")
	List<ProductEntity> findByName(@Param("name") String name);
}
