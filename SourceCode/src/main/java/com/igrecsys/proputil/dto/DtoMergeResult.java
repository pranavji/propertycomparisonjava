package com.igrecsys.proputil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class DtoMergeResult {

    @Getter
    @Setter
    private String mergeResult;

    @Getter @Setter
    private String propertyFile1;

    @Getter @Setter
    private String propertyFile2;

    @Getter @Setter
    private String resultMessage;

}
