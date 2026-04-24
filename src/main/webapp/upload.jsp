<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>test</title>
    </head>
    <script>
        async function checkLogin() {
            const res = await fetch("api/validate");
            if (!res.ok){
                document.write("<h3>This Service is only available to registered users.</h3> <a href = 'login.jsp'> Log In</a> <a href = 'signup.jsp'> Sign Up </a> <a href = 'index.jsp'>Back to Search</a>")
            }
        }
        checkLogin();
    </script>
    <body>
        <h1>Test</h1>
    </body>
</html>