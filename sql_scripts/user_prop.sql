INSERT IGNORE INTO users(username, password) VALUES
('CatCastillo', 'onetwothreeonefive'),
('Nettlehook', 'fivesixhorsesevenseven'),
('fenrickal', 'sndjgm3489lkns');

INSERT IGNORE INTO uploaded_recipes(user_id, recipe_id)
SELECT u.id, r.id FROM users u, recipe_summaries r
WHERE (u.username = 'CatCastillo' AND r.name = 'Not-so-Hot and Sour Soup')
   OR (u.username = 'CatCastillo' AND r.name = 'Rocky Road Brownies')
   OR (u.username = 'Nettlehook'  AND r.name = 'Mabon Brownies')
   OR (u.username = 'fenrickal'   AND r.name = 'Basic Brownies')
   OR (u.username = 'fenrickal'   AND r.name = 'Espresso Brownies')
   OR (u.username = 'fenrickal'   AND r.name = 'Vegan Brownies');

INSERT IGNORE INTO liked_recipes(user_id, recipe_id)
SELECT u.id, r.id FROM users u, recipe_summaries r
WHERE (u.username = 'CatCastillo' AND r.name = 'Mabon Brownies')
   OR (u.username = 'CatCastillo' AND r.name = 'Espresso Brownies')
   OR (u.username = 'Nettlehook'  AND r.name = 'Mabon Brownies')
   OR (u.username = 'Nettlehook'  AND r.name = 'Not-so-Hot and Sour Soup');

INSERT IGNORE INTO bookmarked_recipes(user_id, recipe_id)
SELECT u.id, r.id FROM users u, recipe_summaries r
WHERE (u.username = 'CatCastillo' AND r.name = 'Basic Brownies')
   OR (u.username = 'Nettlehook'  AND r.name = 'Basic Brownies')
   OR (u.username = 'Nettlehook'  AND r.name = 'Not-so-Hot and Sour Soup')
   OR (u.username = 'fenrickal'   AND r.name = 'Not-so-Hot and Sour Soup');