# QuizMint
QuizMint is an engaging quiz application developed using Kotlin and Firebase, designed to provide an interactive and rewarding experience for users. 
It offers a variety of quiz categories, robust user authentication, and real-time database features. QuizMint enhances the traditional quiz format 
with a reward system where users can earn coins and convert them into real money.

# Screenshot
<p>
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/e1d65d96-1e31-41fe-8e16-485bed61c032.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/b01a934e-9cda-46fd-9d98-a613e6e694f6.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/e4ec4fea-a11f-4958-9b11-9647ab64b8fd.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/b7f76134-0178-4255-977b-61a1e47803c4.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/0c26d3f7-aa65-4e5c-b964-060643461f1b.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/30a9278f-bc3e-454b-9e9e-70118fde8e78.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/77605746-c677-416a-aaab-3e2ed358e819.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/84b7eeaf-5f2e-4ed2-9f90-9e0ccb2aa3be.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/6b398b6d-f179-433b-9e7d-e3847ac8aa49.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/aa39ce38-befa-4286-bafd-d58f07520578.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/0972930c-f7de-466c-87e9-2673676719e2.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/2f4860cc-36a3-4b42-9a7d-e5317228122b.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/cb8ff577-e4b5-47a5-abd7-d2c0c088aa6b.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/48ad5c0d-1f5f-46bf-b5e1-55c2c79dc54f.png" alt="feed example" width = "200" >
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/AndroidRider/Media-Player/assets/140700822/47533e98-e192-4e1b-ad4c-95973735a40e.png" alt="feed example" width = "200" >
  
</p>

# Purpose:
The primary purpose of QuizMint is to provide users with interactive quizzes across various categories while integrating a unique reward system. 
By combining educational content with gamification elements, QuizMint aims to enhance user engagement and enjoyment. Users can earn coins based on 
their quiz performance, which are then converted into real money, offering a tangible incentive for participation.

# Features:
## 1. User Authentication:
<p><b>1. SignUp : </b>New users can register by providing their email and password, creating a secure account in the Firebase Authentication system.</p>
<p><b>2. SignIn : </b>Returning users can log in with their registered email and password to access the app.</p>
<p><b>3. Forgot Password : </b>Users who forget their password can reset it through a secure email-based recovery process.</p>

##  2. Home Screen:
<p><b>1. Category Selection : </b>Upon signing in, users are directed to the home screen, where they can select from a range of quiz categories.</p>
<p><b>2. Search Category : </b>Users can search for specific quiz categories to find quizzes that match their interests.</p>

## 3. Quiz Play:
<p><b>1. Play Screen : </b>When a user selects a category, they are taken to the play screen where questions from Firestore are presented. Users answer these questions to complete the quiz.</p>
<p><b>2. Score Evaluation : </b>After completing the quiz, the user's score is evaluated. If the user scores 60% or higher, a winner screen is displayed; otherwise, a lost screen appears.</p>

## 4. Reward System:
<p><b>1. Spin Chance : </b>Users who score 60% or higher are granted one chance to spin a wheel on the spin screen.</p>
<p><b>2. Coin Earning : </b>The spin wheel allows users to earn coins based on where the wheel stops.</p>
<p><b>3. Coin Conversion : </b>The earned coins are automatically converted into rupees, which users can withdraw.</p>

## 5. History Screen:
<p><b>1. Earnings and Withdrawals : </b>Users can view a detailed history of their earned and withdrawn coins.</p>
<p><b>2. Filtering : </b>The history screen provides filtering options, allowing users to view records based on earnings and withdrawals separately.</p>

## 6. Profile Screen:
<p><b>1. User Information : </b>This screen displays the user's profile information, including their name, email, and profile image.</p>
<p><b>2. Profile Update : </b>Users can update their profile details and upload a new profile image.</p>

# Technical Implementation:
<p><b>1. Development Platform: </b>Developed using Kotlin for Android to leverage its modern features and enhance app performance.</p>
<p><b>2. Authentication: </b>Firebase Authentication for secure user sign-up, sign-in, and password recovery.</p>
<p><b>3. User Interface (UI): </b>Designed an intuitive and visually appealing interface with XML, Material Design, fonts, and responsive layouts.</p>
<p><b>4. Backend: </b>Firebase Realtime Database for storing user data, Firestore for quiz questions and categories.</p>
<p><b>5. Navigation: </b>Navigation Component</p>

# Challenges:
<p><b>1. Integrating Firebase Services:</b>Ensuring smooth integration of Firebase Authentication, Realtime Database, and Firestore was challenging, particularly in managing user sessions and data synchronization.</p>
<p><b>2. Real-time Data Handling:</b>Handling real-time updates and ensuring the app remains responsive while fetching data from Firestore required careful management of asynchronous tasks.</p>
<p><b>3. UI/UX Design:</b>Designing an intuitive and visually appealing user interface that enhances the user experience while maintaining functionality.</p>
<p><b>4. Reward System Implementation:</b>Creating a fair and engaging reward system that provides users with a meaningful incentive to perform well in quizzes.</p>

# Lessons Learned:
<p><b>1. Firebase Integration: </b>Gained extensive experience in integrating and managing various Firebase services, understanding their strengths and limitations.</p>
<p><b>2. Asynchronous Programming:</b>Enhanced skills in handling asynchronous operations in Kotlin, ensuring smooth and responsive user interactions.</p>
<p><b>3. UI/UX Design:</b>Learned the importance of designing user-centric interfaces that are both functional and visually appealing.</p>
<p><b>4. User Feedback:</b>Realized the value of user feedback in iterating and improving the app to better meet user needs and expectations.</p>

# Future Improvements:
<p><b>1. Enhanced Quiz Features: </b>Adding more quiz categories, different difficulty levels, and timed quizzes to increase variety and challenge.</p>
<p><b>2. Social Features: </b>Implementing social features such as friend lists, leaderboards, and quiz challenges to foster a sense of community and competition among users.</p>
<p><b>3. Advanced Analytics: </b>Integrating advanced analytics to provide users with detailed performance insights and personalized quiz recommendations.</p>
<p><b>4. Localization: </b>Supporting multiple languages to make the app accessible to a broader audience.</p>
<p><b>5. Monetization: </b>Introducing in-app purchases and advertisements to generate revenue while maintaining a good user experience.</p>
<p><b>6. Dark Mode: </b>Implement a dark mode option for the app, providing users with a visually comfortable alternative and potentially extending device battery life for users who prefer darker interfaces.</p>

# Conclusion:
QuizMint is a comprehensive and interactive quiz application that combines educational quizzes with a unique reward system. 
It demonstrates the effective use of Kotlin and Firebase to create a seamless and engaging user experience. 
This project showcases my proficiency in Android development and my ability to integrate various Firebase services to build feature-rich applications.
