package com.nsind.widget.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WidgetScriptService {

    @Value("${widget.api-url:http://localhost:8086/api/v1/widget}")
    private String widgetApiUrl;

    public String generateWidgetScript(String chatbotId, String apiKey, String position, String theme) {
        String scriptUrl = widgetApiUrl + "/chatbot.js";

        String script = String.format(
                "(function() {\n" +
                "    const chatbotId = '%s';\n" +
                "    const apiKey = '%s';\n" +
                "    const position = '%s';\n" +
                "    const theme = '%s';\n" +
                "    \n" +
                "    const script = document.createElement('script');\n" +
                "    script.src = '%s';\n" +
                "    script.async = true;\n" +
                "    script.onload = function() {\n" +
                "        if (window.ChatBot) {\n" +
                "            ChatBot.init({\n" +
                "                chatbotId: chatbotId,\n" +
                "                apiKey: apiKey,\n" +
                "                position: position || 'bottom-right',\n" +
                "                theme: theme || 'light'\n" +
                "            });\n" +
                "        }\n" +
                "    };\n" +
                "    document.head.appendChild(script);\n" +
                "})();\n",
                chatbotId,
                apiKey,
                position != null ? position : "bottom-right",
                theme != null ? theme : "light",
                scriptUrl
        );

        log.info("Generated widget script for chatbot: {}", chatbotId);
        return script;
    }

    public String generateInstallationCode(String chatbotId, String apiKey) {
        return String.format(
                "<script src=\"%s/chatbot.js\"></script>\n" +
                "<script>\n" +
                "ChatBot.init({\n" +
                "  chatbotId: '%s',\n" +
                "  apiKey: '%s'\n" +
                "})\n" +
                "</script>",
                widgetApiUrl,
                chatbotId,
                apiKey
        );
    }
}

