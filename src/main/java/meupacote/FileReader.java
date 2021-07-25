package meupacote;

import java.io.*;
import java.nio.file.Path;

public class FileReader {

    public void read(Path path) {

        File file = new File(String.valueOf(path));

        try {
            final InputStream input = new FileInputStream(String.valueOf(path));
            int content;
            while((content = input.read()) != -1) {
               System.out.print((char) content);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
