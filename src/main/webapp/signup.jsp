<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up</title>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <div class="layout" style="text-align: center; margin-top: 50px;">
        <h1>Create an Account</h1>
        <form id="signup-form">
            <table style="margin: 0 auto; background: #e9f5ff; padding: 20px; border-radius: 10px;">
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
                    <td><input type="submit" value="Register" style="width: 100%;" onclick="handleSignup(event)"/></td>
                </tr>
            </table>
        </form>
        <p id="error-msg" style="color:red;"></p>
        <p>Already have an account? <a href="login.jsp">Log in here</a></p>
    </div>

    <script>
    async function handleSignup(e) {
        e.preventDefault();
        const form = document.getElementById("signup-form");
        const data = new FormData(form);

        const payload = Object.fromEntries(data.entries());
        const res = await fetch("api/auth/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (res.status === 201) {
            window.location.href = "login.jsp";
        } else if (res.status === 409) {
            document.getElementById("error-msg").textContent = "Username already taken.";
        } else if (res.status === 400) {
            document.getElementById("error-msg").textContent = "Invalid username or password format.";
        } else {
            document.getElementById("error-msg").textContent = "Something went wrong. Try again.";
        }
    }
    </script>
</body>
</html>