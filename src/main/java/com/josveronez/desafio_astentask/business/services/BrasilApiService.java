package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.BrasilAPIResponseDTO;
import com.josveronez.desafio_astentask.business.exceptions.ExternalAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class BrasilApiService {

    private static final Logger log = LoggerFactory.getLogger(BrasilApiService.class);
    private final RestClient restClient;

    public BrasilApiService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://brasilapi.com.br/api").build();
    }


    @Cacheable(value = "feriados", key = "#ano")
    public List<BrasilAPIResponseDTO> buscarFeriados(int ano) {
        return restClient.get()
                .uri("/feriados/v1/{ano}", ano)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ExternalAPIException("Feriados para o ano " + ano + " não encontrados na API externa.");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ExternalAPIException("Erro de comunicação com o serviço de feriados.");
                })
                .body(new ParameterizedTypeReference<List<BrasilAPIResponseDTO>>() {});
    }

}
