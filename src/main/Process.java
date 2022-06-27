package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {
    private static final int INIT_CAPACITY = 100;

    public Map<Integer, ArrayList<String>> parsedLogs = new HashMap<Integer, ArrayList<String>>(INIT_CAPACITY);
    Pattern datePattern, canPattern, idPattern, bytePattern, messagePattern, bytesPattern;

    public Process() {
        initPatterns();
    }

    public void initPatterns() {
        datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{6}", Pattern.CASE_INSENSITIVE);
        canPattern = Pattern.compile("\\s(\\w+)\\s", Pattern.CASE_INSENSITIVE);
        idPattern = Pattern.compile("\\s(\\d{3})\\s", Pattern.CASE_INSENSITIVE);
        bytePattern = Pattern.compile("\\[\\d{1}\\]", Pattern.CASE_INSENSITIVE);
        messagePattern = Pattern.compile("\\w{2}\\s\\w{2}\\s\\w{2}\\s\\w{2}\\s\\w{2}\\s\\w{2}\\s\\w{2}\\s\\w{2}", Pattern.CASE_INSENSITIVE);
        bytesPattern = Pattern.compile("(\\w{2})\\s(\\w{2})\\s(\\w{2})\\s(\\w{2})\\s(\\w{2})\\s(\\w{2})\\s(\\w{2})\\s(\\w{2})", Pattern.CASE_INSENSITIVE);
    }

    public void findMatch(ArrayList<String> readedLog) {
        Matcher dateMatcher; // datePattern.matcher(log);
        Matcher canMatcher;
        Matcher idMatcher;
        Matcher byteMatcher;
        Matcher messageMatcher;

        int log_id = 0;

        for (String l : readedLog) {
            ArrayList<String> data = new ArrayList<>(5);

            dateMatcher = datePattern.matcher(l);
            canMatcher = canPattern.matcher(l);
            idMatcher = idPattern.matcher(l);
            byteMatcher = bytePattern.matcher(l);
            messageMatcher = messagePattern.matcher(l);

            dateMatcher.find();
            data.add(dateMatcher.group());

            canMatcher.find();
            data.add(canMatcher.group(1));

            idMatcher.find();
            data.add(idMatcher.group(1));

            byteMatcher.find();
            data.add(byteMatcher.group());

            messageMatcher.find();
            data.add(messageMatcher.group());

            parsedLogs.put(log_id, data);
            log_id++;
        }
    }

    public void printParsedLogs() {
        for (Map.Entry<Integer, ArrayList<String>> entry : parsedLogs.entrySet()) {
            int key = entry.getKey();

            System.out.println("***************");
            System.out.println("ID: " + key);

            for (String l : entry.getValue()) {
                System.out.println(l);
            }
        }
    }

    public void analyzeChademo() {
        for (Map.Entry<Integer, ArrayList<String>> entry : parsedLogs.entrySet()) {
            int key = entry.getKey();

            int index = 0;
            for (String l : entry.getValue()) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        Matcher bytesMatcher = bytesPattern.matcher(l);
                        bytesMatcher.find();
                        for (int i = 1; i <= bytesMatcher.groupCount(); i++) {
                            String bytes = bytesMatcher.group(i);
                            String reverseBytes = new StringBuilder(bytes).reverse().toString();
//
//                            System.out.println("*****BYTE " + i + "********");
//                            System.out.println(Integer.decode("0x" + reverseBytes.toLowerCase(Locale.ROOT)));
//                            System.out.println("********************");
                        }
                        break;
                }
                index++;
            }
        }
    }

}
