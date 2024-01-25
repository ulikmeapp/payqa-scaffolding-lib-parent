package com.ulikme.country.service.internal;

import com.ulikme.country.domain.models.CountryModel;
import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity;
import com.ulikme.country.infrastructure.persistence.mongo.mappers.CountryModelMapper;
import com.ulikme.country.infrastructure.persistence.mongo.repositories.CountryRepository;
import com.ulikme.country.service.CountryServiceJava;
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JavaCountryService implements CountryServiceJava {

    private final CountryRepository repository;

    @Override
    public List<CountryModel> listAll() {
        return CountryModelMapper.INSTANCE.inverseMap(repository.findAll());
    }

    @Override
    public CountryModel save(CountryModel country) {
        CountryEntity countryEntity = CountryModelMapper.INSTANCE.map(country);
        CountryEntity savedCountry = repository.save(countryEntity);
        return CountryModelMapper.INSTANCE.inverseMap(savedCountry);
    }

    @Override
    public void delete(String code) {
        if (!repository.existsByCode(code)) {
            throw new NotFoundException("Country with code " + code + " does not exists.");
        }
        repository.deleteByCode(code);
    }

}
