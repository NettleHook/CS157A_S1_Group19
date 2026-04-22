# What Can I Cook?

A recipe search app!

### Currently implemented:
#### Search and results:
- Search by ingredients
- Search by food category
- search by diet
- search by serving size
- search by calorie cap
- search by cooking time + prep time
- hour-minute conversion

#### Full Recipe Page
- new page featuring full recipe information

#### Login System


### To be implemented:
#### Search and results:
- If user has a registered diet(s), results are automatically filtered
  
#### Recipe Uploading
- Only available to logged in users
- Recipe is automatically added to user's uploaded recipe's list
- Regex filtering to prevent injection attacks
- Comparisons with an ingredient list to verify input (string/array comparisons)
- Sanitize user-entered field

#### Ingredient Tracker
- Users and Guests can keep a list of ingredients, this could be things they usually have or what is currently in their fridge
- Guests ingredient tracker is tied to their session, so will only be accessible temporarily
- Ingredient list can be pulled up in Full Recipe page for easy comparison
  
#### Liking and Bookmarking Recipes
- Users can bookmark recipes they want to save for later. These can later be accessed through the bookmarks page in the account profile
- Users can like recipes. Liked recipes can later be accessed through the Liked Recipes page in the account profile
- Both bookmarks and liking recipes can be used to quickly access recipes
  
#### User Profile:
Make sure user profile grants access to the following:
- Bookmark List
- Liked Recipes List
- My Recipes List
- Saved Ingredients List


