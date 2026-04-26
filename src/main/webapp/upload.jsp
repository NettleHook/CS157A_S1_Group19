<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="app.Constants" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Recipe Upload</title>
        <link href="styles/reset.css" rel="stylesheet" type="text/css">
        <link href="styles/theme.css" rel="stylesheet" type="text/css">
        <link href="styles/style.css" rel="stylesheet" type="text/css">
    </head>
    <script>
        function addIngredient() {
            const container = document.getElementById('ingredients');
            const row = document.createElement('div');
            row.className = 'ingredient-row';
            row.innerHTML = `
            <input type="text" name="ingredient-input-name" placeholder="Enter ingredient name" required/>
            <input type="number" name="ingredient-input-amt" placeholder="Enter amount (if applicable)"/>
            <input type="text" name="ingredient-input-unit" placeholder="Enter unit" required/>
            <button type="button" onclick="removeIngredient(this)">Remove</button>
            `;//unit may need to be converted to searchable dropdown to limit user input
            container.appendChild(row);
        }
        
        function removeIngredient(btn) {
            const row = btn.parentElement;
            if (document.querySelectorAll('.ingredient-row').length > 1) {
                row.remove();
            }
        }
        function addStep() {
            const container = document.getElementById('steps');
            const row = document.createElement('div');
            row.className = 'step-row';
            row.innerHTML = `
            <input type="text" name="step"/>
            <button type="button" onclick="removeStep(this)">Remove</button>
            `;
            container.appendChild(row);
        }
        
        function removeStep(btn) {
            const row = btn.parentElement;
            if (document.querySelectorAll('.step-row').length > 1) {
                row.remove();
            }
        }
        async function checkLogin() {
            const res = await fetch("api/validate");
            if (!res.ok){
                document.write("<h3>This Service is only available to registered users.</h3> <a href = 'login.jsp'> Log In</a> <a href = 'signup.jsp'> Sign Up </a> <br> <a href = 'index.jsp'>Back to Search</a>")
            }
        }
        checkLogin();
    </script>
    <body>
        <h1>Recipe Upload</h1>

        <form id="recipe-upload">
            <div class = "recip-name-div">
                <label for = "recipe_name">Recipe Name:</label>
                <input type = "text", name= "recipe_name" required>

            </div>					<div class="serving-size-div">
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
                <input type="text" name="ingredient-input-unit" placeholder="Enter unit" required/>
            </div>
        </div>
        <button type="button" onclick="addIngredient()">+ Add Ingredient</button>
        <div class="diet-cat" id="diet-cat">
            <label for="diet-cat">Diet:</label>
            <div>
                <% for(Constants.Option option : Constants.DIETS) { %>
                <input type="checkbox" id="<%= option.id() %>" name="diet-cat" value="<%= option.id() %>">
                <label for="<%= option.id() %>"><%= option.text() %></label>
                <% } %>
            </div>
        </div>

        <div class="food-cat" id="food-cat">
            <label for="food-cat">Category:</label>
            <div>
                <% for(Constants.Option option : Constants.CATEGORIES) { %>
                <input type="radio" id="<%= option.id() %>" name="food-cat" value="<%= option.id() %>" required>
                <label for="<%= option.id() %>"><%= option.text() %></label>
                <% } %>
            </div>
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
    <script>
        async function uploadRecipe(e) {
            e.preventDefault();
            const form = document.getElementById("recipe-upload");
            const data = new FormData(form);
            
            try {
                const response = await fetch("api/upload", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: new URLSearchParams(data)
                });
                
                const returnVal = await response.json();
                
                if (response.status === 200) {
                    window.location.href = "./recipe_page.jsp?rsid=" + returnVal.data.resid;
                } else {
                    document.write("<h2>Something went wrong: " + returnVal.data.error + "</h2>");
                }
                
            } catch (error) {
                console.error("Request failed: ", error);
                document.write("<h2>Something went wrong: " + error.message + "</h2>");
            }
        }
    </script>
</body>
</html>