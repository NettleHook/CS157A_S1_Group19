package app.object;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Recipe(
    @JsonAlias("user_id") String username,
    String name,
    int servingSize,
    int prepTime,
    int cookTime,
    Integer calories,
    List<String> categories,
    List<String> diets,
    
    @JsonAlias({"fullRecipe", "recipeFull"}) 
    RecipeDetail recipeDetails,
    
    List<Ingredient> ingredients
) {}
