<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile</title>
    <link href="styles/reset.css" rel="stylesheet" type="text/css">
    <link href="styles/theme.css" rel="stylesheet" type="text/css">
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <link href="styles/user.css" rel="stylesheet" type="text/css">
</head>
<body>
    <t:layout pageTitle="User">
        <div class="welcome-banner">
            <h1>Welcome back, <span id="welcome-username">...</span></h1>
        </div>

        <div class="menu">
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
                    <div id="ingredients-container"></div>
                </div>
            </div>
        </div>
    </t:layout>
    <script>    
        function init(config) {
            document.getElementById("welcome-username").textContent = config.username;
        }

        window.addEventListener('configReady', (e) => init(e.detail));

        document.querySelectorAll(".tab-link").forEach(link => {
            link.addEventListener("click", function(e) {
                e.preventDefault();
                document.querySelectorAll(".tab-link").forEach(l => l.classList.remove("active"));
                document.querySelectorAll(".tab-panel").forEach(p => p.classList.remove("active"));
                this.classList.add("active");
                document.getElementById(this.dataset.tab).classList.add("active");
            });
        });

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
        loadBookmarks();

        async function loadIngredients() {
            try {
                const res = await fetch("api/me/ingredients");
                if (res.ok) {
                    const json = await res.json();
                    const data = json.data ?? [];
                    const container = document.getElementById("ingredients-container");
                    container.innerHTML= "";
                    
                    if (data.length === 0) {
                        container.innerHTML = "<p>Your saved ingredients will appear here.</p>";
                    } else {
                        data.forEach(i => {
                            const box = document.createElement("div");
                            box.className = "ingredient-box";

                            const name = document.createElement("span");
                            name.textContent = i.name;
                            const unit = document.createElement("span");
                            unit.textContent = i.unit;
                            const amount = document.createElement("span");
                            amount.textContent = i.amount;

                            const btn = document.createElement("button");

                            box.appendChild(name);
                            box.appendChild(btn);
                            container.appendChild(box);
                        });
                    }
                }
            } catch (err) {
                console.error("Failed to load ingredients:", err);
            }
        }
        loadIngredients();
    </script>

</body>
</html>