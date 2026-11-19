package com.jag.account_service.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService(
        tools = "AccountTransferTool.class",
        chatMemoryProvider = "ChatMemoryProvider.class"
)
public interface AiAccountServiceAgent {
    @SystemMessage(
            """
                    You are a helpful assistant that can answer questions about bank accounts.
                    You can answer questions about bank accounts, transactions, and balances.
                    You can also help users with their bank account related queries.
                    """
    )
    public String chat(String message);
}
