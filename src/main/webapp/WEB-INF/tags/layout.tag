<%@tag description="Main Layout" pageEncoding="UTF-8"%>
<%@attribute name="pageTitle" required="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>${pageTitle}</title>
    <link href="styles/reset.css" rel="stylesheet" type="text/css">
    <link href="styles/theme.css" rel="stylesheet" type="text/css">
    <link href="styles/layout.css" rel="stylesheet" type="text/css">
</head>
<body>
    <div class="layout">
        <nav>
            <div class="start">
                <div id="title-box">
                    <h1>What Can I Cook?</h1>
                </div>
                <a href="upload.jsp">Upload</a>
                <a href="search.jsp">Search</a>
            </div>
            <div class="center"></div>
            <div class="end">
                <div id="logged-out" style="display: none;">
                    
                    <a href="login.jsp" id="login">Log In</a>
                    <a href="signup.jsp" id="signup">Sign Up</a>
                </div>
                
                <div id="logged-in" style="display: none;">
                    <a href="api/logout" onclick="handleLogout(event)">Log Out</a>
                    <a href="user.jsp">Profile</a>
                </div>
            </div>
        </nav>
        <main id="main" class="main">
            <jsp:doBody/>
        </main>
        <script src="js/validate.js"></script>
        <script type="module">
            const json = await validateUser();

            const config = {
                isLoggedIn: json.data != null,
                userId: json.data?.userId,
                username: json.data?.username,
            };

            if (config.isLoggedIn) {
                document.getElementById('logged-in').style.display = 'block';
            } else {
                document.getElementById('logged-out').style.display = 'block';
            }

            window.dispatchEvent(new CustomEvent('configReady', { detail: config }));
        </script>
        <script>
            async function handleLogout(e) {
                e.preventDefault();
                await fetch("api/logout", { method: "POST" });
                window.location.href = "login.jsp";
            }
        </script>
    </div>
    <footer>
    </footer>
</body>
</html>