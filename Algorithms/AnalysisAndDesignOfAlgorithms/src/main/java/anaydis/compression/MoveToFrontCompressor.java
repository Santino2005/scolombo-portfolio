//Testeando para saber si se pueden pasar los test ignorados del team city

package anaydis.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import org.jetbrains.annotations.NotNull;

public class MoveToFrontCompressor implements Compressor {

    @Override
    public void encode(@NotNull InputStream in, @NotNull OutputStream out) throws IOException {
        LinkedList<Integer> list = initializeAsciiList();

        int readByte;
        while ((readByte = in.read()) != -1) {
            int index = list.indexOf(readByte);
            out.write(index);

            list.remove(index);
            list.addFirst(readByte);
        }
    }

    @Override
    public void decode(@NotNull InputStream in, @NotNull OutputStream out) throws IOException {
        LinkedList<Integer> list = initializeAsciiList();

        int readByte;
        while ((readByte = in.read()) != -1) {
            int symbol = list.get(readByte);
            out.write(symbol);

            list.remove(readByte);
            list.addFirst(symbol);
        }
    }

    private LinkedList<Integer> initializeAsciiList() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            list.add(i);
        }
        return list;
    }
}
