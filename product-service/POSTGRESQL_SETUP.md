# PostgreSQL Kurulum ve Yapılandırma Kılavuzu

## 🐘 PostgreSQL ile Çalışma

Product Service artık **PostgreSQL 5432** portu üzerinden çalışacak şekilde yapılandırılmıştır.

## 📋 Gereksinimler

- PostgreSQL 12 veya üzeri
- Port: 5432 (varsayılan)

## 🚀 PostgreSQL Kurulumu

### Windows:

```bash
# PostgreSQL resmi sitesinden indirebilirsiniz:
https://www.postgresql.org/download/windows/

# veya Chocolatey ile:
choco install postgresql
```

### Docker ile PostgreSQL (Önerilen):

```bash
docker run --name product-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=productdb \
  -p 5432:5432 \
  -d postgres:15
```

### Docker Compose ile:

```yaml
version: "3.8"
services:
  postgres:
    image: postgres:15
    container_name: product-postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: productdb
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

Kaydedin ve çalıştırın:

```bash
docker-compose up -d
```

## 🔧 Database Oluşturma

### PostgreSQL Client ile:

```bash
# PostgreSQL'e bağlan
psql -U postgres -h localhost

# Database oluştur
CREATE DATABASE productdb;

# Database'i kontrol et
\l

# Database'e geç
\c productdb

# Çıkış
\q
```

### pgAdmin ile:

1. pgAdmin'i açın
2. Server'a sağ tıklayın → Create → Database
3. Database adı: `productdb`
4. Owner: `postgres`
5. Save

## ⚙️ Yapılandırma Detayları

### application.yml Ayarları:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/productdb
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: postgres
    hikari:
      maximum-pool-size: 10 # Maksimum connection pool boyutu
      minimum-idle: 5 # Minimum idle connection sayısı
      connection-timeout: 30000 # Connection timeout (ms)

  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update # Otomatik tablo oluşturma/güncelleme
    show-sql: true # SQL logları göster
```

### Farklı Ortamlar için Yapılandırma:

#### Development (application-dev.yml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/productdb_dev
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

#### Production (application-prod.yml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://prod-server:5432/productdb_prod
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate # Production'da sadece validate
    show-sql: false
```

## 🔍 Database Kontrol

### Tablo Kontrolü:

```sql
-- Oluşturulan tabloları görüntüle
\dt

-- products tablosunu görüntüle
SELECT * FROM products;

-- Tablo yapısını görüntüle
\d products
```

### Örnek SQL Sorguları:

```sql
-- Tüm ürünleri listele
SELECT id, name, description, price_amount, price_currency, stock_quantity
FROM products;

-- Fiyata göre sıralama
SELECT * FROM products ORDER BY price_amount DESC;

-- Stokta olan ürünler
SELECT * FROM products WHERE stock_quantity > 0;

-- Para birimine göre filtreleme
SELECT * FROM products WHERE price_currency = 'TRY';
```

## 📊 products Tablosu Yapısı

Hibernate tarafından otomatik oluşturulacak tablo yapısı:

```sql
CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    price_amount NUMERIC(19, 2) NOT NULL,
    price_currency VARCHAR(3) NOT NULL,
    stock_quantity INTEGER NOT NULL
);
```

## 🔒 Güvenlik İpuçları

### 1. Güçlü Parola Kullanın:

```yaml
spring:
  datasource:
    password: ${DB_PASSWORD} # Environment variable kullanın
```

### 2. Connection Pool Ayarları:

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

### 3. Production için SSL:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/productdb?ssl=true&sslmode=require
```

## 🐛 Sorun Giderme

### Hata: Connection refused

```
Çözüm:
1. PostgreSQL servisinin çalıştığından emin olun
2. Port 5432'nin açık olduğunu kontrol edin
3. Firewall ayarlarını kontrol edin
```

### Hata: Database "productdb" does not exist

```
Çözüm:
Database'i manuel olarak oluşturun:
psql -U postgres -c "CREATE DATABASE productdb;"
```

### Hata: Authentication failed

```
Çözüm:
1. Username ve password'ü kontrol edin
2. pg_hba.conf dosyasındaki authentication ayarlarını kontrol edin
```

### Docker Container'ı Kontrol Etme:

```bash
# Container'ın çalışıp çalışmadığını kontrol et
docker ps | grep postgres

# Container loglarını görüntüle
docker logs product-postgres

# Container'ı yeniden başlat
docker restart product-postgres

# Container'a bağlan
docker exec -it product-postgres psql -U postgres -d productdb
```

## 📈 Performans İpuçları

### Index Oluşturma:

```sql
-- Name üzerinde index
CREATE INDEX idx_product_name ON products(name);

-- Price üzerinde index
CREATE INDEX idx_product_price ON products(price_amount);

-- Currency üzerinde index
CREATE INDEX idx_product_currency ON products(price_currency);
```

### Monitoring:

```sql
-- Aktif bağlantıları görüntüle
SELECT * FROM pg_stat_activity WHERE datname = 'productdb';

-- Tablo boyutunu görüntüle
SELECT pg_size_pretty(pg_total_relation_size('products'));
```

## 🧪 Test

Uygulama başlatıldığında PostgreSQL bağlantısını test edin:

```bash
# Uygulamayı başlat
mvn spring-boot:run

# Logları kontrol et
# Başarılı bağlantı için şu satırları görmelisiniz:
# HikariPool-1 - Starting...
# HikariPool-1 - Start completed.
```

## 🔄 H2'den PostgreSQL'e Geçiş

Eğer test için H2 kullanmak isterseniz, test klasörü altında `application-test.yml` oluşturun:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

## 📚 Faydalı Komutlar

```bash
# PostgreSQL servisi başlat (Windows)
net start postgresql-x64-15

# PostgreSQL servisi durdur (Windows)
net stop postgresql-x64-15

# PostgreSQL servisi durumu (Linux/Mac)
sudo systemctl status postgresql

# PostgreSQL servisi başlat (Linux/Mac)
sudo systemctl start postgresql

# Backup al
pg_dump -U postgres productdb > backup.sql

# Backup'tan geri yükle
psql -U postgres productdb < backup.sql
```

## ✅ Hazırlık Kontrol Listesi

- [ ] PostgreSQL kuruldu
- [ ] PostgreSQL servisi çalışıyor
- [ ] `productdb` database'i oluşturuldu
- [ ] pom.xml'de PostgreSQL driver var
- [ ] application.yml yapılandırıldı
- [ ] Bağlantı bilgileri doğru
- [ ] Firewall/Port ayarları yapıldı

## 🎉 Tamamlandı!

Product Service artık PostgreSQL ile çalışmaya hazır!

Uygulamayı başlattığınızda Hibernate otomatik olarak `products` tablosunu oluşturacaktır.
