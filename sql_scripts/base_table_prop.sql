INSERT IGNORE INTO units(name) VALUES
('cup'),
('tsp'),
('thumb'),
('bulb'),
('shot'),
('tbsp'),
('oz'),
('g'),
(''),
('stalk'),
('to taste'),
('pack'),
('small'),
('medium'),
('large');

INSERT INTO categories(name) VALUES
('Desserts and Sweets'),
('Soups and Stews'),
('Bread'),
('Salads'),
('Snacks'),
('Dressings and Sauces'),
('Main Dishes');

INSERT INTO diets(name) VALUES
('Keto'),
('Atkins'),
('Paleo'),
('Pescatarian'),
('Vegetarian'),
('Vegan'),
('Halal-friendly'),
('Kosher-friendly');

INSERT INTO feedback_categories(category) VALUES
('Add Unit'),
('Add Diet'),
('Bug Report'),
('Report Recipe'),
('Other');

INSERT IGNORE INTO users(name, password) VALUES
(dev_cad, $argon2i$v=19$m=65536,t=10,p=1$9sX6+jn9Iw2bfg/X2H/rVA$UfbGHk+VR3AT2emoGuTQw80Qf+K1qmDjGaTsaGWFfxo);