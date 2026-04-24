package app.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import app.utils.JsonUtils;

public class ApiResponse {
    public static void write(HttpServletResponse res, Object data) throws StreamWriteException, DatabindException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        
        JsonUtils.MAPPER.writeValue(res.getWriter(), response);
    }
}
