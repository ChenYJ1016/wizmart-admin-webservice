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
                        <td>${product.productStock}</td>
                        <td>
                            <button type="button" onclick="openUpdateModal(${product.productId}, '${product.productName}', '${product.productDescription}', ${product.productPrice}, ${product.productStock})">Update</button>
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

        <div id="updateModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeUpdateModal()">&times;</span>
                <h2>Update Product</h2>
                <form id="updateForm" action="" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input type="hidden" name="_method" value="PUT">
                    <input type="text" id="updateName" name="name" placeholder="Name" required>
                    <input type="text" id="updateDescription" name="description" placeholder="Description" required>
                    <input type="number" id="updatePrice" name="price" placeholder="Price" step="0.01" required>
                    <input type="number" id="updateQuantity" name="quantity" placeholder="Quantity" required>
                    <button type="submit">Update</button>
                </form>
            </div>
        </div>

        <div id="createModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeCreateModal()">&times;</span>
                <h2>Create New Product</h2>
                <form action="/admin/products/create" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input type="text" name="name" placeholder="Name" required>
                    <input type="text" name="description" placeholder="Description" required>
                    <input type="number" name="price" placeholder="Price" step="0.01" required>
                    <input type="number" name="quantity" placeholder="Quantity" required>
                    <button type="submit">Create</button>
                </form>
            </div>
        </div>

        <!-- Error Modal -->
        <div id="errorModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeErrorModal()">&times;</span>
                <h2>Error</h2>
                <div id="errorMessages"></div>
                <button onclick="closeErrorModal()">Close</button>
            </div>
        </div>
    </div>

    <script>
        function openUpdateModal(id, name, description, price, quantity) {
            document.getElementById('updateForm').action = '/admin/products/update/' + id;
            document.getElementById('updateName').value = name;
            document.getElementById('updateDescription').value = description;
            document.getElementById('updatePrice').value = price;
            document.getElementById('updateQuantity').value = quantity;
            document.getElementById('updateModal').style.display = "block";
        }

        function closeUpdateModal() {
            document.getElementById('updateModal').style.display = "none";
        }

        function openCreateModal() {
            document.getElementById('createModal').style.display = "block";
        }

        function closeCreateModal() {
            document.getElementById('createModal').style.display = "none";
        }

        function openErrorModal(errors) {
            const errorMessages = document.getElementById('errorMessages');
            errorMessages.innerHTML = '';
            errors.forEach(error => {
                const errorMsg = document.createElement('p');
                errorMsg.textContent = error;
                errorMessages.appendChild(errorMsg);
            });
            document.getElementById('errorModal').style.display = "block";
        }

        function closeErrorModal() {
            document.getElementById('errorModal').style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == document.getElementById('updateModal')) {
                closeUpdateModal();
            } else if (event.target == document.getElementById('createModal')) {
                closeCreateModal();
            } else if (event.target == document.getElementById('errorModal')) {
                closeErrorModal();
            }
        }

        // If there are errors passed from the server, open the error modal
        <#if errors??>
            let errors = [<#list errors as error>"${error.defaultMessage}"<#if error_has_next>,</#if></#list>];
            openErrorModal(errors);
        </#if>
    </script>
</body>
</html>
