<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*"%>
<%@ page import="java.util.stream.Collectors"%>
<%@ page import="app.Constants" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recipes</title>
<link href="reset.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<%
	String[] ingredient = request.getParameterValues("ingredient-input");

	// getParameterValues returns a String[] for multi-value params
	List<String> ingredients = Arrays.stream(ingredient).map(String::trim).filter(s -> !s.isEmpty()) // drop blank inputs
			.collect(Collectors.toList());

	out.println(ingredients);
	%>
	<%
	String categoryId = request.getParameter("food-cat");
	String category = null;

    for (Constants.Option option : Constants.CATEGORIES) {
        if (option.id().equals(categoryId)) {
            category = option.text();
            break;
        }
    }
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
		String password = "n3ttl3h00k";
		try {
			java.sql.Connection con;
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/WhatCanICook?autoReconnect=true&useSSL=false", user,
			password);

			String query = "SELECT * FROM RecipeSummary";
			List<String> params = new ArrayList<>(); // collect all bind values in order

			if (ingredients != null) {
				out.println("Recipes that use " + ingredients);
				//Assuming "ALL" search
				String placeholders = String.join(",", Collections.nCopies(ingredients.size(), "?"));

				query += " WHERE name IN (SELECT recipeName FROM RecipeIngredients WHERE ingredientName IN (" + placeholders
				+ ") GROUP BY recipeName HAVING COUNT(*) = " + ingredients.size() + ")";
				params.addAll(ingredients); // add ingredient placeholders
				if (!category.equals("All")) {
			query += " AND name IN (SELECT DISTINCT recipeName FROM RecipeCategory WHERE categoryName = ?)";
			params.add(category); // add category placeholder
				}
			} else {
				if (!category.equals("All")) {
			query += " WHERE name IN (SELECT DISTINCT recipeName FROM RecipeCategory WHERE categoryName = ?)";
			params.add(category); // add category placeholder
				}
			}

			PreparedStatement stmt = con.prepareStatement(query);
			// Bind each ingredient to its placeholder (PreparedStatement index is 1-based)
			for (int i = 0; i < params.size(); i++) {
				stmt.setString(i + 1, params.get(i));
			}
			out.println("Query: " + stmt + "\n");
			ResultSet rs = stmt.executeQuery();

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