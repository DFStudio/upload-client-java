package dfstudio.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface HttpClient {
  public HttpResponse post(String url, Map<String, String> parameters) throws IOException;
  public HttpResponse put(String url, String contentType, InputStream input) throws IOException;
}
