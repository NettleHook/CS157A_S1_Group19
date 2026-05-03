package app.user.diets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/diets/*")
public class DietsRouter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        if (path == null) return;
        
        switch (path) {
            case "/register" -> DietsController.registerDiet(req, res);
        }

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        if (path == null) return;
        switch (path) {
            case "/get_diets" -> DietsController.getDiets(req, res);
        }
        
    }
}