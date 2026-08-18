<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Executive Control Center | GlobalTrade SCM</title>
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
            --transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
        }

        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: var(--bg-dark); color: var(--text-primary); min-height: 100vh; }

        .navbar { height: 64px; background: rgba(15, 23, 42, 0.85); backdrop-filter: blur(12px); border-bottom: 1px solid var(--border-dark); display: flex; align-items: center; justify-content: space-between; padding: 0 32px; position: sticky; top: 0; z-index: 100; }
        .brand { display: flex; align-items: center; gap: 10px; font-size: 18px; font-weight: 800; color: white; text-decoration: none; }
        .brand-logo { width: 32px; height: 32px; background: linear-gradient(135deg, var(--brand-primary), var(--brand-accent)); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; }
        
        .nav-links { display: flex; gap: 8px; }
        .nav-link { color: var(--text-secondary); text-decoration: none; font-weight: 600; font-size: 13px; padding: 8px 14px; border-radius: var(--radius-sm); transition: var(--transition); }
        .nav-link.active, .nav-link:hover { background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); border: 1px solid rgba(56, 189, 248, 0.3); }

        .user-menu { display: flex; align-items: center; gap: 16px; }
        .user-badge { font-size: 12px; font-weight: 700; background: rgba(16, 185, 129, 0.15); color: var(--success); padding: 4px 10px; border-radius: 20px; border: 1px solid rgba(16, 185, 129, 0.3); }
        .btn-logout { background: transparent; border: 1px solid var(--border-dark); color: #fca5a5; padding: 6px 14px; border-radius: var(--radius-sm); font-weight: 600; font-size: 12px; cursor: pointer; text-decoration: none; }

        .container { max-width: 1280px; margin: 36px auto; padding: 0 24px; }
        .page-header { margin-bottom: 32px; }
        .page-header h1 { font-size: 32px; font-weight: 800; letter-spacing: -0.5px; margin-bottom: 8px; }
        .page-header p { color: var(--text-secondary); font-size: 14px; }

        .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 32px; }
        .stat-card { background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(12px); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 20px; transition: var(--transition); }
        .stat-card:hover { border-color: var(--brand-primary); transform: translateY(-2px); box-shadow: 0 10px 25px -5px var(--brand-glow); }
        .stat-title { font-size: 11px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.5px; }
        .stat-value { font-size: 30px; font-weight: 800; color: white; margin: 10px 0 4px; }
        .stat-sub { font-size: 12px; color: var(--text-secondary); }
        .pill { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: 700; margin-top: 6px; }
        .pill-blue { background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); }
        .pill-green { background: rgba(16, 185, 129, 0.15); color: var(--success); }
        .pill-yellow { background: rgba(245, 158, 11, 0.15); color: var(--warning); }

        .panel-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 24px; }
        .panel { background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(12px); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 24px; margin-bottom: 24px; }
        .panel h3 { font-size: 18px; font-weight: 700; margin-bottom: 6px; }
        .panel-desc { font-size: 13px; color: var(--text-secondary); margin-bottom: 20px; }

        table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        th, td { padding: 14px; text-align: left; border-bottom: 1px solid var(--border-dark); font-size: 13px; }
        th { font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 11px; }

        .terminal-box { background: var(--bg-dark); border: 1px solid var(--border-dark); border-radius: var(--radius-sm); padding: 14px; font-family: monospace; font-size: 12px; color: var(--brand-accent); }
    </style>
</head>
<body>

    <header class="navbar">
        <a href="#" class="brand">
            <div class="brand-logo">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L2 7L12 12L22 7L12 2Z"/><path d="M2 17L12 22L22 17"/><path d="M2 12L12 17L22 12"/></svg>
            </div>
            <span>GlobalTrade SCM</span>
        </a>
        <nav class="nav-links">
            <a href="dashboard.jsp" id="link-overview" class="nav-link active" style="display: none;">Overview</a>
            <a href="shipments.jsp" id="link-shipments" class="nav-link" style="display: none;">Shipments</a>
            <a href="inventory.jsp" id="link-inventory" class="nav-link" style="display: none;">Warehouse</a>
            <a href="customs.jsp" id="link-customs" class="nav-link" style="display: none;">Customs</a>
            <a href="vendor.jsp" id="link-vendor" class="nav-link" style="display: none;">Vendor Portal</a>
        </nav>
        <div class="user-menu">
            <span class="user-badge" id="role-badge">USER</span>
            <a href="index.html" class="btn-logout" onclick="localStorage.clear()">Logout</a>
        </div>
    </header>

    <div class="container">
        <div class="page-header">
            <h1>Executive Control Center</h1>
            <p>Master supply chain operations, security audit logs, and global performance metrics.</p>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-title">Logistics Operations</div>
                <div class="stat-value">1,482</div>
                <div class="stat-sub">1,120 In Transit • 342 Delivered</div>
                <span class="pill pill-blue">42 International Ports</span>
            </div>
            <div class="stat-card">
                <div class="stat-title">Warehouse & Stock</div>
                <div class="stat-value">28,940</div>
                <div class="stat-sub">Available Units • 12 Low Stock</div>
                <span class="pill pill-yellow">Automated Monitoring</span>
            </div>
            <div class="stat-card">
                <div class="stat-title">Customs & Compliance</div>
                <div class="stat-value">99.4%</div>
                <span class="pill pill-green">Approved Filings</span>
                <div class="stat-sub" style="margin-top: 4px;">2 Holds • 0 Rejected</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">Vendor Performance</div>
                <div class="stat-value">98.5%</div>
                <div class="stat-sub">On-Time Dispatches</div>
                <span class="pill pill-blue">Tier-1 Qualified</span>
            </div>
        </div>

        <div class="panel-grid">
            <div class="panel">
                <h3>🔒 System Security & Audit Log Stream</h3>
                <p class="panel-desc">Real-time audit log stream capturing system transactions and user activities.</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Action Executed</th>
                            <th>Target Entity</th>
                            <th>Timestamp</th>
                            <th>Transaction Context</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>#1082</td>
                            <td><strong style="color: var(--brand-accent);">CARRIER_BOOKING</strong></td>
                            <td>Shipment #SCM-TRK-90812</td>
                            <td>Just now</td>
                            <td><span style="color: var(--success); font-weight: 700;">TRANSACTION COMMIT</span></td>
                        </tr>
                        <tr>
                            <td>#1081</td>
                            <td><strong style="color: var(--success);">CREATE_SHIPMENT</strong></td>
                            <td>Shipment #SCM-TRK-90814</td>
                            <td>12 mins ago</td>
                            <td><span style="color: var(--success); font-weight: 700;">COMPLETED</span></td>
                        </tr>
                        <tr>
                            <td>#1080</td>
                            <td><strong style="color: var(--warning);">AUTOMATED_STOCK_SCAN</strong></td>
                            <td>Inventory SKU-RAW-109</td>
                            <td>15 mins ago</td>
                            <td><span>AUTOMATED MONITOR</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="panel">
                <h3>⏱️ Background Monitors</h3>
                <p class="panel-desc">Active background monitoring services:</p>

                <div class="terminal-box" style="margin-bottom: 12px;">
                    <strong>Inventory Monitor Service</strong><br>
                    <span style="color: var(--text-secondary);">Automated Stock Scanner</span><br>
                    Status: <span style="color: var(--success);">ACTIVE</span>
                </div>

                <div class="terminal-box">
                    <strong>Carrier Coordinator Service</strong><br>
                    <span style="color: var(--text-secondary);">Freight Booking Coordinator</span><br>
                    Status: <span style="color: var(--brand-accent);">ACTIVE</span>
                </div>
            </div>
        </div>
    </div>

    <script>
        const rawRoles = localStorage.getItem('scm_roles');
        const roles = rawRoles ? JSON.parse(rawRoles) : [];
        const roleBadge = document.getElementById('role-badge');
        
        if (roles.length > 0) roleBadge.innerText = roles[0];

        // Access Control: Only ADMIN can view dashboard.jsp (Executive Overview)
        if (!roles.includes('ADMIN')) {
            alert('Access Denied: The Executive Overview is reserved for System Administrators.');
            if (roles.includes('COORDINATOR')) window.location.href = 'shipments.jsp';
            else if (roles.includes('CUSTOMS_AGENT')) window.location.href = 'customs.jsp';
            else if (roles.includes('WAREHOUSE_MANAGER')) window.location.href = 'inventory.jsp';
            else if (roles.includes('VENDOR_REP')) window.location.href = 'vendor.jsp';
            else window.location.href = 'index.html';
        } else {
            document.getElementById('link-overview').style.display = 'inline-block';
            document.getElementById('link-shipments').style.display = 'inline-block';
            document.getElementById('link-inventory').style.display = 'inline-block';
            document.getElementById('link-customs').style.display = 'inline-block';
            document.getElementById('link-vendor').style.display = 'inline-block';
        }
    </script>
</body>
</html>
