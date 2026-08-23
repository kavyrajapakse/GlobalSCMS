<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customs Clearance & Port Terminal Operations | GlobalTrade SCM</title>
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

        /* Status Distribution & Live Monitoring Card */
        .top-row-cards { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-bottom: 24px; }
        .analytics-card { background: rgba(15, 23, 42, 0.75); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 20px; }
        .analytics-title { font-size: 13px; font-weight: 700; color: var(--text-secondary); margin-bottom: 12px; display: flex; justify-content: space-between; }
        .progress-bar-wrap { height: 10px; background: rgba(30, 41, 59, 0.8); border-radius: 5px; overflow: hidden; display: flex; }
        .bar-delivered { background: var(--success); height: 100%; transition: width 0.3s; }

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
        .modal-card { width: 100%; max-width: 620px; background: var(--bg-surface); border: 1px solid var(--border-dark); border-radius: var(--radius-lg); padding: 32px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.7); max-height: 90vh; overflow-y: auto; }
        
        .doc-chip { display: inline-flex; align-items: center; gap: 6px; background: rgba(30, 41, 59, 0.9); border: 1px solid var(--border-dark); padding: 6px 12px; border-radius: 6px; font-size: 12px; color: var(--brand-accent); margin-right: 8px; margin-bottom: 8px; text-decoration: none; }
        .doc-chip:hover { border-color: var(--brand-accent); background: rgba(37, 99, 235, 0.15); }
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
                <a href="customs.jsp" id="side-customs" class="nav-item active">
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
                <span class="role-badge" id="role-badge">CUSTOMS</span>
                <a href="index.html" class="btn-logout" onclick="localStorage.clear()">Logout</a>
            </div>
        </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
        <div class="page-header">
            <div>
                <h1>Customs Clearance & Port Terminal Operations</h1>
                <p>Manage import/export declarations, container berth assignments, trade compliance, and port release approvals.</p>
            </div>
            <button class="btn-action-primary" onclick="openFileDeclarationModal()">+ File Customs Declaration</button>
        </div>

        <!-- Sub-Feature Navigation Tabs -->
        <div class="sub-tabs">
            <button class="tab-btn active" onclick="switchTab('declarations-tab', this)">📑 Active Declarations & Port Filings</button>
            <button class="tab-btn" onclick="switchTab('alerts-tab', this)">🔔 Live Port Clearance Event Stream</button>
        </div>

        <!-- Tab 1: Active Customs Declarations Table -->
        <div id="declarations-tab" class="tab-content active">
            
            <!-- Top Metric Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-title">Total Declarations</div>
                    <div class="stat-value" id="count-total">0</div>
                    <div class="stat-sub">Filed Manifests</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Approved Clearance</div>
                    <div class="stat-value" id="count-approved" style="color: var(--success);">0</div>
                    <div class="stat-sub">Cleared for Entry & Port Release</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Pending Inspection</div>
                    <div class="stat-value" id="count-pending" style="color: var(--warning);">0</div>
                    <div class="stat-sub">Under Document Review</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Customs Holds</div>
                    <div class="stat-value" id="count-hold" style="color: var(--danger);">0</div>
                    <div class="stat-sub">Held at Terminal</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Port Terminal</div>
                    <div class="stat-value" style="font-size: 15px; color: var(--brand-accent); margin-top: 14px;">Hambantota Deep Water (Berth #04)</div>
                    <div class="stat-sub">Gate-In Verified</div>
                </div>
            </div>

            <!-- Health & Processing Progress Bar -->
            <div class="top-row-cards">
                <div class="analytics-card">
                    <div class="analytics-title">
                        <span>Customs Clearance & Inspection Progress</span>
                        <span id="analytics-legend" style="font-size: 11px; font-weight: 600;">Loading breakdown...</span>
                    </div>
                    <div class="progress-bar-wrap">
                        <div id="bar-approved" class="bar-delivered" style="width: 0%;"></div>
                        <div id="bar-pending" style="background: var(--warning); height: 100%; width: 0%;"></div>
                        <div id="bar-hold" style="background: var(--danger); height: 100%; width: 0%;"></div>
                    </div>
                </div>

                <div class="timer-card">
                    <div style="font-size: 12px; font-weight: 700; color: var(--brand-accent); text-transform: uppercase; margin-bottom: 6px;">⏱️ Live Port Clearance Service</div>
                    <div style="font-size: 13px; color: var(--text-secondary);">Audit Frequency: <strong style="color: white;">Every 15 Minutes</strong></div>
                    <div style="font-size: 13px; color: var(--text-secondary); margin-top: 2px;">Status: <span style="color: var(--success); font-weight: 700;">● Live Clearance Polling Active</span></div>
                </div>
            </div>

            <!-- Live Declarations Table -->
            <div class="panel">
                <h3>Registered Customs Declarations</h3>
                <p class="panel-desc">Real-time customs inspection filings linked directly to active ocean and air freight shipments.</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Filing #</th>
                            <th>Shipment Tracking #</th>
                            <th>Declaration Specification</th>
                            <th>Filed Date</th>
                            <th>Clearance Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="customs-table-body">
                        <tr><td colspan="7" style="color: var(--text-muted);">Loading customs filings...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Tab 2: Port Clearance Alerts Stream -->
        <div id="alerts-tab" class="tab-content">
            <div class="panel">
                <h3>🔔 Live Port Clearance & Inspection Stream</h3>
                <p class="panel-desc">Real-time customs clearance telemetry logged automatically by EJB Timer Services.</p>
                
                <div id="alerts-stream-container" style="display: flex; flex-direction: column; gap: 12px; margin-top: 16px;">
                    <div style="color: var(--text-secondary);">Loading port clearance logs...</div>
                </div>
            </div>
        </div>

    </main>

    <!-- Modal Form: File Customs Declaration -->
    <div id="file-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">File Customs Clearance Declaration</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Submit new import/export declaration for active freight shipment.</p>
            
            <form id="file-declaration-form">
                <div class="form-group">
                    <label for="select-shipment">Select Active Freight Shipment</label>
                    <select id="select-shipment" required></select>
                </div>

                <div class="form-group">
                    <label for="declaration-details">Customs Declaration Specification & Tariff HS Code</label>
                    <textarea id="declaration-details" rows="3" placeholder="e.g. Standard import manifest declaration cleared under Sri Lanka Customs Tariff HS Code 8542.31." required>Standard import manifest declaration cleared under Sri Lanka Customs Tariff HS Code 8542.31.</textarea>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeFileDeclarationModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Submit Declaration</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Form: View Customs Manifest & Inspection Specifications -->
    <div id="view-details-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">👁 Customs Manifest & Inspection Details</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 16px;">Full trade compliance, port terminal berth assignment, and verified shipping documents.</p>
            
            <div id="manifest-details-content" style="font-size: 13px; line-height: 1.8; color: var(--text-secondary);">
                Loading declaration specifications...
            </div>

            <div style="margin-top: 20px; background: rgba(30, 41, 59, 0.6); padding: 16px; border-radius: 8px; border: 1px solid var(--border-dark);">
                <div style="font-weight: 700; color: white; margin-bottom: 8px;">📄 Verified International Trade Documents</div>
                <div>
                    <a href="#" onclick="alert('Viewing Commercial Invoice PDF: Verified'); return false;" class="doc-chip">📄 Commercial Invoice (CI-8890.pdf)</a>
                    <a href="#" onclick="alert('Viewing Bill of Lading PDF: Verified'); return false;" class="doc-chip">📄 Bill of Lading (BL-MAERSK-901.pdf)</a>
                    <a href="#" onclick="alert('Viewing Packing List PDF: Verified'); return false;" class="doc-chip">📄 Packing List (PL-2026.pdf)</a>
                    <a href="#" onclick="alert('Viewing Certificate of Origin PDF: Verified'); return false;" class="doc-chip">📄 Certificate of Origin (COO-LK-102.pdf)</a>
                </div>
            </div>

            <div style="margin-top: 24px;">
                <button type="button" class="btn-submit" onclick="closeViewDetailsModal()">Close Specification</button>
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

        if (!roles.includes('ADMIN') && !roles.includes('CUSTOMS_AGENT')) {
            var targetPage = 'index.html';
            if (roles.includes('COORDINATOR')) targetPage = 'shipments.jsp';
            else if (roles.includes('WAREHOUSE_MANAGER')) targetPage = 'inventory.jsp';
            else if (roles.includes('VENDOR_REP')) targetPage = 'vendor.jsp';
            alert('Access Restricted: Your security role (' + (roles[0] || 'USER') + ') does not have authorization to access Customs Clearance.');
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

        var currentCustomsData = [];
        var activeShipments = [];

        function switchTab(tabId, btn) {
            document.querySelectorAll('.tab-content').forEach(function(el) { el.classList.remove('active'); });
            document.querySelectorAll('.tab-btn').forEach(function(el) { el.classList.remove('active'); });
            document.getElementById(tabId).classList.add('active');
            btn.classList.add('active');
            if (tabId === 'alerts-tab') loadAlerts();
        }

        async function loadCustomsFilings() {
            var tbody = document.getElementById('customs-table-body');

            try {
                var res = await fetch('api/customs', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    currentCustomsData = await res.json();
                    tbody.innerHTML = '';
                    
                    var approved = 0, pending = 0, hold = 0;
                    var total = currentCustomsData.length;

                    if (total === 0) {
                        tbody.innerHTML = '<tr><td colspan="7" style="color: var(--text-secondary);">No customs filings recorded. Click "+ File Customs Declaration" to submit one!</td></tr>';
                    } else {
                        currentCustomsData.forEach(function(item) {
                            var statusStr = item.status || 'CLEARANCE_REQUESTED';
                            var pillClass = 'pill-yellow';
                            var actionButtonsHtml = '';

                            if (statusStr === 'APPROVED') {
                                pillClass = 'pill-green';
                                approved++;
                                actionButtonsHtml = '<button class="btn-table-action" onclick="openViewDetailsModal(' + item.id + ')">👁 View Manifest</button>';
                            } else if (statusStr === 'REJECTED' || statusStr === 'CUSTOMS_HOLD') {
                                pillClass = 'pill-red';
                                hold++;
                                actionButtonsHtml = '<button class="btn-table-action" onclick="updateFilingStatus(' + item.id + ', \'APPROVED\')">🔓 Release Hold</button>' +
                                                    '<button class="btn-table-action" onclick="openViewDetailsModal(' + item.id + ')">👁 View</button>';
                            } else {
                                pending++;
                                actionButtonsHtml = '<button class="btn-table-action" onclick="updateFilingStatus(' + item.id + ', \'APPROVED\')">✅ Approve</button>' +
                                                    '<button class="btn-table-action" onclick="updateFilingStatus(' + item.id + ', \'CUSTOMS_HOLD\')">⚠️ Issue Hold</button>' +
                                                    '<button class="btn-table-action" onclick="openViewDetailsModal(' + item.id + ')">👁 View</button>';
                            }

                            var trackingStr = item.shipment ? item.shipment.trackingNumber : 'N/A';
                            var dateStr = item.filedAt ? new Date(item.filedAt).toLocaleDateString() : 'Recent';
                            var detailsStr = item.declarationDetails || 'Standard import manifest declaration.';

                            tbody.innerHTML += '<tr>' +
                                '<td>#' + item.id + '</td>' +
                                '<td><strong>' + item.filingNumber + '</strong></td>' +
                                '<td><span style="color: var(--brand-accent); font-weight: 700;">' + trackingStr + '</span></td>' +
                                '<td>' + detailsStr + '</td>' +
                                '<td>' + dateStr + '</td>' +
                                '<td><span class="pill ' + pillClass + '">' + statusStr + '</span></td>' +
                                '<td>' + actionButtonsHtml + '</td>' +
                            '</tr>';
                        });
                    }

                    document.getElementById('count-total').innerText = total;
                    document.getElementById('count-approved').innerText = approved;
                    document.getElementById('count-pending').innerText = pending;
                    document.getElementById('count-hold').innerText = hold;

                    if (total > 0) {
                        var approvedPct = Math.round((approved / total) * 100);
                        var pendingPct = Math.round((pending / total) * 100);
                        var holdPct = Math.round((hold / total) * 100);

                        document.getElementById('bar-approved').style.width = approvedPct + '%';
                        document.getElementById('bar-pending').style.width = pendingPct + '%';
                        document.getElementById('bar-hold').style.width = holdPct + '%';

                        document.getElementById('analytics-legend').innerText = 'Approved: ' + approvedPct + '% | Pending: ' + pendingPct + '% | Held: ' + holdPct + '%';
                    } else {
                        document.getElementById('analytics-legend').innerText = 'No declaration data available';
                    }

                } else {
                    tbody.innerHTML = '<tr><td colspan="7" style="color: var(--danger);">Failed to load customs declarations. Session expired.</td></tr>';
                }
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="7" style="color: var(--danger);">Network error contacting server.</td></tr>';
            }
        }

        async function updateFilingStatus(filingId, newStatus) {
            try {
                var res = await fetch('api/customs/' + filingId + '/status?status=' + newStatus, {
                    method: 'PUT',
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    loadCustomsFilings();
                    loadAlerts();
                    alert('Customs Declaration Updated to ' + newStatus + '! (Logistics Shipment Status automatically updated).');
                } else {
                    var errObj = await res.json();
                    alert('⚠️ Customs Operation Warning: ' + (errObj.message || 'Failed to update customs filing status.'));
                }
            } catch (err) {
                alert('Network error updating customs status.');
            }
        }

        function openViewDetailsModal(id) {
            var filing = currentCustomsData.find(function(f) { return f.id === id; });
            if (!filing) return;

            var trackingStr = filing.shipment ? filing.shipment.trackingNumber : 'N/A';
            var originStr = filing.shipment ? (filing.shipment.origin + ' ➔ ' + filing.shipment.destination) : 'Hambantota ➔ Nagoya';
            var vendorStr = filing.shipment ? (filing.shipment.vendorName || 'Lanka Freight Ltd') : 'Lanka Freight Ltd';
            var dateStr = filing.filedAt ? new Date(filing.filedAt).toLocaleString() : 'Recent';
            var statusStr = filing.status || 'CLEARANCE_REQUESTED';
            var detailsStr = filing.declarationDetails || 'Standard import manifest declaration cleared under Sri Lanka Customs Tariff HS Code 8542.31.';

            document.getElementById('manifest-details-content').innerHTML = 
                '<div style="background: rgba(30, 41, 59, 0.6); padding: 16px; border-radius: 8px; border: 1px solid var(--border-dark); margin-bottom: 16px;">' +
                    '<div><strong>Filing Number:</strong> <span style="color: var(--brand-accent); font-weight: 700;">' + filing.filingNumber + '</span></div>' +
                    '<div><strong>Linked Shipment Tracking:</strong> ' + trackingStr + '</div>' +
                    '<div><strong>Vendor / Importer:</strong> ' + vendorStr + '</div>' +
                    '<div><strong>Port Route:</strong> ' + originStr + '</div>' +
                    '<div><strong>Port Terminal & Berth:</strong> Hambantota Deep Water Terminal (Berth #04)</div>' +
                    '<div><strong>Gate-In Status:</strong> <span style="color: var(--success); font-weight: 700;">Gate-In Verified</span></div>' +
                    '<div><strong>Filing Timestamp:</strong> ' + dateStr + '</div>' +
                    '<div><strong>Clearance Status:</strong> <span style="color: var(--brand-accent); font-weight: 700;">' + statusStr + '</span></div>' +
                '</div>' +
                '<div><strong>Declaration Tariff Specification:</strong></div>' +
                '<div style="color: white; margin-top: 4px;">' + detailsStr + '</div>';

            document.getElementById('view-details-modal').style.display = 'flex';
        }

        function closeViewDetailsModal() {
            document.getElementById('view-details-modal').style.display = 'none';
        }

        async function loadShipmentsForModal() {
            try {
                var res = await fetch('api/shipments', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    activeShipments = await res.json();
                    var select = document.getElementById('select-shipment');
                    select.innerHTML = '';

                    if (activeShipments.length === 0) {
                        select.innerHTML = '<option value="">No active shipments available</option>';
                    } else {
                        activeShipments.forEach(function(s) {
                            select.innerHTML += '<option value="' + s.id + '">' + s.trackingNumber + ' (' + s.origin + ' ➔ ' + s.destination + ')</option>';
                        });
                    }
                }
            } catch (err) {}
        }

        async function loadAlerts() {
            var container = document.getElementById('alerts-stream-container');
            if (!container) return;
            try {
                var res = await fetch('api/alerts?limit=200', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    var logs = await res.json();
                    container.innerHTML = '';
                    
                    var customsLogs = logs.filter(function(log) {
                        var action = (log.action || '').toUpperCase();
                        return action.indexOf('PORT') !== -1 || action.indexOf('CUSTOMS') !== -1 || 
                               action.indexOf('CLEARANCE') !== -1 || action.indexOf('HOLD') !== -1 ||
                               action.indexOf('DECLARATION') !== -1 || action.indexOf('TARIFF') !== -1;
                    });

                    if (!customsLogs || customsLogs.length === 0) {
                        container.innerHTML = '<div style="color: var(--text-secondary); padding: 16px; background: rgba(30, 41, 59, 0.4); border: 1px dashed var(--border-dark); border-radius: 8px;">' +
                            '🛡️ <strong>No recent customs or port clearance event logs found.</strong><br>' +
                            '<span style="font-size: 12px; color: var(--text-muted);">File a declaration or click "Approve / Release" on any filing to generate live port clearance event telemetry!</span>' +
                            '</div>';
                    } else {
                        customsLogs.forEach(function(log) {
                            var actionStr = log.action || 'CUSTOMS_EVENT';
                            var isApproved = actionStr.indexOf('APPROVED') !== -1 || actionStr.indexOf('CLEARED') !== -1;
                            var isHold = actionStr.indexOf('HOLD') !== -1 || actionStr.indexOf('REJECTED') !== -1;
                            var badgeColor = isApproved ? '#10b981' : (isHold ? '#ef4444' : '#38bdf8');
                            var timeStr = log.timestamp ? new Date(log.timestamp).toLocaleString() : 'Recent Event';
                            var detailsStr = log.details || 'Port clearance transaction logged.';
                            var userStr = log.username ? 'by Agent ' + log.username : '';
                            
                            container.innerHTML += '<div style="padding: 14px 16px; background: rgba(30, 41, 59, 0.7); border: 1px solid var(--border-dark); border-left: 4px solid ' + badgeColor + '; border-radius: 8px; margin-bottom: 10px;">' +
                                '<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">' +
                                    '<span style="color: ' + badgeColor + '; font-weight: 700; font-size: 13px;">[' + actionStr + ']</span>' +
                                    '<span style="font-size: 11px; color: var(--text-muted);">' + timeStr + '</span>' +
                                '</div>' +
                                '<div style="color: #f1f5f9; font-size: 13px; margin-bottom: 4px;">' + detailsStr + '</div>' +
                                (userStr ? '<div style="font-size: 11px; color: var(--text-secondary);">' + userStr + '</div>' : '') +
                            '</div>';
                        });
                    }
                }
            } catch (err) {
                container.innerHTML = '<div style="color: var(--danger); padding: 12px;">Network error fetching customs alerts.</div>';
            }
        }

        document.getElementById('file-declaration-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var shipmentId = document.getElementById('select-shipment').value;
            var details = encodeURIComponent(document.getElementById('declaration-details').value);

            if (!shipmentId) {
                alert('Please select a valid freight shipment.');
                return;
            }

            try {
                var res = await fetch('api/customs?shipmentId=' + shipmentId + '&details=' + details, {
                    method: 'POST',
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    closeFileDeclarationModal();
                    loadCustomsFilings();
                    loadAlerts();
                    alert('Customs Clearance Declaration Filed Successfully!');
                } else {
                    alert('Failed to file customs declaration: ' + await res.text());
                }
            } catch (err) {
                alert('Network error filing customs declaration.');
            }
        });

        function openFileDeclarationModal() {
            loadShipmentsForModal();
            document.getElementById('file-modal').style.display = 'flex';
        }

        function closeFileDeclarationModal() {
            document.getElementById('file-modal').style.display = 'none';
        }

        window.addEventListener('load', function() {
            loadCustomsFilings();
            loadAlerts();
            // 10-Second Live Polling Interval for Port Clearance
            setInterval(function() {
                loadCustomsFilings();
                loadAlerts();
            }, 10000);
        });
    </script>
</body>
</html>
