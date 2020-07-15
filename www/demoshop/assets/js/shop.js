var p1 = 0;
var p2 = 0;
var p3 = 0;
var cart = 0;
var statusPolling;
var paymentId = '';

function increaseP1() {
    p1 = p1 +1
    console.debug(p1)
    updateCart()
}

function increaseP2() {
    p2 = p2 +1
    console.debug(p2)
    updateCart()
}

function increaseP3() {
    p3 = p3 +1
    console.debug(p3)
    updateCart()
}

function updateCart() {
    cart = p1 * 10 + p2 * 20 + p3 * 50;
    document.getElementById('p1c').value = p1
    document.getElementById('p2c').value = p2
    document.getElementById('p3c').value = p3

    document.getElementById('p1count').innerHTML = "Product 1 x " + p1
    document.getElementById('p2count').innerHTML = "Product 2 x " + p2
    document.getElementById('p3count').innerHTML = "Product 3 x " + p3

    document.getElementById('total').innerHTML = "Total: " + cart
    console.debug(cart)
}

function initiatePayment() {
    var xhttp = new XMLHttpRequest();
    var params = {
        reference_number: Math.floor(Math.random() * 1000) + 1,
        amount: cart
    }
    xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        var qrImage = document.getElementById('QR')
        var response = JSON.parse(this.response)
        paymentId = response.payment_id
        qrImage.src = 'data:image/png;charset=utf-8;base64, ' + response.qr_code
        qrImage.classList.remove('d-none')
        document.getElementById('instructions').classList.remove('d-none')
        document.getElementById('cancelPayment').classList.remove('d-none')
        document.getElementById('initPayment').classList.add('d-none')
        statusPolling = window.setInterval(pollStatus, 1000)
      }
    };
    xhttp.open("POST", "http://localhost/new", true);
    xhttp.send(JSON.stringify(params));
}

function cancelPayment() {
    var xhttp = new XMLHttpRequest();
    var params = {
        payment_id: paymentId
    }
    xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        document.getElementById('QR').classList.add('d-none')
        document.getElementById('instructions').classList.add('d-none')
        document.getElementById('cancelPayment').classList.add('d-none')
        document.getElementById('initPayment').classList.remove('d-none')
        window.clearInterval(statusPolling)
      }
    };
    xhttp.open("POST", "http://localhost/cancel", true);
    xhttp.send(JSON.stringify(params));
}

function pollStatus() {
    var xhttp = new XMLHttpRequest();
    var params = {
        payment_id: paymentId
    }
    xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        var response = JSON.parse(this.response)
        if (response.code == 'approved') {
            document.getElementById('QR').classList.add('d-none')
            document.getElementById('instructions').innerHTML = "Payment successful"
            document.getElementById('cancelPayment').classList.add('d-none')
            document.getElementById('initPayment').classList.add('d-none')
            window.clearInterval(statusPolling)
        }

        if (response.code == 'canceled') {
            document.getElementById('QR').classList.add('d-none')
            document.getElementById('instructions').innerHTML = "Payment canceled"
            document.getElementById('cancelPayment').classList.add('d-none')
            document.getElementById('initPayment').classList.add('d-none')
            window.clearInterval(statusPolling)
        }
      }
    };
    xhttp.open("POST", "http://localhost/status", true);
    xhttp.send(JSON.stringify(params));
}