INSERT INTO recipe_summaries(name, serving_size, prep_time_min, cook_time_min, calories) VALUES
('Korean Style Popcorn Chicken', 3, 40, 25, 758),
('Tteokbokki', 2, 10, 10, 381),
('Jajangmyeon', 6, 20, 30, 522);

INSERT INTO recipe_full(recipe_id, description) VALUES
(8, '1. Place chicken pieces in a bowl. Add rice wine, ginger powder, salt, and black pepper. Mix well, cover, and marinate in the fridge for 30 mins. \n 2. Coat each chicken piece thoroughly with potato starch. \n 3. Heat cooking oil in a deep saucepan to 175C/347F. Deep fry rice cakes in batches for under 10 seconds until outer layer is crispy. Set aside on paper towels. \n 4. Deep fry battered chicken in batches for 2-3 mins until golden and cooked through. Set aside on paper towels. For extra crunch, double fry the chicken. \n 5. Mix ketchup, gochujang, honey, brown sugar, soy sauce, sesame oil, and minced garlic in a bowl. \n 6. Pour sauce into a heated skillet and bring to boil on medium heat for 1-2 mins, stirring constantly, until slightly thickened. \n 7. Add fried chicken and rice cakes and coat quickly and thoroughly with sauce. Serve immediately.'),
(9, '1. Soak rice cakes in warm water for 10 minutes. \n 2. Mix gochujang, sugar, soy sauce, minced garlic, and gochugaru in a bowl to make the sauce. \n 3. Boil soup stock in a shallow pot over medium high heat. Dissolve the sauce into the stock. \n 4. Once boiling, add rice cakes, fish cakes, and sliced onion. Boil for 3-5 mins until rice cakes are fully cooked. \n 5. Simmer on low heat for a further 2-4 mins to thicken the sauce. \n 6. Add sesame oil, sesame seeds, and chopped green onion. Stir quickly and serve warm.'),
(10, '1. Rinse pork and pat dry. Mix with rice wine, salt, black pepper, and ginger powder. Marinate for 15 mins. \n 2. Cut onion, zucchini, and potato into small cubes. Thin slice mushrooms. Cut cabbage into small pieces. Julienne cut cucumber and set aside. \n 3. Heat wok and add lard. Add black bean paste and stir on medium heat for 3-5 mins. Add brown sugar and stir for 2-3 more mins. Scoop out paste and set aside. \n 4. Add pork to wok and stir until half cooked. Add onion, zucchini, and potato and stir for 3-5 mins. Add mushrooms and cabbage and stir for 2-3 mins. \n 5. Add black bean paste back in and mix. Add chicken stock and rice wine and simmer for 5-7 mins. \n 6. Mix potato starch with water and stir into wok to thicken sauce. \n 7. Boil noodles for 3-5 mins. Rinse in cold water and drain. \n 8. Pour black bean sauce over noodles. Top with cucumber. Serve immediately.');

INSERT INTO recipe_ingredients(recipe_id, ingredient_id, unit_id, amount) VALUES
(8, 'chicken thigh', 7, NULL),
(8, 'Korean rice cake', 7, NULL),
(8, 'rice wine', 6, 1),
(8, 'ginger powder', 2, 2),
(8, 'salt', 2, 0.5),
(8, 'black pepper', 2, 0.25),
(8, 'potato starch', 1, 1),
(8, 'cooking oil', 7, NULL),
(8, 'ketchup', 6, 5),
(8, 'gochujang', 6, 1.5),
(8, 'honey', 6, 2),
(8, 'brown sugar', 6, 2),
(8, 'soy sauce', 6, 1),
(8, 'sesame oil', 2, 2),
(8, 'garlic', 2, 0.5),
(9, 'Korean rice cake', 7, NULL),
(9, 'Korean fish cake', 7, NULL),
(9, 'Korean soup stock', 1, 2),
(9, 'onion', 7, NULL),
(9, 'gochujang', 6, 3),
(9, 'sugar', 6, 1.5),
(9, 'soy sauce', 6, 1),
(9, 'garlic', 2, 1),
(9, 'gochugaru', 2, 1),
(9, 'sesame seeds', 2, 1),
(9, 'sesame oil', 2, 1),
(9, 'green onion', 8, 1),
(10, 'jajangmyeon noodles', 7, NULL),
(10, 'onion', 7, NULL),
(10, 'zucchini', 7, NULL),
(10, 'potato', 7, NULL),
(10, 'button mushrooms', 7, NULL),
(10, 'cabbage', 7, NULL),
(10, 'pork', 7, NULL),
(10, 'rice wine', 6, 5),
(10, 'salt', 9, 0),
(10, 'black pepper', 9, 0),
(10, 'ginger powder', 2, 0.25),
(10, 'Korean black bean paste', 6, 6),
(10, 'lard', 7, NULL),
(10, 'brown sugar', 6, 2),
(10, 'chicken stock', 1, 1),
(10, 'potato starch', 6, 5),
(10, 'cucumber', 7, NULL);

INSERT INTO recipe_categories(recipe_id, category_id) VALUES
(8, 5),
(9, 5),
(10, 7);

INSERT INTO recipe_diets(recipe_id, diet_id) VALUES
(8, 7),
(9, 7);