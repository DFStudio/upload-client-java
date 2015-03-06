package dfstudio.http;

import dfstudio.io.IoUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

public class JavaHttpResponse implements HttpResponse {
  private final HttpURLConnection connection;

  JavaHttpResponse(HttpURLConnection connection) {
    this.connection = connection;
  }

  @Override
  public int getStatusCode() throws IOException {
    return connection.getResponseCode();
  }

  @Override
  public String getMessageAsString() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    IoUtil.copy(connection.getInputStream(), output);
    return output.toString("UTF-8");
  }
}
