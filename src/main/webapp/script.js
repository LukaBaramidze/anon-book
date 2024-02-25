async function handlePurchase(productName) { //handles the modal window
    const url = `http://localhost:8989/candy-shop/store/product?name=${productName}`;
    const modal = document.getElementById('purchaseModal');
    const response = await fetch(url);

    if (!response.ok) {
        console.error(`HTTP error! Status: ${response.status}`);
        return;
    }

    const productInfo = await response.json();
    console.log(productInfo.name);
    productNameModal.textContent = `Product: ${productInfo.name}`;
    productPriceModal.textContent = `Price: $${productInfo.price}`;
    productsLeftModal.textContent = `Products Left: ${productInfo.amount}`;

    modal.style.display = 'flex';
}

async function completePurchase() { // sends purchase request to ProductServlet and responds
    const quantityInput = document.getElementById('quantityInput').value;
    const productName = document.getElementById('productNameModal').textContent.substring(9);
    console.log(productName);
    const jsonData = {
        name: productName,
        amount: quantityInput
    };

    const url = 'http://localhost:8989/candy-shop/store/product';
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData),
    });

    if (!response.ok) {
        console.error(`HTTP error! Status: ${response.status}`);
        return;
    }

    const contentType = response.headers.get('Content-Type');

    if (contentType && contentType.includes('application/json')) {
        const responseBody = await response.json();
        alert(`Purchase of ${productName} was successful! Remaining amount: ${responseBody.remainingAmount}`);
        closeModal();
    } else {
        const responseBody = await response.text();
        alert(`Purchase was not successful. Server response: ${responseBody}`);
    }
}

function closeModal() {
    const modal = document.getElementById('purchaseModal');
    modal.style.display = 'none';
}

async function fetchAndDisplayProducts() { // display's the products(still works if another product is added to the product list)
    const response = await fetch('http://localhost:8989/candy-shop/store');

    if (!response.ok) {
        console.error(`HTTP error! Status: ${response.status}`);
        return;
    }

    const data = await response.json();
    const productNames = data.productNames || [];
    const productList = document.getElementById('productList');
    productList.innerHTML = '';

    productNames.forEach(productName => {
        productList.innerHTML += `
            <li class="product-item">
                <span>${productName}</span>
                <button class="purchase-button" onclick="handlePurchase('${productName}')">Purchase</button>
            </li>
        `;
    });
}

async function addProducts() { // sends request to change product amount, and responds
    const name = document.getElementById('productNameInput').value;
    const amount = document.getElementById('amountInput').value;
    const password = document.getElementById('passwordInput').value;

    const jsonData = {
        password: password,
        name: name,
        amount: amount
    };

    const url = 'http://localhost:8989/candy-shop/store/product';
    const response = await fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData),
    });

    if (!response.ok) {
        console.error(`HTTP error! Status: ${response.status}`);
        return;
    }

    const contentType = response.headers.get('Content-Type');

    if (contentType && contentType.includes('application/json')) {
        const responseBody = await response.json();
        alert(`Change request of ${name} was successful! Remaining amount: ${responseBody.remainingAmount}`);
        closeModal();
    } else {
        const responseBody = await response.text();
        alert(`Change/Add request was not successful. Server response: ${responseBody}`);
    }
}

window.onload = fetchAndDisplayProducts; // loading the products