package com.ulikme.country.api.facade;

import com.ulikme.country.domain.models.CountryModel;
import com.ulikme.country.domain.rr.request.CountryRequest;

public interface CountryFacadeJava {

    CountryModel register(CountryRequest request);

    CountryModel update(String code, CountryRequest request);

}
