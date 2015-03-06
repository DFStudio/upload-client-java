package dfstudio.io;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class IoUtil {
  public static void copy(String input, OutputStream output) throws IOException {
    copy(input,"UTF-8",output);
  }
  public static void copy(String input, String charset, OutputStream output) throws IOException {
    copy(new ByteArrayInputStream(input.getBytes(charset)),output);
  }
  public static void copy(InputStream input, OutputStream output) throws IOException {
    try {
      byte[] buffer = new byte[4096];
      int bytesRead = 0;
      while((bytesRead = input.read(buffer)) != -1) {
        output.write(buffer, 0, bytesRead);
      }
      output.flush();
    } finally {
      close(input);
      close(output);
    }
  }

  public static void close(Closeable closable) {
    try {
      closable.close();
    } catch (IOException ignore) {
      ;
    }
  }
}
