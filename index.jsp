<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="app.Constants"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>What Can I Cook?</title>
<link href="favicon.ico" rel="icon" type="image/x-icon">
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
      <input type="text" name="ingredient-input" placeholder="Enter ingredient" />
      <button type="button" onclick="removeIngredient(this)">Remove</button>
    `;
    container.appendChild(row);
  }

  function removeIngredient(btn) {
    const row = btn.parentElement;
    // Keep at least one ingredient row
    if (document.querySelectorAll('.ingredient-row').length > 1) {
      row.remove();
    }
  }
</script>

<body>
	<div class="layout">
		<nav>
			<div class="start">
				<div id="title-box">
					<h1>What Can I Cook?</h1>
				</div>
				<a href="${index.jsp}">Search</a>
			</div>
			<div class="center"></div>
			<div class="end">
				<a href="">Log In</a> <a href="">Sign Up</a>
			</div>
		</nav>
		<main>
			<form class="recipe-search" id="recipe-search" action="search.jsp"
				method="POST">
				<div class="ingredients" id="ingredients">
					<label for="ingredients">Ingredients:</label>
					<div class="ingredient-row">
						<input type="text" id="ingredient-input" name="ingredient-input"
							placeholder="Enter ingredient">
						<button type="button" onclick="removeIngredient(this)">Remove</button>
					</div>
				</div>
				<button type="button" onclick="addIngredient()">+ Add
					Ingredient</button>
				<div class="diet-cat" id="diet-cat">
					<label for="diet-cat">Diet:</label>
					<div>
						<%
						for (Constants.Option option : Constants.DIETS) {
						%>
						<input type="checkbox" id="<%=option.id()%>"
							value="<%=option.id()%>"> <label
							for="<%=option.id()%>"><%=option.text()%></label>
						<%
						}
						%>
					</div>
				</div>

				<div class="food-cat" id="food-cat">
					<label for="food-cat">Category:</label>
					<div>
						<%
						for (Constants.Option option : Constants.CATEGORIES) {
						%>
						<input type="radio" id="<%=option.id()%>" name="food-cat"
							value="<%=option.id()%>"> <label
							for="<%=option.id()%>"><%=option.text()%></label>
						<%
						}
						%>
					</div>
				</div>
				<div class="serving-size-div">
					<label for="serving-size">Serving Size:</label> <input
						type="number" id="serving-size" name="serving-size">
				</div>
				<div class="prep-time-div">
					<label for="prep-time">Prep Time:</label>
					<div>
						<input type="number" id="prep-time-hours" name="prep-time-hours">
						<input type="number" id="prep-time-minutes"
							name="prep-time-minutes">
					</div>
				</div>
				<div class="cook-time-div">
					<label for="cook-time">Cooking Time:</label>
					<div>
						<input type="number" id="cook-time-hours" name="cook-time-hours">
						<input type="number" id="cook-time-minutes"
							name="cook-time-minutes">
					</div>
				</div>
				<div class="calories-div">
					<label for="calories">Calorie Cap:</label> <input type="number"
						id="calories" name="calories">
				</div>
				<input type="submit" value="Submit">
			</form>
		</main>
	</div>
</body>

</html>
