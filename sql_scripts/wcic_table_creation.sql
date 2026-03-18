CREATE TABLE guests (
    session_id VARCHAR(50) PRIMARY KEY
);

CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE ingredients (
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE recipe_summaries (
    name VARCHAR(50) PRIMARY KEY,
    serving_size VARCHAR(50),
    prep_time_min INT,
    cook_time_min INT,
    calories INT
);

CREATE TABLE recipe_full (
    recipe_name VARCHAR(50) PRIMARY KEY,
    ingredient_list VARCHAR(1000)  NOT NULL,
    steps VARCHAR(10000)  NOT NULL
);

CREATE TABLE categories (
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE diets (
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE recipe_categories (
    recipe_name VARCHAR(50),
    category_name VARCHAR(50),
    PRIMARY KEY (recipe_name , category_name)
);

CREATE TABLE recipe_diets (
    recipe_name VARCHAR(50),
    diet_name VARCHAR(50),
    PRIMARY KEY (recipe_name , diet_name)
);

CREATE TABLE recipe_ingredients (
    recipe_name VARCHAR(50),
    ingredient_name VARCHAR(50),
    amounts VARCHAR(50)  NOT NULL,
    PRIMARY KEY (recipe_name , ingredient_name)
);

CREATE TABLE liked_recipes (
    user_id VARCHAR(50),
    recipe_name VARCHAR(50),
    PRIMARY KEY (user_id , recipe_name)
);

CREATE TABLE bookmarked_recipes (
    user_id VARCHAR(50),
    recipe_name VARCHAR(50),
    PRIMARY KEY (user_id , recipe_name)
);

CREATE TABLE uploaded_recipes (
    user_id VARCHAR(50),
    recipe_name VARCHAR(50),
    PRIMARY KEY (user_id , recipe_name)
);

CREATE TABLE user_diets (
    user_id VARCHAR(50),
    diet VARCHAR(50),
    PRIMARY KEY (user_id , diet)
);

CREATE TABLE user_ingredient_lists (
    user_id VARCHAR(50),
    ingredient_name VARCHAR(50),
    amount VARCHAR(50),
    PRIMARY KEY (user_id , ingredient_name)
);

CREATE TABLE guest_ingredient_lists (
    session_id VARCHAR(50),
    ingredient_name VARCHAR(50),
    amount VARCHAR(50),
    PRIMARY KEY (session_id , ingredient_name)
);
