<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up - What Can I Cook?</title>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <div class="layout" style="text-align: center; margin-top: 50px;">
        <h1>Create an Account</h1>
        <form action="signup_process.jsp" method="POST">
            <table style="margin: 0 auto; background: #e9f5ff; padding: 20px; border-radius: 10px;">
                <tr>
                    <td>First Name:</td>
                    <td><input type="text" name="first_name" required /></td>
                </tr>
                <tr>
                    <td>Last Name:</td>
                    <td><input type="text" name="last_name" required /></td>
                </tr>
                <tr>
                    <td>Email:</td>
                    <td><input type="email" name="email" required /></td>
                </tr>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" required /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" required /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Register" style="width: 100%;" /></td>
                </tr>
            </table>
        </form>
        <p>Already have an account? <a href="login.jsp">Log in here</a></p>
    </div>
</body>
</html>