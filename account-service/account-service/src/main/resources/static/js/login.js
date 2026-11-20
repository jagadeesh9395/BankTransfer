const API_BASE = '/api/v1/auth';

function showRegister() {
    document.querySelector('.login-form').style.display = 'none';
    document.getElementById('registerForm').style.display = 'flex';
}

function hideRegister() {
    document.getElementById('registerForm').style.display = 'none';
    document.querySelector('.login-form').style.display = 'flex';
}

function showError(message) {
    let errorDiv = document.querySelector('.error-message');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        document.querySelector('.login-card').insertBefore(errorDiv, document.querySelector('.login-form'));
    }
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
    setTimeout(() => {
        errorDiv.style.display = 'none';
    }, 5000);
}

function showSuccess(message) {
    let successDiv = document.querySelector('.success-message');
    if (!successDiv) {
        successDiv = document.createElement('div');
        successDiv.className = 'success-message';
        document.querySelector('.login-card').insertBefore(successDiv, document.querySelector('.login-form'));
    }
    successDiv.textContent = message;
    successDiv.style.display = 'block';
    setTimeout(() => {
        successDiv.style.display = 'none';
    }, 5000);
}

async function login() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    if (!username || !password) {
        showError('Please enter both username and password');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            // Store user info in sessionStorage
            sessionStorage.setItem('user', JSON.stringify(data));
            // Redirect to chat page
            window.location.href = 'chat.html';
        } else {
            const error = await response.json();
            showError(error.error || 'Login failed');
        }
    } catch (error) {
        showError('Network error. Please try again.');
    }
}

async function register() {
    const username = document.getElementById('regUsername').value.trim();
    const password = document.getElementById('regPassword').value;
    const customerId = document.getElementById('regCustomerId').value.trim();
    const accountNumber = document.getElementById('regAccountNumber').value.trim();

    if (!username || !password || !customerId || !accountNumber) {
        showError('Please fill in all fields');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, customerId, accountNumber })
        });

        if (response.ok) {
            showSuccess('Registration successful! Please login.');
            hideRegister();
            document.getElementById('username').value = username;
        } else {
            const error = await response.json();
            showError(error.error || 'Registration failed');
        }
    } catch (error) {
        showError('Network error. Please try again.');
    }
}

// Allow Enter key to submit
document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        const registerForm = document.getElementById('registerForm');
        if (registerForm.style.display === 'flex') {
            register();
        } else {
            login();
        }
    }
});
