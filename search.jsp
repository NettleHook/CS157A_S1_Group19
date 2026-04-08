<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*"%>
<%@ page import="java.util.stream.Collectors"%>
<%@ page import="app.Constants"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recipes</title>
<link href="reset.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<%!private String getTime(String hour, String minute) {
		int minutes = 0;
		if (hour != null && !hour.isEmpty()) {
			minutes = Integer.parseInt(hour) * 60;
		}
		if (minute != null && !minute.isEmpty()) {
			minutes += Integer.parseInt(minute);
		}
		return minutes + "";
	}%>
	<%
	String[] ingredient = request.getParameterValues("ingredient-input");

	// getParameterValues returns a String[] for multi-value params
	List<String> ingredients = Arrays.stream(ingredient).map(String::trim).filter(s -> !s.isEmpty()) // drop blank inputs
			.collect(Collectors.toList());

	String categoryId = request.getParameter("food-cat");
	String category = null;

	if (categoryId == null) {
		category = "All";
	} else {

		for (Constants.Option option : Constants.CATEGORIES) {
			if (option.id().equals(categoryId)) {
		category = option.text();
		break;
			}
		}
	}
	String dietId = request.getParameter("diet-cat");
	String diet = null;

	for (Constants.Option option : Constants.DIETS) {
		if (option.id().equals(dietId)) {
			diet = option.text();
			break;
		}
	}
	String servSize = request.getParameter("serving-size");
	String prepTime = getTime(request.getParameter("prep-time-hours"), request.getParameter("prep-time-minutes"));
	String cookTime = getTime(request.getParameter("cook-time-hours"), request.getParameter("cook-time-minutes"));
	String calories = request.getParameter("calories");
	%>
	<h1>
		Food Category:
		<%=category%>
	</h1>

	<%
	String db = "CS157A";
		String user; // assumes database name is the same as username
		user = "root";
		String password = "n3ttl3h00k";
		try {
			java.sql.Connection con;
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/WCIC?autoReconnect=true&useSSL=false", user,
			password);

			List<String> conditions = new ArrayList<>();
			List<String> params = new ArrayList<>();

			if (ingredients != null && !ingredients.isEmpty()) {
		String placeholders = String.join(", ", Collections.nCopies(ingredients.size(), "?"));
		conditions.add("id IN (SELECT recipe_id FROM recipe_ingredients WHERE ingredient_id IN (" + placeholders
		+ ") GROUP BY recipe_id HAVING COUNT(*) = " + ingredients.size() + ")");
		params.addAll(ingredients);
			}

			if (category != "All") {
		conditions.add(
		"id IN (SELECT DISTINCT recipe_id FROM recipe_categories WHERE category_id = (SELECT id FROM categories WHERE name = ?))");
		params.add(category);
			}
			//convert to using this field for views ? Or only for logged in users

			if (diet != null) {
		conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_diets WHERE dietName = ?)");
		params.add(diet);
			}
			//Keep this equals to, or at least servSize?
			if (!servSize.isEmpty() && servSize != null) {
		conditions.add("serving_size = ?");
		params.add(servSize);
			}
			if (!prepTime.equals("0")) {
		conditions.add("prep_time_min = ?");
		params.add(prepTime);
			}
			if (!cookTime.equals("0")) {
		conditions.add("cook_time_min = ?");
		params.add(cookTime);
			}
			if (!calories.isEmpty() && calories != null) {
		conditions.add("calories <= ?");
		params.add(calories);
			}
			// Build the final query
			String query = "SELECT * FROM recipe_summaries";
			if (!conditions.isEmpty()) {
		query += " WHERE " + String.join(" AND ", conditions);
			}
			// Bind each ingredient to its placeholder (PreparedStatement index is 1-based)
			PreparedStatement stmt = con.prepareStatement(query);
			for (int i = 0; i < params.size(); i++) {
		stmt.setString(i + 1, params.get(i));
			}

			//out.println("Query: " + stmt + "\n");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
		out.println("<table border='1'>" + "<tr>" + "<td>Recipe Name:</td>" + "<td>Serving Size:</td>"
				+ "<td>Prep Time:</td>" + "<td>Cook Time:</td>" + "<td>Calories:</td>" + "</tr>");

		do {
			out.println("<tr>" + "<td><a href = './recipe_page.jsp?rsid=" + rs.getString(1) + "'>" + rs.getString(2) + "</a></td>" + "<td>" + rs.getString(3) + " </td>" + "<td>"
					+ rs.getString(4) + " </td>" + "<td>" + rs.getString(5) + " </td>" + "<td>" + rs.getString(6)
					+ " </td>" + "</tr>");
		} while (rs.next());
		out.println("</table>");
			} else {
		out.println("<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>");
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			out.println("SQLException caught: " + e.getMessage());
		}
	%>
</body>
</html>