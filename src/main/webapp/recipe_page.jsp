<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*"%>
<%@ page import="app.Database"%>

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
					<h1 id = title></h1>
				</div>
				<nav>
					<div class="start">
						<button onclick="window.location.href='index.jsp';">Search</button>
						<button onclick="window.location.href='upload.jsp';" id = "recipeUploader">Upload New Recipe </button>
						<button onclick="window.location.href='user.jsp';" id = "profile"> My Profile</button>
					</div>
					<div class="center"></div>
					<div class="end"></div>
				</nav>
			</div>

		<main class = "page">
			<%
				java.util.function.Function<String, String> esc = s ->
				s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
				String rsid = request.getParameter("rsid");
			%>
			<div style="position: fixed; top: 15px; right: 20px;">
				<a href="index.jsp">
					<button>
						Likes
					</button>
					<button>
						<img  class = 'bookmark-container' data-bookmarked='false' src = './assets/unbookmarked.svg' alt = 'Bookmark' onclick='toggleBookmark(this, <%= rsid %>)'>
					</button>
				</a>
			</div>
			<%
				int recipe_id;
				try {
					recipe_id = Integer.parseInt(rsid);
				} catch (NumberFormatException e) {
					response.sendRedirect("./index.jsp");
					return;
				}
				String query1 = "SELECT * FROM recipe_summaries rs, recipe_full rf WHERE rs.id = rf.recipe_id AND rs.id = ?";
				String query2 = "SELECT ri.ingredient_id, u.name, ri.amount FROM recipe_ingredients ri, units u WHERE ri.unit_id = u.id AND ri.recipe_id = ?";
				String query3 = "SELECT diets.name FROM diets, recipe_diets WHERE diets.id=recipe_diets.diet_id AND recipe_diets.recipe_id = ?";
				try(Connection con = Database.getConnection();
				PreparedStatement stmt1 = con.prepareStatement(query1);
				PreparedStatement stmt2 = con.prepareStatement(query2);
				PreparedStatement stmt3 = con.prepareStatement(query3);) {
					
					stmt1.setInt(1, recipe_id);
					ResultSet recipe_rs = stmt1.executeQuery();
					
					//second query for ingredients + amounts
					
					stmt2.setInt(1, recipe_id);
					ResultSet ingredients_rs = stmt2.executeQuery();
					String ingredients_html = "";
					String amount = "";
					while (ingredients_rs.next()) {
						amount = ingredients_rs.getString(3);
						ingredients_html += "<tr><td>" + esc.apply(ingredients_rs.getString(1)) + "</td><td>"
						+ esc.apply(amount) + "</td><td>"
						+ esc.apply(ingredients_rs.getString(2)) + "</td></tr>";
					}
					
					stmt3.setInt(1, recipe_id);
					ResultSet diet_rs = stmt3.executeQuery();
					//stringify
					String diet_html = "<h3> Diet Compatibility: </h3> <p> ";
					while(diet_rs.next()){
						diet_html += diet_rs.getString(1) + ", ";
					}
					if (diet_html.endsWith(", "))
					diet_html = diet_html.substring(0, diet_html.length() - 2);
					diet_html += "</p>";
					
					//Should only be one result from query1!
					if (recipe_rs.next()) {
						out.println("<div><h1>" + esc.apply(recipe_rs.getString(2)) + " </h1><div class = 'recipe-metadata'><p> Serving Size: "
						+ esc.apply(recipe_rs.getString(3)) + "</p><p> Prep Time: " + esc.apply(recipe_rs.getString(4)) + "</p><p> Cook Time: "
						+ esc.apply(recipe_rs.getString(5)) + "</p><p> Calories: " + esc.apply(recipe_rs.getString(6)) + "</p></div><div>"
						+ diet_html + "</div>"
						+ "<div class = 'ingredients'><h3> Ingredients </h3> <table>" + ingredients_html
						+ "</table></div><div class = 'instructions'><h3> Steps: </h3><pre> " +esc.apply(recipe_rs.getString(8))
						+ " </pre></div></div>");
						
					} else {
						out.println("<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>");
					}
				} catch (SQLException e) {
					out.println("SQLException caught: " + e.getMessage());
				}
			%>
		</main>
		<script type="text/javascript" src="js/statHandler.js"></script>
	</body>
</html>