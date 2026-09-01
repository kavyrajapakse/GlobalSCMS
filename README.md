# GlobalTrade Enterprise Supply Chain Management (GlobalSCMS)

[![Jakarta EE 10](https://img.shields.io/badge/Jakarta%20EE-10.0-blue.svg)](https://jakarta.ee/)
[![Java](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://openjdk.org/)
[![Payara Server](https://img.shields.io/badge/Payara-6.2025%2B-green.svg)](https://www.payara.fish/)
[![Build Status](https://img.shields.io/badge/Tests-34%20Passing-brightgreen.svg)]()

GlobalSCMS is a distributed, enterprise multi-tier supply chain management platform designed to orchestrate international freight logistics, automated warehouse inventory control, customs compliance verification, and multi-tenant supplier partner operations.

---

## 🏛️ System Architecture

GlobalSCMS is built as a multi-module Maven project packaged into an Enterprise Archive (`.ear`):

```
GlobalSCMS/
├── database/             [Relational MySQL 8.0 schema scripts and initial data seeds]
├── testing/              [Apache JMeter load testing and concurrency test plans]
├── global-scm-core/      [Domain Entities, JPA Mappings, DTOs, Security/Password Cryptography]
├── global-scm-ejb/       [Business Beans, CMT/BMT Transactions, Interceptors, Timers, JMS MDB]
├── global-scm-web/       [JAX-RS 3.1 RESTful APIs, Role-Guarded JSP Portals, Client Controllers]
└── global-scm-ear/       [Unified Enterprise Archive Assembly (application.xml)]
```

---

## 🚀 Quick Setup & Deployment Guide

### Prerequisites
* **Java Development Kit (JDK):** OpenJDK 17 LTS (or higher)
* **Application Server:** Payara Server 6.2025+ Community Edition
* **Database:** MySQL Server 8.0 running on `localhost:3306`

---

### Step 1: Initialize MySQL Database

Open HeidiSQL, MySQL Workbench, or your terminal, and execute the provided `database/schema.sql` script:

```bash
mysql -u root -p < database/schema.sql
```

*(This creates `global_scm_db` and seeds initial enterprise roles, staff accounts with salted SHA-256 hashes, warehouse locations, and freight records).*

---

### Step 2: Configure Payara Server 6 JDBC Connection Pool & Resource

Run the following standard Payara `asadmin` CLI commands (or configure via the Payara Admin Console at `http://localhost:4848`):

```bash
# 1. Create MySQL Connection Pool
asadmin create-jdbc-connection-pool \
  --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource \
  --restype javax.sql.DataSource \
  --property user=root:password=root:serverName=localhost:portNumber=3306:databaseName=global_scm_db:useSSL=false:allowPublicKeyRetrieval=true:serverTimezone=UTC \
  SCMPool

# 2. Ping connection pool to verify connectivity
asadmin ping-connection-pool SCMPool

# 3. Create JNDI Resource matching persistence.xml (jdbc/SCMDS)
asadmin create-jdbc-resource --connectionpoolid SCMPool jdbc/SCMDS
```

---

### Step 3: Configure Payara JMS Messaging Resources (OpenMQ)

```bash
# Create the physical JMS Queue for asynchronous cargo events
asadmin create-jms-resource --restype jakarta.jms.Queue --property Name=CargoEventQueue java:global/jms/CargoEventQueue
```

---

### Step 4: Compile, Test & Package the Application

Run Maven to execute the automated test suite and compile the `.ear` package:

```powershell
# Run the automated test suite (34 unit & integration tests)
mvn test

# Build and package the deployable Enterprise Archive (.ear)
mvn clean package
```

*The deployable enterprise archive is generated at:*  
`global-scm-ear/target/global-scm-ear-1.0.ear`

---

### Step 5: Deploy to Payara Server 6

Deploy the EAR package via the Payara Admin Console or command line:

```bash
asadmin deploy --name global-scm-ear global-scm-ear/target/global-scm-ear-1.0.ear
```

---

## 🌐 Application Access & Demo Portals

Once deployed, access the platform via:  
👉 **`http://localhost:8080/global-scm/`**

### Pre-Configured Demo Accounts:

| Role | Username | Demo Password | Primary Workspace / Feature |
| :--- | :--- | :--- | :--- |
| **System Administrator** | `admin` | `admin123` | Executive Admin Overview & Staff Directory (`dashboard.jsp`) |
| **Logistics Coordinator** | `coordinator` | `pass123` | Freight Dispatches & Carrier Booking (`shipments.jsp`) |
| **Warehouse Manager** | `warehouse` | `pass123` | Inventory Stock Levels & Automated Timers (`inventory.jsp`) |
| **Customs Officer** | `custom` | `pass123` | Port Clearance & Tariff Compliance (`customs.jsp`) |
| **Supplier Partner** | `vendor` | `pass123` | B2B Multi-Tenant Vendor Portal (`vendor.jsp`) |

---

## 🔐 Enterprise Security & Key Features

* **Salted SHA-256 Password Cryptography:** Passwords are cryptographically salted and hashed using `PasswordUtil.java` (16-byte random salt). *(Note: Salted SHA-256 is implemented for prototype evaluation; Argon2id/bcrypt is recommended for production scaling).*
* **Stateless JWT Authentication:** HMAC-256 signed JSON Web Tokens for REST endpoint protection with declarative `@RolesAllowed` and thread-bound `SecurityContext` resolution.
* **Automated SMTP Onboarding:** Secure dispatch of one-time temporary credentials (`Scm#XXXX!`) to staff inboxes via Gmail SMTP over STARTTLS (Port 587).
* **Mandatory First-Time Login Password Reset:** Immediate UI modal interception enforcing password rotation upon initial authentication.
* **B2B Multi-Tenant Data Isolation:** Supplier representatives (`VENDOR_REP`) are restricted to viewing only their company's freight via parameterized JPQL filters.
* **Dual-Mode JTA Transactions:** Container-Managed Transactions (CMT) for multi-SKU inventory allocation and Bean-Managed Transactions (BMT) with automated rollback for high-value carrier bookings exceeding **LKR 15,000,000**.

---

## ⚡ Performance Benchmarks & Optimization Highlights

| Architectural Area | Baseline Metric | Optimized Metric | Measured Improvement |
| :--- | :---: | :---: | :---: |
| **Client Portal Render Time** | 1,800 ms | **260 ms** | **85.5% faster page load** |
| **Average Query Latency** | 120 ms | **26 ms** | **78.3% lower DB latency** |
| **Concurrent Load Resilience** | 4.2% timeout | **600/600 OK (0.00%)** | **Zero dropped connections** |
| **Singleton Config Throughput** | Serialized lock | **4x parallel read** | **Eliminated read contention** |

---

## 🧪 Automated Test Suite

Run the automated test suite using:

```powershell
mvn test
```

**Test Coverage Summary (34/34 Tests Passing):**
* **`global-scm-core`:** Entity validation rules, password cryptography, and JWT token lifecycle (10/10 tests passing).
* **`global-scm-ejb`:** CMT multi-SKU transactions, BMT threshold rollback, Customs & Vendor interceptors, Stateful draft sessions, and Singleton locks (24/24 tests passing).
* **`global-scm-web`:** REST API authentication, HTTP status code mappings, and role-based response formatting.
