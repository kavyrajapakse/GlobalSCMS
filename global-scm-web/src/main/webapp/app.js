function fillCredentials(username, password) {
    document.getElementById('username').value = username;
    document.getElementById('password').value = password;
    document.getElementById('login-error').style.display = 'none';
}

document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorBox = document.getElementById('login-error');
    const submitBtn = document.getElementById('submit-btn');

    errorBox.style.display = 'none';
    submitBtn.disabled = true;

    try {
        const response = await fetch('api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('scm_jwt', data.token);
            localStorage.setItem('scm_username', data.username);
            localStorage.setItem('scm_roles', JSON.stringify(data.roles));

            // Role-Based Automatic Routing
            const roles = data.roles || [];
            if (roles.includes('ADMIN')) {
                window.location.href = 'dashboard.jsp';
            } else if (roles.includes('CUSTOMS_AGENT')) {
                window.location.href = 'customs.jsp';
            } else if (roles.includes('WAREHOUSE_MANAGER')) {
                window.location.href = 'inventory.jsp';
            } else if (roles.includes('COORDINATOR')) {
                window.location.href = 'shipments.jsp';
            } else if (roles.includes('VENDOR_REP')) {
                window.location.href = 'vendor.jsp';
            } else {
                window.location.href = 'dashboard.jsp';
            }
        } else {
            const errText = await response.text();
            errorBox.innerText = errText || 'Authentication failed. Invalid username or password.';
            errorBox.style.display = 'block';
        }
    } catch (err) {
        errorBox.innerText = 'Network error contacting server endpoint.';
        errorBox.style.display = 'block';
    } finally {
        submitBtn.disabled = false;
    }
});