function openViewModal(productCard) {
    const productName = productCard.getAttribute('data-product-name');
    const productDescription = productCard.getAttribute('data-product-description');
    const productPrice = productCard.getAttribute('data-product-price');
    const productColour = productCard.getAttribute('data-product-colour');
    const productGender = productCard.getAttribute('data-product-gender');
    const productCategory = productCard.getAttribute('data-product-category');
    const sizeQuantities = productCard.getAttribute('data-product-size-quantities').split(';');

    // Populate the modal with product details
    document.getElementById('viewProductName').textContent = productName;
    document.getElementById('viewProductDescription').textContent = productDescription;
    document.getElementById('viewProductPrice').textContent = productPrice;
    document.getElementById('viewProductColour').textContent = productColour;
    document.getElementById('viewProductGender').textContent = productGender;
    document.getElementById('viewProductCategory').textContent = productCategory;

    // Clear previous size & quantities
    const sizeQuantitiesContainer = document.getElementById('viewProductSizeQuantities');
    sizeQuantitiesContainer.innerHTML = ''; // Clear existing content

    sizeQuantities.forEach(sizeQuantity => {
        const [size, quantity] = sizeQuantity.split(':');
        if (size && quantity) {
            const listItem = document.createElement('li');
            listItem.textContent = `Size: ${size}, Quantity: ${quantity}`;
            sizeQuantitiesContainer.appendChild(listItem);
        }
    });

    // Show the modal
    document.getElementById('viewModal').style.display = 'block';
}

function closeViewModal() {
    document.getElementById('viewModal').style.display = 'none';
}



function openCreateModal() {
    document.getElementById('createModal').style.display = 'block';
}

function closeCreateModal() {
    document.getElementById('createModal').style.display = 'none';
}

function openUpdateModal(productCard) {
    document.getElementById('updateProductId').value = productCard.dataset.productId;
    document.getElementById('updateProductName').value = productCard.dataset.productName;
    document.getElementById('updateProductDescription').value = productCard.dataset.productDescription;
    document.getElementById('updateProductPrice').value = productCard.dataset.productPrice;
    document.getElementById('updateProductColour').value = productCard.dataset.productColour;
    document.getElementById('updateProductGender').value = productCard.dataset.productGender;
    document.getElementById('updateProductCategory').value = productCard.dataset.productCategory;

    document.getElementById('updateModal').style.display = 'block';
}

function closeUpdateModal() {
    document.getElementById('updateModal').style.display = 'none';
}

function addSizeQuantityField() {
    const container = document.getElementById('sizeQuantityContainer');
    const sizeQuantityPair = document.createElement('div');
    sizeQuantityPair.className = 'size-quantity-pair';
    const index = container.querySelectorAll('.size-quantity-pair').length;

    sizeQuantityPair.innerHTML = `
        <input type="text" name="sizeQuantities[${index}].size" placeholder="Size (e.g. M)" required>
        <input type="number" name="sizeQuantities[${index}].quantity" placeholder="Quantity" min="0" required>
    `;

    container.appendChild(sizeQuantityPair);
}
