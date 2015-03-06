package dfstudio.api.upload.client;

import dfstudio.http.HttpClient;
import dfstudio.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author msgile
 * @author $LastChangedBy$
 * @version $Revision$  $LastChangedDate$
 * @since 2/26/15
 */
public class DfsUploadClient {
  public static DfsUploadClient newUpload(HttpClient client, String dfstudioUrl, String username, String account, String password) {
    Map<String,String> authenticationData = new HashMap<String,String>(6);
    authenticationData.put("username",username);
    authenticationData.put("account",account);
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
          HttpResponse response = client.post(baseUrl+"/rest/v3/session.json",authenticationData);
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
    String imageUrl = String.format("%s/path/%s",authenticateUrl,path);
    Map<String,String> parameters = new HashMap<String,String>(3);
    if ( setup != null && ! setup.isEmpty()) {
      parameters.put("setup",setup);
    }
    parameters.put("action","uploadurlrequest");
    parameters.put("type","image");
    HttpResponse response = client.post(imageUrl,parameters);
    validateResponse(response);
    String responseMessage = response.getMessageAsString();
    String uploadUrl = extractJsonMapValue(responseMessage,"uploadUrl");
    String contentType = extractJsonMapValue(responseMessage,"uploadContentType");
    String callbackUrl = extractJsonMapValue(responseMessage,"uploadOnCompleteUrl");
    response = client.put(uploadUrl, contentType, source);
    validateResponse(response);
    response = client.post(callbackUrl,new TreeMap<String,String>());
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


