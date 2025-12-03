
// Bank A Login
function loginBankA(event) {
    event.preventDefault();

    const email = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    login(email, password, 'Bank A', 'bankA');
    return false;
}

// Bank B Login
function loginBankB(event) {
    event.preventDefault();

    const email = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    login(email, password, 'Bank B', 'bankB');
    return false;
}

// Bank C Login
function loginBankC(event) {
    event.preventDefault();

    const email = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    login(email, password, 'Bank C', 'bankC');
    return false;
}

// Genel login fonksiyonu
function login(email, password, bankName, bankKey) {
    const errorMessage = document.getElementById('error-message');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');

    // Backend'e POST istegi gonder
    const API = window.API_URL || window.location.origin;

    fetch(`${API}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}&bankName=${encodeURIComponent(bankName)}`
    })

    .then(response => {
        if (!response.ok) {
            throw new Error('HTTP error ' + response.status);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            sessionStorage.setItem(bankKey + '_logged_in', 'true');
            if (data.user) {
                sessionStorage.setItem(bankKey + '_user', JSON.stringify(data.user));
                sessionStorage.setItem(bankKey + '_email', data.user.email || email);
            } else {
                sessionStorage.setItem(bankKey + '_email', email);
                sessionStorage.removeItem(bankKey + '_user');
            }
            window.location.href = bankKey + '.html';
        } else {
            errorMessage.textContent = data.message || 'Giris basarisiz.';
            errorMessage.style.display = 'block';
            usernameInput.value = '';
            passwordInput.value = '';
            usernameInput.placeholder = 'Yanlis girdiniz';
        }
    })
    .catch(error => {
        errorMessage.textContent = 'Baglanti hatasi: ' + error.message;
        errorMessage.style.display = 'block';
        usernameInput.value = '';
        passwordInput.value = '';
        usernameInput.placeholder = 'Yanlis girdiniz';
    });
}

// Logout fonksiyonlari
function logoutBankA() {
    sessionStorage.removeItem('bankA_logged_in');
    sessionStorage.removeItem('bankA_email');
    sessionStorage.removeItem('bankA_user');
    window.location.href = 'bankA.html';
}

function logoutBankB() {
    sessionStorage.removeItem('bankB_logged_in');
    sessionStorage.removeItem('bankB_email');
    sessionStorage.removeItem('bankB_user');
    window.location.href = 'bankB.html';
}

function logoutBankC() {
    sessionStorage.removeItem('bankC_logged_in');
    sessionStorage.removeItem('bankC_email');
    sessionStorage.removeItem('bankC_user');
    window.location.href = 'bankC.html';
}
