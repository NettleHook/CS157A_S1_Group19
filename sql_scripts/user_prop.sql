INSERT INTO Users(userID, password) VALUES
('CatCastillo', 'onetwothreeonefive'),
('Nettlehook', 'fivesixhorsesevenseven'),
('fenrickal', 'sndjgm3489lkns');

INSERT INTO UploadedRecipes(userId, recipeName) VALUES
('CatCastillo', 'Not-so-Hot and Sour Soup'),
('CatCastillo', 'Rocky Road Brownies'),
('Nettlehook', 'Mabon Brownies'),
('fenrickal', 'Basic Brownies'),
('fenrickal', 'Espresso Brownies'),
('fenrickal', 'Vegan Brownies');

INSERT INTO LikedRecipes(userId, recipeName) VALUES
('CatCastillo', 'Mabon Brownies'),
('CatCastillo', 'Espresso Brownies'),
('Nettlehook', 'Mabon Brownies'),
('Nettlehook', 'Not-so-Hot and Sour Soup');

INSERT INTO BookmarkedRecipes(userId, recipeName) VALUES
('CatCastillo', 'Basic Brownies'),
('Nettlehook', 'Basic Brownies'),
('Nettlehook', 'Not-so-Hot and Sour Soup'),
('fenrickal', 'Not-so-Hot and Sour Soup');