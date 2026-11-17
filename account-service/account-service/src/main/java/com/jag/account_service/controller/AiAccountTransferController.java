package com.jag.account_service.controller;


import com.jag.account_service.service.AiAccountServiceAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiAccountTransferController {

    private AiAccountServiceAgent aiAccountServiceAgent;

    public AiAccountTransferController(AiAccountServiceAgent aiAccountServiceAgent) {
        this.aiAccountServiceAgent = aiAccountServiceAgent;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return aiAccountServiceAgent.chat(message);
    }
}
