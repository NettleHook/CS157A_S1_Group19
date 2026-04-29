# What Can I Cook?

A recipe search app!

### Currently implemented:
#### Search and results:
- Search by ingredients
- Search by food category
  - Now searching with views
- search by diet(s)
- search by serving size
- search by calorie cap
- search by cooking time + prep time
- hour-minute conversion
- Added checkboxes to switch between searching for any or all ingredients or diets

#### Full Recipe Page
- new page featuring full recipe information

#### Login System
- Passwords are encrypted
- Session-based authentication

#### Recipe Uploading
- Only available to logged in users
- Recipe is automatically added to user's uploaded recipe's list
- Comparisons with a unit list to verify input
- New ingredients are added to ingredients table
- Sanitize user-entered field
  - Regex filtering to prevent injection attacks

#### Views:
- Views are created based on food category
- When searching, if a food category is selected, search is done with the view
- When a recipe is uploaded, the view for the food category it belongs to is updated

#### User Profile:
Make sure user profile grants access to the following:
- Bookmark List
- Liked Recipes List
- My Recipes List
- Saved Ingredients List

#### Liking and Bookmarking Recipes
- Backend support for bookmarking has been added

-----
### To be implemented:
#### Search and results:
- If user has a registered diet(s), results are automatically filtered

#### Ingredient Tracker
- Users and Guests can keep a list of ingredients, this could be things they usually have or what is currently in their fridge
- Guests ingredient tracker is tied to their session, so will only be accessible temporarily
- Ingredient list can be pulled up in Full Recipe page for easy comparison
  
#### Liking and Bookmarking Recipes
- Users can bookmark recipes they want to save for later. These can later be accessed through the bookmarks page in the account profile
- Users can like recipes. Liked recipes can later be accessed through the Liked Recipes page in the account profile
- Both bookmarks and liking recipes can be used to quickly access recipes
  


