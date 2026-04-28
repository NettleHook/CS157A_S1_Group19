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
            color: #55f5b0;
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
            background: #55f5b0;
            color: #fff;
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

        .bookmark-box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 15px;
            margin: 8px 0;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            color: #333;
            cursor: pointer;
            transition: background 0.2s;
        }

        .bookmark-box:hover {
            background: #f9f6f1;
        }

        .bookmark-btn {
            background: none;
            border: none;
            cursor: pointer;
            font-size: 18px;
        }
    </style>
</head>
<body>

    <div class="layout">
        <nav>
            <div class="start">
                <div id="title-box">
                    <h1>What Can I Cook?</h1>
                </div>
                <a href="index.jsp">Search</a>
            </div>
            <div class="center"></div>
            <div class="end">
                <a href="api/logout" onclick="handleLogout(event)">Log Out</a>
            </div>
        </nav>

        <div class="welcome-banner">
            <h1>Welcome back, <span id="welcome-username">...</span></h1>
        </div>

        <div class="main-layout">

            <div class="sidebar">
                <ul>
                    <li><a href="#" class="tab-link active" data-tab="bookmarks">Bookmark List</a></li>
                    <li><a href="#" class="tab-link" data-tab="liked">Liked Recipes</a></li>
                    <li><a href="#" class="tab-link" data-tab="myrecipes">My Recipes</a></li>
                    <li><a href="#" class="tab-link" data-tab="ingredients">Saved Ingredients</a></li>
                </ul>
            </div>

            <div class="content">
                <div id="bookmarks" class="tab-panel active">
                    <h2>Bookmark List</h2>
                    <div id="bookmarks-container"></div>
                </div>

                <div id="liked" class="tab-panel">
                    <h2>Liked Recipes</h2>
                    <p>Recipes you've liked will appear here.</p>
                </div>

                <div id="myrecipes" class="tab-panel">
                    <h2>My Recipes</h2>
                    <p>Recipes you've created will appear here.</p>
                </div>

                <div id="ingredients" class="tab-panel">
                    <h2>Saved Ingredients</h2>
                    <p>Your saved ingredients will appear here.</p>
                </div>
            </div>
        </div>
    </div>

    <script>
        async function loadUser() {
            // Temporary blocked out for testing
            const res = await fetch("api/validate");
            if (res.ok) {
                const data = await res.json();
                document.getElementById("welcome-username").textContent = data.username;
            } else {
                window.location.href = "login.jsp";
            }

            // Temporary: remove for testing
            //document.getElementById("welcome-username").textContent = "TestUser";
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

        async function loadBookmarks() {
        try {
            const res = await fetch("api/bookmarks");
            if (res.ok) {
                const bookmarks = await res.json();
                const container = document.getElementById("bookmarks-container");
                container.innerHTML= "";
                
                if (bookmarks.length === 0) {
                    container.innerHTML = "<p>No bookmarks yet.</p>";
                } else {
                    bookmarks.forEach(recipeName => {
                        const box = document.createElement("div");
                        box.className = "bookmark-box";

                        const name = document.createElement("span");
                        name.textContent = recipeName;

                        const btn = document.createElement("button");
                        btn.className = "bookmark-btn";
                        btn.textContent = "🔖";
                        btn.onclick = async () => {
                            await fetch("api/bookmarks", {
                                method: "DELETE",
                                headers: { "Content-Type": "application/json" },
                                body: JSON.stringify({ recipeName: recipeName })
                            });
                            loadBookmarks();
                        };

                        box.appendChild(name);
                        box.appendChild(btn);
                        container.appendChild(box);
                    });
                }
            }
        } catch (err) {
            console.error("Failed to load bookmarks:", err);
        }
    }

        loadUser();
        loadBookmarks();
    </script>

</body>
</html>