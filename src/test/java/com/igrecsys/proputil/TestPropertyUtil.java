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
                "title\t = \tDigital Document 'and' Filing System (DDFS)\tNumérique de documents et système de classement (DDFS)\r\n" +
                        "greeting\t = \tGreetings, it is now\tBonjour, il est maintenant\r\n" +
                        "common.ddfsTitle\t = \tDigital Document Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n" +
                        "common.status.active\t = \tActive\tactif";
        String prop2 =
                "common.ddfsTitle\t = \tDigital \"govind\" Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n" +
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
        assertEquals( "2 properties found in Property File 1 that is not in Property File 2 ",result.getResultMessage1());
        assertEquals( "3 properties found in Property File 2 that is not in Property File 1 ",result.getResultMessage2());
        assertEquals( "1 properties have difference in content ",result.getResultMessage3());
        assertEquals("title  =  Digital Document \\u2019and\\u2019 Filing System (DDFS)\tNumérique de documents et système de classement (DDFS)\r\n" +
                "greeting  =  Greetings, it is now\tBonjour, il est maintenant\r\n",result.getDifferenceF1F2());
        assertEquals( "common.mail  =  Mail\tCourrier\r\n" +
                "common.sex.female  =  Female\tFemme\r\n" +
                "common.designation  =  Designation\tLa désignation\r\n",result.getDifferenceF2F1());
        assertEquals( "common.ddfsTitle  =  Digital Document Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n" , result.getDifferenceContentF1());
        assertEquals( "common.ddfsTitle  =  Digital \\u201Cgovind\\u201C Filing System (DDFS)\tDépôt du document numérique Système (DDFS)\r\n", result.getDifferenceContentF2());
        assertEquals(result.getPropertyFile1(), prop1 );
        assertEquals(result.getPropertyFile2(),prop2);
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
