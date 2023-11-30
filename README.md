# TheGameAtlas
 Project for CSE 201 by Softlock

Documentation:
- This is an application to view available games offered on different platforms.

How to use the application:
- Open TheGameAtlas.jar in the folder
- View all the available games in the main screen
  + Scroll through the app and click on the thumbnail to view each game.
  + On the top, the user can search using keywords or sort by tags and platforms of each game.
- Log in using the Login button at the top left corner
  + For new users, use the Create Account button to create a new account by filling in the information. Afterwards, fill in the account information to log in.
- After logging in, normal users can request new games to be added by using the request button on top right. Inside, the user can request a game by entering the game name and link to the game.
  + All users can submit requests to add games.
- After logging in, admins can view all requests by going to the Request Page button on the top right corner. Inside, the admin can view a list of requests on the left, with approve or deny buttons at the bottom. After approving a request, the admin can enter information of the game on the form on the right. All information must be entered. For the platforms, the admin needs to enter 3 letters, corresponding to 3 platforms. 
  + Steam|Epic Games|Switch
  + For each platform, enter "t" if it is available, or "f" if it isn't.
  + Example: "tft" means the game is available on Steam and Switch, not Epic Games.

Maintenance:
- App must come with all the Databases files.