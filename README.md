# SuperMarket API - Sistema de Gestión de Ventas

API RESTful desarrollada con Spring Boot para la gestión de ventas de la cadena de supermercados **SuperMarkets Nova**. El sistema permite administrar sucursales, productos, ventas y generar reportes analíticos.

## 📋 Tabla de Contenidos

- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Configuración del Proyecto](#configuración-del-proyecto)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Endpoints de la API](#endpoints-de-la-api)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Reglas de Negocio](#reglas-de-negocio)
- [Manejo de Errores](#manejo-de-errores)

## 🚀 Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **Spring Web MVC**
- **PostgreSQL**
- **Lombok**
- **Maven**

## 📦 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **JDK 21** o superior
- **Maven 3.9.11** o superior
- **PostgreSQL 12** o superior
- Un cliente REST (Postman, Insomnia, Thunder Client, etc.)

## ⚙️ Configuración del Proyecto

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd SuperMarket
```

### 2. Configurar la Base de Datos

Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE SuperMarkets;
```

### 3. Configurar application.properties

El archivo `src/main/resources/application.properties` ya está configurado con los siguientes valores:

```properties
spring.application.name=SuperMarkets

spring.datasource.url=jdbc:postgresql://localhost:5433/SuperMarkets
spring.datasource.username=postgres
spring.datasource.password=admin123
spring.jpa.hibernate.ddl-auto=update
```

### 4. Container Docker
docker run --name SuperMarkets -e POSTGRES_PASSWORD=admin123 -e POSTGRES_USER=postgres -e POSTGRES_DB=SuperMarkets -p 5433:5432 -d postgres



**Nota:** Ajusta el puerto (5433), usuario y contraseña según tu configuración local de PostgreSQL.

### 5. Ejecutar el Proyecto

#### Usando Maven Wrapper (recomendado):

**En Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**En Windows:**
```bash
mvnw.cmd spring-boot:run
```

#### Usando Maven instalado:
```bash
mvn spring-boot:run
```

La aplicación se iniciará en `http://localhost:8080`

## 📁 Estructura del Proyecto

```
src/main/java/com/mcabrera/SuperMarket/
├── controllers/          # Controladores REST
│   ├── BranchController.java
│   ├── ProductController.java
│   ├── SaleController.java
│   └── ReportsController.java
├── services/            # Lógica de negocio
│   ├── BranchService.java
│   ├── ProductService.java
│   ├── SaleService.java
│   └── ReportService.java
├── repositories/        # Acceso a datos (JPA)
│   ├── IBranchRepository.java
│   ├── IProductRepository.java
│   ├── ISaleRepository.java
│   └── ISaleDetailRepository.java
├── entities/           # Entidades JPA
│   ├── Branch.java
│   ├── Product.java
│   ├── Sale.java
│   └── SaleDetail.java
├── dtos/              # Data Transfer Objects
│   ├── BranchDTO.java
│   ├── ProductDTO.java
│   ├── SaleRequestDTO.java
│   ├── SaleResponseDTO.java
│   ├── SaleDetailRequestDTO.java
│   ├── SaleDetailResponseDTO.java
│   └── TopProductDTO.java
├── exceptions/        # Manejo de excepciones
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── BusinessRuleException.java
│   └── DuplicateResourceException.java
└── SuperMarketApplication.java
```

## 🔌 Endpoints de la API

Todos los endpoints están bajo el prefijo: `/api/v1/`

### 🏢 Branch (Sucursales)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/branches` | Crear nueva sucursal |
| GET | `/api/v1/branches` | Listar todas las sucursales |
| GET | `/api/v1/branches/{branch_id}` | Obtener sucursal por ID |
| PUT | `/api/v1/branches/{branch_id}` | Actualizar sucursal |
| DELETE | `/api/v1/branches/{branch_id}` | Eliminar sucursal |

### 📦 Product (Productos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/products` | Crear nuevo producto |
| GET | `/api/v1/products` | Listar todos los productos |
| GET | `/api/v1/products/{product_id}` | Obtener producto por ID |
| PUT | `/api/v1/products/{product_id}` | Actualizar producto |
| DELETE | `/api/v1/products/{product_id}` | Eliminar producto |

### 🛒 Sale (Ventas)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/sales` | Registrar nueva venta |
| GET | `/api/v1/sales` | Listar todas las ventas |
| GET | `/api/v1/sales/{sale_id}` | Obtener venta por ID |
| DELETE | `/api/v1/sales/{sale_id}` | Eliminar venta (restaura stock) |

### 📊 Reports (Reportes)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/reports/branch-sales?branch_id={id}&start={fecha}&end={fecha}` | Reporte de ventas por sucursal y rango de fechas |
| GET | `/api/v1/reports/top-products?limit={n}` | Top N productos más vendidos |

## 📝 Ejemplos de Uso

### 1. Crear una Sucursal

**Request:**
```http
POST /api/v1/branches
Content-Type: application/json

{
  "name": "Sucursal Centro",
  "address": "Av. Principal 123",
  "phone": "2234-5678",
  "branchCode": "SUC001"
}
```

**Response:**
```json
{
  "branchId": 1,
  "name": "Sucursal Centro",
  "address": "Av. Principal 123",
  "phone": "2234-5678",
  "branchCode": "SUC001"
}
```

### 2. Crear un Producto

**Request:**
```http
POST /api/v1/products
Content-Type: application/json

{
  "name": "Coca Cola 2L",
  "description": "Bebida gaseosa sabor cola",
  "price": 15.50,
  "stock": 100,
  "category": "Bebidas",
  "productCode": "PROD001"
}
```

**Response:**
```json
{
  "productId": 1,
  "name": "Coca Cola 2L",
  "description": "Bebida gaseosa sabor cola",
  "price": 15.50,
  "stock": 100,
  "category": "Bebidas",
  "productCode": "PROD001"
}
```

### 3. Registrar una Venta

**Request:**
```http
POST /api/v1/sales
Content-Type: application/json

{
  "branchId": 1,
  "customerName": "Juan Pérez",
  "paymentMethod": "Efectivo",
  "saleDetails": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**Response:**
```json
{
  "saleId": 1,
  "branchId": 1,
  "branchName": "Sucursal Centro",
  "saleDate": "2024-11-26T10:30:00",
  "total": 45.50,
  "customerName": "Juan Pérez",
  "paymentMethod": "Efectivo",
  "saleDetails": [
    {
      "saleDetailId": 1,
      "productId": 1,
      "productName": "Coca Cola 2L",
      "quantity": 2,
      "unitPrice": 15.50,
      "subtotal": 31.00
    },
    {
      "saleDetailId": 2,
      "productId": 2,
      "productName": "Pan Integral",
      "quantity": 1,
      "unitPrice": 14.50,
      "subtotal": 14.50
    }
  ]
}
```

### 4. Reporte de Ventas por Sucursal

**Request:**
```http
GET /api/v1/reports/branch-sales?branch_id=1&start=2024-11-01T00:00:00&end=2024-11-30T23:59:59
```

**Response:**
```json
{
  "branchId": 1,
  "startDate": "2024-11-01T00:00:00",
  "endDate": "2024-11-30T23:59:59",
  "totalSales": 15,
  "totalRevenue": 1250.75,
  "sales": "[...]"
}
```

### 5. Top Productos Más Vendidos

**Request:**
```http
GET /api/v1/reports/top-products?limit=5
```

**Response:**
```json
[
  {
    "productId": 1,
    "productName": "Coca Cola 2L",
    "category": "Bebidas",
    "totalQuantitySold": 150
  },
  {
    "productId": 3,
    "productName": "Arroz 5lb",
    "category": "Granos",
    "totalQuantitySold": 120
  }
]
```

## ⚖️ Reglas de Negocio

### 1. Cálculo Automático de Totales
- El total de la venta se calcula automáticamente sumando los subtotales de cada detalle
- El subtotal de cada detalle se calcula como: `unitPrice × quantity`
- El precio unitario se toma del producto al momento de la venta

### 2. Gestión de Stock
- Al registrar una venta, el stock del producto se reduce automáticamente
- Si el stock es insuficiente, la venta no se registra y se devuelve un error
- Al eliminar una venta, el stock se restaura automáticamente

### 3. Restricciones de Eliminación
- **Productos:** No se puede eliminar un producto que tenga ventas asociadas
- **Sucursales:** No se puede eliminar una sucursal que tenga ventas registradas

### 4. Validaciones
- El precio de un producto debe ser mayor a 0
- El stock no puede ser negativo
- La cantidad en un detalle de venta debe ser mayor a 0
- Los códigos de producto y sucursal deben ser únicos

## ❌ Manejo de Errores

La API utiliza un sistema centralizado de manejo de excepciones con respuestas estructuradas:

### Tipos de Errores

#### 404 - Not Found
```json
{
  "timestamp": "2024-11-26T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado con ID: 999",
  "path": "/api/v1/products/999"
}
```

#### 400 - Bad Request (Regla de Negocio)
```json
{
  "timestamp": "2024-11-26T10:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Stock insuficiente para el producto: Coca Cola 2L. Disponible: 5, Solicitado: 10",
  "path": "/api/v1/sales"
}
```

#### 409 - Conflict (Recurso Duplicado)
```json
{
  "timestamp": "2024-11-26T10:30:00",
  "status": 409,
  "error": "Duplicate Resource",
  "message": "Ya existe un producto con el código: PROD001",
  "path": "/api/v1/products"
}
```

## 🧪 Pruebas

Para probar la API puedes usar:

1. **Postman**: Importa la colección desde el archivo `SuperMarket.postman_collection.json` (si está disponible)
2. **cURL**: Ejemplos de comandos cURL para cada endpoint
3. **Swagger/OpenAPI**: (Opcional - puede agregarse con springdoc-openapi)

### Ejemplo con cURL:

```bash
# Crear sucursal
curl -X POST http://localhost:8080/api/v1/branches \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sucursal Centro",
    "address": "Av. Principal 123",
    "phone": "2234-5678",
    "branchCode": "SUC001"
  }'

# Listar productos
curl -X GET http://localhost:8080/api/v1/products
```

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos.

## 👥 Autor

**mcabrera** - Proyecto SuperMarket API

---

**Nota:** Este proyecto utiliza `spring.jpa.hibernate.ddl-auto=update`, lo que significa que las tablas se crearán/actualizarán automáticamente en la base de datos. Para entornos de producción, se recomienda usar `validate` o `none` y gestionar el esquema con herramientas como Flyway o Liquibase.