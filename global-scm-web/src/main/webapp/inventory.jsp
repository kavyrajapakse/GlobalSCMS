<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Warehouse Inventory & Stock Control | GlobalTrade SCM</title>
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
        .stats-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 16px; margin-bottom: 24px; }
        .stat-card { background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(12px); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 18px; }
        .stat-title { font-size: 11px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.5px; }
        .stat-value { font-size: 26px; font-weight: 800; color: white; margin: 8px 0 4px; }
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
        .pill-red { background: rgba(239, 68, 68, 0.15); color: var(--danger); }

        .btn-table-action { padding: 6px 10px; background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); border: 1px solid rgba(56, 189, 248, 0.3); border-radius: var(--radius-sm); font-size: 12px; font-weight: 600; cursor: pointer; transition: var(--transition); margin-right: 4px; }
        .btn-table-action:hover { background: var(--brand-primary); color: white; }

        .form-grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-size: 12px; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }
        .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 11px 14px; background: rgba(30, 41, 59, 0.8); border: 1px solid var(--border-dark); border-radius: var(--radius-md); color: white; font-family: inherit; font-size: 14px; }

        .btn-action-primary { padding: 12px 20px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); box-shadow: 0 4px 12px var(--brand-glow); }
        .btn-submit { width: 100%; padding: 13px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); }

        .modal-backdrop { position: fixed; inset: 0; background: rgba(9, 13, 22, 0.8); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 200; }
        .modal-card { width: 100%; max-width: 540px; background: var(--bg-surface); border: 1px solid var(--border-dark); border-radius: var(--radius-lg); padding: 32px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.7); max-height: 90vh; overflow-y: auto; }
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
                <a href="shipments.jsp" id="side-shipments" class="nav-item" onclick="return checkNavAccess(event, ['ADMIN', 'COORDINATOR'], 'Shipments & Freight')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 17L12 22L22 17"/><path d="M2 12L12 17L22 12"/><path d="M12 2L2 7L12 12L22 7L12 2Z"/></svg>
                    <span>Shipments & Freight</span>
                </a>
                <a href="inventory.jsp" id="side-inventory" class="nav-item active">
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
                <span class="role-badge" id="role-badge">WAREHOUSE</span>
                <a href="index.html" class="btn-logout" onclick="localStorage.clear()">Logout</a>
            </div>
        </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
        <div class="page-header">
            <div>
                <h1>Warehouse Stock Control & Catalog Management</h1>
                <p>Monitor warehouse stock levels, inventory adjustments, and automated reorder queues.</p>
            </div>
            <button class="btn-action-primary" onclick="openCreateModal()">+ Register New Inventory Stock</button>
        </div>

        <!-- Sub-Feature Navigation Tabs -->
        <div class="sub-tabs">
            <button class="tab-btn active" onclick="switchTab('catalog-tab', this)">📦 Stock Catalog & Inventory</button>
            <button class="tab-btn" onclick="switchTab('replenishment-tab', this)">🔄 Reorder Queue & Dispatch</button>
            <button class="tab-btn" onclick="switchTab('alerts-tab', this)">⚠️ Low-Stock Alerts</button>
        </div>

        <!-- Tab 1: Catalog & Inventory Table -->
        <div id="catalog-tab" class="tab-content active">
            
            <!-- Top Metric Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-title">Total Products</div>
                    <div class="stat-value" id="count-total">0</div>
                    <div class="stat-sub">Registered Catalog</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">In Stock Items</div>
                    <div class="stat-value" id="count-instock" style="color: var(--success);">0</div>
                    <div class="stat-sub">Optimal Quantity</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Low Stock Alert</div>
                    <div class="stat-value" id="count-lowstock" style="color: var(--warning);">0</div>
                    <div class="stat-sub">Below Threshold</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Out of Stock</div>
                    <div class="stat-value" id="count-outofstock" style="color: var(--danger);">0</div>
                    <div class="stat-sub">Depleted Inventory</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Depot Location</div>
                    <div class="stat-value" style="font-size: 16px; color: var(--brand-accent); margin-top: 14px;">Colombo Central</div>
                    <div class="stat-sub">Main Warehouse</div>
                </div>
            </div>

            <!-- Distribution Bar & Automated Tracking Card -->
            <div class="top-row-cards">
                <div class="analytics-card">
                    <div class="analytics-title">
                        <span>Warehouse Health & Stock Distribution</span>
                        <span id="analytics-legend" style="font-size: 11px; font-weight: 600;">Loading stock health...</span>
                    </div>
                    <div class="progress-bar-wrap">
                        <div id="bar-instock" class="bar-delivered" style="width: 0%;"></div>
                        <div id="bar-lowstock" class="bar-pending" style="width: 0%;"></div>
                        <div id="bar-outofstock" style="background: var(--danger); height: 100%; width: 0%;"></div>
                    </div>
                </div>

                <div class="timer-card">
                    <div style="font-size: 12px; font-weight: 700; color: var(--brand-accent); text-transform: uppercase; margin-bottom: 6px;">⏱️ Automated Stock Health Service</div>
                    <div style="font-size: 13px; color: var(--text-secondary);">Scanning Frequency: <strong style="color: white;">Every 5 Minutes</strong></div>
                    <div style="font-size: 13px; color: var(--text-secondary); margin-top: 2px;">Status: <span style="color: var(--success); font-weight: 700;">● Active Monitoring Service</span></div>
                </div>
            </div>

            <!-- Live Inventory SKU Table -->
            <div class="panel">
                <h3>Live Warehouse Inventory Catalog</h3>
                <p class="panel-desc">Real-time product stock levels updated across all warehouse depots.</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Stock Code</th>
                            <th>Item Name</th>
                            <th>Category</th>
                            <th>Location</th>
                            <th>Stock Qty</th>
                            <th>Unit Price (LKR)</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="inventory-table-body">
                        <tr><td colspan="9" style="color: var(--text-muted);">Loading inventory catalog...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Tab 2: Reorder Queue & Dispatch -->
        <div id="replenishment-tab" class="tab-content">
            <div class="panel" style="max-width: 650px;">
                <h3>🔄 Automated Stock Monitoring & Reorder Queue</h3>
                <p class="panel-desc">Automated background service scans inventory levels every 5 minutes and queues low-stock items for restocking approval.</p>

                <div style="padding: 16px; background: rgba(30, 41, 59, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; margin-bottom: 16px;">
                    <div style="font-size: 13px; font-weight: 700; color: var(--brand-accent);">Automated Inventory Rule:</div>
                    <div style="font-size: 13px; color: var(--text-secondary); margin-top: 4px;">When automated tracking service detects item stock below threshold, a reorder dispatch request (+100 Units) is queued for Warehouse Manager authorization.</div>
                </div>

                <div id="replenishment-queue-container" style="display: flex; flex-direction: column; gap: 12px;">
                    <div style="color: var(--text-secondary);">Loading replenishment queue...</div>
                </div>
            </div>
        </div>

        <!-- Tab 3: Low-Stock Alerts -->
        <div id="alerts-tab" class="tab-content">
            <div class="panel">
                <h3>⚠️ Low-Stock & Warehouse Alerts</h3>
                <p class="panel-desc">Real-time inventory event stream logged automatically.</p>
                
                <div id="alerts-stream-container" style="display: flex; flex-direction: column; gap: 12px; margin-top: 16px;">
                    <div style="color: var(--text-secondary);">Loading stock alerts...</div>
                </div>
            </div>
        </div>

    </main>

    <!-- Modal Form: Register New Inventory SKU -->
    <div id="create-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">Register New Inventory Stock</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Add a new product stock item to warehouse catalog.</p>
            
            <form id="create-sku-form">
                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="new-sku">Stock Code (SKU)</label>
                        <input type="text" id="new-sku" placeholder="e.g. SKU-WH-108" required>
                    </div>
                    <div class="form-group">
                        <label for="new-name">Item Name</label>
                        <input type="text" id="new-name" placeholder="e.g. Microcontroller Unit MCU-32" required>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="new-category">Category</label>
                        <input type="text" id="new-category" placeholder="e.g. Semiconductors" value="Semiconductors" required>
                    </div>
                    <div class="form-group">
                        <label for="new-location">Depot Location</label>
                        <input type="text" id="new-location" placeholder="e.g. Rack A4 - Bin 12" value="Colombo Central Depot" required>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="new-qty">Initial Quantity</label>
                        <input type="number" id="new-qty" value="120" required>
                    </div>
                    <div class="form-group">
                        <label for="new-threshold">Reorder Threshold</label>
                        <input type="number" id="new-threshold" value="30" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="new-price">Unit Price (LKR)</label>
                    <input type="number" id="new-price" value="4500" required>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeCreateModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Save Inventory Item</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Form: Adjust Stock Quantity -->
    <div id="adjust-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">🔄 Adjust Stock Quantity</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Issue or restock items directly in inventory system.</p>
            
            <form id="adjust-stock-form">
                <input type="hidden" id="adjust-item-id">
                
                <div class="form-group">
                    <label for="adjust-delta">Stock Quantity Adjustment (+/-)</label>
                    <input type="number" id="adjust-delta" value="50" required>
                    <small style="color: var(--text-muted); font-size: 11px;">Enter positive number to Restock (+50), or negative to Issue stock (-20).</small>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeAdjustModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Apply Stock Adjustment</button>
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

        if (!roles.includes('ADMIN') && !roles.includes('WAREHOUSE_MANAGER')) {
            var targetPage = 'index.html';
            if (roles.includes('COORDINATOR')) targetPage = 'shipments.jsp';
            else if (roles.includes('CUSTOMS_AGENT')) targetPage = 'customs.jsp';
            else if (roles.includes('VENDOR_REP')) targetPage = 'vendor.jsp';
            alert('Access Restricted: Your security role (' + (roles[0] || 'USER') + ') does not have authorization to access Warehouse Operations.');
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

        var currentInventoryData = [];

        function switchTab(tabId, btn) {
            document.querySelectorAll('.tab-content').forEach(function(el) { el.classList.remove('active'); });
            document.querySelectorAll('.tab-btn').forEach(function(el) { el.classList.remove('active'); });
            document.getElementById(tabId).classList.add('active');
            btn.classList.add('active');
            if (tabId === 'alerts-tab') loadAlerts();
            if (tabId === 'replenishment-tab') loadReplenishment();
        }

        async function loadInventory() {
            var tbody = document.getElementById('inventory-table-body');

            try {
                var res = await fetch('api/inventory', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    currentInventoryData = await res.json();
                    tbody.innerHTML = '';
                    
                    var instock = 0, lowstock = 0, outofstock = 0;
                    var total = currentInventoryData.length;

                    if (total === 0) {
                        tbody.innerHTML = '<tr><td colspan="9" style="color: var(--text-secondary);">No inventory items found. Click "+ Register New Inventory Stock" to add stock!</td></tr>';
                    } else {
                        currentInventoryData.forEach(function(item) {
                            var qty = item.quantity || 0;
                            var threshold = item.reorderThreshold || 30;
                            
                            var statusStr = 'IN_STOCK';
                            var pillClass = 'pill-green';

                            if (qty <= 0) {
                                statusStr = 'OUT_OF_STOCK';
                                pillClass = 'pill-red';
                                outofstock++;
                            } else if (qty <= threshold) {
                                statusStr = 'LOW_STOCK';
                                pillClass = 'pill-yellow';
                                lowstock++;
                            } else {
                                instock++;
                            }

                            var priceFormatted = item.unitPriceLkr ? item.unitPriceLkr.toLocaleString() : '4,500';

                            tbody.innerHTML += '<tr>' +
                                '<td>#' + item.id + '</td>' +
                                '<td><strong>' + (item.sku || 'SKU-WH-101') + '</strong></td>' +
                                '<td>' + item.name + '</td>' +
                                '<td>' + (item.category || 'General') + '</td>' +
                                '<td>' + (item.warehouseLocation || 'Colombo Depot') + '</td>' +
                                '<td><strong>' + qty.toLocaleString() + '</strong></td>' +
                                '<td>Rs. ' + priceFormatted + '</td>' +
                                '<td><span class="pill ' + pillClass + '">' + statusStr + '</span></td>' +
                                '<td>' +
                                    '<button class="btn-table-action" onclick="openAdjustModal(' + item.id + ', 50)">➕ Restock</button>' +
                                    '<button class="btn-table-action" onclick="openAdjustModal(' + item.id + ', -20)">✏️ Adjust</button>' +
                                '</td>' +
                            '</tr>';
                        });
                    }

                    document.getElementById('count-total').innerText = total;
                    document.getElementById('count-instock').innerText = instock;
                    document.getElementById('count-lowstock').innerText = lowstock;
                    document.getElementById('count-outofstock').innerText = outofstock;

                    if (total > 0) {
                        var instockPct = Math.round((instock / total) * 100);
                        var lowstockPct = Math.round((lowstock / total) * 100);
                        var outofstockPct = Math.round((outofstock / total) * 100);

                        document.getElementById('bar-instock').style.width = instockPct + '%';
                        document.getElementById('bar-lowstock').style.width = lowstockPct + '%';
                        document.getElementById('bar-outofstock').style.width = outofstockPct + '%';

                        document.getElementById('analytics-legend').innerText = 'Optimal Stock: ' + instockPct + '% | Low Stock: ' + lowstockPct + '% | Out of Stock: ' + outofstockPct + '%';
                    } else {
                        document.getElementById('analytics-legend').innerText = 'No inventory data available';
                    }

                } else {
                    tbody.innerHTML = '<tr><td colspan="9" style="color: var(--danger);">Failed to load inventory catalog. Session expired.</td></tr>';
                }
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="9" style="color: var(--danger);">Network error contacting server.</td></tr>';
            }
        }

        async function loadReplenishment() {
            var container = document.getElementById('replenishment-queue-container');
            var lowItems = currentInventoryData.filter(function(i) { return (i.quantity || 0) <= (i.reorderThreshold || 30); });

            if (lowItems.length === 0) {
                container.innerHTML = '<div style="padding: 16px; background: rgba(30, 41, 59, 0.4); border-radius: 8px; color: var(--success); font-weight: 600;">✅ All warehouse items are at optimal stock levels. Zero replenishment required.</div>';
            } else {
                container.innerHTML = '';
                lowItems.forEach(function(item) {
                    container.innerHTML += '<div style="padding: 14px; background: rgba(30, 41, 59, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; display: flex; justify-content: space-between; align-items: center;">' +
                        '<div>' +
                            '<strong style="color: var(--warning);">' + item.name + ' (' + item.sku + ')</strong>' +
                            '<div style="font-size: 12px; color: var(--text-secondary); margin-top: 2px;">Current Qty: ' + item.quantity + ' | Reorder Level: ' + item.reorderThreshold + '</div>' +
                        '</div>' +
                        '<button class="btn-table-action" onclick="triggerRestock(' + item.id + ')">Trigger Restock (+100)</button>' +
                    '</div>';
                });
            }
        }

        async function loadAlerts() {
            var container = document.getElementById('alerts-stream-container');
            try {
                var res = await fetch('api/alerts?limit=20', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    var logs = await res.json();
                    container.innerHTML = '';
                    
                    var warehouseLogs = logs.filter(function(log) {
                        var action = (log.action || '').toUpperCase();
                        return action.indexOf('STOCK') !== -1 || action.indexOf('INVENTORY') !== -1 || action.indexOf('REPLENISH') !== -1;
                    });

                    if (warehouseLogs.length === 0) {
                        container.innerHTML = '<div style="color: var(--text-secondary);">No low-stock or warehouse alerts recorded yet. Perform stock adjustments to generate logs!</div>';
                    } else {
                        warehouseLogs.forEach(function(log) {
                            var actionStr = log.action || 'STOCK_EVENT';
                            var badgeColor = actionStr.indexOf('STOCK') !== -1 ? '#38bdf8' : '#f59e0b';
                            var timeStr = log.timestamp ? new Date(log.timestamp).toLocaleString() : 'Recent Event';
                            var detailsStr = log.details || 'Warehouse inventory transaction logged.';
                            
                            container.innerHTML += '<div style="padding: 14px; background: rgba(30, 41, 59, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; margin-bottom: 8px;">' +
                                '<strong style="color: ' + badgeColor + ';">[' + actionStr + ']</strong> ' + detailsStr +
                                '<div style="font-size: 11px; color: var(--text-muted); margin-top: 4px;">Timestamp: ' + timeStr + '</div>' +
                            '</div>';
                        });
                    }
                }
            } catch (err) {
                container.innerHTML = '<div style="color: var(--danger);">Network error fetching warehouse alerts.</div>';
            }
        }

        document.getElementById('create-sku-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var sku = document.getElementById('new-sku').value;
            var name = document.getElementById('new-name').value;
            var category = document.getElementById('new-category').value;
            var warehouseLocation = document.getElementById('new-location').value;
            var quantity = document.getElementById('new-qty').value;
            var reorderThreshold = document.getElementById('new-threshold').value;
            var unitPriceLkr = document.getElementById('new-price').value;

            try {
                var res = await fetch('api/inventory', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token
                    },
                    body: JSON.stringify({
                        sku: sku,
                        name: name,
                        category: category,
                        warehouseLocation: warehouseLocation,
                        quantity: parseInt(quantity),
                        reorderThreshold: parseInt(reorderThreshold),
                        unitPriceLkr: parseFloat(unitPriceLkr)
                    })
                });

                if (res.ok) {
                    closeCreateModal();
                    loadInventory();
                    alert('Inventory Stock Registered Successfully!');
                } else {
                    alert('Failed to create stock item: ' + await res.text());
                }
            } catch (err) {
                alert('Network error creating inventory stock item.');
            }
        });

        document.getElementById('adjust-stock-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var id = document.getElementById('adjust-item-id').value;
            var delta = document.getElementById('adjust-delta').value;

            try {
                var res = await fetch('api/inventory/' + id + '/adjust?delta=' + delta, {
                    method: 'POST',
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    closeAdjustModal();
                    loadInventory();
                    alert('Stock Adjustment Applied Successfully!');
                } else {
                    var errObj = await res.json();
                    alert('⚠️ Warehouse Operation Warning: ' + (errObj.message || 'Failed to adjust stock.'));
                }
            } catch (err) {
                alert('Network error applying stock adjustment.');
            }
        });

        async function triggerRestock(id) {
            try {
                var res = await fetch('api/inventory/' + id + '/adjust?delta=100', {
                    method: 'POST',
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    loadInventory();
                    loadReplenishment();
                    alert('Automated Restocking Order Dispatched (+100 Units)!');
                } else {
                    alert('Failed to execute restock.');
                }
            } catch (err) {
                alert('Network error triggering restock.');
            }
        }

        function openAdjustModal(id, defaultDelta) {
            document.getElementById('adjust-item-id').value = id;
            document.getElementById('adjust-delta').value = defaultDelta;
            document.getElementById('adjust-modal').style.display = 'flex';
        }

        function closeAdjustModal() { document.getElementById('adjust-modal').style.display = 'none'; }
        function openCreateModal() { document.getElementById('create-modal').style.display = 'flex'; }
        function closeCreateModal() { document.getElementById('create-modal').style.display = 'none'; }

        window.addEventListener('load', loadInventory);
    </script>
</body>
</html>
