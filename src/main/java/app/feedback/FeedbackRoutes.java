package app.feedback;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/feedback/*")
public class FeedbackRoutes extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "";
        }
        switch (path) {
            case "/add" ->
                addMessage(request, response);
            case "/update" ->
                updateMessage(request, response);
            case "/view_all" ->
                viewAllMessages(request, response);
            case "/view_unresolved" ->
                viewUnresolvedMessages(request, response);
            default ->
                response.sendError(404);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "";
        }
        switch (path) {
            case "/categories" ->
                getCategories(request, response);
            default ->
                response.sendError(404);
        }
    }

    protected void addMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FeedbackController.addMessage(request, response);
    }

    protected void updateMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FeedbackController.updateMessageStatus(request, response);
    }

    protected void viewAllMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FeedbackController.viewAllMessages(request, response);
    }

    protected void viewUnresolvedMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FeedbackController.viewUnresolvedMessages(request, response);
    }

    protected void getCategories(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FeedbackController.getCategories(request, response);
    }

}
