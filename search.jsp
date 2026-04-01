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
	<%
	String dietId = request.getParameter("diet-cat");
	String diet = null;

    for (Constants.Option option : Constants.DIETS) {
        if (option.id().equals(dietId)) {
            diet = option.text();
            break;
        }
    }
	%>
	<%
	String servSize = request.getParameter("serving-size");
	String prepTime = request.getParameter("prep-time-hours")*60 + request.getParameter("prep-time-minutes");
	String cookTime = request.getParameter("cook-time-hours")*60 + request.getParameter("cook-time-minutes");
	String calories = request.getParameter("calories-size");
	
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

			List<String> conditions = new ArrayList<>();
		    List<String> params = new ArrayList<>();

		    if (ingredients != null) {
		        String placeholders = String.join(", ", Collections.nCopies(ingredients.size(), "?"));
		        conditions.add("name IN (SELECT recipeName FROM RecipeIngredients WHERE ingredientName IN ("
		                + placeholders + ") GROUP BY recipeName HAVING COUNT(*) = " + ingredients.size() + ")");
		        params.addAll(ingredients);
		    }

		    if (category != "All") {
		        conditions.add("name IN (SELECT DISTINCT recipeName FROM RecipeCategory WHERE categoryName = ?)");
		        params.add(category);
		    }
		    //convert to using this field for views ? Or only for logged in users

		    if (diet != null) {
		        conditions.add("name IN (SELECT DISTINCT recipeName FROM RecipeDiets WHERE dietName = ?)");
		        params.add(diet);
		    }
		    //Keep this equals to, or at least servSize?
		    if (servSize != null) {
		        conditions.add("ServingSize = ?");
		        params.add(servSize);
		    }
		    if (prepTime != null) {
		        conditions.add("prepTime = ?");
		        params.add(prepTime);
		    }
		    if (cookTime != null) {
		        conditions.add("cookTime = ?");
		        params.add(cookTime);
		    }
		    if (calories != null) {
		        conditions.add("calories <= ?");
		        params.add(calories);
		    }
		    // Build the final query
		    String query = "SELECT * FROM RecipeSummary";
		    if (!conditions.isEmpty()) {
		        query += " WHERE " + String.join(" AND ", conditions);
		    }
		 // Bind each ingredient to its placeholder (PreparedStatement index is 1-based)
		    PreparedStatement stmt = con.prepareStatement(query);
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