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
	<div style="position: fixed; top: 15px; left: 20px;">
            <a href="index.jsp">
                <button style="cursor: pointer;">
                    &#8592; Home
                </button>
            </a>
        </div>
		<main class = "page">
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
			if(categoryId == null){
				categoryId = "all";
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
			String view = categoryId.equals("all") ? "recipe_summaries" : categoryId + "_recipes";
			String query = "SELECT * FROM " + view;
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
					out.println("<table class = 'results' border='1'>" + "<tr>" + "<th>Recipe Name:</th>" + "<th>Serving Size:</th>"
					+ "<th>Prep Time:</th>" + "<th>Cook Time:</th>" + "<th>Calories:</th>" + "</tr>");
					
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
		</main>
	</body>
</html>