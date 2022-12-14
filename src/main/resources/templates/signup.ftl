<#--This page is used to display the signup procedure -->
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
<h1>Sign Up</h1>
<#--Use javascript to handle the action-->
<script>
    function validateForm() {
        var x = document.forms["signupFrom"]["username"].value;
        if (x == "") {
            alert("Name must be filled out");
            return false;
        }
    }
</script>
<#--Use the form to get the user's input-->
<form name="signupForm" action="signup" onsubmit="return validateForm()" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username"><br><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password"><br><br>
    <label for="email">Email:</label>
    <input type="email" id="email" name="email"><br><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
