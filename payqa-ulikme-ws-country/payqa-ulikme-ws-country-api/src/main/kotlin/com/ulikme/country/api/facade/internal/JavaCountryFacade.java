package com.ulikme.country.api.facade.internal;

import com.ulikme.country.api.facade.CountryFacadeJava;
import com.ulikme.country.domain.models.CountryModel;
import com.ulikme.country.domain.rr.request.CountryRequest;
import com.ulikme.country.service.CountryService;
import com.ulikme.country.service.CountryServiceJava;
import dev.payqa.scaffolding.apicrud.data.utils.Validator;
import dev.payqa.scaffolding.apicrud.design.exceptions.ApiException;
import dev.payqa.scaffolding.apicrud.design.exceptions.ExceptionDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JavaCountryFacade implements CountryFacadeJava {

    private final CountryServiceJava countryServiceJava;
    private final CountryService countryService;

    @Override
    public CountryModel register(CountryRequest request) {
        Validator.INSTANCE.checkNotNull(request.getCode(), CountryRequest.Fields.code, null);
        Validator.INSTANCE.checkNotEmpty(request.getCode(), CountryRequest.Fields.code, null);
        Validator.INSTANCE.checkNotNull(request.getName(), CountryRequest.Fields.name, null);
        Validator.INSTANCE.checkNotEmpty(request.getName(), CountryRequest.Fields.name, null);

        /*if (countryService.findByCodeOrNull(request.getCode()) != null) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    new ExceptionDetail(
                            HttpStatus.CONFLICT.value(),
                            "alreadyExistsCode",
                            "Already exists a country with code :" + request.getCode()
                    ),
                    null
            );
        }*/

        String phoneCode;
        if (request.getPhoneCode() == null) {
            phoneCode = "";
        } else {
            phoneCode = request.getPhoneCode();
        }

        CountryModel country = new CountryModel(
                request.getCode(),
                request.getName(),
                phoneCode
        );
        return countryServiceJava.save(country);
    }

    @Override
    public CountryModel update(String code, CountryRequest request) {
        CountryModel existentCountry = countryService.findByCode(code);

        String name;
        if (request.getName() != null) {
            name = request.getName();
        } else {
            name = existentCountry.getName();
        }

        String phoneCode;
        if (request.getPhoneCode() != null) {
            phoneCode = request.getPhoneCode();
        } else {
            phoneCode = existentCountry.getPhoneCode();
        }

        CountryModel newCountry = new CountryModel(code, name, phoneCode);

        return countryServiceJava.save(newCountry);
    }
}
