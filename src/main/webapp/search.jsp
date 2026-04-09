<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recipes</title>
<link href="reset.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<%
	String ingredient = request.getParameter("ingredient-input");
	%>
	<%
	String category = request.getParameter("food-cat");
	%>
	<h1>
		Food Category:
		<%=category%>
	</h1>

	<table border="1">
		<tr>
			<td>Recipe Name:</td>
			<td>Serving Size:</td>
			<td>Prep Time:</td>
			<td>Cook Time:</td>
			<td>Calories:</td>
		</tr>
		<%
		String db = "CS157A";
		String user; // assumes database name is the same as username
		user = "root";
		String password = "password";
		try {
			java.sql.Connection con;
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/WhatCanICook?autoReconnect=true&useSSL=false", user,
			password);

			Statement stmt = con.createStatement();
			String query = "SELECT * FROM RecipeSummary";
			if (!ingredient.equals("")) {
				out.println("Recipes that use " + ingredient);
				query = query + " WHERE name IN (SELECT DISTINCT recipeName FROM RecipeIngredients WHERE ingredientName = '"
				+ ingredient + "')";
				if(!category.equals("all")){
					query = query + " AND name IN (SELECT DISTINCT recipeName FROM RecipeCategory WHERE categoryName = '"
							+ category + "')";
				}
			}
			else{
				if(!category.equals("all")){
					query = query + " WHERE name IN (SELECT DISTINCT recipeName FROM RecipeCategory WHERE categoryName = '"
							+ category + "')";
				}
			}
			//out.println("Query: " + query + "\n");
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				out.println("<tr>" + "<td>" + rs.getString(1) + " </td>" + "<td>" + rs.getString(2) + " </td>" + "<td>"
				+ rs.getString(3) + " </td>" + "<td>" + rs.getString(4) + " </td>" + "<td>" + rs.getString(5) + " </td>"
				+ "</tr>");
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			out.println("SQLException caught: " + e.getMessage());
		}
		%>
	</table>
</body>
</html>