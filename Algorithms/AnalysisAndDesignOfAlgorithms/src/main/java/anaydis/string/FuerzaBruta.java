package anaydis.string;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FuerzaBruta implements StringSearcher {

    private String text;
    public FuerzaBruta(String text) {
        this.text = text;
    }
    @Override
    public int count(@NotNull String pattern) {
        int count = 0;
        int m = pattern.length();
        int n = text.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int first(@NotNull String pattern) {
        int m = pattern.length();
        int n = text.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public @NotNull Iterator<Integer> all(@NotNull String pattern) {
        List<Integer> matches = new ArrayList<>();
        int m = pattern.length();
        int n = text.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                matches.add(i);
            }
        }
        return matches.iterator();
    }
    @Override
    public @NotNull StringSearcherType getType() {
        return StringSearcherType.BRUTE_FORCE;
    }
    @Override
    public @NotNull String text() {
        return text;
    }
}
