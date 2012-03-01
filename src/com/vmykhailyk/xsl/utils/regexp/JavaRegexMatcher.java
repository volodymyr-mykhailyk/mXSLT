package com.vmykhailyk.xsl.utils.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 23.03.11
 * Time: 12:04
 */
public class JavaRegexMatcher implements RegExpMatcherInterface {
    private Pattern pattern;
    private Matcher matcher;

    public JavaRegexMatcher(String regex) {
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher("");
    }

    public JavaRegexMatcher(String regex, int theFlags) {
        pattern = Pattern.compile(regex, theFlags);
        matcher = pattern.matcher("");
    }

    @Override
    public boolean test(String source) {
        matcher.reset(source);
        return matcher.find();
    }
}
