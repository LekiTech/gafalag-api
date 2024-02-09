package org.lekitech.gafalag.utils;

public class SearchStringNormalizer {

    /**
     * This method can be static because it is a "pure function"
     * and does not change the "state" outside its scope.
     * @param line
     * @return
     */
    public static String replaceVerticalBar(String line) {
        return line.replaceAll("(?<=[кптцчКПТЦЧ])[i1lӏ|!]", "I");
    }
}
