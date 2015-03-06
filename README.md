# upload-client-java
A reference implementation of an upload client for DF Studio written in Java

# Usage
The main class is [dfstudio.api.upload.client.DfsUploadClient](src/main/java/dfstudio/api/upload/client/DfsUploadClient.java). 

```Java
//This is just an interface with only two methods, feel free to implement your own
String dfstudioUrl = "https://enterprise.dfstudio.com";
String username = "msgile";  //username
String account  = "msgile";  //short name
String password = "##";      
HttpClient http = new JavaHttpClient();
DfsUploadClient client = DfsUploadClient.newUpload(dfstudioUrl,  , "msgile", "##password##");

String folder   = "landscapes"; //optional, can be empty or null, project will be in root folder
String project  = "highlands";
String setup    = "walking path";  //optional, can be empty or null
String filename = "p1001-mountain-trail.jpg";
InputStream stream = getClass().getResourceAsStream("p1001-mountain-trail.jpg");

client.uploadFile(folder,project,setup,filename,stream);
```
