<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Overview | GlobalTrade SCM</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg-dark: #090d16;
            --bg-surface: #0f172a;
            --bg-light: #f8fafc;
            --text-dark: #0f172a;
            --text-muted: #64748b;
            --brand-primary: #2563eb;
            --border-light: #e2e8f0;
            --success: #10b981;
            --warning: #f59e0b;
        }

        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Plus Jakarta Sans', sans-serif; background: var(--bg-light); color: var(--text-dark); min-height: 100vh; }

        .navbar { height: 64px; background: white; border-bottom: 1px solid var(--border-light); display: flex; align-items: center; justify-content: space-between; padding: 0 32px; }
        .brand { font-size: 18px; font-weight: 800; color: var(--brand-primary); }
        .nav-links { display: flex; gap: 20px; }
        .nav-link { color: var(--text-muted); text-decoration: none; font-weight: 600; font-size: 14px; padding: 8px 12px; border-radius: 6px; }
        .nav-link.active, .nav-link:hover { background: #eff6ff; color: var(--brand-primary); }

        .container { max-width: 1200px; margin: 36px auto; padding: 0 24px; }
        .page-header { margin-bottom: 28px; }
        .page-header h1 { font-size: 28px; font-weight: 800; margin-bottom: 6px; }
        .page-header p { color: var(--text-muted); font-size: 14px; }

        .stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 32px; }
        .stat-card { background: white; border: 1px solid var(--border-light); border-radius: 12px; padding: 24px; box-shadow: 0 2px 4px rgba(0,0,0,0.03); }
        .stat-title { font-size: 13px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; }
        .stat-value { font-size: 32px; font-weight: 800; color: var(--text-dark); margin: 12px 0 4px; }
        .stat-badge { display: inline-block; padding: 3px 8px; border-radius: 4px; font-size: 12px; font-weight: 700; }
        .badge-blue { background: #eff6ff; color: var(--brand-primary); }
        .badge-green { background: #ecfdf5; color: var(--success); }

        .panel { background: white; border: 1px solid var(--border-light); border-radius: 12px; padding: 28px; margin-bottom: 24px; }
        .panel h3 { font-size: 18px; font-weight: 700; margin-bottom: 12px; }
        .panel p { color: var(--text-muted); font-size: 14px; margin-bottom: 16px; }
    </style>
</head>
<body>
    <header class="navbar">
        <div class="brand">GlobalTrade SCM Portal</div>
        <nav class="nav-links">
            <a href="dashboard.jsp" class="nav-link active">Overview</a>
            <a href="shipments.jsp" class="nav-link">Shipments & Logistics</a>
            <a href="inventory.jsp" class="nav-link">Warehouse Inventory</a>
            <a href="customs.jsp" class="nav-link">Customs Compliance</a>
            <a href="index.html" class="nav-link" style="color: #ef4444;">Logout</a>
        </nav>
    </header>

    <div class="container">
        <div class="page-header">
            <h1>Executive SCM Control Center</h1>
            <p>Real-time enterprise metrics powered by Payara 6 EJB Services & MySQL JTA Transactions.</p>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-title">Active Logistics Containers</div>
                <div class="stat-value">1,482</div>
                <span class="stat-badge badge-blue">42 International Ports</span>
            </div>
            <div class="stat-card">
                <div class="stat-title">Trade Clearances</div>
                <div class="stat-value">99.4%</div>
                <span class="stat-badge badge-green">Automated Verification</span>
            </div>
            <div class="stat-card">
                <div class="stat-title">EJB Timers Monitored</div>
                <div class="stat-value">3 Active</div>
                <span class="stat-badge badge-blue">Background Auto-Retry</span>
            </div>
        </div>

        <div class="panel">
            <h3>🔒 Security Framework Architecture (LO 4)</h3>
            <p>Your authentication context is protected via custom JAAS <code>SCMLoginModule</code> with 256-bit HMAC JWT tokens.</p>
        </div>
    </div>
</body>
</html>
