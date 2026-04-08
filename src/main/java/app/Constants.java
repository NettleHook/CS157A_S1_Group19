package app;

public class Constants {
    public record Option(String id, String text) {};

    public static final Option[] CATEGORIES = { 
        new Option("desserts-sweets", "Desserts and Sweets"), 
        new Option("bread", "Bread"), 
        new Option("soups-stews",  "Soups and Stews"), 
        new Option("salads", "Salads"), 
        new Option("dressings-sauces", "Dressings and Sauces"),
        new Option("snacks", "Snacks"),
        new Option("main-dishes", "Main Dishes"),
        new Option("all", "All")
    };

    public static final Option[] DIETS = { 
        new Option("keto", "Keto"),
        new Option("atkins", "Atkins"),
        new Option("paleo", "Paleo"),
        new Option("pescatarian", "Pescatarian"),
        new Option("vegetarian", "Vegetarian"),
        new Option("vegan", "Vegan"),
        new Option("halal", "Halal-friendly"),
        new Option("kosher", "Kosher-friendly")
    };
}