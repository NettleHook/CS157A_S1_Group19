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
		<div style="position: fixed; top: 15px; left: 20px;">
			<a href="index.jsp">
				<button style="cursor: pointer;">
					&#8592; Home
				</button>
			</a>
		</div>
		<main class = "page" name = "Results">
		</main>
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