package com.sejong.sejongpeer.infra.sms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/** SMS 메시지를 보내는 서비스를 제공합니다. Aligo API를 사용하여 SMS를 전송합니다. */
@Service
public class AligoSmsService {
    private static final String ALIGO_BASE_URL = "https://apis.aligo.in";

    private final WebClient webClient;

    @Value("${aligo.api.key}")
    private String apiKey;

    @Value("${aligo.api.userId}")
    private String apiUserId;

    @Value("${aligo.api.sender}")
    private String sender;

    @Autowired
    public AligoSmsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ALIGO_BASE_URL).build();
    }

    /**
     * @param receiver 받는사람의 전화번호 010xxxxyyyy 형식
     * @param msg 보낼 메시지 90byte 한글 최대 45자 가능
     * @return 전송결과 또는 오류
     */
    public String sendSms(String receiver, String msg) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("key", apiKey);
        formData.add("user_id", apiUserId);
        formData.add("sender", sender);
        formData.add("receiver", receiver);
        formData.add("msg", msg);

        return webClient
                .post()
                .uri("/send/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
