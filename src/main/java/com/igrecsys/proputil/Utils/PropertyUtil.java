package com.igrecsys.proputil.Utils;

import com.igrecsys.proputil.dto.DtoCompareResult;
import com.igrecsys.proputil.dto.DtoMergeResult;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertyUtil {

    public static DtoCompareResult compare(String src1, String src2) throws Exception {
        Properties file1 = new Properties();
        Properties file2 = new Properties();
        Properties outputf1f2 = new Properties();
        Properties outputf2f1 = new Properties();
        Properties compareResultF1 = new Properties();
        Properties compareResultF2 = new Properties();

        file1.load(new StringReader(src1));
        file2.load(new StringReader(src2));

        Set<Object> file1Keys = file1.keySet();
        Set<Object> file2Keys = file2.keySet();


        Set<Object> f1f2keys = new HashSet<>();
        f1f2keys.addAll(file1Keys);

        Set<Object> f2f1keys = new HashSet<>();
        f2f1keys.addAll(file2Keys);

        f1f2keys.removeAll(file2Keys);
        f2f1keys.removeAll(file1Keys);

        f1f2keys.forEach(k -> outputf1f2.setProperty(k.toString(), file1.getProperty(k.toString())));
        f2f1keys.forEach(k -> outputf2f1.setProperty(k.toString(), file2.getProperty(k.toString())));


        Set<Object> interseciont = file1Keys
                .stream()
                .filter(file2Keys::contains)
                .filter(k -> !file1.getProperty(k.toString()).equals(file2.getProperty(k.toString())))
                .collect(Collectors.toSet());


        String message1 = f1f2keys.size() + " properties found in Property File 1 that is not in Property File 2 ";


        String message2 = f2f1keys.size() + " properties found in Property File 2 that is not in Property File 1 ";

        String message3 = interseciont.size() + " properties have difference in content ";


        interseciont.forEach(q -> compareResultF1.setProperty(q.toString(), file1.getProperty(q.toString())));
        interseciont.forEach(q -> compareResultF2.setProperty(q.toString(), file2.getProperty(q.toString())));


        return new DtoCompareResult(getPropertyAsString(outputf1f2), getPropertyAsString(outputf2f1), getPropertyAsString(compareResultF1), getPropertyAsString(compareResultF2), src1, src2, message1, message2, message3);


    }


    public static DtoMergeResult merge(String src1, String src2) throws Exception {
        LinkedHashMap<String, String> file1 = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> file2 = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> output = new LinkedHashMap<String, String>();

        BufferedReader file1InputStream;
        BufferedReader file2InputStream;
        StringBuilder mergeResult = new StringBuilder();


        file1InputStream = new BufferedReader(new StringReader(src1));
        file2InputStream = new BufferedReader(new StringReader(src2));

        int i = 0;
        String line = file1InputStream.readLine();
        while (line != null) {
            try {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    file1.put(tokens[0].trim(), tokens[1]
                            .replaceAll("\"", "“")
                            .replaceAll("'", "`")
                            .trim());

                } else {
                    file1.put("comment" + i, line);
                    i++;

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            line = file1InputStream.readLine();
        }


        line = file2InputStream.readLine();
        while (line != null) {
            try {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    file2.put(tokens[0].trim(), tokens[1]
                            .replaceAll("\"", "“")
                            .replaceAll("'", "`")
                            .trim());

                } else {
                    file2.put("comment" + i, line);
                    i++;
                    System.out.println("comment " + line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            line = file2InputStream.readLine();
        }


        file1.forEach((k, v) -> output.put(k, v));
        file2.forEach((k, v) -> output.put(k, v)); //for java 8

        String message = "Merged Property 1  with " + file1.keySet().size()
                + " keys and  Property 2 with " + file2.keySet().size() + " keys to produce output of " + output.keySet().size() + " properties ";


        for (String key : output.keySet()) {

            if (key.startsWith("comment"))
                mergeResult.append(output.get(key) + "\n");
            else
                mergeResult.append(key + "  =  " + output.get(key) + "\n");
        }


        return new DtoMergeResult(mergeResult.toString(), src1, src2, message);


    }

    private static String getPropertyAsString(Properties prop) {

        StringBuffer result = new StringBuffer();
        prop.forEach((p, k) -> result.append(getFormattedProperty(p.toString(), k.toString())));
        return result.toString();
    }

    private static String getFormattedProperty(String p, String k) {
        return p + "  =  " + k.
                replaceAll("\"", "“")
                .replaceAll("'", "`")
                .trim() + "\r\n";
    }
}
