<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile</title>
    <link href="styles/reset.css" rel="stylesheet" type="text/css">
    <link href="styles/theme.css" rel="stylesheet" type="text/css">
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Georgia, serif;
            background: #f9f6f1;
            min-height: 100vh;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 30px;
            background: #fff;
            border-bottom: 1px solid #e0e0e0;
        }

        .topbar a {
            text-decoration: none;
            color: #555;
            font-size: 14px;
            border: 1px solid #ccc;
            padding: 6px 14px;
            border-radius: 6px;
            background: #fff;
        }

        .topbar a:hover {
            background: #f0f0f0;
        }

        .welcome-banner {
            text-align: center;
            padding: 50px 20px 30px;
        }

        .welcome-banner h1 {
            font-size: 2.4em;
            color: #2c2c2c;
            font-weight: normal;
        }

        .welcome-banner h1 span {
            font-weight: bold;
            color: #c0392b;
        }

        .main-layout {
            display: flex;
            max-width: 1100px;
            margin: 0 auto;
            padding: 20px 30px 40px;
            gap: 30px;
        }

        .sidebar {
            width: 220px;
            flex-shrink: 0;
        }

        .sidebar ul {
            list-style: none;
            border: 1px solid #ddd;
            border-radius: 10px;
            overflow: hidden;
            background: #fff;
        }

        .sidebar ul li a {
            display: block;
            padding: 15px 20px;
            text-decoration: none;
            color: #333;
            font-size: 15px;
            border-bottom: 1px solid #eee;
            transition: background 0.2s;
        }

        .sidebar ul li:last-child a {
            border-bottom: none;
        }

        .sidebar ul li a:hover,
        .sidebar ul li a.active {
            background: #c0392b;
            color: #fff;
        }

        .sidebar ul li a .tab-icon {
            margin-right: 10px;
        }

        .content {
            flex: 1;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 30px;
            min-height: 400px;
        }

        .tab-panel {
            display: none;
        }

        .tab-panel.active {
            display: block;
        }

        .tab-panel h2 {
            font-size: 1.4em;
            margin-bottom: 15px;
            color: #2c2c2c;
            border-bottom: 2px solid #f0f0f0;
            padding-bottom: 10px;
        }

        .tab-panel p {
            color: #888;
            font-size: 14px;
        }
    </style>
</head>
<body>

    <div class="topbar">
        <a href="index.jsp">&#8592; Home</a>
        <a href="api/logout" onclick="handleLogout(event)">Log Out</a>
    </div>

    <div class="welcome-banner">
        <h1>Welcome back, <span id="display-username">...</span></h1>
    </div>

    <div class="main-layout">

        <div class="sidebar">
            <ul>
                <li><a href="#" class="tab-link active" data-tab="bookmarks"><span class="tab-icon">🔖</span>Bookmark List</a></li>
                <li><a href="#" class="tab-link" data-tab="liked"><span class="tab-icon">❤️</span>Liked Recipes</a></li>
                <li><a href="#" class="tab-link" data-tab="myrecipes"><span class="tab-icon">📋</span>My Recipes</a></li>
                <li><a href="#" class="tab-link" data-tab="ingredients"><span class="tab-icon">🧺</span>Saved Ingredients</a></li>
            </ul>
        </div>

        <div class="content">
            <div id="bookmarks" class="tab-panel active">
                <h2>🔖 Bookmark List</h2>
                <p>Your bookmarked recipes will appear here.</p>
            </div>

            <div id="liked" class="tab-panel">
                <h2>❤️ Liked Recipes</h2>
                <p>Recipes you've liked will appear here.</p>
            </div>

            <div id="myrecipes" class="tab-panel">
                <h2>📋 My Recipes</h2>
                <p>Recipes you've created will appear here.</p>
            </div>

            <div id="ingredients" class="tab-panel">
                <h2>🧺 Saved Ingredients</h2>
                <p>Your saved ingredients will appear here.</p>
            </div>
        </div>

    </div>

    <script>
        async function loadUser() {
        /*
            const res = await fetch("api/validate");
            if (res.ok) {
                const data = await res.json();
                document.getElementById("display-username").textContent = data.username;
            } else {
                window.location.href = "login.jsp";
            }
                */
    
            // Temporary being used as testing
            document.getElementById("display-username").textContent = "TestUser";
        }

        document.querySelectorAll(".tab-link").forEach(link => {
            link.addEventListener("click", function(e) {
                e.preventDefault();
                document.querySelectorAll(".tab-link").forEach(l => l.classList.remove("active"));
                document.querySelectorAll(".tab-panel").forEach(p => p.classList.remove("active"));
                this.classList.add("active");
                document.getElementById(this.dataset.tab).classList.add("active");
            });
        });

        async function handleLogout(e) {
            e.preventDefault();
            await fetch("api/logout", { method: "POST" });
            window.location.href = "login.jsp";
        }

        loadUser();
    </script>

</body>
</html>