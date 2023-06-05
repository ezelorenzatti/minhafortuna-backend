package br.com.lorenzatti.minhafortuna.backend.bcbapi.service;

import br.com.lorenzatti.minhafortuna.backend.bcbapi.dto.BCBCurrency;
import br.com.lorenzatti.minhafortuna.backend.bcbapi.dto.BCBREsponse;
import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.repository.CurrencyRepository;
import br.com.lorenzatti.minhafortuna.backend.history.model.History;
import br.com.lorenzatti.minhafortuna.backend.history.repository.HistoryRepository;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;


@Configuration
@EnableScheduling
@Service
public class BcbApi {

    private Logger logger = LoggerFactory.getLogger(BcbApi.class);

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private HistoryRepository historyRepository;

    private static final String TIME_ZONE = "America/Sao_Paulo";

    private static final String PATTERN = "yyyyMMdd";

    private static final String URL_DAILY_VALUES = "https://www4.bcb.gov.br/Download/fechamento/{DATA}.csv";

    private static final String CURRENCIES = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/Moedas?%24format=json";

    @Scheduled(cron = "0 5 0 * * *", zone = TIME_ZONE)
    public void updateDailyValues() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final Date date = Utils.getDayBefore();
        try {
            logger.info("Iniciando atualizacaoo de referencias monetarias para " + sdf.format(date));
            Optional<File> dailyValues = Optional.ofNullable(downloadFile(date));
            if(dailyValues.isPresent()){
                importDailyValues(dailyValues.get());
            }
        } catch (Exception e) {
            logger.info("Nao ha referencias monetarias para " + sdf.format(date));
        }
    }

    public List<BCBCurrency> getCurrencies(){
        RestTemplate restTemplate = getRestTemplate();
        BCBREsponse bcbrEsponse = restTemplate.getForEntity(CURRENCIES, BCBREsponse.class).getBody();
        return bcbrEsponse.getValue();
    }



    public void updatePeriodValues(Date startDate, Date endDate) throws Exception {
        Date current = startDate;
        while (current.getTime() <= endDate.getTime()) {
            Optional<File> dailyValues = Optional.ofNullable(downloadFile(current));
            if(dailyValues.isPresent()){
                importDailyValues(dailyValues.get());
            }
            current = incrementDay(current, 1);
        }
    }

    private Date incrementDay(Date date, int numDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, numDays);
        return calendar.getTime();
    }

    private void importDailyValues(File dailyValues) throws Exception{
        List<Currency> currencies = currencyRepository.findAll();
        List<String> codes = new ArrayList<>();
        currencies.forEach(currency -> codes.add(currency.getCode()));
        try (Stream<String> stream = Files.lines(Paths.get(dailyValues.getAbsolutePath()), StandardCharsets.ISO_8859_1)) {
            stream.map(this::buildHistory)
                    .filter(history -> codes.contains(history.getCode()))
                    .forEach(history -> historyRepository.save(history));
        } catch (IOException e) {
            throw e;
        }
    }

    private History buildHistory(String line) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] split = line.split(";");
        History history = new History();
        try {
            history.setDate(sdf.parse(split[0]));
            history.setCode(split[3]);
            history.setBuy(Double.parseDouble(split[4].replace(",", ".")));
            history.setSell(Double.parseDouble(split[5].replace(",", ".")));
        } catch (Exception e) {
        }
        return history;
    }


    private File downloadFile(Date data) {
        String url = getUrl(data, URL_DAILY_VALUES);
        File file = null;
        try {
            RestTemplate restTemplate = getRestTemplate();
            file = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
                File ret = File.createTempFile("download", ".csv");
                StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                return ret;
            });
        } catch (Exception e) {
        }
        return file;
    }

    private String getUrl(Date date, String url) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return url.replace("{DATA}", sdf.format(date));
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
