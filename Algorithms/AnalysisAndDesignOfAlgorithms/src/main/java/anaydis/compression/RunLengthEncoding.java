package anaydis.compression;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RunLengthEncoding implements Compressor{
    @Override
    public void encode(@NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
        int current = inputStream.read();
        while(current != -1){
            int count = 1;
            int next;
            while((next = inputStream.read()) == current && count < 255){
                count++;
            }
            outputStream.write(current);
            outputStream.write(count);
            current = next;
        }
    }

    @Override
    public void decode(@NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
        int current;
        while((current = inputStream.read()) != -1){
            int count = inputStream.read();
            if(count == -1){
                throw new IOException("Incorrect format");
            }
            for(int i = 0; i < count; i++){
                outputStream.write(current);
            }
        }
    }
    /////////
}
