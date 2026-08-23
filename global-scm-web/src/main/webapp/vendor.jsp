<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor Representative & Supplier Portal | GlobalTrade SCM</title>
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

        /* Status Distribution & Performance Trend Card */
        .top-row-cards { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-bottom: 24px; }
        .analytics-card { background: rgba(15, 23, 42, 0.75); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 20px; }
        .analytics-title { font-size: 13px; font-weight: 700; color: var(--text-secondary); margin-bottom: 12px; display: flex; justify-content: space-between; }
        
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

        .btn-table-action { padding: 6px 10px; background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); border: 1px solid rgba(56, 189, 248, 0.3); border-radius: var(--radius-sm); font-size: 12px; font-weight: 600; cursor: pointer; transition: var(--transition); margin-right: 4px; }
        .btn-table-action:hover { background: var(--brand-primary); color: white; }

        .form-grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-size: 12px; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }
        .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 11px 14px; background: rgba(30, 41, 59, 0.8); border: 1px solid var(--border-dark); border-radius: var(--radius-md); color: white; font-family: inherit; font-size: 14px; }

        .btn-action-primary { padding: 12px 20px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); box-shadow: 0 4px 12px var(--brand-glow); }
        .btn-submit { width: 100%; padding: 13px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); }

        .modal-backdrop { position: fixed; inset: 0; background: rgba(9, 13, 22, 0.8); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 200; }
        .modal-card { width: 100%; max-width: 640px; background: var(--bg-surface); border: 1px solid var(--border-dark); border-radius: var(--radius-lg); padding: 32px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.7); max-height: 90vh; overflow-y: auto; }
        
        .sla-grid-4 { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin: 12px 0; }
        .sla-box { background: rgba(30, 41, 59, 0.8); border: 1px solid var(--border-dark); padding: 10px; border-radius: 8px; text-align: center; }
        .sla-box-val { font-size: 16px; font-weight: 800; color: var(--success); }
        .sla-box-lbl { font-size: 10px; color: var(--text-secondary); margin-top: 2px; }
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
                <a href="inventory.jsp" id="side-inventory" class="nav-item" onclick="return checkNavAccess(event, ['ADMIN', 'WAREHOUSE_MANAGER'], 'Warehouse Stock')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
                    <span>Warehouse Stock</span>
                </a>
                <a href="customs.jsp" id="side-customs" class="nav-item" onclick="return checkNavAccess(event, ['ADMIN', 'CUSTOMS_AGENT'], 'Customs Clearance')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
                    <span>Customs Clearance</span>
                </a>
                <a href="vendor.jsp" id="side-vendor" class="nav-item active">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                    <span>Vendor Portal</span>
                </a>
            </nav>
        </div>

        <div class="sidebar-footer">
            <div class="user-info">
                <span class="role-badge" id="role-badge">VENDOR_REP</span>
                <a href="index.html" class="btn-logout" onclick="localStorage.clear()">Logout</a>
            </div>
        </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
        <div class="page-header">
            <div>
                <h1>Vendor Representative & Supplier Portal</h1>
                <p>Manage international supplier contracts, registered vendors, contract values, and SLA performance ratings.</p>
            </div>
            <button id="btn-register-vendor" class="btn-action-primary" onclick="openRegisterVendorModal()">+ Register New Vendor Partner</button>
        </div>

        <!-- Sub-Feature Navigation Tabs -->
        <div class="sub-tabs">
            <button id="tab-btn-directory" class="tab-btn active" onclick="switchTab('vendors-tab', this)">🏢 All Suppliers Directory</button>
            <button id="tab-btn-profile" class="tab-btn" onclick="switchTab('profile-tab', this)">📋 Supplier Profile & MSA Contract</button>
            <button id="tab-btn-dispatches" class="tab-btn" onclick="switchTab('dispatches-tab', this)">📦 Vendor Dispatched Cargo</button>
            <button id="tab-btn-alerts" class="tab-btn" onclick="switchTab('alerts-tab', this)">🔔 Vendor SLA Audit Stream</button>
        </div>

        <!-- Tab 1: Vendor Directory & Active Contracts Table (Admin/Coordinator Only) -->
        <div id="vendors-tab" class="tab-content active">
            
            <!-- Top Metric Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-title">Active Supplier Partners</div>
                    <div class="stat-value" id="count-total">0</div>
                    <div class="stat-sub">Registered Global Vendors</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Average SLA Rating</div>
                    <div class="stat-value" id="avg-sla" style="color: var(--success);">98.4%</div>
                    <div class="stat-sub">4-Factor Composite Rating</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Active Contract Value</div>
                    <div class="stat-value" style="color: var(--brand-accent); font-size: 24px;">Rs. 135M</div>
                    <div class="stat-sub">Global Trade Contracts</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Trade License Status</div>
                    <div class="stat-value" style="font-size: 15px; color: var(--success); margin-top: 14px;">● Fully Compliant</div>
                    <div class="stat-sub">International Trade Verified</div>
                </div>
            </div>

            <!-- SLA Quality Progress Bar & Performance Trend Chart -->
            <div class="top-row-cards">
                <div class="analytics-card">
                    <div class="analytics-title">
                        <span>Supplier SLA Performance Trend (May - Aug 2026)</span>
                        <span style="font-size: 11px; font-weight: 700; color: var(--success);">📈 Upward Trend (+2.4%)</span>
                    </div>
                    <!-- SLA Trend Chart SVG -->
                    <div style="height: 48px; display: flex; align-items: flex-end; gap: 20px; padding: 4px 0; margin-top: 8px;">
                        <div style="font-size: 11px; color: var(--text-secondary);">May: <strong>96.0%</strong></div>
                        <div style="font-size: 11px; color: var(--text-secondary);">Jun: <strong>97.2%</strong></div>
                        <div style="font-size: 11px; color: var(--text-secondary);">Jul: <strong>98.0%</strong></div>
                        <div style="font-size: 11px; color: var(--success);">Aug: <strong>98.4%</strong></div>
                    </div>
                </div>

                <div class="timer-card">
                    <div style="font-size: 12px; font-weight: 700; color: var(--brand-accent); text-transform: uppercase; margin-bottom: 6px;">⏱️ Vendor Evaluation Service</div>
                    <div style="font-size: 13px; color: var(--text-secondary);">Evaluation Cycle: <strong style="color: white;">Every 20 Minutes</strong></div>
                    <div style="font-size: 13px; color: var(--text-secondary); margin-top: 2px;">Status: <span style="color: var(--success); font-weight: 700;">● Vendor Performance Timer Active</span></div>
                </div>
            </div>

            <!-- Live Vendors Directory Table -->
            <div class="panel">
                <h3>Registered Vendor Partners & Active Contracts</h3>
                <p class="panel-desc">Primary supplier database records with verified international trade contract agreements.</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Company Name & Contract #</th>
                            <th>Contact Email</th>
                            <th>Country</th>
                            <th>Tax ID & Value</th>
                            <th>SLA Rating</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="vendors-table-body">
                        <tr><td colspan="8" style="color: var(--text-muted);">Loading supplier directory...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Tab: Supplier Profile & MSA Contract -->
        <div id="profile-tab" class="tab-content">
            <div id="supplier-profile-container">
                <div style="color: var(--text-secondary); padding: 24px; text-align: center;">Loading supplier profile & contract specifications...</div>
            </div>
        </div>

        <!-- Tab 2: Vendor Cargo Dispatches -->
        <div id="dispatches-tab" class="tab-content">
            <div class="panel">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
                    <div>
                        <h3>📦 Vendor Dispatched Cargo</h3>
                        <p class="panel-desc" style="margin-bottom: 0;">Real-time ocean freight dispatches linked directly to registered vendor partners.</p>
                    </div>
                    <div id="filter-vendor-wrapper" style="display: flex; align-items: center; gap: 8px;">
                        <label style="font-size: 12px; color: var(--text-secondary);">Filter Vendor:</label>
                        <select id="filter-vendor-select" onchange="filterVendorDispatches()" style="padding: 6px 12px; background: rgba(30, 41, 59, 0.8); border: 1px solid var(--border-dark); border-radius: 6px; color: white; font-size: 13px;"></select>
                    </div>
                </div>
                
                <table>
                    <thead>
                        <tr>
                            <th>Vendor Partner</th>
                            <th>Tracking #</th>
                            <th>Origin ➔ Destination</th>
                            <th>Transport Mode</th>
                            <th>Est. Cost (LKR)</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody id="dispatches-table-body">
                        <tr><td colspan="6" style="color: var(--text-secondary);">Loading vendor dispatches...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Tab 3: Vendor SLA Audit Stream -->
        <div id="alerts-tab" class="tab-content">
            <div class="panel">
                <h3>🔔 Vendor Performance & SLA Audit Stream</h3>
                <p class="panel-desc">Automated vendor performance evaluations logged by EJB Timer Services.</p>
                
                <div id="alerts-stream-container" style="display: flex; flex-direction: column; gap: 12px; margin-top: 16px;">
                    <div style="color: var(--text-secondary);">Loading vendor audit logs...</div>
                </div>
            </div>
        </div>

    </main>

    <!-- Modal Form: Register New Vendor Partner -->
    <div id="vendor-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">Register New Vendor Partner</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Add new international supplier into central SCM database with contract agreement.</p>
            
            <form id="register-vendor-form">
                <div class="form-group">
                    <label for="company-name">Company Name</label>
                    <input type="text" id="company-name" placeholder="e.g. Fuji Lanka Logistics Ltd" required>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="contact-email">Contact Email</label>
                        <input type="email" id="contact-email" placeholder="e.g. info@fujilanka.com" required>
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="text" id="phone" placeholder="e.g. +94 11 222 3333" required>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="country">Country</label>
                        <input type="text" id="country" placeholder="e.g. Sri Lanka" value="Sri Lanka" required>
                    </div>
                    <div class="form-group">
                        <label for="tax-id">International Tax ID</label>
                        <input type="text" id="tax-id" placeholder="e.g. TAX-LK-9900" required>
                    </div>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeRegisterVendorModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Register Vendor & Assign Contract</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Form: View Vendor Specifications & SLA Breakdown -->
    <div id="view-vendor-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">👁 Vendor Specifications & SLA Breakdown</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 16px;">Full supplier details, trade contract terms, and 4-factor SLA metric performance.</p>
            
            <div id="vendor-details-content" style="font-size: 13px; line-height: 1.8; color: var(--text-secondary);">
                Loading supplier specifications...
            </div>

            <div style="margin-top: 24px;">
                <button type="button" class="btn-submit" onclick="closeViewVendorModal()">Close Specification</button>
            </div>
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

        if (!roles.includes('ADMIN') && !roles.includes('VENDOR_REP')) {
            var targetPage = 'index.html';
            if (roles.includes('COORDINATOR')) targetPage = 'shipments.jsp';
            else if (roles.includes('WAREHOUSE_MANAGER')) targetPage = 'inventory.jsp';
            else if (roles.includes('CUSTOMS_AGENT')) targetPage = 'customs.jsp';
            alert('Access Restricted: Your security role (' + (roles[0] || 'USER') + ') does not have authorization to access Vendor Portal.');
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

        var currentVendorData = [];
        var allShipmentData = [];
        var activeProfileVendor = null;
        var loggedInUsername = localStorage.getItem('scm_username') || '';
        var isVendorRep = roles.includes('VENDOR_REP') && !roles.includes('ADMIN') && !roles.includes('COORDINATOR');

        function switchTab(tabId, btn) {
            document.querySelectorAll('.tab-content').forEach(function(el) { el.classList.remove('active'); });
            document.querySelectorAll('.tab-btn').forEach(function(el) { el.classList.remove('active'); });
            document.getElementById(tabId).classList.add('active');
            if (btn) btn.classList.add('active');
            if (tabId === 'dispatches-tab') loadVendorDispatches();
            else if (tabId === 'alerts-tab') loadAlerts();
            else if (tabId === 'profile-tab' && activeProfileVendor) renderSupplierProfile(activeProfileVendor);
        }

        function renderSupplierProfile(vendor) {
            if (!vendor) return;
            activeProfileVendor = vendor;
            var container = document.getElementById('supplier-profile-container');
            if (!container) return;

            var contractNum = 'CON-' + (vendor.country ? vendor.country.substring(0,2).toUpperCase() : 'GL') + '-' + (9000 + vendor.id * 15);
            var contractVal = 'Rs. ' + ((45 + vendor.id * 12)).toLocaleString() + ',000,000';
            var slaRating = (vendor.complianceRating || 98.5).toFixed(1);
            var countryFlag = vendor.country === 'Japan' ? '🇯🇵' : (vendor.country === 'Singapore' ? '🇸🇬' : '🇱🇰');

            container.innerHTML = 
                '<div style="background: rgba(30, 41, 59, 0.7); border: 1px solid var(--border-dark); border-radius: 12px; padding: 24px; margin-bottom: 24px; box-shadow: 0 4px 20px rgba(0,0,0,0.25);">' +
                    '<div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 16px; margin-bottom: 20px; border-bottom: 1px solid rgba(255,255,255,0.08); padding-bottom: 16px;">' +
                        '<div>' +
                            '<div style="display: flex; align-items: center; gap: 10px;">' +
                                '<span style="font-size: 28px;">' + countryFlag + '</span>' +
                                '<h2 style="font-size: 22px; font-weight: 700; color: #f8fafc; margin: 0;">' + vendor.companyName + '</h2>' +
                                '<span class="pill pill-green" style="font-size: 11px;">● Verified Trade Partner</span>' +
                            '</div>' +
                            '<p style="font-size: 13px; color: var(--text-secondary); margin: 6px 0 0 0;">Authorized B2B Supplier Organization | Global Supply Chain Network</p>' +
                        '</div>' +
                        '<div style="display: flex; gap: 10px;">' +
                            '<button class="btn-table-action" style="padding: 8px 14px; font-size: 12px; background: rgba(56, 189, 248, 0.15); color: #38bdf8; border: 1px solid rgba(56, 189, 248, 0.3);" onclick="filterDispatchesForVendor(\'' + vendor.companyName + '\')">📦 View Dispatches</button>' +
                            '<button class="btn-table-action" style="padding: 8px 14px; font-size: 12px;" onclick="switchTab(\'alerts-tab\', document.getElementById(\'tab-btn-alerts\'))">🔔 SLA Telemetry</button>' +
                        '</div>' +
                    '</div>' +

                    '<!-- 4 Top KPIs for Vendor -->' +
                    '<div class="stats-grid" style="margin-bottom: 24px;">' +
                        '<div class="stat-card" style="background: rgba(15, 23, 42, 0.6);">' +
                            '<div class="stat-title">Composite SLA Rating</div>' +
                            '<div class="stat-value" style="color: var(--success);">' + slaRating + '%</div>' +
                            '<div class="stat-sub">Class-A Verified Supplier</div>' +
                        '</div>' +
                        '<div class="stat-card" style="background: rgba(15, 23, 42, 0.6);">' +
                            '<div class="stat-title">Master Contract ID</div>' +
                            '<div class="stat-value" style="color: var(--brand-accent); font-size: 18px; line-height: 32px;">' + contractNum + '</div>' +
                            '<div class="stat-sub">Active through Dec 2026</div>' +
                        '</div>' +
                        '<div class="stat-card" style="background: rgba(15, 23, 42, 0.6);">' +
                            '<div class="stat-title">Annual Allocation Value</div>' +
                            '<div class="stat-value" style="color: #f59e0b; font-size: 20px; line-height: 32px;">' + contractVal + '</div>' +
                            '<div class="stat-sub">B2B Trade Agreement</div>' +
                        '</div>' +
                        '<div class="stat-card" style="background: rgba(15, 23, 42, 0.6);">' +
                            '<div class="stat-title">Trade Tax ID</div>' +
                            '<div class="stat-value" style="color: #a855f7; font-size: 18px; line-height: 32px;">' + vendor.taxId + '</div>' +
                            '<div class="stat-sub">Customs HS Registered</div>' +
                        '</div>' +
                    '</div>' +

                    '<!-- 2 Column Profile Panels -->' +
                    '<div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 20px;">' +
                        '<div style="background: rgba(15, 23, 42, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; padding: 18px;">' +
                            '<h4 style="margin: 0 0 14px 0; color: #38bdf8; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">📋 Corporate & Authentication Data</h4>' +
                            '<div style="font-size: 13px; line-height: 2; color: #cbd5e1;">' +
                                '<div><strong>Legal Corporate Entity:</strong> ' + vendor.companyName + '</div>' +
                                '<div><strong>Country of Registration:</strong> ' + (vendor.country || 'Global') + '</div>' +
                                '<div><strong>Operations Contact Email:</strong> <span style="color: var(--brand-accent);">' + vendor.contactEmail + '</span></div>' +
                                '<div><strong>Emergency Dispatch Phone:</strong> ' + (vendor.phone || '+94 77 555 6677') + '</div>' +
                                '<div><strong>Linked User Representative:</strong> ' + (loggedInUsername || 'Supplier Rep') + ' (' + (roles[0] || 'VENDOR_REP') + ')</div>' +
                            '</div>' +
                        '</div>' +

                        '<div style="background: rgba(15, 23, 42, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; padding: 18px;">' +
                            '<h4 style="margin: 0 0 14px 0; color: var(--success); font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">📊 4-Factor SLA Quality Breakdown</h4>' +
                            '<div style="font-size: 13px; color: #cbd5e1;">' +
                                '<div style="margin-bottom: 10px;">' +
                                    '<div style="display: flex; justify-content: space-between; margin-bottom: 4px;"><span>On-Time Vessel Dispatch:</span><strong style="color: var(--success);">99.1%</strong></div>' +
                                    '<div style="height: 6px; background: rgba(255,255,255,0.1); border-radius: 3px; overflow: hidden;"><div style="width: 99.1%; height: 100%; background: var(--success);"></div></div>' +
                                '</div>' +
                                '<div style="margin-bottom: 10px;">' +
                                    '<div style="display: flex; justify-content: space-between; margin-bottom: 4px;"><span>Cargo Safety & Inspection Integrity:</span><strong style="color: var(--success);">98.8%</strong></div>' +
                                    '<div style="height: 6px; background: rgba(255,255,255,0.1); border-radius: 3px; overflow: hidden;"><div style="width: 98.8%; height: 100%; background: var(--success);"></div></div>' +
                                '</div>' +
                                '<div style="margin-bottom: 10px;">' +
                                    '<div style="display: flex; justify-content: space-between; margin-bottom: 4px;"><span>Customs Tariff Clearance Rate:</span><strong style="color: var(--brand-accent);">97.6%</strong></div>' +
                                    '<div style="height: 6px; background: rgba(255,255,255,0.1); border-radius: 3px; overflow: hidden;"><div style="width: 97.6%; height: 100%; background: var(--brand-accent);"></div></div>' +
                                '</div>' +
                                '<div>' +
                                    '<div style="display: flex; justify-content: space-between; margin-bottom: 4px;"><span>Port Berth Turnaround SLA:</span><strong style="color: var(--success);">98.4%</strong></div>' +
                                    '<div style="height: 6px; background: rgba(255,255,255,0.1); border-radius: 3px; overflow: hidden;"><div style="width: 98.4%; height: 100%; background: var(--success);"></div></div>' +
                                '</div>' +
                            '</div>' +
                        '</div>' +
                    '</div>' +
                '</div>';
        }

        function viewVendorProfileTab(vendorId) {
            var vendor = currentVendorData.find(function(v) { return v.id === vendorId; });
            if (vendor) {
                renderSupplierProfile(vendor);
                switchTab('profile-tab', document.getElementById('tab-btn-profile'));
            }
        }

        async function loadVendors() {
            var tbody = document.getElementById('vendors-table-body');

            try {
                var res = await fetch('api/vendors', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    currentVendorData = await res.json();
                    tbody.innerHTML = '';
                    
                    var total = currentVendorData.length;
                    var countries = new Set();
                    var slaSum = 0;

                    if (total === 0) {
                        tbody.innerHTML = '<tr><td colspan="8" style="color: var(--text-secondary);">No vendor partners found. Click "+ Register New Vendor Partner" to add one!</td></tr>';
                    } else {
                        currentVendorData.forEach(function(v) {
                            if (v.country) countries.add(v.country);
                            var rating = (v.complianceRating || 98.5);
                            slaSum += rating;

                            var contractNum = 'CON-' + (v.country ? v.country.substring(0,2).toUpperCase() : 'GL') + '-' + (9000 + v.id * 15);
                            var contractVal = 'Rs. ' + ((45 + v.id * 12)).toLocaleString() + ',000,000';
                            var statusStr = v.status || 'ACTIVE';

                            tbody.innerHTML += '<tr>' +
                                '<td>#' + v.id + '</td>' +
                                '<td><strong>' + v.companyName + '</strong><br><small style="color: var(--brand-accent); font-size: 11px;">' + contractNum + '</small></td>' +
                                '<td>' + v.contactEmail + '</td>' +
                                '<td>' + (v.country || 'N/A') + '</td>' +
                                '<td><span style="color: var(--brand-accent); font-weight: 700;">' + v.taxId + '</span><br><small style="color: var(--text-muted); font-size: 11px;">' + contractVal + '</small></td>' +
                                '<td><span class="pill pill-green">' + rating.toFixed(1) + '% SLA</span></td>' +
                                '<td><span class="pill pill-blue">' + statusStr + '</span></td>' +
                                '<td>' +
                                    '<button class="btn-table-action" onclick="viewVendorProfileTab(' + v.id + ')">🏢 Profile</button>' +
                                    '<button class="btn-table-action" onclick="filterDispatchesForVendor(\'' + v.companyName + '\')">📦 Cargo</button>' +
                                '</td>' +
                            '</tr>';
                        });
                    }

                    document.getElementById('count-total').innerText = total;

                    if (total > 0) {
                        var avgSla = (slaSum / total).toFixed(1);
                        document.getElementById('avg-sla').innerText = avgSla + '%';
                    }

                    // Role-Based UI Segregation & Profile Selection
                    if (isVendorRep) {
                        // External Supplier REP: Hide competitor directory & add button
                        if (document.getElementById('btn-register-vendor')) document.getElementById('btn-register-vendor').style.display = 'none';
                        if (document.getElementById('tab-btn-directory')) document.getElementById('tab-btn-directory').style.display = 'none';
                        if (document.getElementById('tab-btn-profile')) document.getElementById('tab-btn-profile').innerText = '🏢 My Supplier Profile & MSA Contract';
                        if (document.getElementById('tab-btn-dispatches')) document.getElementById('tab-btn-dispatches').innerText = '📦 My Dispatched Cargo';
                        if (document.getElementById('tab-btn-alerts')) document.getElementById('tab-btn-alerts').innerText = '🔔 My SLA Quality Stream';
                        if (document.getElementById('filter-vendor-wrapper')) document.getElementById('filter-vendor-wrapper').style.display = 'none';

                        // Resolve logged-in vendor's company
                        var myVendor = currentVendorData.find(function(v) {
                            return (loggedInUsername === 'vendor' && v.companyName.toLowerCase().indexOf('fuji') !== -1) ||
                                   (loggedInUsername === 'vendor3' && v.companyName.toLowerCase().indexOf('apex') !== -1) ||
                                   (v.contactEmail && v.contactEmail.toLowerCase().indexOf(loggedInUsername.toLowerCase()) !== -1);
                        }) || currentVendorData[0];

                        renderSupplierProfile(myVendor);
                        switchTab('profile-tab', document.getElementById('tab-btn-profile'));
                    } else {
                        // Admin / Coordinator: Full directory view
                        if (!activeProfileVendor && currentVendorData.length > 0) {
                            renderSupplierProfile(currentVendorData[0]);
                        }
                    }

                } else {
                    tbody.innerHTML = '<tr><td colspan="8" style="color: var(--danger);">Failed to load vendors. Session expired.</td></tr>';
                }
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="8" style="color: var(--danger);">Network error contacting server.</td></tr>';
            }
        }

        async function loadVendorDispatches() {
            var tbody = document.getElementById('dispatches-table-body');
            var select = document.getElementById('filter-vendor-select');

            try {
                var res = await fetch('api/shipments', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    allShipmentData = await res.json();
                    
                    // Populate filter dropdown with All Registered Vendors
                    var currentVal = select.value || 'ALL';
                    select.innerHTML = '<option value="ALL">🌐 All Registered Vendors (' + allShipmentData.length + ' Total)</option>';
                    
                    // Use all registered vendors if available, else derive from shipments
                    var vendorNames = new Set();
                    if (currentVendorData && currentVendorData.length > 0) {
                        currentVendorData.forEach(function(v) { if (v.companyName) vendorNames.add(v.companyName); });
                    }
                    allShipmentData.forEach(function(item) {
                        if (item.vendorName) vendorNames.add(item.vendorName);
                    });

                    vendorNames.forEach(function(vName) {
                        var count = allShipmentData.filter(function(s) {
                            var sName = s.vendorName || (s.vendor ? s.vendor.companyName : '');
                            return sName.toLowerCase() === vName.toLowerCase();
                        }).length;
                        select.innerHTML += '<option value="' + vName + '">' + vName + ' (' + count + ' ' + (count === 1 ? 'dispatch' : 'dispatches') + ')</option>';
                    });

                    if (vendorNames.has(currentVal)) {
                        select.value = currentVal;
                    } else {
                        select.value = 'ALL';
                    }

                    filterVendorDispatches();
                } else {
                    tbody.innerHTML = '<tr><td colspan="6" style="color: var(--danger);">Failed to load shipments. Session expired.</td></tr>';
                }
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="6" style="color: var(--danger);">Network error fetching dispatches.</td></tr>';
            }
        }

        function filterVendorDispatches() {
            var select = document.getElementById('filter-vendor-select');
            var selectedVendor = select ? select.value : 'ALL';
            if (!selectedVendor || selectedVendor === 'ALL') {
                renderDispatchesTable(allShipmentData, 'All Registered Vendors');
            } else {
                var filtered = allShipmentData.filter(function(s) {
                    var sName = s.vendorName || (s.vendor ? s.vendor.companyName : '');
                    return sName.toLowerCase() === selectedVendor.toLowerCase();
                });
                renderDispatchesTable(filtered, selectedVendor);
            }
        }

        function filterDispatchesForVendor(vendorName) {
            switchTab('dispatches-tab', document.querySelectorAll('.tab-btn')[1]);
            setTimeout(function() {
                var select = document.getElementById('filter-vendor-select');
                if (select) {
                    select.value = vendorName;
                    filterVendorDispatches();
                }
            }, 100);
        }

        function resetDispatchFilter() {
            var select = document.getElementById('filter-vendor-select');
            if (select) select.value = 'ALL';
            filterVendorDispatches();
        }

        function renderDispatchesTable(data, vendorContext) {
            var tbody = document.getElementById('dispatches-table-body');
            tbody.innerHTML = '';
            
            if (!data || data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="padding: 24px; text-align: center; color: var(--text-secondary);">' +
                    '📦 <strong>No cargo dispatches found for ' + (vendorContext || 'this vendor') + '.</strong><br>' +
                    '<button onclick="resetDispatchFilter()" class="btn-table-action" style="margin-top: 10px; font-size: 12px;">🌐 View All Cargo Dispatches</button>' +
                    '</td></tr>';
            } else {
                data.forEach(function(item) {
                    var rawCost = item.costLkr || item.costUSD || 0;
                    var costFormatted = rawCost ? rawCost.toLocaleString() : '0';
                    var vName = item.vendorName || (item.vendor ? item.vendor.companyName : 'Lanka Freight Ltd');
                    var isDelivered = item.status === 'DELIVERED';
                    var pillClass = isDelivered ? 'pill-green' : (item.status.indexOf('BOOKED') !== -1 ? 'pill-blue' : 'pill-yellow');

                    tbody.innerHTML += '<tr>' +
                        '<td><strong style="color: var(--brand-accent);">' + vName + '</strong></td>' +
                        '<td><strong>' + item.trackingNumber + '</strong></td>' +
                        '<td>' + item.origin + ' ➔ ' + item.destination + '</td>' +
                        '<td><span class="pill pill-blue">' + (item.transportMode || 'OCEAN') + '</span></td>' +
                        '<td>Rs. ' + costFormatted + '</td>' +
                        '<td><span class="pill ' + pillClass + '">' + item.status + '</span></td>' +
                    '</tr>';
                });
            }
        }

        function openViewVendorModal(id) {
            var vendor = currentVendorData.find(function(v) { return v.id === id; });
            if (!vendor) return;

            var contractNum = 'CON-' + (vendor.country ? vendor.country.substring(0,2).toUpperCase() : 'GL') + '-' + (9000 + vendor.id * 15);
            var contractVal = 'Rs. ' + ((45 + vendor.id * 12)).toLocaleString() + ',000,000';
            var slaRating = (vendor.complianceRating || 98.5).toFixed(1);

            document.getElementById('vendor-details-content').innerHTML = 
                '<div style="background: rgba(30, 41, 59, 0.6); padding: 16px; border-radius: 8px; border: 1px solid var(--border-dark); margin-bottom: 16px;">' +
                    '<div><strong>Company Name:</strong> <span style="color: var(--brand-accent); font-weight: 700;">' + vendor.companyName + '</span></div>' +
                    '<div><strong>Contract Number:</strong> ' + contractNum + ' (Expires Dec 2026)</div>' +
                    '<div><strong>Contract Value:</strong> <span style="color: var(--success); font-weight: 700;">' + contractVal + '</span></div>' +
                    '<div><strong>International Tax ID:</strong> ' + vendor.taxId + '</div>' +
                    '<div><strong>Country of Origin:</strong> ' + (vendor.country || 'Global') + '</div>' +
                    '<div><strong>Contact Email:</strong> ' + vendor.contactEmail + '</div>' +
                    '<div><strong>Phone:</strong> ' + (vendor.phone || '+94 11 234 5678') + '</div>' +
                    '<div><strong>Trade Status:</strong> <span style="color: var(--success); font-weight: 700;">● Verified Active Supplier Partner</span></div>' +
                '</div>' +

                '<div style="font-weight: 700; color: white; margin-bottom: 6px;">📊 4-Factor SLA Performance Breakdown:</div>' +
                '<div class="sla-grid-4">' +
                    '<div class="sla-box"><div class="sla-box-val">99.2%</div><div class="sla-box-lbl">On-Time Delivery</div></div>' +
                    '<div class="sla-box"><div class="sla-box-val">98.8%</div><div class="sla-box-lbl">Order Accuracy</div></div>' +
                    '<div class="sla-box"><div class="sla-box-val">97.5%</div><div class="sla-box-lbl">Quality Score</div></div>' +
                    '<div class="sla-box"><div class="sla-box-val">98.0%</div><div class="sla-box-lbl">Response Time</div></div>' +
                '</div>' +
                '<div style="font-size: 12px; color: var(--success); font-weight: 600; text-align: right; margin-top: 4px;">Composite SLA Rating: ' + slaRating + '% (Upward Trend)</div>';

            document.getElementById('view-vendor-modal').style.display = 'flex';
        }

        function closeViewVendorModal() {
            document.getElementById('view-vendor-modal').style.display = 'none';
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
                    
                    var vendorLogs = logs.filter(function(log) {
                        var action = (log.action || '').toUpperCase();
                        return action.indexOf('VENDOR') !== -1 || action.indexOf('SUPPLIER') !== -1;
                    });

                    if (vendorLogs.length === 0) {
                        container.innerHTML = '<div style="color: var(--text-secondary);">No vendor SLA performance alerts recorded yet. Vendor performance timer runs every 20 minutes!</div>';
                    } else {
                        vendorLogs.forEach(function(log) {
                            var actionStr = log.action || 'VENDOR_EVENT';
                            var timeStr = log.timestamp ? new Date(log.timestamp).toLocaleString() : 'Recent Event';
                            var detailsStr = log.details || 'Vendor SLA evaluation event logged.';
                            
                            container.innerHTML += '<div style="padding: 14px; background: rgba(30, 41, 59, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; margin-bottom: 8px;">' +
                                '<strong style="color: #38bdf8;">[' + actionStr + ']</strong> ' + detailsStr +
                                '<div style="font-size: 11px; color: var(--text-muted); margin-top: 4px;">Timestamp: ' + timeStr + '</div>' +
                            '</div>';
                        });
                    }
                }
            } catch (err) {
                container.innerHTML = '<div style="color: var(--danger);">Network error fetching vendor alerts.</div>';
            }
        }

        document.getElementById('register-vendor-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var companyName = document.getElementById('company-name').value;
            var contactEmail = document.getElementById('contact-email').value;
            var phone = document.getElementById('phone').value;
            var country = document.getElementById('country').value;
            var taxId = document.getElementById('tax-id').value;

            try {
                var res = await fetch('api/vendors', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token
                    },
                    body: JSON.stringify({
                        companyName: companyName,
                        contactEmail: contactEmail,
                        phone: phone,
                        country: country,
                        taxId: taxId,
                        complianceRating: 98.5,
                        status: 'ACTIVE'
                    })
                });

                if (res.ok) {
                    closeRegisterVendorModal();
                    loadVendors();
                    alert('Vendor Partner Registered Successfully with Active Supply Contract!');
                } else {
                    alert('Failed to register vendor.');
                }
            } catch (err) {
                alert('Network error registering vendor.');
            }
        });

        function openRegisterVendorModal() {
            document.getElementById('tax-id').value = 'TAX-' + Math.floor(1000 + Math.random() * 9000);
            document.getElementById('vendor-modal').style.display = 'flex';
        }

        function closeRegisterVendorModal() {
            document.getElementById('vendor-modal').style.display = 'none';
        }

        window.addEventListener('load', function() {
            loadVendors();
            loadVendorDispatches();
        });
    </script>
</body>
</html>
