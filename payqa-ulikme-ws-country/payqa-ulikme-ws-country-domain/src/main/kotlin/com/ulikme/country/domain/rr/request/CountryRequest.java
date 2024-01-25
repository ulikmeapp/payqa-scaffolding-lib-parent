package com.ulikme.country.domain.rr.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class CountryRequest {

    private String code;
    private String name;
    private String phoneCode;

}
