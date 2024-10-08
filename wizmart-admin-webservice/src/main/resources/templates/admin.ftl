<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Page</title>
    <link rel="stylesheet" href="/static/css/styles.css">
    <link rel="icon" href="/static/icon/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="navbar">
        <a href="#home">Home</a>
        <a href="#orders">Orders</a>
        <a href="/admin/logout" class="logout-button">Logout</a>
    </div>

    <div class="container">
        <div class="header-container">
            <h1>Product List</h1>
            <button class="add-product-button" onclick="openCreateModal()">Add Product</button>
        </div>

        <div class="product-container">
            <div class="product-card-container">
                <#list products as product>
                    <div class="product-card"
					    data-product-id="${product.productId}"
					    data-product-name="${product.productName?html}"
					    data-product-description="${product.productDescription?html}"
					    data-product-price="${product.productPrice}"
					    data-product-image-url="${product.productImageUrl?html}"
					    data-product-colour="${product.productColour?html}"
					    data-product-gender="${product.productGender?html}"
					    data-product-category="${product.productCategory?html}"
					    data-product-size-quantities="
					        <#assign sizeQuantitiesString=''>
					        <#list product.sizeQuantities as sq>
					            <#if sq_has_next>
					                <#assign sizeQuantitiesString += sq.size?trim + ':' + sq.quantity + ';'>
					            <#else>
					                <#assign sizeQuantitiesString += sq.size?trim + ':' + sq.quantity>
					            </#if>
					        </#list>
					        ${sizeQuantitiesString}">
					    <img src="${product.productImageUrl}" alt="${product.productName}" onclick="openViewModal(this.parentElement)">
					    <p>${product.productName}</p>
					    <button onclick="openUpdateModal(this.parentElement)" type="update">Update</button>
					    <form action="/admin/delete/${product.productId}" method="post" style="display:inline;">
					        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
					        <input type="hidden" name="_method" value="DELETE">
					        <button type="delete">Delete</button>
					    </form>
					</div>
                </#list>
            </div>
        </div>
    </div>

    <!-- View Product Modal -->
	<div id="viewModal" class="modal">
	    <div class="modal-content">
	        <span class="close" onclick="closeViewModal()">&times;</span>
	        <h2>View Product</h2>
	        <div class="form-group">
	            <label for="viewProductName">Name:</label>
	            <p id="viewProductName"></p>
	        </div>
	
	        <div class="form-group">
	            <label for="viewProductDescription">Description:</label>
	            <p id="viewProductDescription"></p>
	        </div>
	
	        <div class="form-group">
	            <label for="viewProductPrice">Price:</label>
	            <p id="viewProductPrice"></p>
	        </div>
	
	        <div class="form-group">
	            <label for="viewProductColour">Colour:</label>
	            <p id="viewProductColour"></p>
	        </div>
	
	        <div class="form-group">
	            <label for="viewProductGender">Gender:</label>
	            <p id="viewProductGender"></p>
	        </div>
	
	        <div class="form-group">
	            <label for="viewProductCategory">Category:</label>
	            <p id="viewProductCategory"></p>
	        </div>
	
	        <div class="form-group">
	            <label for="viewProductSizeQuantities">Sizes & Quantities:</label>
	            <ul id="viewProductSizeQuantities"></ul>
	        </div>
	    </div>
	</div>


    <!-- Update Product Modal -->
    <div id="updateModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeUpdateModal()">&times;</span>
            <h2>Update Product</h2>
            <form id="updateForm" action="" method="post" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="_method" value="PUT">
                <input type="hidden" id="updateProductId" name="productId">

                <div class="form-group">
                    <label for="updateProductName">Name:</label>
                    <input type="text" id="updateProductName" name="productName" required>
                </div>

                <div class="form-group">
                    <label for="updateProductDescription">Description:</label>
                    <textarea id="updateProductDescription" name="productDescription" required></textarea>
                </div>

                <div class="form-group">
                    <label for="updateProductPrice">Price:</label>
                    <input type="number" id="updateProductPrice" name="productPrice" step="0.01" min="0" required>
                </div>
                
                 <div class="form-group">
                    <label for="updateProductImageFile">New Image:</label>
                    <input type="file" id="updateProductImageFile" name="productImageFile" accept="image/*">
                </div>

                <div class="form-group">
                    <label for="updateProductColour">Colour:</label>
                    <input type="text" id="updateProductColour" name="productColour">
                </div>

                <div class="form-group">
                    <label for="updateProductGender">Gender:</label>
                    <input type="text" id="updateProductGender" name="productGender">
                </div>

                <div class="form-group">
                    <label for="updateProductCategory">Category:</label>
                    <input type="text" id="updateProductCategory" name="productCategory">
                </div>

                <div class="form-group" id="updateSizeQuantityContainer">
                    <label>Size & Quantity:</label>
                    <div class="size-quantity-pair">
                        <input type="text" name="sizeQuantities[0].size" placeholder="Size (e.g. M)" class="form-control" required>
                        <input type="number" name="sizeQuantities[0].quantity" placeholder="Quantity" min="0" required>
                    </div>
                </div>
				
				<button type="button" onclick="addUpdateSizeQuantityField('updateSizeQuantityContainer')">Add Size/Quantity</button>

                <button type="submit">Update</button>
            </form>
        </div>
    </div>

    <!-- Create Product Modal -->
    <div id="createModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeCreateModal()">&times;</span>
            <h2>Create New Product</h2>
            <form id="createForm" action="/admin/create" method="post" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-group">
                    <label for="createName">Name:</label>
                    <input type="text" id="createName" name="productName" required>
                </div>

                <div class="form-group">
                    <label for="createDescription">Description:</label>
                    <textarea id="createDescription" name="productDescription" required></textarea>
                </div>

                <div class="form-group">
                    <label for="createPrice">Price:</label>
                    <input type="number" id="createPrice" name="productPrice" step="0.01" min="0" required>
                </div>

                <div class="form-group">
                    <label for="createImage">Image:</label>
                    <input type="file" id="createImage" name="productImageFile" accept="image/*" required>
                </div>

                <div class="form-group">
                    <label for="createColour">Colour:</label>
                    <input type="text" id="createColour" name="productColour">
                </div>

                <div class="form-group">
                    <label for="createGender">Gender:</label>
                    <input type="text" id="createGender" name="productGender">
                </div>

                <div class="form-group">
                    <label for="createCategory">Category:</label>
                    <input type="text" id="createCategory" name="productCategory">
                </div>

                <div class="form-group" id="sizeQuantityContainer">
                    <label>Size & Quantity:</label>
                    <div class="size-quantity-pair">
                        <input type="text" name="sizeQuantities[0].size" placeholder="Size (e.g. M)" required>
                        <input type="number" name="sizeQuantities[0].quantity" placeholder="Quantity" min="0" required>
                    </div>
                </div>
				<button type="button" onclick="addSizeQuantityField()">Add More</button>

                <button type="submit">Create</button>
            </form>
        </div>
    </div>

    <script src="/static/js/admin.js"></script>
</body>
</html>
