<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login - What Can I Cook?</title>
    <link href="styles/style.css" rel="stylesheet" type="text/css"> </head>
<body>
    <div class="layout" style="text-align: center; margin-top: 50px;">
        <h1>Log In</h1>
        <form action="login_process.jsp" method="POST"> <table style="margin: 0 auto; background: #f4f4f4; padding: 20px; border-radius: 10px;">
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
                    <td><input type="submit" value="Login" style="width: 100%;" /></td>
                </tr>
            </table>
        </form>
        <p>New user? <a href="signup.jsp">Sign up here</a></p>
    </div>
</body>
</html>