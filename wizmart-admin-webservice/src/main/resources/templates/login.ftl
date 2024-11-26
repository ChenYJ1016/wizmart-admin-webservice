<!-- src/main/resources/templates/login.ftl -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Login</title>
    <link rel="stylesheet" href="/static/admin/css/loginstyles.css">
</head>
<body>
    <div class="login-container">
        <h2>Admin Login</h2>
        <form action="/auth/login" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" name="username" required>
            </div>

            <div class="form-group">
                <label for="rawPassword">Password:</label>
                <input type="password" name="rawPassword" required>
            </div>

            <button type="submit">Login</button>
        </form>
        <#if error??>
            <div class="error-message">${error}</div>
        </#if>
    </div>
</body>
</html>
