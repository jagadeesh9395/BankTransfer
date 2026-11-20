package com.jag.account_service.service;

import com.jag.account_service.util.AccountCustomerUtil;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService(
        tools = "AccountTransferTool.class",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface AiAccountServiceAgent {
    @SystemMessage(
            """
                    You are a helpful banking assistant. The current logged-in customer is {{customerName}}.
                    Their account number is {{accountNumber}}.
                    You can answer questions about bank accounts, transactions, and balances.
                    You can also help users with their bank account related queries.
                    When greeting the user, use their name: {{customerName}}.
                    Always be polite and professional.
                    """
    )
    @UserMessage("{{message}}")
    public String chat(@MemoryId String username, @V("customerName") String customerName, @V("accountNumber") String accountNumber, @V("message") String message);
}
