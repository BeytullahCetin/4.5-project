# Product Service API Test Kılavuzu

## 🚀 Başlangıç

Product Service başarıyla Onion Architecture prensiplerine uygun şekilde geliştirilmiştir.

### Katman Yapısı

```
product-service/
├── domain/                 # İş mantığı ve entity'ler
│   ├── entities/          # Product, ProductId
│   ├── repositories/      # ProductRepository (interface)
│   └── valueobjects/      # Price, Stock, Currency
├── application/           # Use Cases ve DTOs
│   ├── dtos/             # Request/Response DTOs
│   ├── usecases/         # CRUD use cases
│   └── ports/            # ProductServicePort
├── infrastructure/        # Teknik implementasyonlar
│   ├── entities/         # ProductEntity (JPA)
│   ├── repositories/     # ProductRepositoryImpl
│   └── mappers/          # ProductMapper
└── web/                  # API ve Exception handling
    ├── controllers/      # ProductController
    └── exceptions/       # GlobalExceptionHandler
```

## 📝 API Endpoint'leri

### 1. Ürün Oluşturma

**POST** `/api/v1/products`

```json
{
  "name": "iPhone 15",
  "description": "Apple'ın en yeni akıllı telefonu",
  "price": {
    "amount": 45000.0,
    "currency": "TRY"
  },
  "stock": {
    "quantity": 100
  }
}
```

**Başarılı Yanıt (201 Created):**

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "iPhone 15",
  "description": "Apple'ın en yeni akıllı telefonu",
  "price": {
    "amount": 45000.0,
    "currency": "TRY"
  },
  "stock": {
    "quantity": 100
  }
}
```

### 2. Tüm Ürünleri Listeleme

**GET** `/api/v1/products`

**Başarılı Yanıt (200 OK):**

```json
{
  "products": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "iPhone 15",
      "description": "Apple'ın en yeni akıllı telefonu",
      "price": {
        "amount": 45000.0,
        "currency": "TRY"
      },
      "stock": {
        "quantity": 100
      }
    }
  ],
  "totalCount": 1
}
```

### 3. ID'ye Göre Ürün Getirme

**GET** `/api/v1/products/{id}`

**Başarılı Yanıt (200 OK):**

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "iPhone 15",
  "description": "Apple'ın en yeni akıllı telefonu",
  "price": {
    "amount": 45000.0,
    "currency": "TRY"
  },
  "stock": {
    "quantity": 100
  }
}
```

**Hata Yanıtı (404 Not Found):**

```json
{
  "timestamp": "2025-10-25T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Ürün bulunamadı: invalid-id",
  "path": "/api/v1/products/invalid-id"
}
```

### 4. Ürün Güncelleme

**PUT** `/api/v1/products/{id}`

```json
{
  "name": "iPhone 15 Pro",
  "description": "Güncellenmiş açıklama - Apple'ın en güçlü telefonu",
  "price": {
    "amount": 55000.0,
    "currency": "TRY"
  },
  "stock": {
    "quantity": 150
  }
}
```

**Başarılı Yanıt (200 OK):**

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "iPhone 15 Pro",
  "description": "Güncellenmiş açıklama - Apple'ın en güçlü telefonu",
  "price": {
    "amount": 55000.0,
    "currency": "TRY"
  },
  "stock": {
    "quantity": 150
  }
}
```

### 5. Ürün Silme

**DELETE** `/api/v1/products/{id}`

**Başarılı Yanıt (204 No Content)**

## 🧪 Test Senaryoları

### cURL ile Test

#### 1. Ürün Oluşturma

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "description": "Apple'\''ın en yeni akıllı telefonu",
    "price": {
      "amount": 45000.00,
      "currency": "TRY"
    },
    "stock": {
      "quantity": 100
    }
  }'
```

#### 2. Tüm Ürünleri Listeleme

```bash
curl http://localhost:8080/api/v1/products
```

#### 3. ID'ye Göre Ürün Getirme

```bash
curl http://localhost:8080/api/v1/products/{id}
```

#### 4. Ürün Güncelleme

```bash
curl -X PUT http://localhost:8080/api/v1/products/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Güncellenmiş açıklama",
    "price": {
      "amount": 55000.00,
      "currency": "TRY"
    },
    "stock": {
      "quantity": 150
    }
  }'
```

#### 5. Ürün Silme

```bash
curl -X DELETE http://localhost:8080/api/v1/products/{id}
```

### Validation Test Senaryoları

#### Geçersiz Ürün Adı (Minimum 2 karakter)

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "A",
    "description": "Test açıklama",
    "price": {
      "amount": 100.00,
      "currency": "TRY"
    },
    "stock": {
      "quantity": 10
    }
  }'
```

**Beklenen Yanıt (400 Bad Request):**

```json
{
  "timestamp": "2025-10-25T12:00:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Girdi doğrulama hatası",
  "path": "/api/v1/products",
  "validationErrors": {
    "name": "Ürün adı minimum 2 karakter olmalıdır"
  }
}
```

#### Negatif Fiyat

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "Test açıklama",
    "price": {
      "amount": -100.00,
      "currency": "TRY"
    },
    "stock": {
      "quantity": 10
    }
  }'
```

#### Desteklenmeyen Para Birimi

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "Test açıklama",
    "price": {
      "amount": 100.00,
      "currency": "XYZ"
    },
    "stock": {
      "quantity": 10
    }
  }'
```

## 🔍 PostgreSQL Database Erişimi

PostgreSQL database'e erişmek için:

### psql ile:

```bash
psql -U postgres -h localhost -d productdb
```

### pgAdmin ile:

1. pgAdmin'i açın
2. Server → Databases → productdb

### Bağlantı Bilgileri:

- **Host**: localhost
- **Port**: 5432
- **Database**: productdb
- **Username**: postgres
- **Password**: postgres

Detaylı kurulum için: `POSTGRESQL_SETUP.md`

## ✅ Başarı Kriterleri

- [x] Onion Architecture prensiplerine uygun katman yapısı
- [x] Dependency Inversion - Doğru bağımlılık yönü
- [x] Clean Code - Okunabilir, anlaşılır kod
- [x] Error Handling - Exception handling implementasyonu
- [x] Validation - Input validation'lar
- [x] RESTful API - HTTP standartlarına uygun endpoint'ler
- [x] DTO Pattern - Request/Response DTO'ları

## 🎉 Tebrikler!

Product Service başarıyla tamamlandı. Tüm CRUD işlemleri Onion Architecture prensiplerine uygun şekilde implement edildi.
