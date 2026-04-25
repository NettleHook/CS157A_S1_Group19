package app.api;

public class ApiMessage {
    public static final String INVALID_JSON = "Malformed request body.";
    public static final String UNAUTHORIZED = "You must be logged in to perform this action.";
    
    public static final String DB_ERROR = "A database error occurred.";
    public static final String INTERNAL_ERROR = "An unexpected server error occurred.";
}
