package dfstudio.api.upload.client;

import dfstudio.http.JavaHttpClient;
import org.junit.Test;

public class TestDfsUploadClient {
  @Test
  public void testUplaod() throws Exception {
    DfsUploadClient client = DfsUploadClient.newUpload(new JavaHttpClient(), "http://localhost:8080/studio","msgile", "msgile", "1234qwer");

    client.uploadFile("f1","p1",null,"mountain-trail.jpg",getClass().getResourceAsStream("mountain-trail.jpg"));

    /*
    File file = File.createTempFile("temp",".txt");
    try {
      IoUtil.copy("Just some text","UTF-8",new FileOutputStream(file));
      client.uploadFile("f1","p1",null,file);
    } finally {
      file.delete();
    }
     */

  }

}