# RandomUsers App

## About
This app fetches random profiles from the free, open-source Random User API (https://randomuser.me/).
It then displays those profiles on the User List screen. Each of these profiles can then be inspected
by clicking on the profile; this brings up the User Details screen.


## Libraries used
- Compose UI
- Coroutines
- Koin
- Room
- Retrofit
- Navigation 3
- JUnit5


## Screens
- User List
- User Details


## Functionality
- Infinite scrolling of random user profiles
- Searching for profiles by name
- Displaying of profile details and photo
- Persistence of fetched profiles across process death
- Option to permanently delete a particular profile
- Deeplinking to both User List and User Details screens