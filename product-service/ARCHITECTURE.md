# Product Service - Onion Architecture

## 🏗️ Mimari Yapı

Bu proje **Onion Architecture** (Soğan Mimarisi) prensiplerine göre tasarlanmıştır.

```
┌─────────────────────────────────────────────────────────────┐
│                      WEB LAYER (Dış Katman)                 │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ ProductController                                     │  │
│  │ - getAllProducts()                                    │  │
│  │ - getProductById(id)                                  │  │
│  │ - createProduct(request)                              │  │
│  │ - updateProduct(id, request)                          │  │
│  │ - deleteProduct(id)                                   │  │
│  └────────────────────┬──────────────────────────────────┘  │
│                       │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │ GlobalExceptionHandler                               │  │
│  │ - handleValidationExceptions()                       │  │
│  │ - handleProductNotFoundException()                   │  │
│  │ - handleIllegalArgumentException()                   │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────┬───────────────────────────────────┘
                          │ Dependency Inversion
                          │ (ProductServicePort)
┌─────────────────────────▼───────────────────────────────────┐
│               APPLICATION LAYER (Uygulama Katmanı)          │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ ProductService (implements ProductServicePort)       │  │
│  │ - Use Case'leri orkestre eder                        │  │
│  └────────────────────┬─────────────────────────────────┘  │
│                       │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │ Use Cases:                                           │  │
│  │ - CreateProductUseCase                               │  │
│  │ - GetProductByIdUseCase                              │  │
│  │ - GetAllProductsUseCase                              │  │
│  │ - UpdateProductUseCase                               │  │
│  │ - DeleteProductUseCase                               │  │
│  └────────────────────┬─────────────────────────────────┘  │
│                       │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │ DTOs (Data Transfer Objects):                        │  │
│  │ - CreateProductRequest                               │  │
│  │ - UpdateProductRequest                               │  │
│  │ - ProductResponse                                    │  │
│  │ - ProductListResponse                                │  │
│  │ - PriceDto, StockDto                                 │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                  DOMAIN LAYER (İş Mantığı - Merkez)         │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ Product (Aggregate Root)                             │  │
│  │ - ProductId (Value Object)                           │  │
│  │ - create()                                           │  │
│  │ - reconstruct()                                      │  │
│  │ - updateProduct()                                    │  │
│  │ - updatePrice()                                      │  │
│  │ - updateStock()                                      │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ Value Objects:                                       │  │
│  │ - Price (amount, currency)                           │  │
│  │ - Stock (quantity)                                   │  │
│  │ - Currency (TRY, USD, EUR, GBP)                      │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ Repository Interface:                                │  │
│  │ - ProductRepository (Port)                           │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────┬───────────────────────────────────┘
                          │ Dependency Inversion
                          │ (ProductRepository)
┌─────────────────────────▼───────────────────────────────────┐
│           INFRASTRUCTURE LAYER (Teknik Detaylar)            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ ProductRepositoryImpl                                │  │
│  │ (implements ProductRepository)                       │  │
│  │ - save()                                             │  │
│  │ - findById()                                         │  │
│  │ - findAll()                                          │  │
│  │ - deleteById()                                       │  │
│  │ - existsById()                                       │  │
│  └────────────────────┬─────────────────────────────────┘  │
│                       │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │ JpaProductRepository                                 │  │
│  │ (Spring Data JPA Interface)                          │  │
│  └────────────────────┬─────────────────────────────────┘  │
│                       │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │ ProductEntity (JPA Entity)                           │  │
│  │ @Entity, @Table                                      │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ ProductMapper                                        │  │
│  │ - toDomain(entity)                                   │  │
│  │ - toEntity(domain)                                   │  │
│  └────────────────────┬─────────────────────────────────┘  │
│                       │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │ PostgreSQL Database (Port 5432)                      │  │
│  │ - products table                                     │  │
│  └──────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

## 📂 Klasör Yapısı

```
product-service/
└── src/main/java/com/turkcell/product_service/
    ├── domain/                         # ⭕ MERKEZ - İş mantığı
    │   ├── entities/
    │   │   └── Product.java           # Aggregate Root + ProductId
    │   ├── repositories/
    │   │   └── ProductRepository.java # Repository Port (Interface)
    │   └── valueobjects/
    │       ├── Currency.java          # Para birimi
    │       ├── Price.java             # Fiyat (amount + currency)
    │       └── Stock.java             # Stok miktarı
    │
    ├── application/                    # 📋 İş Akışları
    │   ├── dtos/
    │   │   ├── CreateProductRequest.java
    │   │   ├── UpdateProductRequest.java
    │   │   ├── ProductResponse.java
    │   │   ├── ProductListResponse.java
    │   │   ├── PriceDto.java
    │   │   └── StockDto.java
    │   ├── ports/
    │   │   └── ProductServicePort.java # Service Port (Interface)
    │   ├── services/
    │   │   └── ProductService.java     # Service Implementation
    │   └── usecases/
    │       ├── CreateProductUseCase.java
    │       ├── GetProductByIdUseCase.java
    │       ├── GetAllProductsUseCase.java
    │       ├── UpdateProductUseCase.java
    │       └── DeleteProductUseCase.java
    │
    ├── infrastructure/                 # 🔧 Teknik Implementasyonlar
    │   ├── entities/
    │   │   └── ProductEntity.java      # JPA Entity
    │   ├── mappers/
    │   │   └── ProductMapper.java      # Domain ↔ Entity Mapper
    │   └── repositories/
    │       ├── JpaProductRepository.java       # Spring Data JPA
    │       └── ProductRepositoryImpl.java      # Repository Implementation
    │
    └── web/                            # 🌐 API Layer
        ├── controllers/
        │   └── ProductController.java  # REST Controller
        └── exceptions/
            ├── ErrorResponse.java
            ├── ProductNotFoundException.java
            └── GlobalExceptionHandler.java
```

## 🎯 Dependency Inversion Principle

### Bağımlılık Yönü (Dışarıdan İçe Doğru):

```
Web Layer → Application Layer → Domain Layer
                ↓
Infrastructure Layer → Domain Layer
```

### Temel Prensipler:

1. **Domain Layer** hiçbir katmana bağımlı DEĞİLDİR
2. **Application Layer** sadece Domain'e bağımlıdır
3. **Infrastructure Layer** Domain'e bağımlıdır (Interface'ler aracılığıyla)
4. **Web Layer** Application'a bağımlıdır (ProductServicePort aracılığıyla)

## 🔄 Veri Akışı

### Create Product Flow:

```
1. ProductController
   ↓ CreateProductRequest
2. ProductService (ProductServicePort)
   ↓
3. CreateProductUseCase
   ↓ Domain entities (Product, Price, Stock)
4. ProductRepository (Interface - Domain)
   ↓
5. ProductRepositoryImpl (Infrastructure)
   ↓ ProductMapper.toEntity()
6. JpaProductRepository (Spring Data)
   ↓
7. PostgreSQL Database (Port 5432)
   ↓ ProductEntity saved
8. ProductMapper.toDomain()
   ↓ Product (Domain)
9. CreateProductUseCase
   ↓ ProductResponse (DTO)
10. ProductController
   ↓ JSON Response (201 Created)
```

## ✅ Onion Architecture Kontrol Listesi

- [x] **Domain katmanı hiçbir şeye bağımlı değil** ✓
- [x] **Dependency Inversion** - Interface'ler Domain'de, implementasyonlar Infrastructure'da ✓
- [x] **Port & Adapter Pattern** - ProductServicePort, ProductRepository ✓
- [x] **DTO Pattern** - Katmanlar arası veri transferi için DTO kullanımı ✓
- [x] **Use Case Pattern** - Her iş akışı için ayrı use case ✓
- [x] **Value Objects** - Price, Stock, Currency immutable value objects ✓
- [x] **Aggregate Root** - Product entity ✓
- [x] **Repository Pattern** - Veri erişim soyutlaması ✓
- [x] **Exception Handling** - Global exception handler ✓
- [x] **Validation** - Bean validation ile input kontrolü ✓

## 🚀 Kullanım

### Application Başlatma:

```bash
cd product-service
mvn spring-boot:run
```

### Endpoints:

- `GET    /api/v1/products` - Tüm ürünleri listele
- `GET    /api/v1/products/{id}` - Ürün detayı
- `POST   /api/v1/products` - Yeni ürün oluştur
- `PUT    /api/v1/products/{id}` - Ürün güncelle
- `DELETE /api/v1/products/{id}` - Ürün sil

### PostgreSQL Database:

- Host: `localhost`
- Port: `5432`
- Database: `productdb`
- Username: `postgres`
- Password: `postgres`

Detaylı bilgi için: `POSTGRESQL_SETUP.md`

## 📚 Daha Fazla Bilgi

- Detaylı API testleri için: `API_TEST_GUIDE.md`
- Ödev gereksinimleri için: `../odevler/gun1.md`
