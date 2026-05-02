<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link href="styles/reset.css" rel="stylesheet" type="text/css">
        <link href="styles/theme.css" rel="stylesheet" type="text/css">
        <link href="styles/style.css" rel="stylesheet" type="text/css">
        <link href="styles/upload.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <t:layout pageTitle="Recipe Upload">
            <h1>Recipe Upload</h1>
            <form id="recipe-upload" class="recipe-upload">
                <div class = "recip-name-div">
                    <label for="recipe_name">Recipe Name:</label>
                    <input type="text" name="recipe_name" required>
                </div>
                <div class="serving-size-div">
                <label for="serving-size">Serving Size:</label>
                    <input type="number" id="serving-size" name="serving-size"  min = 0>
                </div>
                <div class="prep-time-div">
                    <label for="prep-time">Prep Time:</label>
                    <div>
                        <input type="number"id="prep-time-hours" name="prep-time-hours" placeholder = "hours" min = 0>
                        <input type="number" id="prep-time-minutes" name="prep-time-minutes" placeholder = "minutes"  min = 0>
                    </div>
                </div>
                <div class="cook-time-div">
                    <label for="cook-time">Cooking Time:</label>
                    <div>
                        <input type="number" id="cook-time-hours" name="cook-time-hours" placeholder = "hours"  min = 0>
                        <input type="number" id="cook-time-minutes" name="cook-time-minutes" placeholder = "minutes"  min = 0>
                    </div>
                </div>
                <div class="calories-div">
                    <label for="calories">Calories per Serving:</label>
                    <input type="number" id="calories" name = "calories"  min = 0>
                </div>
                <div class="ingredients" id="ingredients">
                    <!-- add amounts and unit inputs-->
                    <label for="ingredients">Ingredients:</label>
                    <div class = "ingredient-row">
                        <input type="text" name="ingredient-input-name" placeholder="Enter ingredient name" required/>
                        <input type="number" name="ingredient-input-amt" placeholder="Enter amount (if applicable)"/>
                        <input type="text" name="ingredient-input-unit" placeholder="Enter unit" required list="units-list"/>
                    </div>
                </div>
                <button type="button" onclick="addIngredient()">+ Add Ingredient</button>
                <div class="diet-cat" id="diet-cat">
                    <label for="diet-cat">Diet:</label>
                    <div id="diet-container" class="diet-container"></div>
                </div>
                <div class="food-cat" id="food-cat">
                    <label for="food-cat">Category:</label>
                    <div id="category-container" class="category-container"></div>
                </div>
                <div class = "desc" id = "steps">
                    <!-- Steps as a text box per step?-->
                    <label for="Steps">Steps:</label>
                    <div class = "step-row">
                        <input type="text" name="step" required/>
                    </div>
                </div>
                <button type="button" onclick="addStep()">+ Add Step</button>
                <br>
                <input type="submit" value="Submit" onclick="uploadRecipe(event)">
            </form>
            <p color = "red" id = "error" display="none"></p>
            <datalist id="units-list">
                <option value="to taste">
                <option value="cup">
                <option value="tbsp">
                <option value="tsp">
                <option value="oz">
                <option value="g">
                <option value="pack">
                <option value="bulb">
                <option value="stalk">
                <option value="thumb">
                <option value="shot">
                <option value="self">
                <option value="N/A">
            </datalist>
        </t:layout>
    <script src="js/protected.js"></script>
    <script src="js/upload.js"></script>
</body>
</html>