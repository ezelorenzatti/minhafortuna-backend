package br.com.lorenzatti.minhafortuna.backend.bcbapi.service;

import br.com.lorenzatti.minhafortuna.backend.ativo.model.Ativo;
import br.com.lorenzatti.minhafortuna.backend.ativo.repository.AtivoRepository;
import br.com.lorenzatti.minhafortuna.backend.historico.model.Historico;
import br.com.lorenzatti.minhafortuna.backend.historico.repository.HistoricoRepository;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;


@Configuration
@EnableScheduling
public class BcbApi {

    private Logger logger = LoggerFactory.getLogger(BcbApi.class);

    @Autowired
    private AtivoRepository ativoRepository;

    @Autowired
    private HistoricoRepository historicoRepository;

    private static final String TIME_ZONE = "America/Sao_Paulo";

    private static final String PATTERN = "yyyyMMdd";

    private static final String FECHAMENTO = "https://www4.bcb.gov.br/Download/fechamento/{DATA}.csv";

    @Scheduled(cron = "0 1 0 * * *", zone = TIME_ZONE)
    public void atualizarReferencias() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final Date data = this.getDiaAnterior();
        try {
            logger.info("Iniciando atualização de referências monetárias para " + sdf.format(data));
            File fechamento = downloadFile(data);
            cadastrarFechamento(fechamento);
        } catch (Exception e) {
            logger.info("Não há referências monetárias para " + sdf.format(data));
        }
    }

    private void cadastrarFechamento(File fechamento) {
        List<Ativo> ativos = ativoRepository.findAll();
        List<String> siglas = new ArrayList<>();
        ativos.forEach(ativo -> siglas.add(ativo.getSigla()));
        try (Stream<String> stream = Files.lines(Paths.get(fechamento.getAbsolutePath()), StandardCharsets.ISO_8859_1)) {
            stream.map(this::buildHistorico)
                    .filter(historico -> siglas.contains(historico.getSigla()))
                    .forEach(historico -> historicoRepository.save(historico));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Historico buildHistorico(String line) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] split = line.split(";");
        Historico historico = new Historico();
        try {
            historico.setData(sdf.parse(split[0]));
            historico.setSigla(split[3]);
            historico.setCompra(Double.parseDouble(split[4].replace(",", ".")));
            historico.setVenda(Double.parseDouble(split[5].replace(",", ".")));
        } catch (Exception e) {
        }
        return historico;
    }


    private File downloadFile(Date data) {
        String url = getUrl(data, FECHAMENTO);
        File file = null;
        try {
            RestTemplate restTemplate = getRestTemplate();
            file = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
                File ret = File.createTempFile("download", "csv");
                StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                return ret;
            });
        } catch (Exception e) {
            logger.info("Referências não disponíveis");
        }
        return file;
    }

    private String getUrl(Date data, String url) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return url.replace("{DATA}", sdf.format(data));
    }

    private Date getDiaAnterior() {
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
