<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recipes</title>
<link href="reset.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<%
	String recipe_id = request.getParameter("rsid");

	if (recipe_id == null || recipe_id.isEmpty()) {
		//User shouldn't be here--redirect to home page
		response.sendRedirect("./index.jsp");
	}
	String db = "CS157A";
	String user; // assumes database name is the same as username
	user = "root";
	String password = "n3ttl3h00k";
	try {
		java.sql.Connection con;
		Class.forName("com.mysql.jdbc.Driver");

		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/WCIC?autoReconnect=true&useSSL=false", user,
		password);

		String query1 = "SELECT * FROM recipe_summaries rs, recipe_full rf WHERE rs.id = rf.recipe_id AND rs.id = ?";
		PreparedStatement stmt1 = con.prepareStatement(query1);
		stmt1.setString(1, recipe_id);
		ResultSet recipe_rs = stmt1.executeQuery();

		//second query for ingredients + amounts?
		String query2 = "SELECT ri.ingredient_id, u.name, ri.amount FROM recipe_ingredients ri, units u WHERE ri.unit_id = u.id AND ri.recipe_id = ?";
		PreparedStatement stmt2 = con.prepareStatement(query2);
		stmt2.setString(1, recipe_id);
		ResultSet ingredients_rs = stmt2.executeQuery();
		String ingredients_html = "";
		while (ingredients_rs.next()) {
			ingredients_html += "<tr><td>" + ingredients_rs.getString(3) + "</td><td>" + ingredients_rs.getString(2)
			+ "</td><td>" + ingredients_rs.getString(1) + "</td></tr>";
		}
		//Should only be one result from query1!
		if (recipe_rs.next()) {
			out.println("<div><h1>" + recipe_rs.getString(2) + " </h1><div class = 'recipe-metadata'><p> Serving Size: "
			+ recipe_rs.getString(3) + "</p><p> Prep Time: " + recipe_rs.getString(4) + "</p><p> Cook Time: "
			+ recipe_rs.getString(5) + "</p><p> Calories: " + recipe_rs.getString(6) + "</p></div>"
			+ "<div class = 'ingredients'><h3> Ingredients </h3> <table>" + ingredients_html
			+ "</table></div><div class = 'instructions'><h3> Steps: </h3><pre> " + recipe_rs.getString(8)
			+ " </pre></div></div>");

		} else {
			out.println("<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>");
		}
		recipe_rs.close();
		stmt1.close();
		con.close();
	} catch (SQLException e) {
		out.println("SQLException caught: " + e.getMessage());
	}
	%>
</body>
</html>