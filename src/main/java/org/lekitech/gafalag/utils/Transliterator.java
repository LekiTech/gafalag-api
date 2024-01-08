package org.lekitech.gafalag.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for transliterating English text to Cyrillic script. The transliteration
 * is based on a predefined character mapping. This implementation provides a method
 * to transliterate a given text based on certain criteria including language checks.
 * <p>
 * This class was inspired by Alex Zaitsev's implementation of transliteration utility.
 *
 * @see <a href="https://github.com/alexzaitsev/">Alex Zaitsev</a>
 * @see <a href="https://gist.github.com/alexzaitsev/5f74322a714e83e464a3">Inspiration Source</a>
 */
public class Transliterator {

    private static final int UPPER = 1;
    private static final int LOWER = 2;
    private static final Map<String, String> map = makeTranslitMap();

    /* Initializes the transliteration mapping */
    private static Map<String, String> makeTranslitMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("a", "а");
        map.put("b", "б");
        map.put("v", "в");
        map.put("g", "г");
        map.put("d", "д");
        map.put("e", "е");
        map.put("yo", "ё");
        map.put("zh", "ж");
        map.put("z", "з");
        map.put("i", "и");
        map.put("j", "й");
        map.put("k", "к");
        map.put("l", "л");
        map.put("m", "м");
        map.put("n", "н");
        map.put("o", "о");
        map.put("p", "п");
        map.put("r", "р");
        map.put("s", "с");
        map.put("t", "т");
        map.put("u", "у");
        map.put("f", "ф");
        map.put("h", "х");
        map.put("ts", "ц");
        map.put("ch", "ч");
        map.put("sh", "ш");
        map.put("`", "ъ");
        map.put("y", "у");
        map.put("'", "ь");
        map.put("yu", "ю");
        map.put("ya", "я");
        map.put("x", "кс");
        map.put("w", "в");
        map.put("q", "к");
        map.put("iy", "ий");
        return map;
    }

    /* Determines the character case */
    private static int charClass(char c) {
        return Character.isUpperCase(c) ? UPPER : LOWER;
    }

    /* Retrieves the transliteration for a given string if present */
    private static Optional<String> get(String s) {
        final int charClass = charClass(s.charAt(0));
        return Optional.ofNullable(map.get(s.toLowerCase()))
                .map(result -> charClass == UPPER
                        ? (result.charAt(0) + "").toUpperCase() + (result.length() > 1 ? result.substring(1) : "")
                        : result
                );
    }

    /* Checks if the text is English based on the character set */
    private static boolean isEnglishText(String text) {
        return text.matches("^[a-zA-Z]+$");
    }

    /**
     * Transliterates the given English text to Cyrillic script based on specific rules and character mappings.
     * The transliteration occurs only for certain source languages and if the text is detected as English.
     *
     * @param text    The text to transliterate
     * @param srcLang The source language code
     * @return The transliterated text or original text if conditions aren't met
     */
    public static String translitToCyrillic(String text, String srcLang) {
        final List<String> languagesOnCyrillic = List.of("lez", "tab", "rus");
        if (!isEnglishText(text) || !languagesOnCyrillic.contains(srcLang)) {
            return text;
        }

        final int len = text.length();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ) {
            final String toTranslate = text.substring(i, Math.min(i + 2, len));
            final Optional<String> translatedOpt = get(toTranslate);

            if (translatedOpt.isPresent()) {
                sb.append(translatedOpt.get());
                i += toTranslate.length();
            } else {
                final Optional<String> singleCharOpt = get(toTranslate.charAt(0) + "");
                sb.append(singleCharOpt.orElseGet(() -> toTranslate.substring(0, 1)));
                i++;
            }
        }
        return sb.toString();
    }
}
