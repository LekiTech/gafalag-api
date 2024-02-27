package org.lekitech.gafalag.utils;

public class SearchStringNormalizer {

    /**
     * Replaces certain characters in a given line with the uppercase letter "I".
     * The characters that will be replaced are "i", "1", "l", "ӏ", "|" and "!"
     * if they are preceded by any of the following characters:
     * "к", "п", "т", "ц", "ч", "К", "П", "Т", "Ц" or "Ч".
     * <p>
     * This normalization method is tested and works for the Lezgi and Tabasaran languages.
     * The other languages may not fit.
     * <p>
     * This method can be static because it is a "pure function"
     * and does not change the "state" outside its scope.
     *
     * @param line the input line where replacements will be made.
     * @return a new string with the specified characters replaced by "I".
     */
    public static String replaceVerticalBar(String line) {
        return line.replaceAll("(?<=[кптцчКПТЦЧ])[i1lӏ|!]", "I");
    }

    /**
     * Trims excess spaces in a given line by replacing consecutive whitespace characters with a single space
     * and then trimming any leading or trailing spaces.
     *
     * @param line the input line where excess spaces will be trimmed.
     * @return a new string with excess spaces removed.
     */
    public static String trimSpaces(String line) {
        return line.replaceAll("\\s+", " ").trim();
    }

    /**
     * Normalizes a given string by trimming excess spaces and replacing vertical bars with a single space.
     *
     * @param line the input string to be normalized.
     * @return a new string with excess spaces removed and vertical bars replaced by a single space.
     */
    public static String normalizeString(String line) {
        return replaceVerticalBar(trimSpaces(line));
    }
}
