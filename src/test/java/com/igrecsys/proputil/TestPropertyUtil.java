package com.igrecsys.proputil;

import com.igrecsys.proputil.Utils.PropertyUtil;
import com.igrecsys.proputil.dto.DtoCompareResult;
import com.igrecsys.proputil.dto.DtoMergeResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestPropertyUtil {


    @Test
    public void testCompare() {
        String prop1 =
                "title\t = \tDigital Document and Filing System (DDFS)\tNumérique de documents et système de classement (DDFS)\r\n" +
                        "greeting\t = \tGreetings, it is now\tBonjour, il est maintenant\r\n" +
                        "common.ddfsTitle\t = \tDigital Document Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n" +
                        "common.status.active\t = \tActive\tactif";
        String prop2 =
                "common.ddfsTitle\t = \tDigital govind Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n" +
                        "common.status.active\t = \tActive\tactif\r\n" +
                        "common.designation\t = \tDesignation\tLa désignation\r\n" +
                        "common.mail\t = \tMail\tCourrier\r\n" +
                        "common.sex.female\t = \tFemale\tFemme";
        DtoCompareResult result = null;
        try {
            result = PropertyUtil.compare(prop1, prop2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(result);
        assertEquals(result.getResultMessage1(), "2 properties found in Property File 1 that is not in Property File 2 ");
        assertEquals(result.getResultMessage2(), "3 properties found in Property File 2 that is not in Property File 1 ");
        assertEquals(result.getResultMessage3(), "1 properties have difference in content ");
        assertEquals(result.getDifferenceF1F2(), "title  =  Digital Document and Filing System (DDFS)\tNumérique de documents et système de classement (DDFS)\r\n" +
                "greeting  =  Greetings, it is now\tBonjour, il est maintenant\r\n");
        assertEquals(result.getDifferenceF2F1(), "common.mail  =  Mail\tCourrier\r\n" +
                "common.sex.female  =  Female\tFemme\r\n" +
                "common.designation  =  Designation\tLa désignation\r\n");
        assertEquals(result.getDifferenceContentF1(), "common.ddfsTitle  =  Digital Document Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n");
        assertEquals(result.getDifferenceContentF2(), "common.ddfsTitle  =  Digital govind Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n");
        assertEquals(prop1, result.getPropertyFile1());
        assertEquals(prop2, result.getPropertyFile2());
    }

    @Test
    public void testMerge() {

        String prop1 =
                "title\t = \tDigital Document and Filing System (DDFS)\tNumérique de documents et système de classement (DDFS)\r\n" +
                        "greeting\t = \tGreetings, it is now\tBonjour, il est maintenant\r\n" +
                        "common.ddfsTitle\t = \tDigital Document Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n" +
                        "common.status.active\t = \tActive\tactif";
        String prop2 =
                "common.ddfsTitle\t = \tDigital govind Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n" +
                        "common.status.active\t = \tActive\tactif\r\n" +
                        "common.designation\t = \tDesignation\tLa désignation\r\n" +
                        "common.mail\t = \tMail\tCourrier\r\n" +
                        "common.sex.female\t = \tFemale\tFemme";

        DtoMergeResult result = null;

        try {
            result = PropertyUtil.merge(prop1, prop2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(result);
        assertEquals(result.getMergeResult(), "title  =  Digital Document and Filing System (DDFS)\tNumérique de documents et système de classement (DDFS)\n" +
                "greeting  =  Greetings, it is now\tBonjour, il est maintenant\n" +
                "common.ddfsTitle  =  Digital govind Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\n" +
                "common.status.active  =  Active\tactif\n" +
                "common.designation  =  Designation\tLa désignation\n" +
                "common.mail  =  Mail\tCourrier\n" +
                "common.sex.female  =  Female\tFemme\n");
        assertEquals(result.getResultMessage(), "Merged Property 1  with 4 keys and  Property 2 with 5 keys to produce output of 7 properties ");

        assertEquals(prop1, result.getPropertyFile1());
        assertEquals(prop2, result.getPropertyFile2());
    }

}
