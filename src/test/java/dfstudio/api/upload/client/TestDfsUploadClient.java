package dfstudio.api.upload.client;

import dfstudio.http.HttpClient;
import dfstudio.http.JavaHttpClient;
import org.junit.Test;

import java.io.InputStream;

public class TestDfsUploadClient {

  @Test
  public void testUpload() throws Exception {

    //This is just an interface with only two methods, feel free to implement your own
    String dfstudioUrl = "https://#YOURHOST#.dfstudio.com";
    String username = "#USERNAME#";
    String account  = "#ACCOUNT#";
    String password = "#PASSWORD#";
    HttpClient httpClient = new JavaHttpClient();
    DfsUploadClient dfsUploadClient = DfsUploadClient.newUpload(httpClient, dfstudioUrl, account, username, password);

    String folder   = "landscapes"; //optional, can be empty or null, project will be in root folder
    String project  = "highlands";
    String setup    = "walking path";  //optional, can be empty or null
    String filename = "p1001-mountain-trail.jpg";
    InputStream stream = getClass().getResourceAsStream("p1001-mountain-trail.jpg");

    dfsUploadClient.uploadFile(folder, project, setup, filename, stream);
  }

}