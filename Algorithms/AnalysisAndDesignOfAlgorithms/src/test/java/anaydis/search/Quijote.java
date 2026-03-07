package anaydis.search;

import java.io.*;
import java.util.*;

import java.io.*;
import java.util.*;
import anaydis.search.BinaryTrieMap;
import anaydis.search.RWayTrieMap;
import anaydis.search.TSTTrieMap;

public class Quijote{
    public static void main(String[] args) throws IOException {

        BinaryTrieMap<Integer> btm = new BinaryTrieMap<>();
        RWayTrieMap<Integer> rwtm = new RWayTrieMap<>();
        TSTTrieMap<Integer> tst = new TSTTrieMap<>();

        String filePath = "C:\\Users\\santi\\Faculty\\Anaydis\\algoritmos-scolombo\\src\\test\\resources\\books\\quijote_invertido.txt";

        int[] N_values = {5000, 50000, 100000, 150000, 200000};

        for (int N : N_values) {
            System.out.println("Pruebas con N = " + N);

            processFile(filePath, N, btm);
            processFile(filePath, N, rwtm);
            processFile(filePath, N, tst);

            List<String> invertedWords = generateInvertedWords(btm.keys());

            saveToFile(invertedWords, "inverted_words_" + N + ".txt");
            measureSearchTime(btm, invertedWords, "BinaryTrieMap");
            measureSearchTime(rwtm, invertedWords, "RWayTrieMap");
            measureSearchTime(tst, invertedWords, "TSTTrieMap");

            btm.clear();
            rwtm.clear();
            tst.clear();
        }
    }

    public static void processFile(String filePath, int N, anaydis.search.Map<String, Integer> map) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int wordCount = 0;
            while ((line = reader.readLine()) != null && wordCount < N) {
                String[] words = line.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        Integer currentCount = map.get(word);
                        if (currentCount == null) {
                            map.put(word, 1);
                        } else {
                            map.put(word, currentCount + 1);
                        }
                        wordCount++;
                        if (wordCount >= N) break;
                    }
                }
            }
        }
    }

    public static List<String> generateInvertedWords(Iterator<String> keys) {
        List<String> invertedWords = new ArrayList<>();
        while (keys.hasNext()) {
            String word = keys.next();
            invertedWords.add(new StringBuilder(word).reverse().toString());
        }
        return invertedWords;
    }

    public static void saveToFile(List<String> words, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }
        }
    }

    public static void measureSearchTime(anaydis.search.Map<String, Integer> map, List<String> words, String mapType) {
        long startTime = System.nanoTime();
        for (String word : words) {
            map.get(word);
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(mapType + " tiempo total de búsqueda: " + totalTime + " Nano");
    }
}