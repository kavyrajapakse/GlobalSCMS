<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logistics Operations & Freight Management | GlobalTrade SCM</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg-dark: #090d16;
            --bg-surface: #0f172a;
            --bg-card: #1e293b;
            --text-primary: #f8fafc;
            --text-secondary: #94a3b8;
            --text-muted: #64748b;
            --brand-primary: #2563eb;
            --brand-accent: #38bdf8;
            --brand-glow: rgba(37, 99, 235, 0.25);
            --success: #10b981;
            --warning: #f59e0b;
            --danger: #ef4444;
            --border-dark: #334155;
            --radius-sm: 8px;
            --radius-md: 12px;
            --radius-lg: 20px;
            --sidebar-width: 260px;
            --transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
        }

        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: var(--bg-dark); color: var(--text-primary); min-height: 100vh; display: flex; }

        /* Left Sidebar Navigation */
        .sidebar { width: var(--sidebar-width); height: 100vh; background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(16px); border-right: 1px solid var(--border-dark); position: fixed; left: 0; top: 0; display: flex; flex-direction: column; justify-content: space-between; padding: 24px 16px; z-index: 100; }
        .sidebar-brand { display: flex; align-items: center; gap: 12px; font-size: 18px; font-weight: 800; color: white; text-decoration: none; padding: 0 8px 24px; border-bottom: 1px solid var(--border-dark); }
        .brand-logo { width: 36px; height: 36px; background: linear-gradient(135deg, var(--brand-primary), var(--brand-accent)); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; color: white; }

        .sidebar-nav { display: flex; flex-direction: column; gap: 6px; margin-top: 24px; flex-grow: 1; }
        .nav-item { display: flex; align-items: center; gap: 12px; padding: 12px 16px; color: var(--text-secondary); text-decoration: none; font-size: 14px; font-weight: 600; border-radius: var(--radius-md); transition: var(--transition); }
        .nav-item.active, .nav-item:hover { background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); border: 1px solid rgba(56, 189, 248, 0.3); }

        .sidebar-footer { padding-top: 16px; border-top: 1px solid var(--border-dark); }
        .user-info { display: flex; align-items: center; justify-content: space-between; }
        .role-badge { font-size: 11px; font-weight: 700; background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); padding: 4px 10px; border-radius: 20px; border: 1px solid rgba(56, 189, 248, 0.3); }
        .btn-logout { background: transparent; border: 1px solid var(--border-dark); color: #fca5a5; padding: 6px 12px; border-radius: var(--radius-sm); font-size: 12px; font-weight: 600; text-decoration: none; }

        /* Main Content Layout */
        .main-content { margin-left: var(--sidebar-width); flex-grow: 1; padding: 32px 40px; min-height: 100vh; }
        .page-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 24px; }
        .page-header h1 { font-size: 30px; font-weight: 800; letter-spacing: -0.5px; margin-bottom: 6px; }
        .page-header p { color: var(--text-secondary); font-size: 14px; }

        /* Sub-Feature Tabs Navigation */
        .sub-tabs { display: flex; gap: 12px; margin-bottom: 24px; border-bottom: 1px solid var(--border-dark); padding-bottom: 12px; }
        .tab-btn { background: transparent; border: none; color: var(--text-secondary); font-family: inherit; font-size: 14px; font-weight: 600; padding: 8px 16px; border-radius: var(--radius-sm); cursor: pointer; transition: var(--transition); }
        .tab-btn.active, .tab-btn:hover { background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); }

        .tab-content { display: none; }
        .tab-content.active { display: block; }

        /* Metric Grid Cards */
        .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 24px; }
        .stat-card { background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(12px); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 20px; }
        .stat-title { font-size: 11px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.5px; }
        .stat-value { font-size: 28px; font-weight: 800; color: white; margin: 8px 0 4px; }
        .stat-sub { font-size: 12px; color: var(--text-secondary); }

        /* Status Distribution & Automated Tracking Card */
        .top-row-cards { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-bottom: 24px; }
        .analytics-card { background: rgba(15, 23, 42, 0.75); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 20px; }
        .analytics-title { font-size: 13px; font-weight: 700; color: var(--text-secondary); margin-bottom: 12px; display: flex; justify-content: space-between; }
        .progress-bar-wrap { height: 10px; background: rgba(30, 41, 59, 0.8); border-radius: 5px; overflow: hidden; display: flex; }
        .bar-transit { background: var(--brand-accent); height: 100%; transition: width 0.3s; }
        .bar-delivered { background: var(--success); height: 100%; transition: width 0.3s; }
        .bar-pending { background: var(--warning); height: 100%; transition: width 0.3s; }

        .timer-card { background: rgba(15, 23, 42, 0.75); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 20px; }

        .panel { background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(12px); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 24px; margin-bottom: 24px; }
        .panel h3 { font-size: 18px; font-weight: 700; margin-bottom: 6px; }
        .panel-desc { font-size: 13px; color: var(--text-secondary); margin-bottom: 20px; }

        table { width: 100%; border-collapse: collapse; margin-top: 8px; }
        th, td { padding: 14px; text-align: left; border-bottom: 1px solid var(--border-dark); font-size: 13px; }
        th { font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 11px; }

        .pill { padding: 4px 10px; border-radius: 20px; font-size: 11px; font-weight: 700; }
        .pill-blue { background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); }
        .pill-green { background: rgba(16, 185, 129, 0.15); color: var(--success); }
        .pill-yellow { background: rgba(245, 158, 11, 0.15); color: var(--warning); }

        .btn-table-action { padding: 6px 10px; background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); border: 1px solid rgba(56, 189, 248, 0.3); border-radius: var(--radius-sm); font-size: 12px; font-weight: 600; cursor: pointer; transition: var(--transition); margin-right: 4px; }
        .btn-table-action:hover { background: var(--brand-primary); color: white; }

        .form-grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-size: 12px; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }
        .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 11px 14px; background: rgba(30, 41, 59, 0.8); border: 1px solid var(--border-dark); border-radius: var(--radius-md); color: white; font-family: inherit; font-size: 14px; }

        .btn-action-primary { padding: 12px 20px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); box-shadow: 0 4px 12px var(--brand-glow); }
        .btn-submit { width: 100%; padding: 13px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); }

        .modal-backdrop { position: fixed; inset: 0; background: rgba(9, 13, 22, 0.8); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 200; }
        .modal-card { width: 100%; max-width: 580px; background: var(--bg-surface); border: 1px solid var(--border-dark); border-radius: var(--radius-lg); padding: 32px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.7); max-height: 90vh; overflow-y: auto; }
    </style>
</head>
<body>

    <!-- Left Sidebar Navigation -->
    <aside class="sidebar">
        <div>
            <a href="#" class="sidebar-brand">
                <div class="brand-logo">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L2 7L12 12L22 7L12 2Z"/><path d="M2 17L12 22L22 17"/><path d="M2 12L12 17L22 12"/></svg>
                </div>
                <span>GlobalTrade SCM</span>
            </a>

            <nav class="sidebar-nav">
                <a href="dashboard.jsp" id="side-overview" class="nav-item" style="display: none;" onclick="return checkNavAccess(event, ['ADMIN'], 'Admin Overview')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
                    <span>Admin Overview</span>
                </a>
                <a href="shipments.jsp" id="side-shipments" class="nav-item active">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 17L12 22L22 17"/><path d="M2 12L12 17L22 12"/><path d="M12 2L2 7L12 12L22 7L12 2Z"/></svg>
                    <span>Shipments & Freight</span>
                </a>
                <a href="inventory.jsp" id="side-inventory" class="nav-item" onclick="return checkNavAccess(event, ['ADMIN', 'WAREHOUSE_MANAGER'], 'Warehouse Stock')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
                    <span>Warehouse Stock</span>
                </a>
                <a href="customs.jsp" id="side-customs" class="nav-item" onclick="return checkNavAccess(event, ['ADMIN', 'CUSTOMS_AGENT'], 'Customs Clearance')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
                    <span>Customs Clearance</span>
                </a>
                <a href="vendor.jsp" id="side-vendor" class="nav-item" onclick="return checkNavAccess(event, ['ADMIN', 'VENDOR_REP'], 'Vendor Portal')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                    <span>Vendor Portal</span>
                </a>
            </nav>
        </div>

        <div class="sidebar-footer">
            <div class="user-info">
                <span class="role-badge" id="role-badge">COORDINATOR</span>
                <a href="index.html" class="btn-logout" onclick="localStorage.clear()">Logout</a>
            </div>
        </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
        <div class="page-header">
            <div>
                <h1>Logistics Operations & Freight Management</h1>
                <p>Manage ocean cargo dispatches, carrier capacity bookings, and vessel manifests.</p>
            </div>
            <button class="btn-action-primary" onclick="openCreateModal()">+ Create New Cargo Shipment</button>
        </div>

        <!-- Sub-Feature Navigation Tabs -->
        <div class="sub-tabs">
            <button class="tab-btn active" onclick="switchTab('manifests-tab', this)">🗺️ Operations & Live Manifests</button>
            <button class="tab-btn" onclick="switchTab('booking-tab', this)">🚢 Carrier Capacity Booking</button>
            <button class="tab-btn" onclick="switchTab('alerts-tab', this)">🔔 Dispatch Alerts</button>
        </div>

        <!-- Tab 1: Live Freight Manifests Table -->
        <div id="manifests-tab" class="tab-content active">
            
            <!-- Top Metric Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-title">Total Active Dispatches</div>
                    <div class="stat-value" id="count-total">0</div>
                    <div class="stat-sub">Global Logistics Fleet</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">In Transit (Ocean/Air)</div>
                    <div class="stat-value" id="count-transit" style="color: var(--brand-accent);">0</div>
                    <div class="stat-sub">Active Ocean Cargo</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Pending Booking</div>
                    <div class="stat-value" id="count-pending" style="color: var(--warning);">0</div>
                    <div class="stat-sub">Awaiting Capacity Allocation</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Delivered Manifests</div>
                    <div class="stat-value" id="count-delivered" style="color: var(--success);">0</div>
                    <div class="stat-sub">Port Cleared & Unloaded</div>
                </div>
            </div>

            <!-- Status Distribution & Automated Tracking Card -->
            <div class="top-row-cards">
                <div class="analytics-card">
                    <div class="analytics-title">
                        <span>Fleet Transport Distribution</span>
                        <span id="analytics-legend" style="font-size: 11px; font-weight: 600;">Loading breakdown...</span>
                    </div>
                    <div class="progress-bar-wrap">
                        <div id="bar-transit" class="bar-transit" style="width: 0%;"></div>
                        <div id="bar-delivered" class="bar-delivered" style="width: 0%;"></div>
                        <div id="bar-pending" class="bar-pending" style="width: 0%;"></div>
                    </div>
                </div>

                <div class="timer-card">
                    <div style="font-size: 12px; font-weight: 700; color: var(--brand-accent); text-transform: uppercase; margin-bottom: 6px;">⏱️ Automated Tracking Service</div>
                    <div style="font-size: 13px; color: var(--text-secondary);">Tracking Cycle: <strong style="color: white;">Every 5 Minutes</strong></div>
                    <div style="font-size: 13px; color: var(--text-secondary); margin-top: 2px;">Status: <span style="color: var(--success); font-weight: 700;">● Active Monitoring Service</span></div>
                </div>
            </div>

            <!-- Live Freight Manifests Table -->
            <div class="panel">
                <h3>Active Freight Manifests</h3>
                <p class="panel-desc">Real-time ocean, air, and rail freight shipments synchronized directly with central database records.</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tracking #</th>
                            <th>Vendor</th>
                            <th>Origin ➔ Destination</th>
                            <th>Transport / Priority</th>
                            <th>Cost (LKR)</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="shipments-table-body">
                        <tr><td colspan="8" style="color: var(--text-muted);">Loading live manifests...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Tab 2: Carrier Capacity Booking -->
        <div id="booking-tab" class="tab-content">
            <div class="panel" style="max-width: 600px;">
                <h3>🚢 Carrier Capacity Booking</h3>
                <p class="panel-desc">Reserve container capacity with global ocean carriers under Bean-Managed Transaction control.</p>

                <form id="bmt-booking-form">
                    <div class="form-group">
                        <label for="shipment-id">Shipment ID</label>
                        <input type="number" id="shipment-id" placeholder="e.g. 1" required>
                    </div>

                    <div class="form-group">
                        <label for="carrier-code">Ocean Carrier Line</label>
                        <select id="carrier-code">
                            <option value="MAERSK">Maersk Line Ocean Shipping</option>
                            <option value="MSC">MSC Mediterranean Shipping</option>
                            <option value="COSCO">COSCO Shipping Freight</option>
                            <option value="CMA_CGM">CMA CGM Logistics</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="cost-usd">Container Rate (LKR)</label>
                        <input type="number" id="cost-usd" value="150000" placeholder="Container Rate" required>
                        <small style="color: var(--text-muted); font-size: 11px;">Container budget limit: LKR 15,000,000. Costs exceeding budget will be rejected.</small>
                    </div>

                    <button type="submit" class="btn-submit">Confirm Carrier Capacity Booking</button>
                </form>

                <div id="bmt-response" style="margin-top: 20px; display: none; padding: 14px; border-radius: 8px; font-size: 13px; font-weight: 600;"></div>
            </div>
        </div>

        <!-- Tab 3: Dispatch Alerts -->
        <div id="alerts-tab" class="tab-content">
            <div class="panel">
                <h3>🔔 Real-Time Dispatch & System Alerts</h3>
                <p class="panel-desc">Live logistics event notifications and automated milestone alerts.</p>
                
                <div id="alerts-stream-container" style="display: flex; flex-direction: column; gap: 12px; margin-top: 16px;">
                    <div style="color: var(--text-secondary);">Loading alerts stream...</div>
                </div>
            </div>
        </div>

    </main>

    <!-- Modal Form: Create New Cargo Shipment -->
    <div id="create-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">Create New Cargo Shipment</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Dispatch new ocean, air, or rail freight shipment into primary database.</p>
            
            <form id="create-shipment-form">
                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="new-tracking">Tracking Number</label>
                        <input type="text" id="new-tracking" placeholder="e.g. SCM-TRK-4001" required>
                    </div>
                    <div class="form-group">
                        <label for="new-vendor">Vendor Name</label>
                        <input type="text" id="new-vendor" placeholder="e.g. Lanka Freight Ltd" value="Lanka Freight Ltd" required>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="new-origin">Port of Origin</label>
                        <input type="text" id="new-origin" placeholder="e.g. Hambantota" value="Hambantota Port" required>
                    </div>
                    <div class="form-group">
                        <label for="new-destination">Destination Port</label>
                        <input type="text" id="new-destination" placeholder="e.g. Nagoya" value="Nagoya Port" required>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="new-mode">Transport Mode</label>
                        <select id="new-mode">
                            <option value="OCEAN">OCEAN</option>
                            <option value="AIR">AIR</option>
                            <option value="RAIL">RAIL</option>
                            <option value="ROAD">ROAD</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="new-priority">Dispatch Priority</label>
                        <select id="new-priority">
                            <option value="STANDARD">STANDARD</option>
                            <option value="HIGH">HIGH</option>
                            <option value="URGENT">URGENT</option>
                        </select>
                    </div>
                </div>

                <!-- Interactive Multi-SKU Cargo Item Picker -->
                <div style="background: rgba(30, 41, 59, 0.6); padding: 16px; border: 1px solid var(--border-dark); border-radius: var(--radius-md); margin-bottom: 16px;">
                    <div style="font-size: 13px; font-weight: 700; color: var(--brand-accent); margin-bottom: 8px;">📦 Allocate Warehouse Cargo Items</div>
                    <div class="form-grid-2">
                        <div class="form-group" style="margin-bottom: 8px;">
                            <label for="select-sku">Select Warehouse Stock Product</label>
                            <select id="select-sku"></select>
                        </div>
                        <div class="form-group" style="margin-bottom: 8px;">
                            <label for="select-qty">Quantity to Pack</label>
                            <div style="display: flex; gap: 8px;">
                                <input type="number" id="select-qty" value="20" min="1">
                                <button type="button" class="btn-table-action" onclick="addManifestItem()" style="padding: 10px 14px; height: 44px; white-space: nowrap;">+ Add Item</button>
                            </div>
                        </div>
                    </div>
                    
                    <div id="manifest-items-list" style="margin-top: 8px; font-size: 12px; color: var(--text-secondary);">
                        <em>No products added to shipment manifest yet. Select product above and click "+ Add Item".</em>
                    </div>
                </div>

                <div class="form-group">
                    <label for="new-desc">Cargo Manifest Summary</label>
                    <input type="text" id="new-desc" placeholder="e.g. 20x SKU-WH-101 (Microcontroller Unit MCU-32)" value="20x SKU-WH-101 (Microcontroller Unit MCU-32)" required>
                    <small style="color: var(--brand-accent); font-size: 11px;">Warehouse stock is automatically allocated and deducted for all added items upon dispatch!</small>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="new-weight">Weight (kg)</label>
                        <input type="number" id="new-weight" value="15000" required>
                    </div>
                    <div class="form-group">
                        <label for="new-cost">Est. Cost (LKR)</label>
                        <input type="number" id="new-cost" value="2500000" required>
                    </div>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeCreateModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Dispatch Shipment</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Form: View Shipment Details -->
    <div id="view-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">👁 Cargo Shipment Details</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Full freight specification and tracking metadata.</p>
            
            <div id="view-details-content" style="font-size: 13px; line-height: 1.8; color: var(--text-secondary);">
                Loading shipment specifications...
            </div>

            <div style="margin-top: 24px;">
                <button type="button" class="btn-submit" onclick="closeViewModal()">Close Specifications</button>
            </div>
        </div>
    </div>

    <!-- Modal Form: Update Status -->
    <div id="status-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">✏️ Update Shipment Status</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Update freight status across global transit hubs.</p>
            
            <form id="update-status-form">
                <input type="hidden" id="status-shipment-id">
                
                <div class="form-group">
                    <label for="status-select">New Transport Status</label>
                    <select id="status-select">
                        <option value="PENDING">PENDING</option>
                        <option value="IN_TRANSIT">IN_TRANSIT</option>
                        <option value="CUSTOMS_HOLD">CUSTOMS_HOLD</option>
                        <option value="DELIVERED">DELIVERED</option>
                    </select>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeStatusModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Update Status</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        var token = localStorage.getItem('scm_jwt');
        if (!token) {
            alert('Session expired or missing authentication token. Please sign in to continue.');
            window.location.href = 'index.html';
        }

        var rawRoles = localStorage.getItem('scm_roles');
        var roles = rawRoles ? JSON.parse(rawRoles) : [];
        var roleBadge = document.getElementById('role-badge');
        
        if (roles.length > 0) roleBadge.innerText = roles[0];

        if (!roles.includes('ADMIN') && !roles.includes('COORDINATOR')) {
            var targetPage = 'index.html';
            if (roles.includes('WAREHOUSE_MANAGER')) targetPage = 'inventory.jsp';
            else if (roles.includes('CUSTOMS_AGENT')) targetPage = 'customs.jsp';
            else if (roles.includes('VENDOR_REP')) targetPage = 'vendor.jsp';
            alert('Access Restricted: Your security role (' + (roles[0] || 'USER') + ') does not have authorization to access Logistics Operations.');
            window.location.href = targetPage;
        }

        if (roles.includes('ADMIN')) {
            document.getElementById('side-overview').style.display = 'flex';
        }

        function checkNavAccess(e, permittedRoles, portalName) {
            var hasAccess = false;
            permittedRoles.forEach(function(r) {
                if (roles.includes(r)) hasAccess = true;
            });

            if (!hasAccess) {
                e.preventDefault();
                alert('Access Restricted: Your role (' + (roles[0] || 'USER') + ') is not authorized to access ' + portalName + '.');
                return false;
            }
            return true;
        }

        var currentShipmentData = [];
        var availableInventory = [];
        var selectedManifestItems = [];

        function switchTab(tabId, btn) {
            document.querySelectorAll('.tab-content').forEach(function(el) { el.classList.remove('active'); });
            document.querySelectorAll('.tab-btn').forEach(function(el) { el.classList.remove('active'); });
            document.getElementById(tabId).classList.add('active');
            btn.classList.add('active');
            if (tabId === 'alerts-tab') loadAlerts();
        }

        async function loadWarehouseInventoryForModal() {
            try {
                var res = await fetch('api/inventory', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    availableInventory = await res.json();
                    var select = document.getElementById('select-sku');
                    select.innerHTML = '';
                    if (availableInventory.length === 0) {
                        select.innerHTML = '<option value="">No warehouse stock available</option>';
                    } else {
                        availableInventory.forEach(function(item) {
                            select.innerHTML += '<option value="' + item.sku + '">' + item.sku + ': ' + item.name + ' (In Stock: ' + item.quantity + ')</option>';
                        });
                    }
                }
            } catch (err) {}
        }

        function addManifestItem() {
            var skuSelect = document.getElementById('select-sku');
            var skuVal = skuSelect.value;
            var qtyVal = parseInt(document.getElementById('select-qty').value) || 20;

            if (!skuVal) return;

            var invItem = availableInventory.find(function(i) { return i.sku === skuVal; });
            var itemName = invItem ? invItem.name : skuVal;

            selectedManifestItems.push({ sku: skuVal, name: itemName, quantity: qtyVal });
            renderManifestItemsList();
        }

        function removeManifestItem(index) {
            selectedManifestItems.splice(index, 1);
            renderManifestItemsList();
        }

        function renderManifestItemsList() {
            var container = document.getElementById('manifest-items-list');
            var descInput = document.getElementById('new-desc');
            
            if (selectedManifestItems.length === 0) {
                container.innerHTML = '<em>No products added to shipment manifest yet. Select product above and click "+ Add Item".</em>';
                descInput.value = '20x SKU-WH-101 (Microcontroller Unit MCU-32)';
            } else {
                var html = '<ul style="padding-left: 16px; margin: 4px 0;">';
                var descParts = [];
                selectedManifestItems.forEach(function(item, idx) {
                    html += '<li><strong>' + item.quantity + 'x ' + item.sku + '</strong> (' + item.name + ') ' +
                        '<a href="#" onclick="removeManifestItem(' + idx + '); return false;" style="color: #fca5a5; margin-left: 8px;">[Remove]</a></li>';
                    descParts.push(item.quantity + 'x ' + item.sku + ' (' + item.name + ')');
                });
                html += '</ul>';
                container.innerHTML = html;
                descInput.value = descParts.join(', ');
            }
        }

        async function loadShipments() {
            var tbody = document.getElementById('shipments-table-body');

            try {
                var res = await fetch('api/shipments', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    currentShipmentData = await res.json();
                    tbody.innerHTML = '';
                    
                    var pending = 0, transit = 0, delivered = 0;
                    var total = currentShipmentData.length;

                    if (total === 0) {
                        tbody.innerHTML = '<tr><td colspan="8" style="color: var(--text-secondary);">No freight dispatches found. Click "+ Create New Cargo Shipment" to add one!</td></tr>';
                    } else {
                        currentShipmentData.forEach(function(item) {
                            if (item.status === 'PENDING') pending++;
                            else if (item.status.indexOf('IN_TRANSIT') !== -1 || item.status.indexOf('BOOKED') !== -1) transit++;
                            else if (item.status === 'DELIVERED') delivered++;

                            var pillClass = item.status === 'DELIVERED' ? 'pill-green' : 'pill-blue';
                            var rawCost = item.costLkr || item.costUSD || 0;
                            var costFormatted = rawCost ? rawCost.toLocaleString() : '0';
                            var vendor = item.vendorName || 'Lanka Freight Ltd';
                            var mode = item.transportMode || 'OCEAN';
                            var priority = item.priority || 'STANDARD';

                            tbody.innerHTML += '<tr>' +
                                '<td>#' + item.id + '</td>' +
                                '<td><strong>' + item.trackingNumber + '</strong></td>' +
                                '<td>' + vendor + '</td>' +
                                '<td>' + item.origin + ' ➔ ' + item.destination + '</td>' +
                                '<td><span class="pill pill-yellow">' + mode + ' / ' + priority + '</span></td>' +
                                '<td>Rs. ' + costFormatted + '</td>' +
                                '<td><span class="pill ' + pillClass + '">' + item.status + '</span></td>' +
                                '<td>' +
                                    '<button class="btn-table-action" onclick="openViewModal(' + item.id + ')">👁 View</button>' +
                                    '<button class="btn-table-action" onclick="quickBook(' + item.id + ')">⚓ Book</button>' +
                                    '<button class="btn-table-action" onclick="openStatusModal(' + item.id + ')">✏️ Status</button>' +
                                '</td>' +
                            '</tr>';
                        });
                    }

                    document.getElementById('count-total').innerText = total;
                    document.getElementById('count-pending').innerText = pending;
                    document.getElementById('count-transit').innerText = transit;
                    document.getElementById('count-delivered').innerText = delivered;

                    if (total > 0) {
                        var transitPct = Math.round((transit / total) * 100);
                        var deliveredPct = Math.round((delivered / total) * 100);
                        var pendingPct = Math.round((pending / total) * 100);

                        document.getElementById('bar-transit').style.width = transitPct + '%';
                        document.getElementById('bar-delivered').style.width = deliveredPct + '%';
                        document.getElementById('bar-pending').style.width = pendingPct + '%';

                        document.getElementById('analytics-legend').innerText = 'In Transit: ' + transitPct + '% | Delivered: ' + deliveredPct + '% | Pending: ' + pendingPct + '%';
                    } else {
                        document.getElementById('analytics-legend').innerText = 'No cargo data available';
                    }

                } else {
                    tbody.innerHTML = '<tr><td colspan="8" style="color: var(--danger);">Failed to load shipments. Session expired.</td></tr>';
                }
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="8" style="color: var(--danger);">Network error contacting server.</td></tr>';
            }
        }

        async function loadAlerts() {
            var container = document.getElementById('alerts-stream-container');
            try {
                var res = await fetch('api/alerts?limit=25', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    var logs = await res.json();
                    container.innerHTML = '';
                    
                    var logisticsLogs = logs.filter(function(log) {
                        var action = (log.action || '').toUpperCase();
                        return action.indexOf('SHIPMENT') !== -1 || action.indexOf('CARRIER') !== -1 || action.indexOf('BOOKING') !== -1 || action.indexOf('PORT') !== -1 || action.indexOf('SYSTEM') !== -1;
                    });

                    if (logisticsLogs.length === 0) {
                        container.innerHTML = '<div style="color: var(--text-secondary);">No freight dispatch alerts recorded yet. Perform cargo dispatches or bookings to generate live alerts!</div>';
                    } else {
                        logisticsLogs.forEach(function(log) {
                            var actionStr = log.action || 'LOGISTICS_EVENT';
                            var badgeColor = actionStr.indexOf('CARRIER') !== -1 ? '#38bdf8' : (actionStr.indexOf('CREATE') !== -1 ? '#10b981' : '#f59e0b');
                            var timeStr = log.timestamp ? new Date(log.timestamp).toLocaleString() : 'Recent Event';
                            var detailsStr = log.details || 'Logistics event recorded.';
                            
                            container.innerHTML += '<div style="padding: 14px; background: rgba(30, 41, 59, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; margin-bottom: 8px;">' +
                                '<strong style="color: ' + badgeColor + ';">[' + actionStr + ']</strong> ' + detailsStr +
                                '<div style="font-size: 11px; color: var(--text-muted); margin-top: 4px;">Timestamp: ' + timeStr + '</div>' +
                            '</div>';
                        });
                    }
                }
            } catch (err) {
                container.innerHTML = '<div style="color: var(--danger);">Network error fetching freight alerts.</div>';
            }
        }

        document.getElementById('create-shipment-form').addEventListener('submit', async function(e) {
            e.preventDefault();

            var trackingNumber = document.getElementById('new-tracking').value;
            var vendorName = document.getElementById('new-vendor').value;
            var origin = document.getElementById('new-origin').value;
            var destination = document.getElementById('new-destination').value;
            var transportMode = document.getElementById('new-mode').value;
            var priority = document.getElementById('new-priority').value;
            var cargoDescription = document.getElementById('new-desc').value;
            var weightKg = document.getElementById('new-weight').value;
            var costLkr = document.getElementById('new-cost').value;

            try {
                var res = await fetch('api/shipments', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token
                    },
                    body: JSON.stringify({
                        trackingNumber: trackingNumber,
                        vendorName: vendorName,
                        origin: origin,
                        destination: destination,
                        transportMode: transportMode,
                        priority: priority,
                        cargoDescription: cargoDescription,
                        weightKg: parseFloat(weightKg),
                        costLkr: parseFloat(costLkr),
                        status: 'PENDING'
                    })
                });

                if (res.ok) {
                    closeCreateModal();
                    loadShipments();
                    alert('Cargo Shipment Dispatched Successfully!');
                } else {
                    alert('Failed to dispatch shipment: ' + await res.text());
                }
            } catch (err) {
                alert('Network error dispatching shipment.');
            }
        });

        document.getElementById('bmt-booking-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var shipmentId = document.getElementById('shipment-id').value;
            var carrierCode = document.getElementById('carrier-code').value;
            var costUSD = document.getElementById('cost-usd').value;
            var resBanner = document.getElementById('bmt-response');

            resBanner.style.display = 'none';

            try {
                var response = await fetch('api/carrier-booking?shipmentId=' + shipmentId + '&carrierCode=' + carrierCode + '&costUSD=' + costUSD, {
                    method: 'POST',
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                var text = await response.text();
                resBanner.innerText = text;
                resBanner.style.display = 'block';

                if (response.ok) {
                    resBanner.style.background = 'rgba(16, 185, 129, 0.15)';
                    resBanner.style.color = '#6ee7b7';
                    resBanner.style.border = '1px solid rgba(16, 185, 129, 0.3)';
                    loadShipments();
                } else {
                    resBanner.style.background = 'rgba(239, 68, 68, 0.15)';
                    resBanner.style.color = '#fca5a5';
                    resBanner.style.border = '1px solid rgba(239, 68, 68, 0.3)';
                }
            } catch (err) {
                resBanner.innerText = 'Network error reserving carrier container.';
                resBanner.style.display = 'block';
                resBanner.style.background = 'rgba(239, 68, 68, 0.15)';
                resBanner.style.color = '#fca5a5';
            }
        });

        document.getElementById('update-status-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var id = document.getElementById('status-shipment-id').value;
            var status = document.getElementById('status-select').value;

            try {
                var res = await fetch('api/shipments/' + id + '/status?status=' + status, {
                    method: 'PUT',
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    closeStatusModal();
                    loadShipments();
                } else {
                    alert('Failed to update status.');
                }
            } catch (err) {
                alert('Network error updating status.');
            }
        });

        function openViewModal(id) {
            var item = currentShipmentData.find(function(s) { return s.id === id; });
            if (!item) return;

            var rawCost = item.costLkr || item.costUSD || 0;
            var costFormatted = rawCost ? rawCost.toLocaleString() : '0';
            var vendorName = item.vendorName || 'Lanka Freight Ltd';
            var modeStr = item.transportMode || 'OCEAN';
            var priorityStr = item.priority || 'STANDARD';
            var weightStr = item.weightKg ? item.weightKg.toLocaleString() : '1,500';
            var descStr = item.cargoDescription || 'Industrial container supply shipment.';

            document.getElementById('view-details-content').innerHTML = 
                '<div style="background: rgba(30, 41, 59, 0.6); padding: 16px; border-radius: 8px; border: 1px solid var(--border-dark); margin-bottom: 16px;">' +
                    '<div><strong>Tracking #:</strong> <span style="color: var(--brand-accent); font-weight: 700;">' + item.trackingNumber + '</span></div>' +
                    '<div><strong>Vendor Name:</strong> ' + vendorName + '</div>' +
                    '<div><strong>Route:</strong> ' + item.origin + ' ➔ ' + item.destination + '</div>' +
                    '<div><strong>Transport Mode:</strong> ' + modeStr + '</div>' +
                    '<div><strong>Priority Level:</strong> ' + priorityStr + '</div>' +
                    '<div><strong>Weight:</strong> ' + weightStr + ' kg</div>' +
                    '<div><strong>Total Cost:</strong> Rs. ' + costFormatted + '</div>' +
                    '<div><strong>Current Status:</strong> <span style="color: var(--success); font-weight: 700;">' + item.status + '</span></div>' +
                '</div>' +
                '<div><strong>Cargo Specifications & Packed Manifest:</strong></div>' +
                '<div style="color: white; margin-top: 4px;">' + descStr + '</div>';

            document.getElementById('view-modal').style.display = 'flex';
        }

        function quickBook(id) {
            document.getElementById('shipment-id').value = id;
            switchTab('booking-tab', document.querySelectorAll('.tab-btn')[1]);
        }

        function openStatusModal(id) {
            document.getElementById('status-shipment-id').value = id;
            document.getElementById('status-modal').style.display = 'flex';
        }

        function closeViewModal() { document.getElementById('view-modal').style.display = 'none'; }
        function closeStatusModal() { document.getElementById('status-modal').style.display = 'none'; }
        
        function openCreateModal() {
            selectedManifestItems = [];
            renderManifestItemsList();
            loadWarehouseInventoryForModal();
            document.getElementById('new-tracking').value = 'SCM-TRK-' + Math.floor(1000 + Math.random() * 9000);
            document.getElementById('create-modal').style.display = 'flex';
        }
        
        function closeCreateModal() { document.getElementById('create-modal').style.display = 'none'; }

        window.addEventListener('load', loadShipments);
    </script>
</body>
</html>