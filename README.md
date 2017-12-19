# fitbuddy

Web App for lifting goals

Designed for friends / learning full stack Java web develoment

Hosted on Heroku! Check it out [here](https://fithelper.herokuapp.com/)!

## Built With

* [SparkJava](http://sparkjava.com/documentation#getting-started) - Web Framework
* [MonogoDB](https://www.mongodb.com/) - Database 
* [Morphia](http://mongodb.github.io/morphia/1.2/getting-started) - Java ORM
* [Thinbus Java Secure Remote Password ](https://connect2id.com/products/nimbus-srp/usage) - SRP Authenication
* [Handlebars](http://handlebarsjs.com/) - Java template engine

## Registration 

On the home page the user can click the sign uplink to take them to the registration page. 
From there they can enter a username and password and hit the sign up button.
  
Using thinbus SRP, the authenication flow is described in these following steps
 1. A random salt is created and a verifier is generated from the salt, username, and password on the client side.
 2. The username, salt, and verifier are sent to the server 
 3. The server uses this to create an User object and stores it in the database 
 4. The server sends to the client a message if the User was created and stored succesfully  

The route below deals with these sign up steps.
```
post(Path.Web.DO_SIGNUP, (req,res) -> AuthController.handleSignUp(req,res));
```

## Login

 1. The user supplies their username / password
 2. The server will use the email to fetch the saved salt and verifier of the user
 3. The server will use the salt and verifier to generate a server challenge
 4. The salt and the server challenge is sent back to the client
 5. The client will use the salt and server challenge to generate credentials, which is sent back to the server
 6. The server will then use the received credentials to generate server evidence message, if this is successful without errors and exceptions thrown, then the user is authenticated.
 
The routes below deals with these sign in steps.
```
 post(Path.Web.DO_SIGNIN, (req,res) -> AuthController.handleSignIn(req,res));

 post(Path.Web.DO_AUTH, (req,res) -> AuthController.handleAuth(req,res));
```

## Profile Page

The profile page is where the user can add / delete / edit / view their lifts.

The profile page uses [FullCalendar](https://fullcalendar.io/docs/) to display dates and lifts. 

The calendar displays a month and the default month to display is the current month.

A date will display an event object that displays the number of workouts done on that day. If there is none then nothing will appear on that date. 

Each time a workout is added or deleted the event object will correctly update to display the number of workouts. 

### Add a Workout
  1. Click on the "Add Workout" button on the top navbar or click a day on the full calendar and then click on the "Add Workout"
  2. Enter the exercise, number of sets , and the date 
  3. Click "Next"
  4. Enter the number of reps and the weight for each set. If all sets are identical fill in the first set and click "Set All Sets to Set 1"
  5. Click confirm
  6. The added workout should now be viewable on the calendar.
  
  The route below deals with adding a workout.
```
post(Path.Web.ADD_WORKOUT, (req,res) -> WorkOutController.handleAddWorkout(req,res));;
```
  
### View a workout
  1. Click on the desired date on the calendar
  2. A table will popup and display all workouts on that date.
  
  The route below deals with viewing workouts.
```
post(Path.Web.VIEW_WORKOUT, (req,res) -> WorkOutController.handleViewWorkout(req,res));
```
  
### Delete a workout
  1. Click on the date the workout was done on
  2. A table will popup, find the desired workout and click "Delete" button to the right of that workout
  
  The route below deals with deleting a workout.
```
post(Path.Web.DELETE_WORKOUT, (req,res) -> WorkOutController.handleDeleteWorkout(req,res));
```
  
### Edit a workout
  1. Click on the date the workout was done on
  2. A table will popup, find the desired workout and click "Edit" button to the right of that workout
  3. The exericse, date, weight, and reps are editable, but currently the sets are not (To DO)
  4. Click confirm to edit the workout
  
  The route below deals with editing a workout.
```
post(Path.Web.EDIT_WORKOUT, (req,res) -> WorkOutController.handleUpdateWorkout(req,res));
```

## TODO

  1. Graph a lift
  2. Implement Supersets
  3. Implement Max lifts
  
  
 

