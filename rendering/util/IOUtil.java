package rendering.util;

import org.lwjgl.BufferUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class IOUtil {

    /**
     * load a file and return a string with the file content
     * @param file the location of the file to load
     * @return the string containing the file content
     */
    public static String loadTextFileToString(String file)
    {
        try {

            Reader reader = new FileReader(file);
            Scanner scanner = new Scanner(reader);

            //read lines from file and add to string
            String shader = "";
            while (scanner.hasNext())
            {
                shader += scanner.nextLine() + "\n";
            }

            return shader;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * load a file to a byte buffer
     * @param resource the location of the file to load
     * @return the bytebuffer containg the content of the file
     */
    public static ByteBuffer loadFileToByteBuffer(String resource)
    {
        ByteBuffer buffer = null;

        Path path = Paths.get(resource);
        try (SeekableByteChannel channel = Files.newByteChannel(path)) {
            buffer = BufferUtils.createByteBuffer((int)channel.size() + 1);
            while (channel.read(buffer) != -1)
            {

            }
        }catch (IOException e)
        {
            System.out.println("could not load file: " +  resource);
        }

        buffer.flip();
        return buffer.slice();
    }

}
