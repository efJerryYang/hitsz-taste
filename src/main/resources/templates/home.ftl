<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
</head>
<body>
  <h1>Welcome, ${user.name}!</h1>
  <p>Here is some information about your account:</p>
  <ul>
    <li>Username: ${user.name}</li>
    <li>Email: ${user.email}</li>
    <li>Phone: ${user.phone}</li>
    <#--  <li>Joined on: ${user.joinDate}</li>  -->
  </ul>
  <p><a href="logout">Log out</a></p>
</body>
</html>
