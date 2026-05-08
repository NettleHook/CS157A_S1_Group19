<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link href="styles/reset.css" rel="stylesheet" type="text/css">
    <link href="styles/theme.css" rel="stylesheet" type="text/css">
    <link href="styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <div style="position: fixed; top: 15px; left: 20px;">
        <a href="index.jsp">
            <button>
                &#8592; Home
            </button>
        </a>
    </div>

    <div class="layout" style="text-align: center; margin-top: 50px;">
        <p id="success-msg"></p>
        <p id="error-msg" style="color:red;"></p>
    </div>

    <script>
    async function logout() {
        const res = await fetch("api/logout", {
            method: "POST",
        });

        if (res.ok) {
            document.getElementById("success-msg").textContent = "You have been logged out."
        } else {
            document.getElementById("error-msg").textContent = "Something went wrong. Try again.";
        }
    }
    logout();
    </script>
</body>
</html>