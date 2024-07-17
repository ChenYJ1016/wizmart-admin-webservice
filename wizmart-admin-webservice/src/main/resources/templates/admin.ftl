<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Page</title>
    <style>
        /* General Styles */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
        }
        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .navbar {
            background-color: #333;
            overflow: hidden;
            width: 100%;
        }
        .navbar a {
            float: left;
            display: block;
            color: white;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
            font-size: 17px;
        }
        .navbar a:hover {
            background-color: #ddd;
            color: black;
        }
        .navbar .logout-button {
            float: right;
        }
        /* Table and Modal Styles */
        .table-container {
            width: 80%;
            position: relative;
            margin-top: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        .add-product-button {
            position: absolute;
            top: -50px;
            right: 0;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
        }
        .modal {
            display: none; 
            position: fixed; 
            z-index: 1; 
            left: 0;
            top: 0;
            width: 100%; 
            height: 100%; 
            overflow: auto; 
            background-color: rgba(0,0,0,0.4); 
            padding-top: 60px;
        }
        .modal-content {
            background-color: #fefefe;
            margin: 5% auto; 
            padding: 20px;
            border: 1px solid #888;
            width: 80%; 
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }
        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
    </style>
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

        window.onclick = function(event) {
            if (event.target == document.getElementById('updateModal')) {
                closeUpdateModal();
            } else if (event.target == document.getElementById('createModal')) {
                closeCreateModal();
            }
        }
    </script>
</body>
</html>
