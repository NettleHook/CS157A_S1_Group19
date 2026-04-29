<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Recipes</title>
		<link href="styles/reset.css" rel="stylesheet" type="text/css">
		<link href="styles/theme.css" rel="stylesheet" type="text/css">
		<link href="styles/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div class = "header">
			<div id="title-box">
				<h1>Results</h1>
			</div>
			<nav>
				<div class="start">
					<button onclick="window.location.href='index.jsp';">Search</button>
					<button onclick="window.location.href='upload.jsp';" id = "recipeUploader">Upload New Recipe </button>
					<button onclick="window.location.href='user.jsp';" id = "profile"> My Profile</button>
				</div>
				<div class="center">
				</div>
				<div class="end"></div>
			</nav>
		</div>
		<main class = "page" name = "Results">
		</main>
		<div id="errorPopup" class="popup">
			<span class="popup-close" onclick="errorPopup()">✕</span>
			<p>You must be logged in to access this function.</p>
		</div>
		<script>
			async function loadResults() {
				const params = new URLSearchParams(window.location.search);
				
				const response = await fetch("api/search/", {
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
	</body>
</html>