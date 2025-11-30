

// Bank A Login
function loginBankA(event) {
    event.preventDefault(); // Formun normal submit olmasını engelle
    
    const email = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    console.log('loginBankA çağrıldı:', email); // Debug için
    
    login(email, password, 'Bank A', 'bankA');
    return false; // Ek güvenlik
}

// Bank B Login
function loginBankB(event) {
    event.preventDefault();
    
    const email = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    console.log('loginBankB çağrıldı:', email);
    
    login(email, password, 'Bank B', 'bankB');
    return false;
}

// Bank C Login
function loginBankC(event) {
    event.preventDefault();
    
    const email = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    console.log('loginBankC çağrıldı:', email);
    
    login(email, password, 'Bank C', 'bankC');
    return false;
}

// Genel login fonksiyonu
function login(email, password, bankName, bankKey) {
    const errorMessage = document.getElementById('error-message');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    
    console.log('Login fonksiyonu çağrıldı:', email, bankName); // Debug
    
    // Backend'e POST isteği gönder
    fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}&bankName=${encodeURIComponent(bankName)}`
    })
    .then(response => {
        console.log('Response alındı, status:', response.status);
        if (!response.ok) {
            throw new Error('HTTP error ' + response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log('Response data:', data);
        
        if (data.success) {
            // Giriş başarılı
            sessionStorage.setItem(bankKey + '_logged_in', 'true');
            sessionStorage.setItem(bankKey + '_email', email);
            window.location.href = 'denemexd.html';
        } else {
            // Giriş başarısız
            errorMessage.textContent = data.message;
            errorMessage.style.display = 'block';
            usernameInput.value = '';
            passwordInput.value = '';
            usernameInput.placeholder = 'Yanlış girdiniz';
        }
    })
    .catch(error => {
        console.error('Fetch hatası:', error);
        errorMessage.textContent = 'Bağlantı hatası: ' + error.message;
        errorMessage.style.display = 'block';
        usernameInput.value = '';
        passwordInput.value = '';
        usernameInput.placeholder = 'Yanlış girdiniz';
    });
}

// Logout fonksiyonları
function logoutBankA() {
    sessionStorage.removeItem('bankA_logged_in');
    sessionStorage.removeItem('bankA_email');
    window.location.href = 'bankA.html';
}

function logoutBankB() {
    sessionStorage.removeItem('bankB_logged_in');
    sessionStorage.removeItem('bankB_email');
    window.location.href = 'bankB.html';
}

function logoutBankC() {
    sessionStorage.removeItem('bankC_logged_in');
    sessionStorage.removeItem('bankC_email');
    window.location.href = 'bankC.html';
}