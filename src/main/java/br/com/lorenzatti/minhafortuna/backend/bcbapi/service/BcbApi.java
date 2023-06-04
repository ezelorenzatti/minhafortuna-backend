package br.com.lorenzatti.minhafortuna.backend.bcbapi.service;

import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.currency.repository.CurrencyRepository;
import br.com.lorenzatti.minhafortuna.backend.history.model.History;
import br.com.lorenzatti.minhafortuna.backend.history.repository.HistoryRepository;
import br.com.lorenzatti.minhafortuna.backend.shared.exception.DataParseException;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.DataUtils;
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

    @Scheduled(cron = "0 3 0 * * *", zone = TIME_ZONE)
    public void updateDailyValues() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final Date date = this.getDayBefore();
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

    public void updatePeriodValues(Date startDate, Date endDate) throws Exception {
        Date current = startDate;
        while (current.getTime() < endDate.getTime()) {
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
            logger.info("Referencias nao disponiveis para " + url);
        }
        return file;
    }

    private String getUrl(Date date, String url) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return url.replace("{DATA}", sdf.format(date));
    }

    private Date getDayBefore() {
        Date data = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DATE, -1);
        data = calendar.getTime();
        return data;
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
