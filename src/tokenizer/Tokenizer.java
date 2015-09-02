package tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import utils.HtmlUtils;
import utils.RegexUtils;
import utils.UnicodeUtils;

public class Tokenizer {
	
    public static List<String> tokenize(String str) {
        
        // Step 2) Trim text
        str = str.trim();

        // Step 3) Replace Unicode symbols \u0000
        if (UnicodeUtils.containsUnicode(str)) {
            str = UnicodeUtils.replaceUnicodeSymbols(str);
        }

        // Step 4) Replace HTML symbols &#[0-9];
        if (HtmlUtils.containsHtml(str)) {
            str = HtmlUtils.replaceHtmlSymbols(str);
        }

        // Step 5) Tokenize
        List<String> tokens = new ArrayList<>(); 
        Matcher m = RegexUtils.TOKENIZER_PATTERN.matcher(str);
        while (m.find()) {
          tokens.add(m.group());
        }

        return tokens;
    }
}
