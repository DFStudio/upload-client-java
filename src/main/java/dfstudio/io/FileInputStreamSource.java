package dfstudio.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author msgile
 * @author $LastChangedBy$
 * @version $Revision$  $LastChangedDate$
 * @since 3/5/15
 */
public class FileInputStreamSource implements InputStreamSource {
  public final File file;

  public FileInputStreamSource(File file) {
    this.file = file;
  }

  @Override
  public InputStream open() throws IOException {
    return new FileInputStream(file);
  }
}
