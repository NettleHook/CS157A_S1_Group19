<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="app.Constants" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>What Can I Cook?</title>
        <link href="styles/style.css" rel="stylesheet" type="text/css">
        <link href="styles/layout.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <t:layout pageTitle="Index">
        </t:layout>
            <form class="recipe-search" id="recipe-search" action="search.jsp" method="GET">
                <div class="ingredients" id="ingredients">
                    <label for="ingredients">Ingredients:</label>
                    <div class="ingredient-row">
                        <input type="text" id="ingredient-input" name="ingredient-input" placeholder="Enter ingredient">
                        <button type="button" onclick="removeIngredient(this)">Remove</button>
                    </div>
                </div>
                <button type="button" onclick="addIngredient()">+ Add Ingredient</button>
                <label>
                    <input type="checkbox" name="ingredient-mode" value="exclusive" checked> Must have all
                </label>
                <div class="diet-cat" id="diet-cat">
                    <label for="diet-cat">Diet:</label>
                    <div id="diet-div"></div>
                </div>
                <label>
                    <input type="checkbox" name="diet-mode" value="inclusive"> Can be any diet
                </label>
                <div class="food-cat" id="food-cat">
                    <label for="food-cat">Category:</label>
                    <div>
                        <% for(Constants.Option option : Constants.CATEGORIES) { %>
                        <input type="radio" id="<%= option.id() %>" name="food-cat" value="<%= option.id() %>">
                        <label for="<%= option.id() %>"><%= option.text() %></label>
                        <% } %>
                    </div>
                    <div id="category-div"></div>
                </div>
                <div class="serving-size-div">
                    <label for="serving-size">Serving Size:</label>
                    <input type="number" id="serving-size" name="serving-size" min=0>
                </div>
                <div class="prep-time-div">
                    <label for="prep-time">Prep Time:</label>
                    <div>
                        <input type="number" id="prep-time-hours" name="prep-time-hours" min=0 placeholder="hours">
                        <span>:</span>
                        <input type="number" id="prep-time-minutes" name="prep-time-minutes" min=0 placeholder="minutes">
                    </div>
                </div>
                <div class="cook-time-div">
                    <label for="cook-time">Cooking Time:</label>
                    <div>
                        <input type="number" id="cook-time-hours" name="cook-time-hours" min=0 placeholder="hours">
                        <span>:</span>
                        <input type="number" id="cook-time-minutes" name="cook-time-minutes" min=0 placeholder="minutes">
                    </div>
                </div>
                <div class="calories-div">
                    <label for="calories">Calorie Cap:</label>
                    <input type="number" id="calories" name="calories" min=0>
                </div>
                <input type="submit" value="Submit">
            </form>
        <script>
            function addIngredient() {
                const container = document.getElementById('ingredients');
                const row = document.createElement('div');
                row.className = 'ingredient-row';
                row.innerHTML = `
                    <input type="text" name="ingredient-input" placeholder="Enter ingredient" />
                    <button type="button" onclick="removeIngredient(this)">Remove</button>
                `;
                container.appendChild(row);
            }

            function removeIngredient(btn) {
                const row = btn.parentElement;
                if (document.querySelectorAll('.ingredient-row').length > 1) {
                    row.remove();
                }
            }
        </script>
        <script src="js/diet_search.js"></script>
        <script>
        propogateDiets(1);
        propagateCategories();
        </script>
    </body>
</html>