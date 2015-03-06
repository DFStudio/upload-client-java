package dfstudio.http;

import dfstudio.io.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class JavaHttpClient implements HttpClient {
  @Override
  public HttpResponse post(String url, Map<String, String> parameters) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setDoOutput(true);
    connection.setRequestMethod("POST");
    String charset = "UTF-8";
    connection.setRequestProperty("Accept-Charset", charset);
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
    String parametersEncoded = encodeParameters(parameters, charset);
    IoUtil.copy(parametersEncoded, charset, connection.getOutputStream());
    return new JavaHttpResponse(connection);
  }

  @Override
  public HttpResponse put(String url, String contentType, InputStream input) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setDoOutput(true);
    connection.setRequestMethod("PUT");
    //String charset = "UTF-8";
    connection.setRequestProperty("Content-Type", contentType);
    IoUtil.copy(input,connection.getOutputStream());
    return new JavaHttpResponse(connection);
  }

  static String encodeParameters(Map<String, String> parameters, String charset) {
    try {
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (Map.Entry<String, String> entry : parameters.entrySet()) {
        if (!isFirst) {
          builder.append("&");
        }
        isFirst = false;
        builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset));
      }
      return builder.toString();
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

}
