package com.igrecsys.proputil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class DtoCompareResult {

    @Getter @Setter
    private String differenceF1F2;

    @Getter @Setter
    private String differenceF2F1;
    @Getter @Setter
    private String differenceContentF1;

    @Getter @Setter
    private String differenceContentF2;

    @Getter @Setter
    private String propertyFile1;

    @Getter @Setter
    private String propertyFile2;

    @Getter @Setter
    private String resultMessage1;

    @Getter @Setter
    private String resultMessage2;

    @Getter @Setter
    private String resultMessage3;

    @Getter
    @Setter
    private String outputFileNameF1F2;

    @Getter
    @Setter
    private String outputFileNameF2F1;

    @Getter
    @Setter
    private String outputFileNameCompare;


}
