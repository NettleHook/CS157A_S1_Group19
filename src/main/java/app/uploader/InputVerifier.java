package app.uploader;
import app.Constants;
import app.object.Ingredient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputVerifier{
    private static final String[] UNITS = {"bulb", "cup", "pack", "shot", "oz", "g", "stalk", "tbsp", "tsp", "to taste", "thumb"};
    public static List<Object> verify_summary(
        String recipe_name,
        String serving_size,
        String prep_hours,
        String prep_min,
        String cook_hours,
        String cook_min,
        String calories
    )  {
        // Validate recipe_name
        recipe_name = parseName(recipe_name, "Recipe Name");
        Integer servingSize = parseOptionalInt(serving_size);
        int prep_time = getTime(parseOptionalInt(prep_hours), parseOptionalInt(prep_min));
        int cook_time = getTime(parseOptionalInt(cook_hours), parseOptionalInt(cook_min));
        Integer caloriesInt = parseOptionalInt(calories);

        return Arrays.asList(recipe_name, servingSize, prep_time, cook_time, caloriesInt);
    } 
    public static List<Ingredient> verify_ingredients(String [] ingredient_names, String [] ingredient_amts, String [] ingredient_units){
	    if(ingredient_names.length != ingredient_amts.length || ingredient_names.length != ingredient_units.length){
            throw new IllegalArgumentException("Names, Amounts, and Units do not match. Something is mising.");
        }
        List<Ingredient> ingredients = new ArrayList<>();
        for( int i = 0; i < ingredient_names.length; i++){
            String name = parseName(ingredient_names[i], "Ingredient "+(i+1) + " name");
            String unit = verifyUnit(ingredient_units[i], name);
            Double amt = unit.equals("to taste") ? null : parseOptionalDouble(ingredient_amts[i]);
            ingredients.add(new Ingredient(name, amt, unit));
        }
        return ingredients;
    }
    public static String verify_category(String categoryId){
        String category = null;
        if (categoryId == null) {
            category = "All";
        } else {

            for (Constants.Option option : Constants.CATEGORIES) {
                if (option.id().equals(categoryId)) {
            category = option.text();
            break;
                }
            }
        }
        return category;
    }
    public static List<String> verify_diets(String[] dietIds){
        List<String> diets = new ArrayList<>();
        if (dietIds != null) {
            for (String dietId : dietIds) {
                for (Constants.Option option : Constants.DIETS) {
                    if (option.id().equals(dietId)) {
                        diets.add(option.text());
                        break;
                    }
                }
            }
        }
        return diets;
    }
    public static String verify_steps(String [] steps){
        String description = "";
        for(int i = 0; i< steps.length; i++){
            description = description + (i+1) + ". " + parseStep(steps[i]) + "\n";
        }
        return description;
    }
    private static String parseStep(String value) {
        Pattern STEP_PATTERN = Pattern.compile("^[^<>&\"\\\\`]+$");
        if (value == null || value.isEmpty()) {
            return "";
        }
        Matcher stepMatcher = STEP_PATTERN.matcher(value);
        if (!stepMatcher.matches()) {
            throw new IllegalArgumentException("At least one step contains invalid characters: " + value);
        }
        return value;
    }
    private static String parseName(String name, String field){
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(field + " is required");
        }
        Pattern namePattern = Pattern.compile("^[a-zA-Z0-9 .,!?':;()-]+$");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            throw new IllegalArgumentException(field + " contains invalid characters: " + name);
        }
    return name;

    }
    private static Integer parseOptionalInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 0) {
                parsed = 0;
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected a number but got: " + value);
        }
    }
    private static Double parseOptionalDouble(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            double parsed = Double.parseDouble(value);
            if (parsed < 0) {
                throw new IllegalArgumentException("Amount must be at least 0");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected a number but got: " + value);
        }
    }
    private static String verifyUnit(String unit, String ingredient_name){
        if(unit == null || unit.equals("N/A")){
            return "";
        }
        if (!Arrays.asList(UNITS).contains(unit)) {
            throw new IllegalArgumentException("Ingredient " + ingredient_name + " has invalid unit: " + unit);
        }
        return unit;
    }
    
    private static int getTime(Integer hour, Integer minute) {
		int minutes = 0;
		if (hour != null ) {
			minutes = hour * 60;
		}
		if (minute != null) {
			minutes += minute;
		}
		return minutes;
	}
}



