<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
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
        <t:layout pageTitle="Search">
		</t:layout>
		<div id="errorPopup" class="popup">
			<span class="popup-close" onclick="errorPopup()">✕</span>
			<p>You must be logged in to access this function.</p>
		</div>
		<script>
			async function loadResults() {
				const params = new URLSearchParams(window.location.search);
				
				const response = await fetch("api/search/search", {
					method: "POST",
					body: params, // URLSearchParams is sent as application/x-www-form-urlencoded
				});
				
				const returnVal = await response.json();
				const main = document.querySelector("main");
				
				if (response.ok) {
					main.innerHTML = returnVal.data.results;
				} else {
					main.innerHTML = "<p>An error occurred: " + (returnVal.error ?? "Unknown error") + "</p>";
				}
			}
			
			loadResults();
		</script>
		<script type="text/javascript" src="js/statHandler.js"></script>
	</body>
</html>