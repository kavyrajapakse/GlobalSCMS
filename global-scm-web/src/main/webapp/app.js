let currentMode = 'login';

function switchAuthMode(mode) {
    currentMode = mode;
    const tabLogin = document.getElementById('tab-login');
    const tabRegister = document.getElementById('tab-register');
    const demoSection = document.getElementById('demo-section');
    const roleGroup = document.getElementById('role-group');
    const formTitle = document.getElementById('form-title');
    const formDesc = document.getElementById('form-desc');
    const submitLabel = document.getElementById('submit-label');
    const errorBox = document.getElementById('auth-error');

    errorBox.style.display = 'none';

    if (mode === 'login') {
        tabLogin.classList.add('active');
        tabRegister.classList.remove('active');
        demoSection.style.display = 'block';
        roleGroup.style.display = 'none';
        formTitle.innerText = 'Welcome back';
        formDesc.innerText = 'Authenticate with SCMRealm to access protected supply chain modules.';
        submitLabel.innerText = 'Authenticate (JAAS Login)';
    } else {
        tabRegister.classList.add('active');
        tabLogin.classList.remove('active');
        demoSection.style.display = 'none';
        roleGroup.style.display = 'block';
        formTitle.innerText = 'Create Account';
        formDesc.innerText = 'Register a new user directly into MySQL via JPA User Entity.';
        submitLabel.innerText = 'Create Account (JPA Register)';
    }
}

function fillCredentials(username, password) {
    document.getElementById('username').value = username;
    document.getElementById('password').value = password;
    document.getElementById('auth-error').style.display = 'none';
}

document.getElementById('auth-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role-select').value;
    const errorBox = document.getElementById('auth-error');
    const submitBtn = document.getElementById('submit-btn');

    errorBox.style.display = 'none';
    submitBtn.disabled = true;

    const endpoint = currentMode === 'login' ? 'api/auth/login' : 'api/auth/register';
    const payload = currentMode === 'login' 
                    ? { username, password } 
                    : { username, password, roles: [role] };

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const data = await response.json();
            if (currentMode === 'login') {
                localStorage.setItem('scm_jwt', data.token);
                localStorage.setItem('scm_username', data.username);
                window.location.href = 'dashboard.jsp';
            } else {
                // Registration successful: switch to Login tab!
                switchAuthMode('login');
                document.getElementById('username').value = username;
                document.getElementById('password').value = '';
                errorBox.style.background = 'rgba(16, 185, 129, 0.15)';
                errorBox.style.borderColor = 'rgba(16, 185, 129, 0.3)';
                errorBox.style.color = '#6ee7b7';
                errorBox.innerText = 'Account registered successfully! Please sign in with your password.';
                errorBox.style.display = 'block';
            }
        } else {
            const errText = await response.text();
            errorBox.style.background = 'var(--danger-bg)';
            errorBox.style.borderColor = 'rgba(239, 68, 68, 0.3)';
            errorBox.style.color = '#fca5a5';
            errorBox.innerText = errText || 'Authentication failed.';
            errorBox.style.display = 'block';
        }
    } catch (err) {
        errorBox.innerText = 'Network error contacting server endpoint.';
        errorBox.style.display = 'block';
    } finally {
        submitBtn.disabled = false;
    }
});