# Infrastructure Katmanı

Bu katman, Onion Architecture'ın dış katmanıdır ve domain katmanı ile dış dünya (veritabanı, API'ler, vb.) arasında köprü görevi görür.

## 📁 Katman Yapısı

```
infrastructure/
├── ProductEntity.java          # JPA Entity
├── ProductMapper.java          # Domain ↔ Infrastructure dönüşümleri
├── ProductRepositoryImpl.java # Repository Implementation
├── InfrastructureTest.java    # Test sınıfı
└── README.md                  # Bu dosya
```

## 🏗️ Bileşenler

### 1. ProductEntity.java

- **Görev**: JPA Entity sınıfı
- **Özellikler**:
  - `@Entity`, `@Table`, `@Id`, `@Column` anotasyonları
  - Domain entity'den JPA entity'ye dönüşüm
  - Veritabanı tablosu ile eşleşme
  - Timestamp yönetimi (`@PreUpdate`)

### 2. ProductMapper.java

- **Görev**: Domain ↔ Infrastructure dönüşümleri
- **Özellikler**:
  - Static metodlar
  - Domain Product ↔ JPA ProductEntity dönüşümü
  - Liste dönüşümleri
  - Entity güncelleme metodları

### 3. ProductRepositoryImpl.java

- **Görev**: Repository Implementation
- **Özellikler**:
  - In-memory veri saklama (ConcurrentHashMap)
  - Thread-safe operasyonlar
  - Tüm repository metodlarının implementasyonu
  - Debug ve test metodları

## 🔄 Veri Akışı

```
Domain Layer          Infrastructure Layer
┌─────────────┐       ┌─────────────────┐
│   Product   │ ←──→  │  ProductEntity  │
│ (Domain)    │       │    (JPA)        │
└─────────────┘       └─────────────────┘
        │                       │
        │                       │
        ▼                       ▼
┌─────────────┐       ┌─────────────────┐
│ProductRepo   │ ←──→  │ProductRepoImpl  │
│(Interface)   │       │(Implementation) │
└─────────────┘       └─────────────────┘
```

## 🚀 Kullanım Örnekleri

### Repository Kullanımı

```java
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final Map<UUID, ProductEntity> productStore = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        productStore.put(entity.getId(), entity);
        return ProductMapper.toDomain(entity);
    }
}
```

### Mapper Kullanımı

```java
// Domain → Entity
ProductEntity entity = ProductMapper.toEntity(domainProduct);

// Entity → Domain
Product domainProduct = ProductMapper.toDomain(entity);

// Liste dönüşümleri
List<ProductEntity> entities = ProductMapper.toEntityList(domainProducts);
List<Product> domainProducts = ProductMapper.toDomainList(entities);
```

## 🧪 Test

Infrastructure katmanını test etmek için:

```java
public class InfrastructureTest {
    public static void main(String[] args) {
        ProductRepositoryImpl repository = new ProductRepositoryImpl();

        // Test verileri oluştur ve test et
        Product product = Product.create("Test Product", "Description",
            new Price(new BigDecimal("100.00"), Currency.TRY),
            new Stock(10));

        Product saved = repository.save(product);
        System.out.println("Ürün kaydedildi: " + saved.getName());
    }
}
```

## 📋 Özellikler

### ✅ Tamamlanan Özellikler

- [x] ProductEntity JPA sınıfı
- [x] ProductMapper dönüşüm sınıfı
- [x] ProductRepositoryImpl implementasyonu
- [x] In-memory veri saklama
- [x] Thread-safe operasyonlar
- [x] Tüm repository metodları
- [x] Test sınıfı

### 🔄 Gelecek Geliştirmeler

- [ ] Gerçek veritabanı entegrasyonu
- [ ] Caching mekanizması
- [ ] Transaction yönetimi
- [ ] Performance optimizasyonları
- [ ] Monitoring ve logging

## 🎯 Onion Architecture Prensipleri

1. **Dependency Inversion**: Infrastructure, domain'e bağımlıdır
2. **Interface Segregation**: Repository interface'i domain'de tanımlanır
3. **Single Responsibility**: Her sınıf tek sorumluluğa sahiptir
4. **Open/Closed**: Yeni implementasyonlar eklenebilir

## 🔧 Konfigürasyon

### Maven Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Application Properties

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

## 📚 İlgili Dosyalar

- **Domain Layer**: `../domain/entities/Product.java`
- **Domain Layer**: `../domain/repositories/ProductRepository.java`
- **Controller Layer**: `../controller/ProductsController.java`
