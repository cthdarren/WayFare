# WayFare

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

