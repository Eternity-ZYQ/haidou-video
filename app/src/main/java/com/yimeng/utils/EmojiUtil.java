package com.yimeng.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : huiGer
 * Time   : 2018/11/12 0012 下午 03:53.
 * Desc   : 处理Emoji表情（解决数据库不支持utf8mb4的问题）
 */
public class EmojiUtil {
    //编码
    public static String emojiConvert(String str) throws UnsupportedEncodingException {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(
                        sb,
                        "[["
                                + URLEncoder.encode(matcher.group(1),
                                "UTF-8") + "]]");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw e;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //解码
    public static String emojiRecovery(String str) throws UnsupportedEncodingException {
        String patternString = "\\[\\[(.*?)\\]\\]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb,
                        URLDecoder.decode(matcher.group(1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw e;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 删除emj
     */
    public static String cleanEmoji(String str) {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb,"");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
