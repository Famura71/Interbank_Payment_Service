// Bank A için kullanıcı bilgileri
const bankAUsers = {
    'admin': 'admin123',
    'user1': 'pass1'
};

// Bank B için kullanıcı bilgileri
const bankBUsers = {
    'admin': 'admin123',
    'user2': 'pass2'
};

// Bank C için kullanıcı bilgileri
const bankCUsers = {
    'admin': 'admin123',
    'user3': 'pass3'
};

function loginBankA(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('error-message');

    if (bankAUsers[username] && bankAUsers[username] === password) {
        sessionStorage.setItem('bankA_logged_in', 'true');
        sessionStorage.setItem('bankA_username', username);
        document.getElementById('login-section').style.display = 'none';
        document.getElementById('content-section').style.display = 'block';
        return false;
    } else {
        errorMessage.textContent = 'Kullanıcı adı veya şifre hatalı!';
        errorMessage.style.display = 'block';
        return false;
    }
}

function loginBankB(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('error-message');
    
    if (bankBUsers[username] && bankBUsers[username] === password) {
        sessionStorage.setItem('bankB_logged_in', 'true');
        sessionStorage.setItem('bankB_username', username);
        document.getElementById('login-section').style.display = 'none';
        document.getElementById('content-section').style.display = 'block';
        return false;
    } else {
        errorMessage.textContent = 'Kullanıcı adı veya şifre hatalı!';
        errorMessage.style.display = 'block';
        return false;
    }
}

function loginBankC(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('error-message');
    
    if (bankCUsers[username] && bankCUsers[username] === password) {
        sessionStorage.setItem('bankC_logged_in', 'true');
        sessionStorage.setItem('bankC_username', username);
        document.getElementById('login-section').style.display = 'none';
        document.getElementById('content-section').style.display = 'block';
        return false;
    } else {
        errorMessage.textContent = 'Kullanıcı adı veya şifre hatalı!';
        errorMessage.style.display = 'block';
        return false;
    }
}

function logoutBankA() {
    sessionStorage.removeItem('bankA_logged_in');
    sessionStorage.removeItem('bankA_username');
    document.getElementById('content-section').style.display = 'none';
    document.getElementById('login-section').style.display = 'block';
}

function logoutBankB() {
    sessionStorage.removeItem('bankB_logged_in');
    sessionStorage.removeItem('bankB_username');
    document.getElementById('content-section').style.display = 'none';
    document.getElementById('login-section').style.display = 'block';
}

function logoutBankC() {
    sessionStorage.removeItem('bankC_logged_in');
    sessionStorage.removeItem('bankC_username');
    document.getElementById('content-section').style.display = 'none';
    document.getElementById('login-section').style.display = 'block';
}
    if (!sessionStorage.getItem('bankA_logged_in')) {
        window.location.href = 'bankA-login.html';
    }
}

function checkBankBLogin() {
    if (!sessionStorage.getItem('bankB_logged_in')) {
        window.location.href = 'bankB-login.html';
    }
}

function checkBankCLogin() {
    if (!sessionStorage.getItem('bankC_logged_in')) {
        window.location.href = 'bankC-login.html';
    }
}

function logoutBankA() {
    sessionStorage.removeItem('bankA_logged_in');
    sessionStorage.removeItem('bankA_username');
    window.location.href = 'bankA-login.html';
}

function logoutBankB() {
    sessionStorage.removeItem('bankB_logged_in');
    sessionStorage.removeItem('bankB_username');
    window.location.href = 'bankB-login.html';
}

function logoutBankC() {
    sessionStorage.removeItem('bankC_logged_in');
    sessionStorage.removeItem('bankC_username');
    window.location.href = 'bankC-login.html';
}