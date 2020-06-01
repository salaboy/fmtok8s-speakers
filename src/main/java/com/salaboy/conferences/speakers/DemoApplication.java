package com.salaboy.conferences.speakers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@EnableZeebeClient
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Value("${version:0.0.0}")
    private String version;

    @Autowired
    private ZeebeClientLifecycle client;


    @GetMapping("/info")
    public String getInfo() {
        return "{ \"name\" : \"Speakers Service\", \"version\" : \"" + version + "\", \"source\": \"https://github.com/salaboy/fmtok8s-speakers/releases/tag/v" + version + "\" }";
    }

    @PostMapping("/{proposalId}")
    public String confirmAcceptance(@PathVariable String proposalId) {
        log.info("> Confirmation for Proposal Received: " + proposalId);
        client.newPublishMessageCommand()
                .messageName("SpeakerConfirmation").correlationKey(proposalId)
                .send().join();
        return "Proposal " + proposalId + " Confirmed by Speaker";
    }


}
