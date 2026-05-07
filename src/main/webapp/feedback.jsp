<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <link href="styles/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
                <form id="feedback">
                    <select id="feedback_category" name="feedback_category" placeholder="What type of feedback?" required></select>
                    <label for="message">What would you like to tell us?</label>
                    <input type="text" name = "message" required>
                    <label for="contact_info">Please provide your contact info if you'd like us to respond: </label>
                    <input type="text" name = "contact_info" placeholder = "Optional"/>
                    <input type="submit" value = "Submit" onclick="addMessage(event)">
                </form>
        <script src="js/feedback.js"></script>
    </body>
</html>
