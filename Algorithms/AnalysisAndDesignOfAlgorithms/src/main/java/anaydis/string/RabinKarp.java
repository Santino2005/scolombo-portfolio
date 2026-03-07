package anaydis.string;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class RabinKarp implements StringSearcher {
    private final String text;
    private final int q = 997;
    private final int d = 256;

    public RabinKarp(@NotNull String text) {
        this.text = text;
    }

    @Override
    public int count(@NotNull String pattern) {
        int count = 0;
        int M = pattern.length();
        int N = text.length();
        int patternHash = 0;
        int textHash = 0;
        int h = 1;

        for (int i = 0; i < M - 1; i++) {
            h = (h * d) % q;
        }
        for (int i = 0; i < M; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % q;
            textHash = (d * textHash + text.charAt(i)) % q;
        }

        for (int i = 0; i <= N - M; i++) {
            if (patternHash == textHash) {
                int j;
                for (j = 0; j < M; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }
                }
                if (j == M) {
                    count++;
                }
            }
            if (i < N - M) {
                textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + M)) % q;
                if (textHash < 0) {
                    textHash += q;
                }
            }
        }
        return count;
    }

    @Override
    public int first(@NotNull String pattern) {
        int M = pattern.length();
        int N = text.length();
        int patternHash = 0;
        int textHash = 0;
        int h = 1;

        for (int i = 0; i < M - 1; i++) {
            h = (h * d) % q;
        }

        for (int i = 0; i < M; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % q;
            textHash = (d * textHash + text.charAt(i)) % q;
        }

        for (int i = 0; i <= N - M; i++) {
            if (patternHash == textHash) {
                int j;
                for (j = 0; j < M; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }
                }
                if (j == M) {
                    return i;
                }
            }
            if (i < N - M) {
                textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + M)) % q;
                if (textHash < 0) {
                    textHash += q;
                }
            }
        }
        return -1;
    }

    @Override
    public @NotNull Iterator<Integer> all(@NotNull String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        int M = pattern.length();
        int N = text.length();
        int patternHash = 0;
        int textHash = 0;
        int h = 1;

        for (int i = 0; i < M - 1; i++) {
            h = (h * d) % q;
        }
        for (int i = 0; i < M; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % q;
            textHash = (d * textHash + text.charAt(i)) % q;
        }

        for (int i = 0; i <= N - M; i++) {
            if (patternHash == textHash) {
                int j;
                for (j = 0; j < M; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }
                }
                if (j == M) {
                    occurrences.add(i);
                }
            }
            if (i < N - M) {
                textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + M)) % q;
                if (textHash < 0) {
                    textHash += q;
                }
            }
        }
        return occurrences.iterator();
    }

    @Override
    public @NotNull StringSearcherType getType() {
        return StringSearcherType.RABIN_KARP;
    }

    @Override
    public @NotNull String text() {
        return text;
    }
}
