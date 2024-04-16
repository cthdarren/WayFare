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

## Screenshots
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/260bb7d7-55f9-4a6e-bd91-126e00f42adf" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/58aed12c-d6d1-4658-8a13-402b8dc8a60e" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/244ff86a-e571-436b-ad5b-3b5d096c5139" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/2414becd-a23e-41cf-8be9-4ac2e7286126" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/1b8926b6-b0dd-43a8-b8d3-5cfdc826658d" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/a0aac774-e7d5-4d22-9cd3-d8233e2e0842" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/fb3b9e95-25d2-412a-b02a-ff44a5c2d3f2" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/d83a7559-be17-4027-80aa-eb85a8283f4b" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/1bc28150-f094-4813-a1f3-ba3e4dd983ff" width = 170/>
<img src = "https://github.com/cthdarren/WayFare/assets/41722713/4a203ca4-5a14-4363-9c71-b97a8580208d" width = 170/>



## Link to backend repo
https://github.com/cthdarren/wayfare-backend

## API documentation for backend server
<https://documenter.getpostman.com/view/4694871/2sA35D64HZ>

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

## How to Use AzureStorageManager

### Integration Steps
To use AzureStorageManager in your Android application, follow these steps:

1. **Include AzureStorageManager in Your Project**
   - Add package into your code 
  `import com.example.wayfare.Utils.AzureStorageManager;`

1. **Call the `uploadBlob` Method**
   - Invoke the `uploadBlob` method from your activity or fragment to initiate the file upload process.
   - Pass the following parameters:
     - `Context context`: The context of the calling activity or fragment.
     - `Uri videoUri`: The URI of the video file to be uploaded.
     - `Callback callback`: A callback to handle success and failure responses.

2. **Handle Callbacks**
   - Implement the `onResponse` and `onFailure` methods of the `Callback` interface to handle upload responses.
   - In `onResponse`, you can perform actions upon successful file upload, such as navigating to another activity.
   - In `onFailure`, handle errors and display appropriate messages to the user.

## Contents of AzureStorageManager

### Methods
- **`uploadBlob(Context context, Uri fileUri, Callback callback)`**:
  Initiates the file upload process to Azure Blob Storage. ***This is what you will be using.***
- **`uploadFile(Uri videoUri, Context context, String sasToken, String accountName, String containerName, Callback callback)`**:
  Uploads the file to the specified Azure Blob Storage container.
- **`getBytesFromUri(Uri uri, Context context)`**:
  Reads the content of a file URI and returns it as a byte array.
- **`getFileNameFromUri(Uri uri, Context context)`**:
  Retrieves the file name from a given file URI.
- **`getFileExtension(String fileName)`**:
  Extracts the file extension from the provided file name.

### Classes and Interfaces
- **`Callback`** (interface):
  Interface for handling asynchronous HTTP requests. Includes methods `onResponse` and `onFailure` to handle success and failure responses, respectively.

## Example Usage
```java
AzureStorageManager.uploadBlob(this, fileUri, new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        // Handle failure
        e.printStackTrace();
        // Perform any error handling logic, such as showing an error message to the user
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        // Handle success
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response code: " + response);
        }
        // Handle successful upload
        String url = response.request().url().toString();
        String urlOfBlob = AzureStorageManager.getBaseUrl(url); //URL of uploaded file

        Log.d("Upload", "File uploaded successfully. URL: " + url);
        //Do your thing. Below is an example
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Start a new activity
                Intent i = new Intent(PostShortActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
});

