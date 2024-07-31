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
        <a href="#products">Products</a>
        <a href="#orders">Orders</a>
        <a href="/admin/logout" class="logout-button">Logout</a>
    </div>

    <div class="container">
        <h1>Product List</h1>
        <div class="table-container">
            <button class="add-product-button" onclick="openCreateModal()">Add Product</button>
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Image</th>
                        <th>Colour</th>
                        <th>Gender</th>
                        <th>Size</th>
                        <th>Category</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <#list products as product>
                    <tr>
                        <td>${product.productId}</td>
                        <td>${product.productName}</td>
                        <td>${product.productDescription}</td>
                        <td>${product.productPrice}</td>
                        <td>${product.productQuantity}</td>
                        <td>
                            <#if product.productImageUrl?has_content>
                                <img src="${product.productImageUrl}" style="max-width: 100px;">
                            <#else>
                                No image
                            </#if>
                        </td>
                        <td>${product.productColour}</td>
                        <td>${product.productGender}</td>
                        <td>${product.productSize}</td>
                        <td>${product.productCategory}</td>
                        <td>
                            <button type="button" onclick="openUpdateModal(
                                ${product.productId},
                                '${product.productName}',
                                '${product.productDescription}',
                                ${product.productPrice},
                                ${product.productQuantity},
                                '${product.productImageUrl}',
                                '${product.productColour}',
                                '${product.productGender}',
                                '${product.productSize}',
                                '${product.productCategory}')">Update</button>
                            <form action="/admin/products/delete/${product.productId}" method="post" style="display:inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                <input type="hidden" name="_method" value="DELETE">
                                <button type="submit">Delete</button>
                            </form>
                        </td>
                    </tr>
                    </#list>
                </tbody>
            </table>
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

                    <label for="updateProductName">Name:</label>
                    <input type="text" id="updateProductName" name="productName" placeholder="Name" required>
                    <br>

                    <label for="updateProductDescription">Description:</label>
                    <textarea id="updateProductDescription" name="productDescription" placeholder="Description" required></textarea>
                    <br>

                    <label for="updateProductPrice">Price:</label>
                    <input type="number" id="updateProductPrice" name="productPrice" placeholder="Price" step="0.01" required>
                    <br>

                    <label for="updateProductQuantity">Stock:</label>
                    <input type="number" id="updateProductQuantity" name="productQuantity" placeholder="Stock" required>
                    <br>

                    <input type="hidden" id="updateProductImageUrl" name="productImageUrl" placeholder="Image URL" readonly>
                    <br>

                    <label for="updateProductImageFile">New Image:</label>
                    <input type="file" id="updateProductImageFile" name="productImageFile" accept="image/*" onchange="handleFileChange('updateProductImageFile', 'updateProductImageUrl')">
                    <br>

                    <label for="updateProductColour">Colour:</label>
                    <input type="text" id="updateProductColour" name="productColour" placeholder="Colour">
                    <br>

                    <label for="updateProductGender">Gender:</label>
                    <input type="text" id="updateProductGender" name="productGender" placeholder="Gender">
                    <br>

                    <label for="updateProductSize">Size:</label>
                    <input type="text" id="updateProductSize" name="productSize" placeholder="Size">
                    <br>

                    <label for="updateProductCategory">Category:</label>
                    <input type="text" id="updateProductCategory" name="productCategory" placeholder="Category">
                    <br>

                    <button type="submit">Update</button>
                </form>
            </div>
        </div>

        <!-- Create Product Modal -->
        <div id="createModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeCreateModal()">&times;</span>
                <h2>Create New Product</h2>
                <form id="createForm" action="/admin/products/create" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                    <label for="createName">Name:</label>
                    <input type="text" id="createName" name="productName" placeholder="Name" required>
                    <br>

                    <label for="createDescription">Description:</label>
                    <textarea id="createDescription" name="productDescription" placeholder="Description" required></textarea>
                    <br>

                    <label for="createPrice">Price:</label>
                    <input type="number" id="createPrice" name="productPrice" placeholder="Price" step="0.01" required>
                    <br>

                    <label for="createStock">Stock:</label>
                    <input type="number" id="createStock" name="productQuantity" placeholder="Stock" required>
                    <br>

                    <label for="createImage">Image:</label>
                    <input type="file" id="createImage" name="productImageFile" accept="image/*" required>
                    <br>

                    <label for="createColour">Colour:</label>
                    <input type="text" id="createColour" name="productColour" placeholder="Colour">
                    <br>

                    <label for="createGender">Gender:</label>
                    <input type="text" id="createGender" name="productGender" placeholder="Gender">
                    <br>

                    <label for="createSize">Size:</label>
                    <input type="text" id="createSize" name="productSize" placeholder="Size">
                    <br>

                    <label for="createCategory">Category:</label>
                    <input type="text" id="createCategory" name="productCategory" placeholder="Category">
                    <br>

                    <button type="submit">Create</button>
                </form>
            </div>
        </div>
    </div>

    <script>
        function openUpdateModal(productId, productName, productDescription, productPrice, productQuantity, productImageUrl, productColour, productGender, productSize, productCategory) {
            document.getElementById('updateForm').action = '/admin/products/update/' + productId;
            document.getElementById('updateProductId').value = productId;
            document.getElementById('updateProductName').value = productName;
            document.getElementById('updateProductDescription').value = productDescription;
            document.getElementById('updateProductPrice').value = productPrice;
            document.getElementById('updateProductQuantity').value = productQuantity;
            document.getElementById('updateProductImageUrl').value = productImageUrl;
            document.getElementById('updateProductColour').value = productColour;
            document.getElementById('updateProductGender').value = productGender;
            document.getElementById('updateProductSize').value = productSize;
            document.getElementById('updateProductCategory').value = productCategory;

            document.getElementById('updateModal').style.display = 'block';
        }

        function closeUpdateModal() {
            document.getElementById('updateModal').style.display = 'none';
        }

        function openCreateModal() {
            document.getElementById('createForm').reset();
            document.getElementById('createModal').style.display = 'block';
        }

        function closeCreateModal() {
            document.getElementById('createModal').style.display = 'none';
        }

        function handleFileChange(fileInputId, urlInputId) {
            var fileInput = document.getElementById(fileInputId);
            var urlInput = document.getElementById(urlInputId);

            if (fileInput.files.length > 0) {
                var file = fileInput.files[0];
                var reader = new FileReader();
                reader.onloadend = function () {
                    urlInput.value = reader.result;
                };
                reader.readAsDataURL(file);
            } else {
                urlInput.value = '';
            }
        }
    </script>
</body>
</html>
