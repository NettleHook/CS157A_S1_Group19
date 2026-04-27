<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*"%>
<%@ page import="java.util.stream.Collectors"%>
<%@ page import="app.Constants"%>
<%@ page import="app.Database" %>

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
			if(ingredient == null) ingredient = new String[0];
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
			String[]  dietIds = request.getParameterValues("diet-cat");
			List<String> diets = new ArrayList<>();
			if (dietIds != null) {
				for (String dietId : dietIds) {
					for (Constants.Option option : Constants.DIETS) {
						if (option.id().equals(dietId)) {
							diets.add(option.text());
							break;
						}
					}
				}
			}
			
			String servSize = request.getParameter("serving-size");
			String prepTime = getTime(request.getParameter("prep-time-hours"), request.getParameter("prep-time-minutes"));
			String cookTime = getTime(request.getParameter("cook-time-hours"), request.getParameter("cook-time-minutes"));
			String calories = request.getParameter("calories");
			
			boolean inclusiveIngredients = !"exclusive".equals(request.getParameter("ingredient-mode"));
			boolean inclusiveDiets = "inclusive".equals(request.getParameter("diet-mode"));
		%>
		<h1>
			Food Category:
			<%=category%>
		</h1>

		<%
			java.util.function.Function<String, String> esc = s ->
			s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
			List<String> conditions = new ArrayList<>();
			List<String> params = new ArrayList<>();
			
			if (ingredients != null && !ingredients.isEmpty()) {
				String placeholders = String.join(", ", Collections.nCopies(ingredients.size(), "?"));
				if(inclusiveIngredients){
					conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_ingredients WHERE ingredient_id IN (" + placeholders
					+ "))");
				}else{
					conditions.add("id IN (SELECT recipe_id FROM recipe_ingredients WHERE ingredient_id IN (" + placeholders
					+ ") GROUP BY recipe_id HAVING COUNT(*) = " + ingredients.size() + ")");
				}
				params.addAll(ingredients);
			}
			
			if (!category.equals("All")) {
				conditions.add(
				"id IN (SELECT DISTINCT recipe_id FROM recipe_categories WHERE category_id = (SELECT id FROM categories WHERE name = ?))");
				params.add(category);
			}
			
			if (diets != null && !diets.isEmpty()) {
				String diet_placeholders = String.join(",", Collections.nCopies(diets.size(), "?"));
				if(inclusiveDiets){
					conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_diets WHERE diet_id IN (SELECT id FROM diets WHERE name IN (" + diet_placeholders + ")))");
					
				}else{
					conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_diets WHERE diet_id IN (SELECT id FROM diets WHERE name IN (" + diet_placeholders + ")) GROUP BY recipe_id HAVING COUNT(*) = " + diets.size() + ")");
				}
				params.addAll(diets);
			}
			if (servSize != null && !servSize.isEmpty()) {
				conditions.add("serving_size >= ?");
				params.add(servSize);
			}
			if (!prepTime.equals("0")) {
				conditions.add("prep_time_min <= ?");
				params.add(prepTime);
			}
			if (!cookTime.equals("0")) {
				conditions.add("cook_time_min <= ?");
				params.add(cookTime);
			}
			if (calories != null && !calories.isEmpty()) {
				conditions.add("calories <= ?");
				params.add(calories);
			}
			// Build the final query
			String query = "SELECT * FROM recipe_summaries";
			if (!conditions.isEmpty()) {
				query += " WHERE " + String.join(" AND ", conditions);
			}
			
			try (Connection con = Database.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);) {
				for (int i = 0; i < params.size(); i++) {
					stmt.setString(i + 1, params.get(i));
				}
				
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					out.println("<table border='1'>" + "<tr>" + "<td>Recipe Name:</td>" + "<td>Serving Size:</td>"
					+ "<td>Prep Time:</td>" + "<td>Cook Time:</td>" + "<td>Calories:</td>" + "</tr>");
					
					do {
						out.println("<tr>" + "<td><a href = './recipe_page.jsp?rsid=" + esc.apply(rs.getString(1)) + "'>" + esc.apply(rs.getString(2)) + "</a></td>" + "<td>" + esc.apply(rs.getString(3)) + " </td>" + "<td>"
						+ esc.apply(rs.getString(4)) + " </td>" + "<td>" + esc.apply(rs.getString(5)) + " </td>" + "<td>" + esc.apply(rs.getString(6))
						+ " </td>" + "</tr>");
					} while (rs.next());
					out.println("</table>");
				} else {
					out.println("<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>");
				}
			} catch (SQLException e) {
				out.println("SQLException caught: " + e.getMessage());
			}
		%>
	</body>
</html>