# upload-client-java
A reference implementation of a very simple upload client for DF Studio written in Java.

This client is not intended for production use.
It is a demonstration of using the DF Studio REST API, written as simply as possible without any 3rd-party library dependencies. JDK 6 or higher is required. 

# Usage
The main class is [dfstudio.api.upload.client.DfsUploadClient](src/main/java/dfstudio/api/upload/client/DfsUploadClient.java).

```Java
//This is just an interface with only two methods, feel free to implement your own
String dfstudioUrl = "https://#YOURHOST#.dfstudio.com";
String username = "#USERNAME#";  //username
String account  = "#ACCOUNT#";  //short name
String password = "#PASSWORD#";
HttpClient httpClient = new JavaHttpClient();
DfsUploadClient dfsUploadClient = DfsUploadClient.newUpload(httpClient, dfstudioUrl, account, username, password);

String folder   = "landscapes"; //optional, can be empty or null, project will be in root folder
String project  = "highlands";
String setup    = "walking path";  //optional, can be empty or null
String filename = "p1001-mountain-trail.jpg";
InputStream stream = getClass().getResourceAsStream("p1001-mountain-trail.jpg");

dfsUploadClient.uploadFile(folder, project, setup, filename, stream);
```
