package br.com.lorenzatti.minhafortuna.backend.restcountries.service;

import br.com.lorenzatti.minhafortuna.backend.bcbapi.service.BcbApi;
import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.repository.CurrencyRepository;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.ColorUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestCountriesApi {

    private Logger logger = LoggerFactory.getLogger(BcbApi.class);

    @Autowired
    private CurrencyRepository currencyRepository;

    private static final String REST_COUNTRIES_URL = "https://restcountries.com/v3.1/all";

    public void fill() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<Object[]> response = restTemplate.getForEntity(REST_COUNTRIES_URL, Object[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Object[] countries = response.getBody();
            for (Object countryObj : countries) {
                if (countryObj instanceof java.util.Map) {
                    try {

                        @SuppressWarnings("unchecked")
                        java.util.Map<String, Object> country = (java.util.Map<String, Object>) countryObj;
                        java.util.Map<String, Object> currencies = (java.util.Map<String, Object>) country.get("currencies");
                        if (currencies != null) {
                            Currency currency = new Currency();
                            for (String currencyCode : currencies.keySet()) {
                                java.util.Map<String, Object> currencyMap = (java.util.Map<String, Object>) currencies.get(currencyCode);
                                String currencyName = currencyMap.get("name").toString();
                                currency.setCode(currencyCode);
                                currency.setName(currencyName);

                                currency.setCustom(false);

                                Optional<Currency> currencyOpt = Optional.ofNullable(currencyRepository.findByCode(currency.getCode()));
                                if (currencyOpt.isPresent()) {
                                    logger.info("Moeda ja cadastrada " + currencyOpt.get());
                                } else {

                                    List<Currency> allCurrencies = currencyRepository.findAll();
                                    List<String> ignoreColors = new ArrayList<>();
                                    for (Currency item : allCurrencies) {
                                        String color = item.getColor();
                                        ignoreColors.add(color);
                                    }
                                    List<String> colors = ColorUtils.generateRandomColors(1, ignoreColors);
                                    currency.setColor(colors.get(0));

                                    currencyRepository.save(currency);
                                }

                            }
                        }
                    } catch (Exception e) {
                        logger.error("Erro ao obter dados", e);
                    }
                }
            }
        } else {
            throw new Exception("Falha ao obter moedas, response code: " + response.getStatusCodeValue());
        }

    }


    private static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        restTemplate.getMessageConverters().add(jsonConverter);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}
