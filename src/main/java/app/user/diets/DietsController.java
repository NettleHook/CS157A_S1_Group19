package app.user.diets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;

import app.Constants;
import app.api.ApiMessage;
import app.api.ApiResponse;
import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;
import app.utils.JsonUtils;

public class DietsController {

    public static void registerDiet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);
        if (userSession == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }
        JsonNode body = JsonUtils.MAPPER.readTree(req.getReader());
        List<String> dietIds = new ArrayList<>();
        List<String> diets = new ArrayList<>();
        if (body.has("diets")) {
            body.get("diets").forEach(e -> dietIds.add(e.asText()));
        }
        for (String dietId : dietIds) {
            for (Constants.Option option : Constants.DIETS) {
                if (option.id().equals(dietId)) {
                    diets.add(option.text());
                    break;
                }
            }
        }
        try {
            DietsService.registerDiets(userSession, diets);
            ApiResponse.write(res, 200, new HashMap<>());
        } catch (SQLException e) {
            ApiResponse.error(res, 400, e.getMessage());
        }

    }

    public static void getDiets(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, String>> allDiets = new ArrayList<>();
        for (Constants.Option option : Constants.DIETS) {
            Map<String, String> diet = new HashMap<>();
            diet.put("id", option.id());
            diet.put("text", option.text());
            allDiets.add(diet);
        }
        data.put("allDiets", allDiets);
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);
        if (userSession == null) {
            ApiResponse.write(res, 401, data);
            return;
        }
        try {
            List<String> user_list = DietsService.getDiets(userSession);
            data.put("userDiets", user_list);
            ApiResponse.write(res, 200, data);
        } catch (SQLException e) {
            ApiResponse.write(res, 500, data);
        }
    }
}
