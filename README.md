# BankApplication - Sistema Bancario de Microservicios

Sistema de gestión bancaria basado en una arquitectura de microservicios con **Spring Boot**, **Spring Data JPA** y base de datos en memoria **H2**.

---

## 🏛️ Arquitectura del Sistema

El proyecto está dividido en dos microservicios independientes:

| Microservicio | Puerto | Descripción | Base de Datos |
| :--- | :---: | :--- | :---: |
| **`client`** | `8001` | Gestión del ciclo de vida de Clientes y Personas | H2 (`clientdb`) |
| **`account`** | `8000` | Gestión de Cuentas, Movimientos/Transacciones y Reporte de Estado de Cuenta | H2 (`mem:testdb`) |

---

## 🛠️ Tecnologías y Requisitos

- **Java:** 11 o 17 (LTS)
- **Framework:** Spring Boot 2.4.2
- **ORM / Persistencia:** Spring Data JPA / Hibernate
- **Base de datos:** H2 Database Engine (In-Memory)
- **Gestor de dependencias:** Apache Maven
- **Lombok:** 1.18.30

---

## 🚀 Cómo Ejecutar los Microservicios

### 1. Iniciar Microservicio de Clientes (`client`)
Desde la raíz del proyecto:
```bash
# En Linux / Mac:
mvn -f client/pom.xml spring-boot:run

# En Windows:
.\mvnw.cmd -f client/pom.xml spring-boot:run
```
*Disponible en: `http://localhost:8001`*

### 2. Iniciar Microservicio de Cuentas y Transacciones (`account`)
En una nueva terminal:
```bash
# En Linux / Mac:
mvn -f account/pom.xml spring-boot:run

# En Windows:
.\mvnw.cmd -f account/pom.xml spring-boot:run
```
*Disponible en: `http://localhost:8000`*

---

## 🧪 Pruebas Unitarias y de Integración

El proyecto incluye las pruebas unitarias y de integración requeridas (F5 y F6):

```bash
# Ejecutar pruebas de Clientes (sampleTest)
mvn -f client/pom.xml -Dtest=sampleTest test

# Ejecutar pruebas de Cuentas y Transacciones (sampleTest)
mvn -f account/pom.xml -Dtest=sampleTest test

# O mediante el evaluador de HackerEarth / Makefile:
make run      # Ejecuta sampleTest en ambos microservicios
make submit   # Ejecuta mainTest para la entrega final
```

---

## 📖 Especificación de Endpoints

### 1. Microservicio `client` (Puerto `8001`)

Ruta base: `/api/clients`

| Método | Endpoint | Descripción | Body (JSON) / Parámetros | Código de Respuesta |
| :---: | :--- | :--- | :---: | :---: |
| `POST` | `/api/clients` | Crear un cliente | Datos del cliente (`dni`, `name`, `password`, `gender`, `age`, `address`, `phone`, `isActive`) | `201 CREATED` |
| `GET` | `/api/clients` | Listar todos los clientes | Ninguno | `200 OK` |
| `GET` | `/api/clients/{id}` | Consultar cliente por ID | ID en path | `200 OK` / `404` |
| `PUT` | `/api/clients/{id}` | Actualizar datos completos | Objeto cliente completo | `200 OK` |
| `PATCH` | `/api/clients/{id}` | Actualización parcial (estado) | `{"isActive": true/false}` | `200 OK` |
| `DELETE` | `/api/clients/{id}` | Eliminar cliente | ID en path | `200 OK` |

#### Ejemplo de Body (`POST /api/clients`):
```json
{
  "dni": "1712345678",
  "name": "Jose Lema",
  "password": "1234",
  "gender": "Masculino",
  "age": 32,
  "address": "Otavalo sn y principal",
  "phone": "098254785",
  "isActive": true
}
```

---

### 2. Microservicio `account` (Puerto `8000`)

#### A. Cuentas (`/api/accounts`)

| Método | Endpoint | Descripción | Body (JSON) / Parámetros | Código de Respuesta |
| :---: | :--- | :--- | :---: | :---: |
| `POST` | `/api/accounts` | Crear cuenta bancaria | Datos de cuenta (`number`, `type`, `initialAmount`, `isActive`, `clientId`) | `201 CREATED` |
| `GET` | `/api/accounts` | Listar todas las cuentas | Ninguno | `200 OK` |
| `GET` | `/api/accounts/{id}` | Consultar cuenta por ID | ID en path | `200 OK` / `404` |
| `PUT` | `/api/accounts/{id}` | Actualizar cuenta | Objeto cuenta completo | `200 OK` |
| `PATCH` | `/api/accounts/{id}` | Actualización de estado | `{"isActive": true/false}` | `200 OK` |
| `DELETE` | `/api/accounts/{id}` | Eliminar cuenta | ID en path | `200 OK` |

#### Ejemplo de Body (`POST /api/accounts`):
```json
{
  "number": "478758",
  "type": "Ahorro",
  "initialAmount": 2000.0,
  "isActive": true,
  "clientId": 1
}
```

---

#### B. Movimientos / Transacciones (`/api/transactions`)

| Método | Endpoint | Descripción | Body (JSON) / Parámetros | Código de Respuesta |
| :---: | :--- | :--- | :---: | :---: |
| `POST` | `/api/transactions` | Registrar movimiento (Débito/Crédito) | `{"type": "Retiro"|"Deposito", "amount": 575.0, "accountId": 1}` | `201 CREATED` |
| `GET` | `/api/transactions` | Listar todas las transacciones | Ninguno | `200 OK` |
| `GET` | `/api/transactions/{id}` | Consultar transacción por ID | ID en path | `200 OK` / `404` |


#### C. Reporte de Estado de Cuenta (`F4`)

- **Método:** `GET`
- **Endpoint:** `/api/transactions/clients/{clientId}/report`
- **Query Params:**
  - `dateTransactionStart`: Fecha inicial en formato `yyyy-MM-dd`
  - `dateTransactionEnd`: Fecha final en formato `yyyy-MM-dd`

#### Ejemplo de URL:
```
GET http://localhost:8000/api/transactions/clients/1/report?dateTransactionStart=2026-01-01&dateTransactionEnd=2026-12-31
```

#### Ejemplo de Respuesta (`200 OK`):
```json
[
  {
    "date": "2026-10-02T19:30:00.000+00:00",
    "client": "Jose Lema",
    "accountNumber": "478758",
    "accountType": "Ahorro",
    "initialAmount": 2000.0,
    "isActive": true,
    "transactionType": "Retiro",
    "amount": -575.0,
    "balance": 1425.0
  },
  {
    "date": "2026-10-02T20:00:00.000+00:00",
    "client": "Jose Lema",
    "accountNumber": "478758",
    "accountType": "Ahorro",
    "initialAmount": 2000.0,
    "isActive": true,
    "transactionType": "Deposito",
    "amount": 600.0,
    "balance": 2025.0
  }
]
```

---

## 📮 Colección de Postman

El proyecto incluye el archivo [`collection_bank_postman.json`](./collection_bank_postman.json) en la raíz con todas las peticiones listas para importar:

1. Abrir Postman.
2. Hacer clic en **Import** (o presionar `Ctrl + O`).
3. Seleccionar el archivo `collection_bank_postman.json`.
4. Ejecutar las peticiones en orden: **1. Clientes** ➡️ **2. Cuentas** ➡️ **3. Movimientos y Reporte**.

## Compilación del proyecto

<img width="1345" height="801" alt="image" src="https://github.com/user-attachments/assets/a136288e-ac73-4754-be51-879f91d5cac2" />
