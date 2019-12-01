package com.igrecsys.proputil.Utils;

import com.igrecsys.proputil.dto.DtoCompareResult;
import com.igrecsys.proputil.dto.DtoMergeResult;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

        f1f2keys.forEach(k -> setToProperty(k, file1.getProperty(k.toString()), outputf1f2));
        f2f1keys.forEach(k -> setToProperty(k, file2.getProperty(k.toString()), outputf2f1));


        Set<Object> interseciont = file1Keys
                .stream()
                .filter(file2Keys::contains)
                .filter(k -> !file1.getProperty(k.toString()).equals(file2.getProperty(k.toString())))
                .collect(Collectors.toSet());


        String message1 = f1f2keys.size() + " properties found in Property File 1 that is not in Property File 2 ";


        String message2 = f2f1keys.size() + " properties found in Property File 2 that is not in Property File 1 ";

        String message3 = interseciont.size() + " properties have difference in content ";


        // interseciont.forEach(q -> compareResultF1.setProperty(q.toString(), file1.getProperty(q.toString())));
        interseciont.forEach(q -> setToProperty(q, file1.getProperty(q.toString()), compareResultF1));
        interseciont.forEach(q -> setToProperty(q, file2.getProperty(q.toString()), compareResultF2));
        //  interseciont.forEach(q -> compareResultF2.setProperty(q.toString(), file2.getProperty(q.toString())));


        String fName = "files/t";
        String fNameF1F2 = "files/t1";
        String fNameF2F1 = "files/t2";
        String fNameCompareFile = "files/t3";
        String fNameF2 = "files/t4";

        Boolean exist = true;
        File outputFile;

        while (exist) {

            fName = "files/Output" + System.currentTimeMillis();
            fNameF1F2 = fName + "F1F2.properties";
            fNameF2F1 = fName + "F2F1.properties";
            fNameCompareFile = fName + "F1.properties";
            fNameF2 = fName + "F2.properties";

            outputFile = new File(fNameF1F2);
            if (!outputFile.createNewFile())
                continue;

            outputFile = new File(fNameF2F1);
            if (!outputFile.createNewFile())
                continue;
            outputFile = new File(fNameCompareFile);
            if (!outputFile.createNewFile())
                continue;

            exist = false;
        }

        OutputStreamWriter outputStream;
        outputStream = new OutputStreamWriter(new FileOutputStream(fNameF1F2), "UTF-8");
        outputf1f2.store(outputStream, message1);
        outputStream.close();
        outputStream = new OutputStreamWriter(new FileOutputStream(fNameF2F1), "UTF-8");
        outputf2f1.store(outputStream, message2);
        outputStream.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fNameCompareFile), "UTF-8"));

        bufferedWriter.write("#" + message3);
        compareResultF1.forEach((k, p) -> {
            try {
                bufferedWriter.write("\r\n" + k + " = " + p + "     |  " + compareResultF2.getProperty(k.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriter.flush();
        bufferedWriter.close();


        return new DtoCompareResult(getPropertyAsString(outputf1f2), getPropertyAsString(outputf2f1), getPropertyAsString(compareResultF1), getPropertyAsString(compareResultF2), src1, src2, message1, message2, message3, fNameF1F2, fNameF2F1, fNameCompareFile);


    }

    private static void setToProperty(Object k, String property, Properties outputf1f2) {
        if (null == property)
            property = "";
        if (k.toString().trim().length() != 0)
            outputf1f2.setProperty(k.toString(), property);
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
                            .replaceAll("\"", "\\\\u201C")
                            .replaceAll("'", "\\\\u2019")
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
                            .replaceAll("\"", "\\\\u201C")
                            .replaceAll("'", "\\\\u2019")
                            .trim());

                } else {
                    file2.put("comment" + i, line);
                    i++;
                    // System.out.println("comment " + line);
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

        Boolean exist = true;
        File outputFile;
        String fName = "files/t";

        while (exist) {

            fName = "files/Output" + System.currentTimeMillis() + ".properties";

            outputFile = new File(fName);
            if (!outputFile.createNewFile())
                continue;


            exist = false;
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fName), "UTF-8"));


        for (String key : output.keySet()) {

            if (key.startsWith("comment"))
                bufferedWriter.write(output.get(key) + "\r\n");
            else
                bufferedWriter.write(key + "  =  " + output.get(key) + "\r\n");
        }

        bufferedWriter.flush();
        bufferedWriter.close();

        return new DtoMergeResult(mergeResult.toString(), src1, src2, message, fName);


    }


    private static String getPropertyAsString(Properties prop) {

        StringBuffer result = new StringBuffer();
        prop.forEach((p, k) -> result.append(getFormattedProperty(p.toString(), k.toString())));
        return result.toString();
    }

    private static String getFormattedProperty(String p, String k) {
        return p + "  =  " + k
                .replaceAll("\"", "\\\\u201C")
                .replaceAll("'", "\\\\u2019")
                .trim() + "\r\n";
    }

    public static String multipartToString(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();

        while (null != line) {
            sb.append(line + "\r\n");
            line = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    public static String processRequest(String src1, String src2, Model model, String mode) {
        if (mode.equals("compare")) {

            DtoCompareResult dtoCompareResult = null;
            try {
                dtoCompareResult = PropertyUtil.compare(src1, src2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("dataresult", dtoCompareResult);

            return "compareUpload";

        } else {

            DtoMergeResult dtoMergeResult;
            dtoMergeResult = null;
            try {
                dtoMergeResult = PropertyUtil.merge(src1, src2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("dataresult", dtoMergeResult);


            return "mergeUpload";
        }
    }
}
