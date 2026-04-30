package app.search;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;

@WebServlet("/api/search/")
public class SearchRouter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        UserSession userSession = AuthService.getUserSession(AuthMiddleware.getSessionId(request, response));
            if (userSession == null) {
                SearchService.getGuestResults(request, response);
            }else{
                SearchService.getUserResults(userSession, request, response);
            }
    }
}