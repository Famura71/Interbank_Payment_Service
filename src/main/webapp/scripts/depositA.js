// Sayfa yüklendiğinde kullanıcı bilgilerini göster
window.onload = function() {
    const isLoggedIn = sessionStorage.getItem('bankA_logged_in') === 'true';

    if (!isLoggedIn) {
        alert('Lütfen önce giriş yapın!');
        window.location.href = 'bankA.html';
        return;
    }

    const userRaw = sessionStorage.getItem('bankA_user');
    if (userRaw) {
        try {
            const user = JSON.parse(userRaw);
            document.getElementById('user-name').textContent = 'İsim: ' + (user.name || '');
            document.getElementById('user-email').textContent = 'Email: ' + (user.email || '');
            document.getElementById('user-balance').textContent = 'Mevcut Bakiye: ' +
                (typeof user.balance === 'number' ? user.balance.toFixed(2) : '0.00') + ' TL';
        } catch (e) {
            console.error('Kullanıcı bilgisi okunamadı', e);
        }
    }
};

function showMessage(text, type) {
    const messageDiv = document.getElementById('message');
    if (!messageDiv) return;
    messageDiv.textContent = text;
    messageDiv.className = 'message ' + type;
    messageDiv.style.display = 'block';
}

function handleDeposit(event) {
    event.preventDefault();

    const amount = parseFloat(document.getElementById('amount').value);
    const description = document.getElementById('description').value;

    if (!amount || amount <= 0) {
        showMessage('Lütfen geçerli bir miktar girin!', 'error');
        return false;
    }

    const user = JSON.parse(sessionStorage.getItem('bankA_user'));

    // Backend'e application/x-www-form-urlencoded isteği gönder
    const params = new URLSearchParams();
    params.append('type', 'deposit');
    params.append('userEmail', user.email);
    params.append('amount', String(amount));
    params.append('description', description);
    params.append('bankName', 'Bank A');

    fetch('/transaction', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        body: params.toString()
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Bakiyeyi güncelle
            user.balance = parseFloat(user.balance) + amount;
            sessionStorage.setItem('bankA_user', JSON.stringify(user));

            showMessage(`✓ ${amount.toFixed(2)} TL başarıyla yatırıldı!`, 'success');

            setTimeout(() => {
                window.location.href = 'bankA.html';
            }, 2000);
        } else {
            showMessage('❌ İşlem başarısız: ' + data.message, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('❌ Bağlantı hatası!', 'error');
    });

    return false;
}
