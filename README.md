# GlobalTrade Enterprise Supply Chain Management (GlobalSCMS)

[![Jakarta EE 10](https://img.shields.io/badge/Jakarta%20EE-10.0-blue.svg)](https://jakarta.ee/)
[![Java](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://openjdk.org/)
[![Payara Server](https://img.shields.io/badge/Payara-6.2023%2B-green.svg)](https://www.payara.fish/)
[![Build Status](https://img.shields.io/badge/Tests-26%20Passing-brightgreen.svg)]()

GlobalSCMS is a distributed, enterprise multi-tier supply chain management platform designed to orchestrate international freight logistics, automated warehouse inventory control, customs compliance verification, and multi-tenant supplier partner operations.

---

## 🏛️ System Architecture

GlobalSCMS is built as a multi-module Maven project packaged into an Enterprise Archive (`.ear`):

```
GlobalSCMS/
├── global-scm-core/      [Domain Entities, JPA Mappings, DTOs, Security/Password Cryptography]
├── global-scm-ejb/       [Business Beans, CMT/BMT Transactions, Interceptors, Timers, JavaMail SMTP]
├── global-scm-web/       [JAX-RS 3.1 RESTful APIs, Role-Guarded JSP Portals, Client-side Controllers]
└── global-scm-ear/       [Unified Enterprise Archive Assembly (application.xml)]
```

---

## 🚀 Quick Setup & Deployment Guide

### Prerequisites
* **Java Development Kit (JDK):** OpenJDK 17 LTS (or higher)
* **Application Server:** Payara Server 6.2023+ Community Edition
* **Database:** MySQL Server 8.0 running on `localhost:3306`

---

### Step 1: Initialize MySQL Database

Open HeidiSQL, MySQL Workbench, or your terminal, and execute the provided `schema.sql` script:

```bash
mysql -u root -p < schema.sql
```

*(This creates `global_scm_db` and seeds initial enterprise roles, staff accounts, warehouse items, and freight records).*

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

### Step 3: Compile, Test & Package the Application

Run the Maven wrapper to execute all automated test cases and compile the `.ear` package:

```powershell
# Run the automated test suite (26 unit/integration tests)
.\mvnw.cmd test

# Build and package the Enterprise Archive (.ear)
.\mvnw.cmd clean package -DskipTests=true
```

*The deployable enterprise archive is generated at:*  
`global-scm-ear/target/global-scm-ear-1.0.ear`

---

### Step 4: Deploy to Payara Server 6

Deploy the EAR package via the Payara Admin Console or command line:

```bash
asadmin deploy --name global-scm-ear global-scm-ear/target/global-scm-ear-1.0.ear
```

---

## 🌐 Application Access & Demo Portals

Once deployed, access the portal via:  
👉 **`http://localhost:8080/global-scm-web/`**

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

* **Salted SHA-256 Password Cryptography:** Passwords are cryptographically salted and hashed using `PasswordUtil.java`.
* **Zero-Trust JWT Authentication:** Stateless HMAC-256 signed JSON Web Tokens for all REST endpoints with declarative `@RolesAllowed` guards.
* **Real-Time SMTP Onboarding:** Automated delivery of one-time temporary passwords (`Scm#XXXX!`) directly to staff inboxes via Google Cloud TLS.
* **Mandatory First-Time Login Password Reset:** Immediate UI interception requiring new staff to configure a permanent password on first sign-in.
* **B2B Multi-Tenant Data Isolation:** Supplier representatives (`VENDOR_REP`) are restricted to viewing only their company's freight and warehouse stock.
* **Dual-Mode JTA Transactions:** Container-Managed Transactions (CMT) for operational workflows and Bean-Managed Transactions (BMT) with automated rollback for high-value carrier bookings (threshold: LKR 15,000,000).

---

## 🧪 Automated Testing

Run the automated test suite anytime using:

```powershell
.\mvnw.cmd test
```

**Test Coverage Summary:**
* **`global-scm-core`:** Entity invariants, password cryptographic hashing, and JWT token lifecycle.
* **`global-scm-ejb`:** CMT transaction creation, BMT threshold rollback, Customs & Vendor interceptors, and inventory threshold detection.
* **`global-scm-web`:** REST API authentication, HTTP status code mappings, and role-based response formatting.
