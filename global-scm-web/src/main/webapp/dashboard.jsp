<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Overview & Enterprise User Management | GlobalTrade SCM</title>
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

        /* Status Distribution & System Health Card */
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
                <a href="dashboard.jsp" id="side-overview" class="nav-item active">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
                    <span>Admin Overview</span>
                </a>
                <a href="shipments.jsp" id="side-shipments" class="nav-item">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 17L12 22L22 17"/><path d="M2 12L12 17L22 12"/><path d="M12 2L2 7L12 12L22 7L12 2Z"/></svg>
                    <span>Shipments & Freight</span>
                </a>
                <a href="inventory.jsp" id="side-inventory" class="nav-item">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
                    <span>Warehouse Stock</span>
                </a>
                <a href="customs.jsp" id="side-customs" class="nav-item">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
                    <span>Customs Clearance</span>
                </a>
                <a href="vendor.jsp" id="side-vendor" class="nav-item">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                    <span>Vendor Portal</span>
                </a>
            </nav>
        </div>

        <div class="sidebar-footer">
            <div class="user-info">
                <span class="role-badge" id="role-badge">ADMIN</span>
                <a href="index.html" class="btn-logout" onclick="localStorage.clear()">Logout</a>
            </div>
        </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
        <div class="page-header">
            <div>
                <h1>Admin Overview & Enterprise User Management</h1>
                <p>System-wide supply chain analytics, JAAS role assignments, and central telemetry control.</p>
            </div>
            <button class="btn-action-primary" onclick="openRegisterUserModal()">+ Register New System User</button>
        </div>

        <!-- Sub-Feature Navigation Tabs -->
        <div class="sub-tabs">
            <button class="tab-btn active" onclick="switchTab('users-tab', this)">👤 System User Management & Roles</button>
            <button class="tab-btn" onclick="switchTab('overview-tab', this)">📊 Global SCM System Executive Metrics</button>
            <button class="tab-btn" onclick="switchTab('alerts-tab', this)">🔔 Central System Audit Log Stream</button>
        </div>

        <!-- Tab 1: System User Management Table -->
        <div id="users-tab" class="tab-content active">
            
            <!-- Top Metric Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-title">Registered System Users</div>
                    <div class="stat-value" id="count-users">0</div>
                    <div class="stat-sub">JAAS Authenticated Accounts</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">Active Security Roles</div>
                    <div class="stat-value" style="color: var(--brand-accent);">5 Roles</div>
                    <div class="stat-sub">RBAC Security Engine</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">System Health & Security</div>
                    <div class="stat-value" style="font-size: 16px; color: var(--success); margin-top: 14px;">● 99.9% Operational Uptime</div>
                    <div class="stat-sub">Payara Enterprise Cluster</div>
                </div>
                <div class="stat-card">
                    <div class="stat-title">EJB Active Timers</div>
                    <div class="stat-value" style="color: var(--warning);">5 Timers</div>
                    <div class="stat-sub">Concurrent Background Scans</div>
                </div>
            </div>

            <!-- Health & Security Card -->
            <div class="top-row-cards">
                <div class="analytics-card">
                    <div class="analytics-title">
                        <span>Role-Based Access Control (RBAC) Distribution</span>
                        <span style="font-size: 11px; font-weight: 700; color: var(--success);">🔒 JAAS JWT Security Active</span>
                    </div>
                    <div class="progress-bar-wrap">
                        <div id="bar-delivered" class="bar-delivered" style="width: 100%;"></div>
                    </div>
                </div>

                <div class="timer-card">
                    <div style="font-size: 12px; font-weight: 700; color: var(--brand-accent); text-transform: uppercase; margin-bottom: 6px;">⏱️ Central Telemetry Engine</div>
                    <div style="font-size: 13px; color: var(--text-secondary);">Timer Service: <strong style="color: white;">5 Domain Beans Active</strong></div>
                    <div style="font-size: 13px; color: var(--text-secondary); margin-top: 2px;">Status: <span style="color: var(--success); font-weight: 700;">● System Telemetry Monitored</span></div>
                </div>
            </div>

            <!-- Live Users Directory Table -->
            <div class="panel">
                <h3>Registered System Accounts & Role Assignments</h3>
                <p class="panel-desc">Central User database records controlling multi-portal authentication and authorization permissions.</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Account Status</th>
                            <th>Assigned Security Roles</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="users-table-body">
                        <tr><td colspan="5" style="color: var(--text-muted);">Loading user accounts...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Tab 2: Global SCM System Executive Metrics -->
        <div id="overview-tab" class="tab-content">
            <div class="panel">
                <h3>📊 Executive Supply Chain Performance Metrics</h3>
                <p class="panel-desc">Real-time aggregate data synthesized across Logistics, Warehouse, Customs, and Vendor portals.</p>
                
                <div class="stats-grid" style="margin-bottom: 0;">
                    <div class="stat-card">
                        <div class="stat-title">Active Cargo Dispatches</div>
                        <div class="stat-value" id="exec-dispatches" style="color: var(--brand-accent);">0</div>
                        <div class="stat-sub">Logistics Freight Manifests</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-title">Inventory Valuation</div>
                        <div class="stat-value" id="exec-inventory" style="color: var(--success);">Rs. 0</div>
                        <div class="stat-sub">Total Warehouse Asset Value</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-title">Customs Approval Rate</div>
                        <div class="stat-value" id="exec-customs" style="color: var(--warning);">100%</div>
                        <div class="stat-sub">Port Terminal Clearance</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-title">Supplier Partner SLA</div>
                        <div class="stat-value" style="color: var(--success);">98.4%</div>
                        <div class="stat-sub">Global Trade SLA Score</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tab 3: Central Audit Log Stream -->
        <div id="alerts-tab" class="tab-content">
            <div class="panel">
                <h3>🔔 Central System Audit Log & Telemetry Stream</h3>
                <p class="panel-desc">Complete system-wide audit trail logged by EJB Interceptors and Timers.</p>
                
                <div id="alerts-stream-container" style="display: flex; flex-direction: column; gap: 12px; margin-top: 16px;">
                    <div style="color: var(--text-secondary);">Loading central audit logs...</div>
                </div>
            </div>
        </div>

    </main>

    <!-- Modal Form: Register New System User -->
    <div id="user-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">Register New System User</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Create new user account and assign JAAS role-based security access.</p>
            
            <form id="register-user-form">
                <div class="form-group">
                    <label for="new-username">Username</label>
                    <input type="text" id="new-username" placeholder="e.g. john_doe" required>
                </div>

                <div class="form-group">
                    <label for="new-password">Password</label>
                    <input type="password" id="new-password" placeholder="Password" required>
                </div>

                <div class="form-group">
                    <label for="new-role">Assigned Security Role</label>
                    <select id="new-role" required>
                        <option value="ADMIN">ADMIN (Full Access)</option>
                        <option value="COORDINATOR">COORDINATOR (Shipments & Freight)</option>
                        <option value="WAREHOUSE_MANAGER">WAREHOUSE_MANAGER (Stock & Inventory)</option>
                        <option value="CUSTOMS_AGENT">CUSTOMS_AGENT (Port Customs Clearance)</option>
                        <option value="VENDOR_REP">VENDOR_REP (Vendor Partner Portal)</option>
                    </select>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeRegisterUserModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Register User Account</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Form: Update User Role -->
    <div id="role-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">✏️ Update Security Role Assignment</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Modify RBAC security role for existing account.</p>
            
            <form id="update-role-form">
                <input type="hidden" id="update-user-id">
                
                <div class="form-group">
                    <label for="update-role-select">New Security Role</label>
                    <select id="update-role-select">
                        <option value="ADMIN">ADMIN (Full System Overview Access)</option>
                        <option value="COORDINATOR">COORDINATOR (Shipments & Freight)</option>
                        <option value="WAREHOUSE_MANAGER">WAREHOUSE_MANAGER (Warehouse Stock)</option>
                        <option value="CUSTOMS_AGENT">CUSTOMS_AGENT (Customs Clearance)</option>
                        <option value="VENDOR_REP">VENDOR_REP (Vendor Portal)</option>
                    </select>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeRoleModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Update Security Role</button>
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

        if (!roles.includes('ADMIN')) {
            alert('Access Restricted: Admin Overview & User Management is restricted strictly to System Administrators.');
            window.location.href = 'shipments.jsp';
        }

        document.getElementById('side-overview').style.display = 'flex';

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

        var currentUserData = [];

        function switchTab(tabId, btn) {
            document.querySelectorAll('.tab-content').forEach(function(el) { el.classList.remove('active'); });
            document.querySelectorAll('.tab-btn').forEach(function(el) { el.classList.remove('active'); });
            document.getElementById(tabId).classList.add('active');
            btn.classList.add('active');
            if (tabId === 'overview-tab') loadExecutiveMetrics();
            else if (tabId === 'alerts-tab') loadAlerts();
        }

        async function loadUsers() {
            var tbody = document.getElementById('users-table-body');

            try {
                var res = await fetch('api/users', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    currentUserData = await res.json();
                    tbody.innerHTML = '';
                    
                    var total = currentUserData.length;

                    if (total === 0) {
                        tbody.innerHTML = '<tr><td colspan="5" style="color: var(--text-secondary);">No user accounts found. Click "+ Register New System User" to create one!</td></tr>';
                    } else {
                        currentUserData.forEach(function(u) {
                            var statusStr = u.active ? 'ACTIVE' : 'INACTIVE';
                            var pillClass = u.active ? 'pill-green' : 'pill-red';
                            
                            var roleBadges = '';
                            if (u.roles && u.roles.length > 0) {
                                u.roles.forEach(function(r) {
                                    roleBadges += '<span class="pill pill-blue" style="margin-right: 4px;">' + (r.name || r) + '</span>';
                                });
                            } else {
                                roleBadges = '<span class="pill pill-yellow">USER</span>';
                            }

                            tbody.innerHTML += '<tr>' +
                                '<td>#' + u.id + '</td>' +
                                '<td><strong>' + u.username + '</strong></td>' +
                                '<td><span class="pill ' + pillClass + '">' + statusStr + '</span></td>' +
                                '<td>' + roleBadges + '</td>' +
                                '<td>' +
                                    '<button class="btn-table-action" onclick="sendOnboardingEmail(' + u.id + ', \'' + u.username + '\')">📧 Send Email</button>' +
                                    '<button class="btn-table-action" onclick="openRoleModal(' + u.id + ')">✏️ Role</button>' +
                                    '<button class="btn-table-action" onclick="toggleUserStatus(' + u.id + ')">' + (u.active ? '🚫 Deactivate' : '✅ Activate') + '</button>' +
                                '</td>' +
                            '</tr>';
                        });
                    }

                    document.getElementById('count-users').innerText = total;

                } else {
                    tbody.innerHTML = '<tr><td colspan="5" style="color: var(--danger);">Failed to load users. Session expired.</td></tr>';
                }
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="5" style="color: var(--danger);">Network error contacting server.</td></tr>';
            }
        }

        async function sendOnboardingEmail(userId, username) {
            try {
                var res = await fetch('api/users/' + userId + '/send-email', {
                    method: 'POST',
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    alert('📧 Onboarding Invitation Email Dispatched Successfully to ' + username + '!');
                } else {
                    alert('Failed to send onboarding email.');
                }
            } catch (err) {
                alert('Network error sending onboarding email.');
            }
        }

        async function toggleUserStatus(userId) {
            try {
                var res = await fetch('api/users/' + userId + '/status', {
                    method: 'PUT',
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    loadUsers();
                } else {
                    alert('Failed to toggle user status.');
                }
            } catch (err) {
                alert('Network error toggling user status.');
            }
        }

        async function loadExecutiveMetrics() {
            try {
                var resS = await fetch('api/shipments', { headers: { 'Authorization': 'Bearer ' + token } });
                if (resS.ok) {
                    var shipments = await resS.json();
                    document.getElementById('exec-dispatches').innerText = shipments.length;
                }

                var resI = await fetch('api/inventory', { headers: { 'Authorization': 'Bearer ' + token } });
                if (resI.ok) {
                    var items = await resI.json();
                    var sum = 0;
                    items.forEach(function(i) { sum += (i.quantity * (i.unitPriceLkr || 4500)); });
                    document.getElementById('exec-inventory').innerText = 'Rs. ' + sum.toLocaleString();
                }
            } catch (err) {}
        }

        async function loadAlerts() {
            var container = document.getElementById('alerts-stream-container');
            try {
                var res = await fetch('api/alerts?limit=50', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    var logs = await res.json();
                    container.innerHTML = '';
                    
                    if (logs.length === 0) {
                        container.innerHTML = '<div style="color: var(--text-secondary);">No system audit logs recorded yet.</div>';
                    } else {
                        logs.forEach(function(log) {
                            var actionStr = log.action || 'SYSTEM_EVENT';
                            var timeStr = log.timestamp ? new Date(log.timestamp).toLocaleString() : 'Recent Event';
                            var detailsStr = log.details || 'System event recorded.';
                            var userStr = log.username ? (' (User: ' + log.username + ')') : '';
                            
                            container.innerHTML += '<div style="padding: 14px; background: rgba(30, 41, 59, 0.6); border: 1px solid var(--border-dark); border-radius: 8px; margin-bottom: 8px;">' +
                                '<strong style="color: #38bdf8;">[' + actionStr + ']</strong> ' + detailsStr + userStr +
                                '<div style="font-size: 11px; color: var(--text-muted); margin-top: 4px;">Timestamp: ' + timeStr + '</div>' +
                            '</div>';
                        });
                    }
                }
            } catch (err) {
                container.innerHTML = '<div style="color: var(--danger);">Network error fetching audit logs.</div>';
            }
        }

        document.getElementById('register-user-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var username = document.getElementById('new-username').value;
            var password = document.getElementById('new-password').value;
            var role = document.getElementById('new-role').value;

            try {
                var res = await fetch('api/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token
                    },
                    body: JSON.stringify({
                        username: username,
                        password: password,
                        role: role
                    })
                });

                if (res.ok) {
                    closeRegisterUserModal();
                    loadUsers();
                    alert('System User Registered Successfully with Role ' + role + '!');
                } else {
                    alert('Failed to register user: ' + await res.text());
                }
            } catch (err) {
                alert('Network error registering user.');
            }
        });

        document.getElementById('update-role-form').addEventListener('submit', async function(e) {
            e.preventDefault();
            var userId = document.getElementById('update-user-id').value;
            var role = document.getElementById('update-role-select').value;

            try {
                var res = await fetch('api/users/' + userId + '/role?role=' + role, {
                    method: 'PUT',
                    headers: { 'Authorization': 'Bearer ' + token }
                });

                if (res.ok) {
                    closeRoleModal();
                    loadUsers();
                    alert('Security Role Updated Successfully to ' + role + '!');
                } else {
                    alert('Failed to update user role.');
                }
            } catch (err) {
                alert('Network error updating user role.');
            }
        });

        function openRegisterUserModal() {
            document.getElementById('user-modal').style.display = 'flex';
        }

        function closeRegisterUserModal() {
            document.getElementById('user-modal').style.display = 'none';
        }

        function openRoleModal(userId) {
            document.getElementById('update-user-id').value = userId;
            document.getElementById('role-modal').style.display = 'flex';
        }

        function closeRoleModal() {
            document.getElementById('role-modal').style.display = 'none';
        }

        window.addEventListener('load', loadUsers);
    </script>
</body>
</html>
