package com.turkcell.product_service.infrastructure;

import com.turkcell.product_service.domain.entities.Product;
import com.turkcell.product_service.domain.valueobjects.Currency;
import com.turkcell.product_service.domain.valueobjects.Price;
import com.turkcell.product_service.domain.valueobjects.Stock;

import java.math.BigDecimal;

/**
 * Infrastructure Test - Infrastructure katmanının çalışıp çalışmadığını test
 * eder
 * Bu sınıf, infrastructure katmanının doğru çalıştığını doğrulamak için
 * kullanılabilir
 * 
 * NOT: Bu test çalıştırılmadan önce PostgreSQL veritabanının çalışır durumda
 * olması gerekir
 * Docker ile: docker-compose -f docker-compose-postgres.yml up -d
 */
public class InfrastructureTest {

	public static void main(String[] args) {
		System.out.println("🏗️ Infrastructure Katmanı Test Başlatılıyor...");
		System.out.println("⚠️  NOT: PostgreSQL veritabanının çalışır durumda olduğundan emin olun!");
		System.out.println("   Docker ile: docker-compose -f docker-compose-postgres.yml up -d");
		System.out.println();

		// Repository oluştur (Spring context olmadan çalışmayacak)
		// Bu test sadece kod yapısını göstermek içindir
		System.out.println("❌ Bu test Spring context olmadan çalışmaz!");
		System.out.println("   Gerçek test için Spring Boot test sınıfları kullanın.");
		System.out.println();

		// Test verileri oluştur (sadece gösterim amaçlı)
		System.out.println("📝 Test verileri oluşturuluyor...");

		// Test verileri oluştur (sadece gösterim amaçlı)
		Product product1 = Product.create(
				"Laptop",
				"Gaming Laptop",
				new Price(new BigDecimal("15000.00"), Currency.TRY),
				new Stock(10));

		Product product2 = Product.create(
				"Mouse",
				"Gaming Mouse",
				new Price(new BigDecimal("500.00"), Currency.TRY),
				new Stock(50));

		Product product3 = Product.create(
				"Keyboard",
				"Mechanical Keyboard",
				new Price(new BigDecimal("800.00"), Currency.TRY),
				new Stock(0) // Stokta yok
		);

		System.out.println("✅ Test verileri oluşturuldu");
		System.out.println("   - " + product1.getName() + " (ID: " + product1.getId() + ")");
		System.out.println("   - " + product2.getName() + " (ID: " + product2.getId() + ")");
		System.out.println("   - " + product3.getName() + " (ID: " + product3.getId() + ")");

		System.out.println();
		System.out.println("📝 Repository İşlemleri (Spring Context gerekli):");
		System.out.println("   - save() - Ürün kaydetme");
		System.out.println("   - findById() - ID ile arama");
		System.out.println("   - findAll() - Tüm ürünleri getirme");
		System.out.println("   - findByNameContaining() - İsim ile arama");
		System.out.println("   - findInStockProducts() - Stokta olan ürünler");
		System.out.println("   - findOutOfStockProducts() - Stokta olmayan ürünler");
		System.out.println("   - findByPriceRange() - Fiyat aralığı ile arama");
		System.out.println("   - deleteById() - Ürün silme");
		System.out.println("   - count() - Toplam ürün sayısı");
		System.out.println("   - countInStockProducts() - Stokta olan ürün sayısı");

		System.out.println();
		System.out.println("🧪 Gerçek test için şu komutu çalıştırın:");
		System.out.println("   mvn test -Dtest=ProductRepositoryIntegrationTest");
		System.out.println();
		System.out.println("🎉 Infrastructure Katmanı Yapısı Hazır!");
	}
}
