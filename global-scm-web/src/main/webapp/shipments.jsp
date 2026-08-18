<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipments & Carrier Bookings | GlobalTrade SCM</title>
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
        .user-badge { font-size: 12px; font-weight: 700; background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); padding: 4px 10px; border-radius: 20px; border: 1px solid rgba(56, 189, 248, 0.3); }
        .btn-logout { background: transparent; border: 1px solid var(--border-dark); color: #fca5a5; padding: 6px 14px; border-radius: var(--radius-sm); font-weight: 600; font-size: 12px; cursor: pointer; text-decoration: none; }

        .container { max-width: 1280px; margin: 36px auto; padding: 0 24px; }
        .page-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 32px; }
        .page-header h1 { font-size: 32px; font-weight: 800; letter-spacing: -0.5px; margin-bottom: 8px; }
        .page-header p { color: var(--text-secondary); font-size: 14px; }

        .btn-action-primary { padding: 12px 20px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); box-shadow: 0 4px 12px var(--brand-glow); }
        .btn-action-primary:hover { transform: translateY(-1px); }

        .stats-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 16px; margin-bottom: 32px; }
        .stat-card { background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(12px); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 18px; }
        .stat-title { font-size: 11px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.5px; }
        .stat-value { font-size: 26px; font-weight: 800; color: white; margin: 8px 0 4px; }
        .stat-sub { font-size: 12px; color: var(--text-secondary); }

        .grid-2 { display: grid; grid-template-columns: 2fr 1fr; gap: 24px; }
        .panel { background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(12px); border: 1px solid var(--border-dark); border-radius: var(--radius-md); padding: 24px; }
        .panel h3 { font-size: 18px; font-weight: 700; margin-bottom: 6px; }
        .panel-desc { font-size: 13px; color: var(--text-secondary); margin-bottom: 20px; }

        table { width: 100%; border-collapse: collapse; margin-top: 8px; }
        th, td { padding: 14px; text-align: left; border-bottom: 1px solid var(--border-dark); font-size: 13px; }
        th { font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 11px; }

        .pill { padding: 4px 10px; border-radius: 20px; font-size: 11px; font-weight: 700; }
        .pill-blue { background: rgba(37, 99, 235, 0.15); color: var(--brand-accent); }
        .pill-green { background: rgba(16, 185, 129, 0.15); color: var(--success); }

        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-size: 12px; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }
        .form-group input { width: 100%; padding: 11px 14px; background: rgba(30, 41, 59, 0.8); border: 1px solid var(--border-dark); border-radius: var(--radius-md); color: white; font-family: inherit; font-size: 14px; }

        .btn-submit { width: 100%; padding: 13px; background: linear-gradient(135deg, var(--brand-primary), #1d4ed8); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; font-size: 14px; cursor: pointer; transition: var(--transition); }
        .response-banner { margin-top: 16px; padding: 12px; border-radius: var(--radius-sm); font-size: 13px; font-weight: 600; display: none; }

        .modal-backdrop { position: fixed; inset: 0; background: rgba(9, 13, 22, 0.8); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 200; }
        .modal-card { width: 100%; max-width: 480px; background: var(--bg-surface); border: 1px solid var(--border-dark); border-radius: var(--radius-lg); padding: 32px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.7); }
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
            <a href="dashboard.jsp" id="link-overview" class="nav-link" style="display: none;">Overview</a>
            <a href="shipments.jsp" id="link-shipments" class="nav-link active" style="display: none;">Shipments</a>
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
            <div>
                <h1>Logistics Operations & Container Bookings</h1>
                <p>Real-time international freight tracking, carrier bookings, and dispatch management.</p>
            </div>
            <button class="btn-action-primary" onclick="openCreateModal()">+ Create New Cargo Shipment</button>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-title">Total Shipments</div>
                <div class="stat-value" id="count-total">0</div>
                <div class="stat-sub">Active Cargoes</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">Pending</div>
                <div class="stat-value" id="count-pending" style="color: var(--warning);">0</div>
                <div class="stat-sub">Awaiting Booking</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">In Transit</div>
                <div class="stat-value" id="count-transit" style="color: var(--brand-accent);">0</div>
                <div class="stat-sub">On High Seas</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">Delivered</div>
                <div class="stat-value" id="count-delivered" style="color: var(--success);">0</div>
                <div class="stat-sub">Port Cleared</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">Delayed</div>
                <div class="stat-value" style="color: var(--danger);">0</div>
                <div class="stat-sub">Zero Weather Holds</div>
            </div>
        </div>

        <div class="grid-2">
            <!-- Active Shipments Manifest Table -->
            <div class="panel">
                <h3>Live Cargo Manifests</h3>
                <p class="panel-desc">Real-time international freight tracking and container dispatch status.</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tracking #</th>
                            <th>Origin Port</th>
                            <th>Destination Port</th>
                            <th>Cost (LKR)</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody id="shipments-table-body">
                        <tr><td colspan="6" style="color: var(--text-muted);">Loading cargo manifests...</td></tr>
                    </tbody>
                </table>
            </div>

            <!-- Carrier Container Booking Form -->
            <div class="panel">
                <h3>🚢 Carrier Container Booking</h3>
                <p class="panel-desc">Reserve container allocations with ocean freight carriers.</p>

                <form id="bmt-booking-form">
                    <div class="form-group">
                        <label for="shipment-id">Shipment ID</label>
                        <input type="number" id="shipment-id" value="1" required>
                    </div>

                    <div class="form-group">
                        <label for="carrier-code">Carrier Code (e.g. MAERSK, MSC)</label>
                        <input type="text" id="carrier-code" value="MAERSK" required>
                    </div>

                    <div class="form-group">
                        <label for="cost-usd">Cost (LKR) [Allocation Limit: LKR 15,000,000]</label>
                        <input type="number" id="cost-usd" value="2500000" required>
                    </div>

                    <button type="submit" class="btn-submit">Reserve Container</button>
                </form>

                <div id="bmt-response" class="response-banner"></div>
            </div>
        </div>
    </div>

    <!-- Modal Form: Create New Shipment -->
    <div id="create-modal" class="modal-backdrop">
        <div class="modal-card">
            <h3 style="margin-bottom: 6px;">Create New Cargo Shipment</h3>
            <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">Add a new cargo shipment to the global dispatch manifest.</p>
            
            <form id="create-shipment-form">
                <div class="form-group">
                    <label for="new-tracking">Tracking Number</label>
                    <input type="text" id="new-tracking" placeholder="e.g. SCM-TRK-90815" required>
                </div>
                <div class="form-group">
                    <label for="new-origin">Origin Port</label>
                    <input type="text" id="new-origin" placeholder="e.g. Port of Colombo" required>
                </div>
                <div class="form-group">
                    <label for="new-destination">Destination Port</label>
                    <input type="text" id="new-destination" placeholder="e.g. Port of Singapore" required>
                </div>
                <div class="form-group">
                    <label for="new-weight">Weight (kg)</label>
                    <input type="number" id="new-weight" value="15000" required>
                </div>
                <div class="form-group">
                    <label for="new-cost">Est. Cost (LKR)</label>
                    <input type="number" id="new-cost" value="2500000" required>
                </div>

                <div style="display: flex; gap: 12px; margin-top: 24px;">
                    <button type="button" class="btn-submit" style="background: transparent; border: 1px solid var(--border-dark);" onclick="closeCreateModal()">Cancel</button>
                    <button type="submit" class="btn-submit">Dispatch Shipment</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        const token = localStorage.getItem('scm_jwt');
        if (!token) {
            alert('Session expired or missing authentication token. Please sign in to continue.');
            window.location.href = 'index.html';
        }

        const rawRoles = localStorage.getItem('scm_roles');
        const roles = rawRoles ? JSON.parse(rawRoles) : [];
        const roleBadge = document.getElementById('role-badge');
        
        if (roles.length > 0) roleBadge.innerText = roles[0];

        if (!roles.includes('ADMIN') && !roles.includes('COORDINATOR')) {
            alert('Access Denied: Your security role does not have access to Logistics Operations.');
            window.location.href = 'index.html';
        }

        if (roles.includes('ADMIN')) {
            document.getElementById('link-overview').style.display = 'inline-block';
            document.getElementById('link-shipments').style.display = 'inline-block';
            document.getElementById('link-inventory').style.display = 'inline-block';
            document.getElementById('link-customs').style.display = 'inline-block';
            document.getElementById('link-vendor').style.display = 'inline-block';
        } else if (roles.includes('COORDINATOR')) {
            document.getElementById('link-shipments').style.display = 'inline-block';
        }

        async function loadShipments() {
            const token = localStorage.getItem('scm_jwt');
            const tbody = document.getElementById('shipments-table-body');

            try {
                const res = await fetch('api/shipments', {
                    headers: { 'Authorization': `Bearer ${token}` }
                });

                if (res.ok) {
                    const data = await res.json();
                    tbody.innerHTML = '';
                    
                    let pending = 0, transit = 0, delivered = 0;

                    if (data.length === 0) {
                        tbody.innerHTML = '<tr><td colspan="6" style="color: var(--text-secondary);">No cargo shipments found. Click "+ Create New Cargo Shipment" to add one!</td></tr>';
                    } else {
                        data.forEach(item => {
                            if (item.status === 'PENDING') pending++;
                            else if (item.status.includes('IN_TRANSIT') || item.status.includes('BOOKED')) transit++;
                            else if (item.status === 'DELIVERED') delivered++;

                            const pillClass = item.status === 'DELIVERED' ? 'pill-green' : 'pill-blue';
                            const costFormatted = item.costUSD ? item.costUSD.toLocaleString() : '0.00';

                            tbody.innerHTML += '<tr>' +
                                '<td>#' + item.id + '</td>' +
                                '<td><strong>' + item.trackingNumber + '</strong></td>' +
                                '<td>' + item.origin + '</td>' +
                                '<td>' + item.destination + '</td>' +
                                '<td>Rs. ' + costFormatted + '</td>' +
                                '<td><span class="pill ' + pillClass + '">' + item.status + '</span></td>' +
                            '</tr>';
                        });
                    }

                    document.getElementById('count-total').innerText = data.length;
                    document.getElementById('count-pending').innerText = pending;
                    document.getElementById('count-transit').innerText = transit;
                    document.getElementById('count-delivered').innerText = delivered;

                } else {
                    tbody.innerHTML = '<tr><td colspan="6" style="color: var(--danger);">Failed to load shipments. Session expired.</td></tr>';
                }
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="6" style="color: var(--danger);">Network error contacting server.</td></tr>';
            }
        }

        document.getElementById('create-shipment-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const token = localStorage.getItem('scm_jwt');
            const trackingNumber = document.getElementById('new-tracking').value;
            const origin = document.getElementById('new-origin').value;
            const destination = document.getElementById('new-destination').value;
            const weightKg = document.getElementById('new-weight').value;
            const costUSD = document.getElementById('new-cost').value;

            try {
                const res = await fetch('api/shipments', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        trackingNumber,
                        origin,
                        destination,
                        weightKg: parseFloat(weightKg),
                        costUSD: parseFloat(costUSD),
                        status: 'PENDING'
                    })
                });

                if (res.ok) {
                    closeCreateModal();
                    loadShipments();
                } else {
                    alert('Failed to dispatch shipment: ' + await res.text());
                }
            } catch (err) {
                alert('Network error dispatching shipment.');
            }
        });

        document.getElementById('bmt-booking-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const shipmentId = document.getElementById('shipment-id').value;
            const carrierCode = document.getElementById('carrier-code').value;
            const costUSD = document.getElementById('cost-usd').value;
            const token = localStorage.getItem('scm_jwt');
            const resBanner = document.getElementById('bmt-response');

            resBanner.style.display = 'none';

            try {
                const response = await fetch(`api/carrier-booking?shipmentId=${shipmentId}&carrierCode=${carrierCode}&costUSD=${costUSD}`, {
                    method: 'POST',
                    headers: { 'Authorization': `Bearer ${token}` }
                });

                const text = await response.text();
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

        function openCreateModal() { document.getElementById('create-modal').style.display = 'flex'; }
        function closeCreateModal() { document.getElementById('create-modal').style.display = 'none'; }

        window.addEventListener('load', loadShipments);
    </script>
</body>
</html>