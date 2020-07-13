var p1 = 0;
var p2 = 0;
var p3 = 0;
var cart = 0;

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
    document.getElementById('QR').classList.remove('d-none')
    document.getElementById('instructions').classList.remove('d-none')
    document.getElementById('cancelPayment').classList.remove('d-none')
    document.getElementById('initPayment').classList.add('d-none')
}

function cancelPayment() {
    document.getElementById('QR').classList.add('d-none')
    document.getElementById('instructions').classList.add('d-none')
    document.getElementById('cancelPayment').classList.add('d-none')
    document.getElementById('initPayment').classList.remove('d-none')
}