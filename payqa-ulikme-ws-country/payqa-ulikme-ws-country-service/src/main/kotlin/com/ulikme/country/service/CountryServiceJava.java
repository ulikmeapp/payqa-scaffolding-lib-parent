package com.ulikme.country.service;

import com.ulikme.country.domain.models.CountryModel;

import java.util.List;

public interface CountryServiceJava {

    List<CountryModel> listAll();

    CountryModel save(CountryModel country);


    void delete(String code);

}
