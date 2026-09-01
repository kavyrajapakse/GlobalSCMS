-- ========================================================
-- GlobalTrade Enterprise Supply Chain Management (GlobalSCMS)
-- Target Database: MySQL Server 8.0+ (InnoDB)
-- ========================================================

CREATE DATABASE IF NOT EXISTS `global_scm_db` 
  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE `global_scm_db`;

-- 1. Roles Table
CREATE TABLE IF NOT EXISTS `roles` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_roles_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- 2. Vendors Table (Suppliers & Freight Partners)
CREATE TABLE IF NOT EXISTS `vendors` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `company_name` VARCHAR(100) NOT NULL,
  `contact_email` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(30) DEFAULT NULL,
  `country` VARCHAR(50) DEFAULT 'Sri Lanka',
  `tax_id` VARCHAR(50) NOT NULL,
  `compliance_rating` DOUBLE DEFAULT '98.5',
  `status` VARCHAR(30) DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vendors_name` (`company_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- 3. Users Table (Enterprise Staff & Vendor Reps)
CREATE TABLE IF NOT EXISTS `users` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(50) NOT NULL,
  `full_name` VARCHAR(100) DEFAULT NULL,
  `email` VARCHAR(100) DEFAULT 'kavithmarajapakse03@gmail.com',
  `phone` VARCHAR(30) DEFAULT '+94 77 123 4567',
  `department` VARCHAR(100) DEFAULT 'Logistics & SCM Operations',
  `password_hash` VARCHAR(255) NOT NULL,
  `ACTIVE` TINYINT(1) NOT NULL DEFAULT '1',
  `requires_password_change` TINYINT(1) NOT NULL DEFAULT '0',
  `vendor_id` BIGINT DEFAULT NULL,
  `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
  `last_login_at` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_users_username` (`USERNAME`),
  KEY `fk_users_vendor` (`vendor_id`),
  CONSTRAINT `fk_users_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `vendors` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- 4. User Roles Association Table
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_ur_role` (`role_id`),
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`ID`),
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Warehouses Table
CREATE TABLE IF NOT EXISTS `warehouses` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(50) NOT NULL,
  `location_code` VARCHAR(20) NOT NULL,
  `capacity_units` INT DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_warehouses_name` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Inventory Items Table (Optimistic Locking via version)
CREATE TABLE IF NOT EXISTS `inventory_items` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `SKU` VARCHAR(32) NOT NULL,
  `item_name` VARCHAR(255) NOT NULL,
  `category` VARCHAR(50) DEFAULT 'General Supply',
  `QUANTITY` INT NOT NULL,
  `min_threshold` INT NOT NULL DEFAULT '30',
  `unit_price_usd` DOUBLE DEFAULT NULL,
  `unit_price_lkr` DOUBLE DEFAULT '4500',
  `warehouse_location` VARCHAR(100) DEFAULT 'Colombo Central Depot',
  `status` VARCHAR(30) DEFAULT 'IN_STOCK',
  `warehouse_id` BIGINT DEFAULT NULL,
  `vendor_id` BIGINT DEFAULT NULL,
  `version` BIGINT DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_inventory_sku` (`SKU`),
  KEY `fk_inventory_warehouse` (`warehouse_id`),
  KEY `fk_inventory_vendor` (`vendor_id`),
  CONSTRAINT `fk_inventory_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `vendors` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_inventory_warehouse` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- 7. Shipments Table (Freight Manifests)
CREATE TABLE IF NOT EXISTS `shipments` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `tracking_number` VARCHAR(64) NOT NULL,
  `ORIGIN` VARCHAR(255) NOT NULL,
  `DESTINATION` VARCHAR(255) NOT NULL,
  `cargo_description` VARCHAR(255) DEFAULT NULL,
  `transport_mode` VARCHAR(20) DEFAULT 'OCEAN',
  `priority` VARCHAR(20) DEFAULT 'STANDARD',
  `weight_kg` DOUBLE DEFAULT NULL,
  `cost_usd` DOUBLE DEFAULT NULL,
  `cost_lkr` DOUBLE DEFAULT NULL,
  `STATUS` VARCHAR(255) NOT NULL DEFAULT 'PENDING',
  `expected_delivery_date` DATE DEFAULT NULL,
  `created_by_user_id` BIGINT DEFAULT NULL,
  `vendor_name` VARCHAR(100) DEFAULT NULL,
  `vendor_id` BIGINT DEFAULT NULL,
  `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
  `VERSION` BIGINT DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_shipments_tracking` (`tracking_number`),
  KEY `fk_shipments_user` (`created_by_user_id`),
  KEY `fk_shipments_vendor` (`vendor_id`),
  CONSTRAINT `fk_shipments_user` FOREIGN KEY (`created_by_user_id`) REFERENCES `users` (`ID`),
  CONSTRAINT `fk_shipments_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `vendors` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- 8. Customs Declarations Table
CREATE TABLE IF NOT EXISTS `customs_filings` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `filing_number` VARCHAR(255) NOT NULL,
  `STATUS` VARCHAR(255) NOT NULL DEFAULT 'PENDING',
  `declaration_details` TEXT,
  `filed_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
  `shipment_id` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_customs_filing` (`filing_number`),
  KEY `fk_customs_shipment` (`shipment_id`),
  CONSTRAINT `fk_customs_shipment` FOREIGN KEY (`shipment_id`) REFERENCES `shipments` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 9. Central System Audit Logs
CREATE TABLE IF NOT EXISTS `audit_logs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(255) NOT NULL,
  `username` VARCHAR(50) DEFAULT NULL,
  `user_id` BIGINT DEFAULT NULL,
  `timestamp` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `details` TEXT,
  PRIMARY KEY (`id`),
  KEY `fk_audit_user` (`user_id`),
  CONSTRAINT `fk_audit_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`ID`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- Seed Initial Enterprise Data
-- ========================================================

-- Roles
INSERT INTO `roles` (`ID`, `name`) VALUES
	(1, 'WAREHOUSE_MANAGER'),
	(2, 'COORDINATOR'),
	(3, 'CUSTOMS_AGENT'),
	(4, 'VENDOR_REP'),
	(5, 'ADMIN')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

-- Warehouses
INSERT INTO `warehouses` (`ID`, `NAME`, `location_code`, `capacity_units`) VALUES
	(1, 'Colombo Central Logistics Depot', 'WH-CMB-01', 50000),
	(2, 'Hambantota Deep Sea Terminal Depot', 'WH-HBT-02', 75000)
ON DUPLICATE KEY UPDATE `NAME`=VALUES(`NAME`);

-- Vendors
INSERT INTO `vendors` (`id`, `company_name`, `contact_email`, `phone`, `country`, `tax_id`, `compliance_rating`, `status`) VALUES
	(1, 'Lanka Freight Ltd', 'operations@lankafreight.lk', '+94 11 234 5678', 'Sri Lanka', 'TAX-LK-9021', 99.2, 'ACTIVE'),
	(2, 'Global Trade Supplies Ltd', 'info@globaltradesupplies.com', '+81 3 5555 0142', 'Japan', 'TAX-JP-8812', 97.8, 'ACTIVE'),
	(3, 'Apex Maritime Logistics', 'support@apexmaritime.sg', '+65 6789 0123', 'Singapore', 'TAX-SG-4410', 95.5, 'ACTIVE'),
	(4, 'Fuji Lanka', 'fujilanka@gmail.com', '+94778945226', 'Sri Lanka', 'TAX-2303', 98.5, 'ACTIVE')
ON DUPLICATE KEY UPDATE `company_name`=VALUES(`company_name`);

-- Users (Default Accounts)
INSERT INTO `users` (`ID`, `USERNAME`, `full_name`, `email`, `phone`, `department`, `password_hash`, `ACTIVE`, `requires_password_change`, `vendor_id`) VALUES
	(1, 'warehouse', 'Ruwan Fernando', 'kavithmarajapakse03@gmail.com', '+94 70 444 5566', 'Depot & Stock Management', 'pass123', 1, 0, NULL),
	(2, 'coordinator', 'Nimal Perera', 'kavithmarajapakse03@gmail.com', '+94 71 222 3344', 'Ocean Freight & Logistics', 'pass123', 1, 0, NULL),
	(3, 'custom', 'Sunil Jayawardena', 'kavithmarajapakse03@gmail.com', '+94 76 333 4455', 'Port Customs Compliance', 'pass123', 1, 0, NULL),
	(4, 'vendor', 'Fuji Lanka Supplier Rep', 'kavithmarajapakse03@gmail.com', '+94 77 555 6677', 'Supplier & Vendor Relations', 'pass123', 1, 0, 4),
	(5, 'admin', 'Kavithma Rajapakse', 'kavithmarajapakse03@gmail.com', '+94 77 111 2233', 'Executive Administration', 'admin123', 1, 0, NULL)
ON DUPLICATE KEY UPDATE `USERNAME`=VALUES(`USERNAME`);

-- User Roles
INSERT IGNORE INTO `user_roles` (`user_id`, `role_id`) VALUES
	(1, 1),
	(2, 2),
	(3, 3),
	(4, 4),
	(5, 5);

-- Inventory Items
INSERT INTO `inventory_items` (`ID`, `SKU`, `item_name`, `category`, `QUANTITY`, `min_threshold`, `unit_price_lkr`, `warehouse_location`, `status`, `version`, `warehouse_id`, `vendor_id`) VALUES
	(1, 'SKU-WH-101', 'Microcontroller Unit MCU-32', 'Semiconductors', 120, 30, 4500, 'Colombo Depot - Bin A1', 'IN_STOCK', 1, 1, 1),
	(2, 'SKU-WH-102', 'High-Capacity Lithium Battery 48V', 'Electronics', 15, 30, 85000, 'Colombo Depot - Bin B4', 'LOW_STOCK', 2, 1, 1),
	(3, 'SKU-WH-103', 'Industrial Fiber Optic Transceiver', 'Telecom', 118, 20, 32000, 'Hambantota Depot - Bin C2', 'IN_STOCK', 3, 2, 1),
	(4, 'SKU-WH-104', 'Stainless Steel Ocean Shipping Bracket', 'Hardware', 430, 50, 1250, 'Colombo Depot - Bin D1', 'IN_STOCK', 2, 1, 1),
	(5, 'SKU-WH-105', 'Industrial Timber Crate (Wood)', 'Furniture', 100, 30, 4500, 'Colombo Central Depot', 'IN_STOCK', 2, 1, 1)
ON DUPLICATE KEY UPDATE `SKU`=VALUES(`SKU`);

-- Shipments
INSERT INTO `shipments` (`ID`, `tracking_number`, `ORIGIN`, `DESTINATION`, `cargo_description`, `transport_mode`, `priority`, `weight_kg`, `cost_lkr`, `STATUS`, `created_by_user_id`, `vendor_name`, `vendor_id`, `VERSION`) VALUES
	(1, 'SCM-TRK-3455', 'Hambantota Port', 'Nagoya Port', 'Industrial electronics manifest', 'OCEAN', 'STANDARD', 1500, 250000, 'DELIVERED', 2, 'Lanka Freight', 1, 3),
	(2, 'SCM-TRK-3400', 'Nagoya Port', 'Hambantota Port', 'High-grade industrial supply cargo', 'OCEAN', 'STANDARD', 2000, 150000, 'BOOKED_WITH_MAERSK', 2, 'Fuji Lanka', 4, 2),
	(3, 'SCM-TRK-7691', 'Nagoya Port', 'Hambantota Port', '20x SKU-WH-105, 20x SKU-WH-104', 'OCEAN', 'STANDARD', 1500, 250000, 'DELIVERED', 2, 'Fuji Lanka', 4, 3)
ON DUPLICATE KEY UPDATE `tracking_number`=VALUES(`tracking_number`);
