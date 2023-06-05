package br.com.lorenzatti.minhafortuna.backend.history.controller;

import br.com.lorenzatti.minhafortuna.backend.bcbapi.service.BcbApi;
import br.com.lorenzatti.minhafortuna.backend.history.dto.HistoryDto;
import br.com.lorenzatti.minhafortuna.backend.history.model.History;
import br.com.lorenzatti.minhafortuna.backend.history.service.HistoryService;
import br.com.lorenzatti.minhafortuna.backend.shared.converter.DataConverter;
import br.com.lorenzatti.minhafortuna.backend.shared.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("history")
public class HistoryRestController {

    @Autowired
    private DataConverter dataConverter;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BcbApi bcbApi;

    @GetMapping
    public ResponseEntity<Response> history(@RequestParam(name = "code") String code, @RequestParam(name = "start") String start, @RequestParam(name = "end") String end) {
        Response<List<HistoryDto>> response = new Response<>();
        Date startDate = dataConverter.toDate(start);
        Date endDate = dataConverter.toDate(end);
        List<HistoryDto> historyDtos = new ArrayList<>();
        List<History> historyList = historyService.getHistory(code, startDate, endDate);
        historyList.forEach(history -> {
            HistoryDto historyDto = new HistoryDto();
            historyDto.setCode(history.getCode());
            historyDto.setDate(history.getDate());
            historyDto.setBuy(history.getBuy());
            historyDto.setSell(history.getSell());
            historyDtos.add(historyDto);
        });
        response.setSuccess(true);
        response.setData(historyDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/fillPeriod")
    public ResponseEntity<Response> fillPeriod(@RequestParam(name = "start") String start, @RequestParam(name = "end") String end, @RequestParam(name = "pattern") String pattern) {
        Response<List<String>> response = new Response<>();
        try {
            Date startDate = dataConverter.toDate(start, pattern);
            Date endDate = dataConverter.toDate(end, pattern);
            bcbApi.updatePeriodValues(startDate, endDate);
            response.setSuccess(true);
            response.setMessage("Período atualizado!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError("Falha ao atualizar o período");
            return ResponseEntity.badRequest().body(response);
        }
    }


}
