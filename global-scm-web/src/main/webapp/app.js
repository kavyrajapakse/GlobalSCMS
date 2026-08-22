function fillCredentials(username, password) {
    document.getElementById('username').value = username;
    document.getElementById('password').value = password;
    document.getElementById('login-error').style.display = 'none';
}

let pendingAuthData = null;
let currentEnteredPassword = '';

function navigateToUserWorkspace(roles) {
    roles = roles || [];
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
}

document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const errorBox = document.getElementById('login-error');
    const submitBtn = document.getElementById('submit-btn');

    errorBox.style.display = 'none';
    submitBtn.disabled = true;
    currentEnteredPassword = password;

    try {
        const response = await fetch('api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            console.log('Login Auth Response:', data);

            localStorage.setItem('scm_jwt', data.token);
            localStorage.setItem('scm_username', data.username);
            localStorage.setItem('scm_roles', JSON.stringify(data.roles));

            // Check if one-time first login password change is required
            const needsChange = (
                data.requiresPasswordChange === true ||
                data.requiresPasswordChange === 1 ||
                data.requiresPasswordChange === 'true' ||
                data.isRequiresPasswordChange === true
            );

            if (needsChange) {
                pendingAuthData = data;
                document.getElementById('first-login-user').value = data.username;
                document.getElementById('first-login-new-pass').value = '';
                document.getElementById('first-login-confirm-pass').value = '';
                document.getElementById('first-login-error').style.display = 'none';
                document.getElementById('first-login-modal').style.display = 'flex';
                document.getElementById('first-login-new-pass').focus();
                return;
            }

            // Normal immediate navigation for verified accounts
            navigateToUserWorkspace(data.roles);
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

// Handler for mandatory first-login password change
document.getElementById('first-login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const newPassword = document.getElementById('first-login-new-pass').value;
    const confirmPassword = document.getElementById('first-login-confirm-pass').value;
    const errorBox = document.getElementById('first-login-error');
    const submitBtn = document.getElementById('first-login-submit-btn');

    errorBox.style.display = 'none';

    if (newPassword.length < 4) {
        errorBox.innerText = 'Password must be at least 4 characters long.';
        errorBox.style.display = 'block';
        return;
    }

    if (newPassword !== confirmPassword) {
        errorBox.innerText = 'Passwords do not match. Please verify your new password.';
        errorBox.style.display = 'block';
        return;
    }

    if (newPassword === currentEnteredPassword) {
        errorBox.innerText = 'New password cannot be the same as the temporary password.';
        errorBox.style.display = 'block';
        return;
    }

    submitBtn.disabled = true;

    try {
        const usernameVal = pendingAuthData ? pendingAuthData.username : document.getElementById('first-login-user').value;
        const res = await fetch('api/users/change-password', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + (pendingAuthData ? pendingAuthData.token : '')
            },
            body: JSON.stringify({
                username: usernameVal,
                currentPassword: currentEnteredPassword,
                newPassword: newPassword
            })
        });

        if (res.ok) {
            alert('🔑 Permanent password configured successfully! Entering your workspace...');
            document.getElementById('first-login-modal').style.display = 'none';
            navigateToUserWorkspace(pendingAuthData ? pendingAuthData.roles : []);
        } else {
            const errData = await res.json();
            errorBox.innerText = errData.message || 'Failed to update permanent password.';
            errorBox.style.display = 'block';
        }
    } catch (err) {
        errorBox.innerText = 'Network error updating password.';
        errorBox.style.display = 'block';
    } finally {
        submitBtn.disabled = false;
    }
});