package dfstudio.api.upload.client;

import dfstudio.http.HttpClient;
import dfstudio.http.JavaHttpClient;
import org.junit.Test;

import java.io.InputStream;

public class TestDfsUploadClient {
  @Test
  public void testUpload() throws Exception {

    //This is just an interface with only two methods, feel free to implement your own
    HttpClient http = new JavaHttpClient();

    String dfstudioUrl = "https://enterprise.dfstudio.com";  //the root url of the dfstudio cluster you sign into
    String account  = "msgile";  //short account name
    String username = "msgile";  //username/login name
    String password = "##";
    DfsUploadClient client = DfsUploadClient.newUpload(http, dfstudioUrl, account, username, password);

    String folder   = "landscapes"; //optional, can be empty or null, project will be in root folder
    String project  = "highlands";
    String setup    = "walking path";  //optional, can be empty or null
    String filename = "p1001-mountain-trail.jpg";
    InputStream stream = getClass().getResourceAsStream(filename);

    client.uploadFile(folder,project,setup,filename,stream);

  }

}