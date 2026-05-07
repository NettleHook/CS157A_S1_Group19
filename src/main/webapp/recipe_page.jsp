<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*"%>
<%@ page import="app.Database"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<t:layout pageTitle = "Recipes"></t:layout>
		<link href="styles/full_recipe.css" rel="stylesheet" type="text/css">
		<link href="styles/theme.css" rel="stylesheet" type="text/css">
		<link href="styles/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<main class = "page">
		</main>
		<div id="errorPopup" class="popup">
			<span class="popup-close" onclick="errorPopup()">✕</span>
			<p>You must be logged in to access this function.</p>
		</div>
		
		<script>
			async function loadResults() {
				const params = new URLSearchParams(window.location.search);
				console.log(params);
				
				const response = await fetch("api/search/fullRecipe", {
					method: "POST",
					body: params, // URLSearchParams is sent as application/x-www-form-urlencoded
				});
				
				const returnVal = await response.json();
				const main = document.querySelector("main");
				
				if (response.ok) {
					main.innerHTML = returnVal.data.results;
				} else {
					main.innerHTML = "<p>An error occurred: " + (json.data.error ?? "Unknown error") + "</p>";
				}
			}
			
			loadResults();
		</script>
		<script type="text/javascript" src="js/statHandler.js"></script>
		<script type="text/javascript" src="js/full_recipe_ingredients.js"></script>
	</body>
</html>