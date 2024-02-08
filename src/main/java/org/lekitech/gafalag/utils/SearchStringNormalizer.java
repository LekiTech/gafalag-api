package org.lekitech.gafalag.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchStringNormalizer {

    public String replaceVerticalBar(String line) {
        Pattern pattern = Pattern.compile("([кптцчКПТЦЧ][i1lӏ|!])");
        Matcher matcher = pattern.matcher(line);
        return matcher.replaceAll(m -> m.group().replace('!', 'I').replace('1', 'I'));
    }
}
