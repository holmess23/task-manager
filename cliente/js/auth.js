const AUTH_URL = 'http://localhost:8080/api/auth';


document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const tab = btn.dataset.tab;

        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));

        btn.classList.add('active');
        document.getElementById(`${tab}-tab`).classList.add('active');  

        document.querySelectorAll('.auth-error').forEach(e => e.textContent = '');
    });
});

document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const name = document.getElementById('register-name').value.trim();
    const email = document.getElementById('register-email').value.trim();
    const password = document.getElementById('register-password').value;
    const errorEl = document.getElementById('register-error');
    const submitBtn = e.target.querySelector('button[type="submit"]');

    errorEl.textContent = '';
    submitBtn.disabled = true;
    submitBtn.textContent = 'Creando cuenta...';

    try {
        const response = await fetch(`${AUTH_URL}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password })
        });

        if(!response.ok){
            const error = await response.json();
            throw new Error(error.message || 'Error al crear la cuenta');
        }

        const data = await response.json();
        saveSession(data);
        window.location.href = 'main.html';

    } catch (error) {
        errorEl.textContent = error.message;
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Crear cuenta';
    }
});

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;
    const errorEl = document.getElementById('login-error');
    const submitBtn = e.target.querySelector('button[type="submit"]');

    errorEl.textContent = '';
    submitBtn.disabled = true;
    submitBtn.textContent = 'Iniciando sesión...';

    try {
        const response = await fetch(`${AUTH_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if(!response.ok){
            const error = await response.json();
            throw new Error(error.message || 'Error al iniciar sesión');
        }

        const data = await response.json();
        saveSession(data);
        window.location.href = 'main.html';

    } catch (error) {
        errorEl.textContent = error.message;
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Iniciar sesión';
    }
});

function saveSession(data){
    localStorage.setItem('token', data.token);
    localStorage.setItem('userName', data.name);
    localStorage.setItem('userEmail', data.email);
    localStorage.setItem('userRole', data.role);
    localStorage.setItem('loginTime', Date.now().toString());
}

if(localStorage.getItem('token')){
    window.location.href = 'main.html';
}