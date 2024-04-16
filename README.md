# WayFare

WayFare is the definitive tour hosting and discovery app, designed with the intention to demonopolize the tour industry and provide a platform with a low barrier for entry for new guides. 

WayFare is furnished with WayFare Journeys – the app’s robust short form video player that greets users on start up. Journeys empowers new tour guides by directly linking the videos to their listings, offering instant exposure, and easily engaging users in one cohesive system. 

As a user, you can book and view upcoming bookings, search and browse for listings, and customise your experience in the app. Any user can also become a WayFarer – a guide dedicated to providing authentic travel experiences – with tour listings that can be discovered through Wayfare Journeys.  

A robust authentication system allows for easy and registration and login, with seamless switching between a user and WayFarer view, and a verification system to prove the reliability of registered WayFarers.

## Features
- Authentication using JWT Tokens and SpringBoot
- Validation of all fields for a smooth UX
- Searching of listings based on filters, including sorting of filters by Hot, Trending and New
- View Journeys created by other users that may link to a listing
- Creation of Journeys using your phones camera
- Make a booking for a tour that you wish to participate in
- View booking details and cancel the booking if you want to
- Sign up to be a WayFarer and start creating your own listings
- View bookings made by other people for your listing
- Review past bookings, both on the user side and the WayFarer side
- Bookmark your favorite listings, and view them later
- Customise your public profile to allow other people to know you better
- Verification of a user's email address by sending an email with a randomly generated OTP (from the backend)  to their specified email address.
- Creation of our own API in the backend
- Changing a currency to a user's desired currency, offering accurate real time updates on current exchange rates.
- Dark mode for visual accessibility especially in low light areas to reduce glare
- Beautiful UI using Material Design 3 elements and practices

## Tech Stack
- Frontend
   - Android Views
   - Material Design 3
   - Java
- Backend
   - http://api.tingtangwalawalabingbang.com/
   - Spring Boot
   - Hosted on DigitalOcean Droplet
   - Database is hosted on free tier shared cluster in MongoDB Atlas
   - Azure Cloud Storage for storage of media
- External APIs Used
   - Google Maps API / Places SDK
   - Exchange Rate API  (https://www.exchangerate-api.com/)
   - Mailgun API for sending emails
   
## Key Screenshots
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/260bb7d7-55f9-4a6e-bd91-126e00f42adf" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/58aed12c-d6d1-4658-8a13-402b8dc8a60e" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/244ff86a-e571-436b-ad5b-3b5d096c5139" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/2414becd-a23e-41cf-8be9-4ac2e7286126" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/1b8926b6-b0dd-43a8-b8d3-5cfdc826658d" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/a0aac774-e7d5-4d22-9cd3-d8233e2e0842" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/fb3b9e95-25d2-412a-b02a-ff44a5c2d3f2" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/d83a7559-be17-4027-80aa-eb85a8283f4b" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/1bc28150-f094-4813-a1f3-ba3e4dd983ff" width = 150/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/4a203ca4-5a14-4363-9c71-b97a8580208d" width = 150/>



## Link to backend repo
https://github.com/cthdarren/wayfare-backend

## API documentation for backend server
<https://documenter.getpostman.com/view/4694871/2sA35D64HZ>

