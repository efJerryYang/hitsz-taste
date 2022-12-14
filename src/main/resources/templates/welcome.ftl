<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h1>Welcome to our site!</h1>
<p>Please login or sign up to continue:</p>
<form action="login" method="post">
    <label>
        <span>Username:</span>
        <input type="text" name="username"/>
    </label>
    <label>
        <span>Password:</span>
        <input type="password" name="password"/>
    </label>
    <input type="submit" value="Login"/>
</form>
<p>Don't have an account? <a href="signup">Sign up here</a>.</p>
</body>
</html>
