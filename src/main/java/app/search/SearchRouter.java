package app.search;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;


@WebServlet("/api/search/*")
public class SearchRouter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) path = "";
        switch (path) {
            case "/search" -> search(request, response);
            case "/fullRecipe" -> fullRecipe(request, response);
            default -> response.sendError(404);
        }
    }

    protected void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        UserSession userSession = AuthService.getUserSession(AuthMiddleware.getSessionId(request, response));
            if (userSession == null) {
                SearchService.getGuestResults(request, response);
            }else{
                SearchService.getUserResults(userSession, request, response);
            }
    }

    protected void fullRecipe(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rsid = request.getParameter("rsid");
		int recipe_id;
		try {
			recipe_id = Integer.parseInt(rsid);
		} catch (NumberFormatException e) {
			response.sendError(404);
            return;
		}
        UserSession userSession = AuthService.getUserSession(AuthMiddleware.getSessionId(request, response));
        if (userSession == null) {
            SearchService.getFullRecipeGuest(request, response, recipe_id);
        }else{
            System.out.println("Full Recipe search for loggedin user");
            SearchService.getFullRecipeUser(request, response, recipe_id, userSession);
        }
    }
}