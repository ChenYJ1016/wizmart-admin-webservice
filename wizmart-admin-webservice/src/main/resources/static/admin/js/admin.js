function openViewModal(productCard) {
    const productName = productCard.getAttribute('data-product-name');
    const productDescription = productCard.getAttribute('data-product-description');
    const productPrice = productCard.getAttribute('data-product-price');
    const productColour = productCard.getAttribute('data-product-colour');
    const productImageUrl = productCard.dataset.productImageUrl;
    const productGender = productCard.getAttribute('data-product-gender');
    const productCategory = productCard.getAttribute('data-product-category');
	const sizeQuantities = productCard.getAttribute('data-product-size-quantities').split(';').map(sq => sq.trim());

    // Populate the modal with product details
    document.getElementById('viewProductName').textContent = productName;
    document.getElementById('viewProductDescription').textContent = productDescription;
    document.getElementById('viewProductPrice').textContent = productPrice;
    document.getElementById('viewProductColour').textContent = productColour;
    document.getElementById('viewProductGender').textContent = productGender;
    document.getElementById('viewProductCategory').textContent = productCategory;
    
    const sizeQuantitiesContainer = document.getElementById('viewProductSizeQuantities');
    sizeQuantitiesContainer.innerHTML = ''; 

    sizeQuantities.forEach(sizeQuantity => {
        const [size, quantity] = sizeQuantity.split(':');
        if (size && quantity) {
            const listItem = document.createElement('li');
            listItem.textContent = `Size: ${size}, Quantity: ${quantity}`;
            sizeQuantitiesContainer.appendChild(listItem);
        }
    });

	const productImage = document.getElementById('viewProductImage');
    productImage.src = productImageUrl;
    productImage.alt = productName;
    
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
    // Existing code to populate other fields...
    const productId = productCard.getAttribute('data-product-id');
    const productName = productCard.getAttribute('data-product-name');
    const productDescription = productCard.getAttribute('data-product-description');
    const productPrice = productCard.getAttribute('data-product-price');
    const productColour = productCard.getAttribute('data-product-colour');
    const productGender = productCard.getAttribute('data-product-gender');
    const productCategory = productCard.getAttribute('data-product-category');

    // Set the form action and other hidden values
    document.getElementById('updateForm').action = '/admin/update/' + productId;
    document.getElementById('updateProductId').value = productId;
    document.getElementById('updateProductName').value = productName;
    document.getElementById('updateProductDescription').value = productDescription;
    document.getElementById('updateProductPrice').value = productPrice;
    document.getElementById('updateProductColour').value = productColour;
    document.getElementById('updateProductGender').value = productGender;
    document.getElementById('updateProductCategory').value = productCategory;

    // Clear previous size/quantity fields
    const sizeQuantitiesString = productCard.dataset.productSizeQuantities.split(';').map(sq => sq.trim());
    const updateSizeQuantityContainer = document.getElementById("updateSizeQuantityContainer");
    updateSizeQuantityContainer.innerHTML = ''; 

    // Populate with existing size/quantity pairs
    sizeQuantitiesString.forEach((sq, index) => {
        const [size, quantity] = sq.split(':');
        if (size && quantity) {
            addUpdateSizeQuantityField('updateSizeQuantityContainer', size, quantity); 
        }
    });

    // Show the modal
    document.getElementById("updateModal").style.display = "block";
}

function closeUpdateModal() {
    document.getElementById('updateModal').style.display = 'none';
}

function addSizeQuantityField(containerId = 'sizeQuantityContainer') {
    const container = document.getElementById(containerId);

    // Get the number of existing pairs to assign the correct index for new inputs
    const pairCount = container.getElementsByClassName('size-quantity-pair').length;

    // Create a new div for the size and quantity inputs
    const newPairDiv = document.createElement('div');
    newPairDiv.classList.add('size-quantity-pair');

    // Create the new size input
    const newSizeInput = document.createElement('input');
    newSizeInput.type = 'text';
    newSizeInput.name = `sizeQuantities[${pairCount}].size`;
    newSizeInput.placeholder = 'Size (e.g. M)';
    newSizeInput.required = true;

    // Create the new quantity input
    const newQuantityInput = document.createElement('input');
    newQuantityInput.type = 'number';
    newQuantityInput.name = `sizeQuantities[${pairCount}].quantity`;
    newQuantityInput.placeholder = 'Quantity';
    newQuantityInput.min = 0;
    newQuantityInput.required = true;

    // Add the inputs to the new div
    newPairDiv.appendChild(newSizeInput);
    newPairDiv.appendChild(newQuantityInput);

    // Append the new div to the container
    container.appendChild(newPairDiv);
}


function addUpdateSizeQuantityField(containerId, size = '', quantity = '') {
    const container = document.getElementById(containerId);
    const sizeQuantityPair = document.createElement('div');
    sizeQuantityPair.className = 'size-quantity-pair';
    const index = container.querySelectorAll('.size-quantity-pair').length;

    sizeQuantityPair.innerHTML = `
        <input type="text" name="sizeQuantities[${index}].size" value="${size}" placeholder="Size (e.g. M)" required>
        <input type="number" name="sizeQuantities[${index}].quantity" value="${quantity}" placeholder="Quantity" min="0" required>
        <button type="button" onclick="removeSizeQuantityField(this, '${index}')">Remove</button>
        <input type="hidden" name="sizeQuantities[${index}]._isRemoved" value="false" class="is-removed">
    `;

    container.appendChild(sizeQuantityPair);
}

function removeSizeQuantityField(button, index) {
    const sizeQuantityPair = button.parentElement;
    
    // Mark the item as removed instead of deleting the DOM element
    sizeQuantityPair.style.display = 'none'; // Hide the pair visually
    sizeQuantityPair.querySelector(`input[name="sizeQuantities[${index}]._isRemoved"]`).value = "true";
}

