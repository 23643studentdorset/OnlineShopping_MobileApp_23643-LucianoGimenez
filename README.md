# OnlineShopping_MobileApp_23643-LucianoGimenez

This repo contains the Final project Mobile App of Dorset Mobile Applications 1 Course for BSc Year 2 

## Online Shopping App
Make an online shopping app consuming JSON API hosted at https://fakestoreapi.com fulfilling the following requirements. 

You are allowed to make your own decisions on implementing the requirements, such as deciding layouts, data types, etc. 
Use common assumptions based on the real-world online shopping apps you have used e.g Cart total should have float values rounded to 2 decimals; Product quantity in cart can't be zero or negative. 
In case you could not decide between two design choices, check the best practices and see what's recommended. 
Log important design decisions in README.md

___


## Requirements:
### Authentication
- [x] Allow User to Signup
- [x] Log In using username and password
- [x] Store userID and token once logged in, to keep the user logged in (even after restarting the app)
### Product Listing
- [x] List Product Categories
- [x] On clicking a Category, list Products in that Category
- [x] On clicking a Product, show Product description, show buy button and controls to change quantity.
### Cart
- [x] Show cart summary
- [ ] Show total amount
- [x] Purchase button to place an order, show order notification
### Show order history
- [x] List users orders
- [x] On clicking an Order, show Order details and Products ordered
- [x] On clicking a Product, take them to Product description page created for 3.3
### Show User details
- [x] Use the stored userID to show user details
- [x] Show a random circular profile image from https://thispersondoesnotexist.com/
- [x] Show Logout button, on click take back to Signup / Log In page (Restart should not auto login after logout)
### UI/Implementational Requirements
- [x] RecyclerView used for all Lists: Categories, Products, Orders
- [x] If logged in, attach authentication token to all requests until logout
- [x] Add a small "About this app" button in the profile page, that shows a page on click with your copyright details and credits
### Bonus
- [ ] ViewPager2 with bottom TabLayout for: Shop, Cart, Orders, Profile icons
- [ ] Show a map marker based on the GPS co-ordinates in user profile (Hint: Follow instructions given here)

___

For this assessment we were asked to create a Mobile App for an online shop using Android Studio. We are using Android Studio Bumblebee, 2021.1.1 Patch 3, and 2 external libraries. [OkHttp](https://square.github.io/okhttp/) to manage the requests clients. [Gson](https://github.com/google/gson) to parse objects from/to Json. We are using a set of APIs provided by [Fake Store Api](https://fakestoreapi.com) where we make all the calls requests to get and post, products, users, and carts. To get the photo into the profile page we are using [This Person Does Not Exists](https://thispersondoesnotexist.com/). Through the assessment we find some challenges, mainly in the Shop and Cart Activities. We were having problems to create and pass the info locally and externally at the same time. When passing the info locally into the app and keep it updated was challenging because of the different formats and objects we were using. Considering the different combinations that a user can do while purchasing products like go blackguards and forwards into different tabs and to manage the carts without being able to do changes into de database made the programming part intricate. Also was difficult to coordinate the request arrival times for the carts in order to render the products into the recycler views. Another problem we find was, when doing the Posts requests, mainly because of our inexperience in the matter. We have not done any request before and in this case, even when we were getting the Post responses with [Postman](https://www.postman.com/), and following the Docs of Okhttp, we did not find what was the problem with our Post request. We decided to ask on [Stackoverflow](https://stackoverflow.com/questions/72105615/error-java-net-sockettimeoutexception-timeout-in-kotlin) and we got an answer explaining what problem we might be having and how to solve it. In the beginning of the project, we were using intent with the Cart between activities. We tried to intent it as an object, but we were getting errors and the app was not compiling. So, we tried another approach by using Gson to convert it into a Json string and the opposite into the next activity. After that when we started using Shared private resources to keep the token and the user id, we decided to take the same approach with the cart. In order to manage the recycler view renders and to coordinate the response of the products requests also we used a Handler with a delay.

___

## Credits:
- [OkHttp](https://square.github.io/okhttp/)
- [Fake Store Api](https://fakestoreapi.com)
- [This Person Does Not Exists](https://thispersondoesnotexist.com/)
- [Stackoverflow](https://stackoverflow.com/questions/72105615/error-java-net-sockettimeoutexception-timeout-in-kotlin)




