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
			async function toggleBookmark(btn, recipeId) {
				const bookmarked = btn.dataset.bookmarked === "true";
				if (bookmarked) {
					const status = await removeBookmark(recipeId);
					if (status === 200) {
						btn.dataset.bookmarked = "false";
						btn.src = "./assets/unbookmarked.svg";
					}
				} else {
					const status = await addBookmark(recipeId);
					if (status === 201) {
						btn.dataset.bookmarked = "true";
						btn.src = "./assets/bookmarked.svg";
					}
				}
			}
			// Add bookmark
			async function addBookmark(recipeId) {
				const res = await fetch("api/stats/bookmark", {
					method: "POST",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({ recipeId: recipeId })
				});
				return res.status;
			}
			
			// Remove bookmark
			async function removeBookmark(recipeId) {
				const res = await fetch("api/stats/bookmark", {
					method: "DELETE",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({ recipeId: recipeId })
				});
				return res.status;
			}
			loadResults();
			
		</script>
	</body>
</html>