package anaydis.compression;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BurrowsWheeler implements Compressor {

    @Override
    public void encode(@NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
        String inputString = streamToString(inputStream);

        List<String> rotations = generateRotations(inputString);

        Collections.sort(rotations);

        int originalIndex = rotations.indexOf(inputString);

        outputStream.write(originalIndex >>> 8);
        outputStream.write(originalIndex & 0xFF);

        for (String rotation : rotations) {
            outputStream.write(rotation.charAt(rotation.length() - 1));
        }
    }

    @Override
    public void decode(@NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
        int originalIndex = (inputStream.read() << 8) | inputStream.read();
        List<Character> lastColumn = new ArrayList<>();
        int byteRead;
        while ((byteRead = inputStream.read()) != -1) {
            lastColumn.add((char) byteRead);
        }
        List<Character> firstColumn = new ArrayList<>(lastColumn);
        Collections.sort(firstColumn);
        List<Integer> next = buildNextArray(firstColumn, lastColumn);
        int currentIndex = originalIndex;
        for (int i = 0; i < lastColumn.size(); i++) {
            outputStream.write(firstColumn.get(currentIndex));
            currentIndex = next.get(currentIndex);
        }
    }

    private List<String> generateRotations(String text) {
        List<String> rotations = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            rotations.add(text.substring(i) + text.substring(0, i));
        }
        return rotations;
    }

    private List<Integer> buildNextArray(List<Character> firstColumn, List<Character> lastColumn) {
        int n = lastColumn.size();
        List<Integer> next = new ArrayList<>(Collections.nCopies(n, 0));
        List<Integer> usedPositions = new ArrayList<>(Collections.nCopies(n, 0));

        for (int i = 0; i < n; i++) {
            char c = lastColumn.get(i);
            for (int j = 0; j < n; j++) {
                if (firstColumn.get(j) == c && usedPositions.get(j) == 0) {
                    next.set(j, i);
                    usedPositions.set(j, 1);
                    break;
                }
            }
        }
        return next;
    }


    private String streamToString(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString();
    }
}
