CREATE TABLE Guests (
    sessionID VARCHAR(50) PRIMARY KEY
);
CREATE TABLE Users (
    userID VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL
);
CREATE TABLE Ingredients (
    name VARCHAR(50) PRIMARY KEY
);
CREATE TABLE RecipeSummary (
    name VARCHAR(50) PRIMARY KEY,
    ServingSize VARCHAR(50),
    prepTime INT,
    cookTime INT,
    calories INT
);
CREATE TABLE FullRecipe (
    recipeName VARCHAR(50) PRIMARY KEY,
    IngredientList VARCHAR(1000)  NOT NULL,
    Steps VARCHAR(10000)  NOT NULL
);
CREATE TABLE Category (
    name VARCHAR(50) PRIMARY KEY
);
CREATE TABLE Diet (
    name VARCHAR(50) PRIMARY KEY
);
CREATE TABLE RecipeCategory (
    recipeName VARCHAR(50),
    categoryName VARCHAR(50),
    PRIMARY KEY (recipeName , categoryName)
);
CREATE TABLE RecipeDiet (
    recipeName VARCHAR(50),
    dietName VARCHAR(50),
    PRIMARY KEY (recipeName , dietName)
);
CREATE TABLE RecipeIngredients (
    recipeName VARCHAR(50),
    ingredientName VARCHAR(50),
    amounts VARCHAR(50)  NOT NULL,
    PRIMARY KEY (recipeName , ingredientName)
);
CREATE TABLE LikedRecipes (
    userId VARCHAR(50),
    recipeName VARCHAR(50),
    PRIMARY KEY (userId , recipeName)
);
CREATE TABLE BookmarkedRecipes (
    userId VARCHAR(50),
    recipeName VARCHAR(50),
    PRIMARY KEY (userId , recipeName)
);
CREATE TABLE UploadedRecipes (
    userId VARCHAR(50),
    recipeName VARCHAR(50),
    PRIMARY KEY (userId , recipeName)
);
CREATE TABLE UserDiet (
    userId VARCHAR(50),
    diet VARCHAR(50),
    PRIMARY KEY (userId , diet)
);
CREATE TABLE UserIngredientList (
    userID VARCHAR(50),
    ingredientName VARCHAR(50),
    amount VARCHAR(50),
    PRIMARY KEY (userID , ingredientName)
);
CREATE TABLE GuestIngredientList (
    sessionID VARCHAR(50),
    ingredientName VARCHAR(50),
    amount VARCHAR(50),
    PRIMARY KEY (sessionID , ingredientName)
);