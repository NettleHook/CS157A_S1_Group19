<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recipes</title>
</head>
<body>
	<table border="1">
		<tr>
			<td>Recipe Name</td>
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

			out.println(db + " database successfully opened.<br/><br/>");

			out.println("Initial test in fetch from \"Recipes\": <br/>");

			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM RecipeSummary");

			while (rs.next()) {
				out.println("<tr>" + "<td>" + rs.getString(1) + " </td>" + "</tr>");
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