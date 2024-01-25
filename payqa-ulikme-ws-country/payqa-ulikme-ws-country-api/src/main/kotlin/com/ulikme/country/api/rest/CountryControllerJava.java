package com.ulikme.country.api.rest;

import com.ulikme.country.api.facade.CountryFacadeJava;
import com.ulikme.country.domain.models.CountryModel;
import com.ulikme.country.domain.rr.request.CountryRequest;
import com.ulikme.country.infrastructure.persistence.mongo.entities.CountryEntity;
import com.ulikme.country.service.CountryService;
import com.ulikme.country.service.CountryServiceJava;
import dev.payqa.scaffolding.apicrud.design.exceptions.http.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
public class CountryControllerJava {

    private final CountryServiceJava countryService;
    private final CountryFacadeJava countryFacade;

    @GetMapping
    public ResponseEntity<List<CountryModel>> listAll() {
        return ResponseEntity.ok(countryService.listAll());
    }

    @PostMapping
    public ResponseEntity<CountryModel> register(@RequestBody CountryRequest request) {
        return ResponseEntity.ok(countryFacade.register(request));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        countryService.delete(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}