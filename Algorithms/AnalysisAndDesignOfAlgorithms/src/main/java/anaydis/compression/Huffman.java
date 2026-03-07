package anaydis.compression;

import anaydis.search.BinaryTrieMap;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman implements Compressor {

    class Node implements Comparable<Node> {
        int frequency;
        char character;
        Node left, right;

        Node(int frequency, char character) {
            this.frequency = frequency;
            this.character = character;
        }

        Node(int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }
    @Override
    public void encode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {

        Map<Character, Integer> frequencyMap = new HashMap<>();
        StringBuilder inputText = new StringBuilder();
        int b;
        while ((b = input.read()) != -1) {
            char c = (char) b;
            inputText.append(c);
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new Node(entry.getValue(), entry.getKey()));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            assert right != null;
            Node parent = new Node(left.frequency + right.frequency, left, right);
            priorityQueue.add(parent);
        }
        Node root = priorityQueue.poll();

        Map<Character, String> huffmanCode = new HashMap<>();
        generateCodes(root, "", huffmanCode);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
        objectOutputStream.writeObject(huffmanCode);
        StringBuilder encodedText = new StringBuilder();
        for (char c : inputText.toString().toCharArray()) {
            encodedText.append(huffmanCode.get(c));
        }
        objectOutputStream.writeObject(encodedText.toString());
    }

    private void generateCodes(Node node, String code, Map<Character, String> huffmanCode) {
        if (node == null) return;
        if (node.left == null && node.right == null) {
            huffmanCode.put(node.character, code);
        }
        generateCodes(node.left, code + "0", huffmanCode);
        generateCodes(node.right, code + "1", huffmanCode);
    }

    @Override
    public void decode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(input);
        try {
            @SuppressWarnings("unchecked")
            Map<Character, String> huffmanCode = (Map<Character, String>) objectInputStream.readObject();
            String encodedText = (String) objectInputStream.readObject();

            Node root = buildHuffmanTree(huffmanCode);

            Node current = root;
            for (char bit : encodedText.toCharArray()) {
                current = (bit == '0') ? current.left : current.right;
                assert current != null;
                if (current.left == null && current.right == null) {
                    output.write(current.character);
                    current = root;
                }
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Error al leer la tabla", e);
        }
    }
    private Node buildHuffmanTree(Map<Character, String> huffmanCode) {
        Node root = new Node(0, '\0');
        for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
            Node current = root;
            for (char bit : entry.getValue().toCharArray()) {
                if (bit == '0') {
                    if (current.left == null) {
                        current.left = new Node(0, '\0');
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = new Node(0, '\0');
                    }
                    current = current.right;
                }
            }
            current.character = entry.getKey();
        }
        return root;
    }

}