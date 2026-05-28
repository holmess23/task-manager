const TOKEN_KEY = 'token';
const USER_NAME_KEY = 'userName';
const USER_EMAIL_KEY = 'userEmail';
const USER_ROLE_KEY = 'userRole';
const LOGIN_TIME_KEY = 'loginTime';
const JWT_EXPIRATION_MS = 24 * 60 * 60 * 1000; 
//const JWT_EXPIRATION_MS = 2 * 60 * 1000; 

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function getUserName() {
    return localStorage.getItem(USER_NAME_KEY);
}

function getUserEmail() {
    return localStorage.getItem(USER_EMAIL_KEY);
}

function getUserRole() {
    return localStorage.getItem(USER_ROLE_KEY) || 'USER';
}

function isAdmin(){
    return getUserRole() === 'ADMIN';
}

function isSessionExpired(){
    const loginTime = Number(localStorage.getItem(LOGIN_TIME_KEY));
    if (!loginTime) return true;
    return (Date.now() - loginTime) > JWT_EXPIRATION_MS;
}

function clearSession(){
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_NAME_KEY);
    localStorage.removeItem(USER_EMAIL_KEY);
    localStorage.removeItem(USER_ROLE_KEY);
    localStorage.removeItem(LOGIN_TIME_KEY);
}

function logout(){
    clearSession();
    window.location.href = 'auth.html';
}

function authHeaders(){
    if(isSessionExpired()){
        clearSession();
        window.location.href = 'auth.html';
        return {};
    }
    return{
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${getToken()}`
    }
}

async function fetchWithAuth(url, options = {}) {
    
    console.log('fetchWithAuth: ', url, options);
    if (isSessionExpired()) {
        clearSession();
        window.location.href = 'auth.html';
        return null;
    }
    const response = await fetch(url, {
        ...options,
        headers: {
            ...authHeaders(),
            ...(options.headers || {})
        }
    });

    if (response.status === 401 || response.status === 403) {
        alert('Tu sesión ha expirado. Por favor, inicia sesión nuevamente.');
        logout();
        return null;
    }

    return response;  
}