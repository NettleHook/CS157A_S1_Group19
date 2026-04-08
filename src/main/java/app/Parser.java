package app;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.object.Recipe;
import app.object.User;

public class Parser {
    public static List<User> parseUsers() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("json/users.json");
        List<User> users = mapper.readValue(jsonFile, new TypeReference<List<User>>(){});
        
        return users;
    }

    public static List<Recipe> parseRecipes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("json/recipes.json");
        List<Recipe> recipes = mapper.readValue(jsonFile, new TypeReference<List<Recipe>>(){});
        
        return recipes;
    }
}
