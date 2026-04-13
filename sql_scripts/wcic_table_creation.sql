CREATE TABLE guests (
    session_id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE ingredients (
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE recipe_summaries (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    serving_size INT,
    prep_time_min INT,
    cook_time_min INT,
    calories INT,
    PRIMARY KEY (id)
);

CREATE TABLE recipe_full (
    recipe_id INT NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (recipe_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe_summaries(id) ON DELETE CASCADE
);

CREATE TABLE categories (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE diets (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE units (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE recipe_categories (
    recipe_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (recipe_id, category_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe_summaries(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE recipe_diets (
    recipe_id INT NOT NULL,
    diet_id INT NOT NULL,
    PRIMARY KEY (recipe_id, diet_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe_summaries(id),
    FOREIGN KEY (diet_id) REFERENCES diets(id)
);

CREATE TABLE recipe_ingredients (
    recipe_id INT NOT NULL,
    ingredient_id VARCHAR(255) NOT NULL,
    unit_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (recipe_id, ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe_summaries(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(name),
    FOREIGN KEY (unit_id) REFERENCES units(id)
);

CREATE TABLE liked_recipes (
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    PRIMARY KEY (user_id, recipe_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe_summaries(id) ON DELETE CASCADE
);

CREATE TABLE bookmarked_recipes (
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    PRIMARY KEY (user_id, recipe_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe_summaries(id) ON DELETE CASCADE
);

CREATE TABLE uploaded_recipes (
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    PRIMARY KEY (user_id, recipe_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe_summaries(id) ON DELETE CASCADE
);

CREATE TABLE user_diets (
    user_id INT NOT NULL,
    diet_id INT NOT NULL,
    PRIMARY KEY (user_id, diet_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (diet_id) REFERENCES diets(id)
);

CREATE TABLE user_ingredient_lists (
    user_id INT NOT NULL,
    ingredient_id VARCHAR(255) NOT NULL,
    unit_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (user_id , ingredient_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(name),
    FOREIGN KEY (unit_id) REFERENCES units(id)
);

CREATE TABLE guest_ingredient_lists (
    session_id VARCHAR(255) NOT NULL,
    ingredient_id VARCHAR(255) NOT NULL,
    unit_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (session_id, ingredient_id),
    FOREIGN KEY (session_id) REFERENCES guests(session_id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(name),
    FOREIGN KEY (unit_id) REFERENCES units(id)
);
