package anaydis.string;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class StringSearcherProviderImpl implements StringSearcherProvider {
    @Override
    public @NotNull StringSearcher getForType(@NotNull StringSearcherType stringSearcherType, @NotNull String s) {
        if(stringSearcherType == StringSearcherType.BRUTE_FORCE){
            return new FuerzaBruta(s);
        }else if(stringSearcherType == StringSearcherType.RABIN_KARP){
            return new RabinKarp(s);
        }else{
            throw new NoSuchElementException("No search Type");
        }
    }
}
