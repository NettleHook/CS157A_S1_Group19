<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>My Profile</title>
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
                        <li><a href="#" class="tab-link" data-tab="diets">My Diets</a></li>
                    </ul>
                </div>

                <div class="content">
                    <div id="bookmarks" class="tab-panel active">
                        <div class="header">
                            <h2>Bookmark List</h2>
                        </div>
                        <div id="bookmarks-container"></div>
                    </div>

                    <div id="liked" class="tab-panel">
                        <div class="header">
                            <h2>Liked Recipes</h2>
                        </div>
                        <p>Recipes you've liked will appear here.</p>
                    </div>

                    <div id="myrecipes" class="tab-panel">
                        <div class="header">
                            <h2>My Recipes</h2>
                        </div>
                        <div id="recipes-container" class="recipes-container"></div>
                    </div>

                    <div id="ingredients" class="tab-panel">
                        <div class="header">
                            <h2>Saved Ingredients</h2>
                            <button class="add-ingredient-btn" onClick="showAddIngredient()">+</button>
                        </div>
                        <div id="ingredients-container"></div>
                        <div id="add-ingredient-container" class="add-ingredient-container" style="display: none;">
                            <form id="add-ingredient-form" class="add-ingredient-form" onsubmit="addIngredient(event)">
                                <input type="text" name="ingredientId" placeholder="Enter ingredient name" required/>
                                <input type="number" name="amount" step=0.01 placeholder="Enter amount (if applicable)"/>
                                <select id="ingredient-input-unit" name="unitId" placeholder="Enter unit" required></select>
                                <input type="submit" value="Add">
                            </form>
                            <div id="error" style="display: none; color: red;"></div>
                        </div>
                    </div>
                    <div id="diets" class="tab-panel">
                        <div class="header">
                            <h2>Saved Diets</h2>
                        </div>
                        <div id="diets-container" class="diets-container">
                            <div id="diet-div" class="diet-items"></div>
                            <button onclick="registerDiets()">Submit Changes</button>
                        </div>
                    </div>
                </div>
            </div>
        </t:layout>
        <script src="js/protected.js"></script>
        <script src="js/user.js"></script>
        <script src ="js/diet_search.js"></script>
        <script>propogateDiets(0);</script>
    </body>
</html>