package com.discord.api.message_scheduler.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.discord.api.message_scheduler.model.Webhook;

@Service
public class ScheduleMessageService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${proxy.url}")
    private String proxyUrl;

    @Value("${discord.scheduled.message}")
    private String messageToSend;

    // cron = "second minute hour <day of month> month day(0-7)"
    // "0 0 12 * * 2" = Wednesday's at 12pm
    @Scheduled(cron = "${discord.cron.schedule}")
    public void sendMessageOnSchedule() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("Scheduled message sent at " + timestamp);
        Webhook webhook = new Webhook();
        webhook.setContent(messageToSend);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Webhook> request = new HttpEntity<Webhook>(webhook, headers);
        restTemplate.postForEntity(proxyUrl, request, Webhook.class);
    }

}
