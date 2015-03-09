package dfstudio.api.upload.client;

import dfstudio.http.HttpClient;
import dfstudio.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class uploads files into DF Studio.
 *
 * <pre>
 *    //This is just an interface with only two methods, feel free to implement your own
 *    HttpClient http = new JavaHttpClient();
 *
 *    String dfstudioUrl = "https://enterprise.dfstudio.com";  //the root url of the dfstudio cluster you sign into
 *    String account  = "msgile";  //short account name
 *    String username = "msgile";  //username/login name
 *    String password = "##";
 *    DfsUploadClient client = DfsUploadClient.newUpload(http, dfstudioUrl, account, username, password);
 *
 *    String folder   = "landscapes"; //optional, can be empty or null, project will be in root folder
 *    String project  = "highlands";
 *    String setup    = "walking path";  //optional, can be empty or null
 *    String filename = "p1001-mountain-trail.jpg";
 *    InputStream stream = getClass().getResourceAsStream(filename);
 *
 *    client.uploadFile(folder,project,setup,filename,stream);
 * </pre>
 *
 *
 *
 * @author msgile
 * @author $LastChangedBy$
 * @version $Revision$  $LastChangedDate$
 * @since 2/26/15
 */
public class DfsUploadClient {
  /**
   *
   * @param client Required {@see JavaHttpClient}
   * @param dfstudioUrl Required
   * @param accountShortName Required
   * @param username Required
   * @param password Required, plain text password
   */
  public static DfsUploadClient newUpload(HttpClient client, String dfstudioUrl, String accountShortName, String username, String password) {
    Map<String,String> authenticationData = new HashMap<String,String>(6);
    authenticationData.put("account", accountShortName);
    authenticationData.put("username",username);
    authenticationData.put("password",password);
    return new DfsUploadClient(client, dfstudioUrl,authenticationData);
  }

  private final HttpClient client;
  private final String baseUrl;
  private final Map<String,String> authenticationData;
  private volatile String authenticateUrl = null;
  private final Object lock = new Object();

  public DfsUploadClient(HttpClient client, String baseUrl, Map<String, String> authenticationData) {
    this.client = client;
    this.baseUrl = baseUrl;
    this.authenticationData = authenticationData;
  }

  private String getAuthenticateUrl() throws IOException {
    if ( authenticateUrl == null ) {
      synchronized (lock)  {
        if ( authenticateUrl == null ) {
          HttpResponse response = client.post(baseUrl+"/rest/v3/session.json", authenticationData);
          if ( response.getStatusCode() != 200 ) {
            throw new IOException("Unable to Authenticate\nCode="+response.getStatusCode()+"\nMessage="+response.getMessageAsString() );
          }
          authenticateUrl = response.getMessageAsString();
          int pos = authenticateUrl.indexOf(".json");
          authenticateUrl = authenticateUrl.substring(1,pos);
        }
      }
    }
    return authenticateUrl;
  }

  public void uploadFile(String folder, String job, String setup, File file) throws IOException {
    uploadFile(folder,job,setup,file.getName(),new FileInputStream(file));
  }

  public void uploadFile(String folder, String job, String setup, String filename, InputStream source) throws IOException {
    String path;
    if ( folder!=null && !folder.isEmpty() ) {
      path = folder+"/"+job+"/"+filename;
    } else {
      path = job+"/"+filename;
    }
    uploadFile(path,setup,source);
  }

  public void uploadFile(String path, String setup, InputStream source) throws IOException {
    String authenticateUrl = getAuthenticateUrl();

    //first, issue an upload request with the image and setup information
    String imageUploadRequestUrl = String.format("%s/path/%s", authenticateUrl, path);
    Map<String,String> parameters = new HashMap<String,String>(3);
    if ( setup != null && ! setup.isEmpty()) {
      parameters.put("setup", setup);
    }
    parameters.put("action","uploadurlrequest");
    parameters.put("type","image");
    HttpResponse response = client.post(imageUploadRequestUrl, parameters);
    validateResponse(response);

    //get the upload url, content type and callback URL returned from the upload request
    String responseMessage = response.getMessageAsString();
    String uploadUrl = extractJsonMapValue(responseMessage, "uploadUrl");
    String contentType = extractJsonMapValue(responseMessage, "uploadContentType");
    String callbackUrl = extractJsonMapValue(responseMessage, "uploadOnCompleteUrl");
    //upload the image
    response = client.put(uploadUrl, contentType, source);
    validateResponse(response);

    //after the image has been uploaded, post to the callback url to notify of the successfully completed upload
    response = client.post(callbackUrl, new HashMap<String,String>());
    validateResponse(response);
  }

  public static void validateResponse(HttpResponse response) throws IOException {
    int code = response.getStatusCode();
    if ( 300 <= code ) {
      throw new IOException("Status Code:"+code+"\n"+response.getMessageAsString());
    }
  }

  public static String extractJsonMapValue(String json, String key) {
    Pattern pattern = Pattern.compile(String.format("\"%s\"\\s*:\\s*\"([^\"]+)\"",key));
    Matcher matcher = pattern.matcher(json);
    if ( matcher.find() ) {
      return matcher.group(1);
    } else {
      return "";
    }
  }
}


