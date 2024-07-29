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
                        <th>Stock</th>
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
                        <td>${product.id}</td>
                        <td>${product.productName}</td>
                        <td>${product.productDescription}</td>
                        <td>${product.productPrice}</td>
                        <td>${product.productStock}</td>
                        <td>
                            <#if product.productImageUrl?has_content>
                                <img src="${product.productImageUrl}" alt="${product.productName}" style="max-width: 100px;">
                            <#else>
                                No image
                            </#if>
                        </td>
                        <td>${product.productColour}</td>
                        <td>${product.gender}</td>
                        <td>${product.size}</td>
                        <td>${product.category}</td>
                        <td>
                            <button type="button" onclick="openUpdateModal(
                                ${product.id},
                                '${product.productName}',
                                '${product.productDescription}',
                                ${product.productPrice},
                                ${product.productStock},
                                '${product.productImageUrl}',
                                '${product.productColour}',
                                '${product.gender}',
                                '${product.size}',
                                '${product.category}')">Update</button>
                            <form action="/admin/products/delete/${product.id}" method="post" style="display:inline;">
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
                <form id="updateForm" action="/admin/products/update" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input type="hidden" name="_method" value="PUT">
                    <input type="hidden" id="updateProductId" name="productId">

                    <label for="updateName">Name:</label>
                    <input type="text" id="updateName" name="productName" placeholder="Name" required>
                    <br>

                    <label for="updateDescription">Description:</label>
                    <textarea id="updateDescription" name="productDescription" placeholder="Description" required></textarea>
                    <br>

                    <label for="updatePrice">Price:</label>
                    <input type="number" id="updatePrice" name="productPrice" placeholder="Price" step="0.01" required>
                    <br>

                    <label for="updateStock">Stock:</label>
                    <input type="number" id="updateStock" name="productStock" placeholder="Stock" required>
                    <br>

                    <label for="updateImageUrl">Current Image:</label>
                    <input type="text" id="updateImageUrl" name="productImageUrl" placeholder="Image URL" readonly>
                    <br>

                    <label for="updateImageFile">New Image:</label>
                    <input type="file" id="updateImageFile" name="productImageFile" accept="image/*" onchange="handleFileChange('updateImageFile', 'updateImageUrl')">
                    <br>

                    <label for="updateColour">Colour:</label>
                    <input type="text" id="updateColour" name="productColour" placeholder="Colour">
                    <br>

                    <label for="updateGender">Gender:</label>
                    <input type="text" id="updateGender" name="gender" placeholder="Gender">
                    <br>

                    <label for="updateSize">Size:</label>
                    <input type="text" id="updateSize" name="size" placeholder="Size">
                    <br>

                    <label for="updateCategory">Category:</label>
                    <input type="text" id="updateCategory" name="category" placeholder="Category">
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
                    <input type="number" id="createStock" name="productStock" placeholder="Stock" required>
                    <br>

                    <label for="createImageFile">Image:</label>
                    <input type="file" id="createImageFile" name="productImageFile" accept="image/*" required>
                    <br>

                    <label for="createColour">Colour:</label>
                    <input type="text" id="createColour" name="productColour" placeholder="Colour">
                    <br>

                    <label for="createGender">Gender:</label>
                    <input type="text" id="createGender" name="productGender" placeholder="Gender">
                    <br>

                    <label for="createSize">Size:</label>
                    <input type="text" id="createSize" name="size" placeholder="Size">
                    <br>

                    <label for="createCategory">Category:</label>
                    <input type="text" id="createCategory" name="category" placeholder="Category">
                    <br>

                    <button type="submit">Create</button>
                </form>
            </div>
        </div>

    </div>

    <script>
        function handleFileChange(fileInputId, imageUrlInputId) {
            const fileInput = document.getElementById(fileInputId);
            const file = fileInput.files[0];
            if (file) {
                document.getElementById(imageUrlInputId).value = file.name;
            }
        }

        function openUpdateModal(productId, name, description, price, stock, imageUrl, colour, gender, size, category) {
            document.getElementById("updateProductId").value = productId;
            document.getElementById("updateName").value = name;
            document.getElementById("updateDescription").value = description;
            document.getElementById("updatePrice").value = price;
            document.getElementById("updateStock").value = stock;
            document.getElementById("updateImageUrl").value = imageUrl;
            document.getElementById("updateColour").value = colour;
            document.getElementById("updateGender").value = gender;
            document.getElementById("updateSize").value = size;
            document.getElementById("updateCategory").value = category;
            document.getElementById("updateModal").style.display = "block";
        }

        function closeUpdateModal() {
            document.getElementById("updateModal").style.display = "none";
        }

        function openCreateModal() {
            document.getElementById("createModal").style.display = "block";
        }

        function closeCreateModal() {
            document.getElementById("createModal").style.display = "none";
        }
    </script>
</body>
</html>