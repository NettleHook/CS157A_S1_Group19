<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>What Can I Cook?</title>
<link href="favicon.ico" rel="icon" type="image/x-icon" />
</head>

<body>
	<div id="wrapper">
		<div id="navigation">
			<span id="nav-home"><a href="${index.jsp}">Search</a></span>
		</div>
		<div id="account_enter">
			<span id="login"><a href="">Log In</a></span> <span id="signup"><a
				href="">Sign Up</a></span>
		</div>
	</div>
	<div id="title-box">
		<h1>What Can I Cook?</h1>
	</div>
	<form id="recipe-search" action="search.jsp" method = "POST">
		<label for="ingredient-input">Ingredients:</label><br> <input
			type="text" id="ingredient-input"> <br> <label
			for="diet-cat">Diet:</label><br>
		<div id="diet-cat">
			<input type="checkbox" id="keto" value="Keto"> <label
				for="keto"> Keto </label> <input type="checkbox" id="atkins"
				value="Atkins"> <label for="atkins"> Atkins </label> <input
				type="checkbox" id="paleo" value="Paleo"> <label for="paleo">
				Paleo </label> <input type="checkbox" id="pescatarian" value="Pescatarian">
			<label for="pescatarian"> Pescatarian </label> <input type="checkbox"
				id="vegetarian" value="Vegetarian"> <label for="vegetarian">
				Vegetarian </label> <input type="checkbox" id="vegan" value="Vegan">
			<label for="vegan"> Vegan </label> <input type="checkbox" id="halal"
				value="halal"> <label for="halal"> Halal-friendly </label> <input
				type="checkbox" id="kosher" value="kosher"> <label
				for="kosher"> Kosher-friendly </label>
		</div>
		<label for="food-cat">Category:</label><br>
		<div id="food-cat">
			<input type="radio" id="sweets" name="food-cat"> <label
				for="sweets">Desserts and Sweets</label> <input type="radio"
				id="bread" name="food-cat"> <label for="bread">Bread</label>
			<input type="radio" id="soups-stews" name="food-cat"> <label
				for="soups-stews">Soups and Stews</label> <input type="radio"
				id="salads" name="food-cat"> <label for="salads">Salads</label>
			<input type="radio" id="sauce" name="food-cat"> <label
				for="sauce">Dressings and Sauces</label> <input type="radio"
				id="snacks" name="food-cat"> <label for="snacks">Snacks</label>
			<input type="radio" id="main-dishes" name="food-cat"> <label
				for="main-dishes">Main Dishes</label>
		</div>
		<input type="submit" value="Submit">
	</form>

</body>

</html>
