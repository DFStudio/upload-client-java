package dfstudio.http;

import java.io.IOException;

public interface HttpResponse {
  public int getStatusCode() throws IOException;
  public String getMessageAsString() throws IOException;
}
