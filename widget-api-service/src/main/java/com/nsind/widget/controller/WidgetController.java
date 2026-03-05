package com.nsind.widget.controller;

import com.nsind.widget.dto.WidgetScriptRequest;
import com.nsind.widget.service.WidgetScriptService;
import com.nsind.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/widget")
@RequiredArgsConstructor
@Tag(name = "Widget", description = "Chatbot widget generation and management")
public class WidgetController {

    private final WidgetScriptService widgetScriptService;

    @PostMapping("/generate-script")
    @Operation(summary = "Generate widget installation script")
    public ResponseEntity<ApiResponse<String>> generateWidgetScript(
            @RequestBody WidgetScriptRequest request) {
        String script = widgetScriptService.generateWidgetScript(
                request.getChatbotId(),
                request.getApiKey(),
                request.getWidgetPosition(),
                request.getWidgetTheme()
        );
        return ResponseEntity.ok(ApiResponse.success(script, "Widget script generated"));
    }

    @GetMapping("/installation/{chatbotId}/{apiKey}")
    @Operation(summary = "Get installation code")
    public ResponseEntity<ApiResponse<String>> getInstallationCode(
            @PathVariable String chatbotId,
            @PathVariable String apiKey) {
        String code = widgetScriptService.generateInstallationCode(chatbotId, apiKey);
        return ResponseEntity.ok(ApiResponse.success(code, "Installation code generated"));
    }

    @GetMapping("/chatbot.js")
    @Operation(summary = "Get widget JavaScript file")
    public ResponseEntity<String> getChatbotWidget() {
        String widgetJs = getWidgetJavaScript();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JAVASCRIPT)
                .body(widgetJs);
    }

    private String getWidgetJavaScript() {
        return "window.ChatBot = window.ChatBot || {};\n" +
                "\n" +
                "window.ChatBot.init = function(config) {\n" +
                "    const chatbotId = config.chatbotId;\n" +
                "    const apiKey = config.apiKey;\n" +
                "    const position = config.position || 'bottom-right';\n" +
                "    const theme = config.theme || 'light';\n" +
                "    \n" +
                "    // Create widget container\n" +
                "    const container = document.createElement('div');\n" +
                "    container.id = 'chatbot-widget';\n" +
                "    container.style.position = 'fixed';\n" +
                "    container.style.zIndex = '9999';\n" +
                "    container.style[position.split('-')[0]] = '20px';\n" +
                "    container.style[position.split('-')[1]] = '20px';\n" +
                "    container.style.width = '350px';\n" +
                "    container.style.height = '500px';\n" +
                "    container.style.backgroundColor = theme === 'dark' ? '#333' : '#fff';\n" +
                "    container.style.borderRadius = '10px';\n" +
                "    container.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)';\n" +
                "    container.style.fontFamily = 'Arial, sans-serif';\n" +
                "    container.style.display = 'flex';\n" +
                "    container.style.flexDirection = 'column';\n" +
                "    \n" +
                "    // Create header\n" +
                "    const header = document.createElement('div');\n" +
                "    header.style.padding = '15px';\n" +
                "    header.style.backgroundColor = '#007bff';\n" +
                "    header.style.color = 'white';\n" +
                "    header.style.borderRadius = '10px 10px 0 0';\n" +
                "    header.textContent = 'Chat Support';\n" +
                "    header.style.fontWeight = 'bold';\n" +
                "    header.style.textAlign = 'center';\n" +
                "    \n" +
                "    // Create messages container\n" +
                "    const messagesContainer = document.createElement('div');\n" +
                "    messagesContainer.style.flex = '1';\n" +
                "    messagesContainer.style.overflowY = 'auto';\n" +
                "    messagesContainer.style.padding = '15px';\n" +
                "    messagesContainer.style.borderColor = theme === 'dark' ? '#555' : '#eee';\n" +
                "    messagesContainer.style.borderBottomStyle = 'solid';\n" +
                "    messagesContainer.style.borderBottomWidth = '1px';\n" +
                "    \n" +
                "    // Create input container\n" +
                "    const inputContainer = document.createElement('div');\n" +
                "    inputContainer.style.padding = '10px';\n" +
                "    inputContainer.style.display = 'flex';\n" +
                "    inputContainer.style.gap = '10px';\n" +
                "    \n" +
                "    // Create input field\n" +
                "    const input = document.createElement('input');\n" +
                "    input.type = 'text';\n" +
                "    input.placeholder = 'Type your message...';\n" +
                "    input.style.flex = '1';\n" +
                "    input.style.padding = '8px';\n" +
                "    input.style.border = '1px solid #ddd';\n" +
                "    input.style.borderRadius = '5px';\n" +
                "    input.style.fontFamily = 'Arial, sans-serif';\n" +
                "    \n" +
                "    // Create send button\n" +
                "    const sendBtn = document.createElement('button');\n" +
                "    sendBtn.textContent = 'Send';\n" +
                "    sendBtn.style.padding = '8px 15px';\n" +
                "    sendBtn.style.backgroundColor = '#007bff';\n" +
                "    sendBtn.style.color = 'white';\n" +
                "    sendBtn.style.border = 'none';\n" +
                "    sendBtn.style.borderRadius = '5px';\n" +
                "    sendBtn.style.cursor = 'pointer';\n" +
                "    sendBtn.style.fontFamily = 'Arial, sans-serif';\n" +
                "    \n" +
                "    // Send message handler\n" +
                "    sendBtn.onclick = function() {\n" +
                "        const message = input.value.trim();\n" +
                "        if (message) {\n" +
                "            addMessage(message, 'user');\n" +
                "            sendChatMessage(chatbotId, apiKey, message);\n" +
                "            input.value = '';\n" +
                "        }\n" +
                "    };\n" +
                "    \n" +
                "    function addMessage(text, sender) {\n" +
                "        const msgDiv = document.createElement('div');\n" +
                "        msgDiv.style.marginBottom = '10px';\n" +
                "        msgDiv.style.padding = '8px';\n" +
                "        msgDiv.style.borderRadius = '5px';\n" +
                "        msgDiv.style.backgroundColor = sender === 'user' ? '#e3f2fd' : '#f5f5f5';\n" +
                "        msgDiv.style.textAlign = sender === 'user' ? 'right' : 'left';\n" +
                "        msgDiv.textContent = text;\n" +
                "        messagesContainer.appendChild(msgDiv);\n" +
                "        messagesContainer.scrollTop = messagesContainer.scrollHeight;\n" +
                "    }\n" +
                "    \n" +
                "    function sendChatMessage(cId, aKey, msg) {\n" +
                "        fetch('/api/v1/chatbot/chat', {\n" +
                "            method: 'POST',\n" +
                "            headers: {\n" +
                "                'Content-Type': 'application/json'\n" +
                "            },\n" +
                "            body: JSON.stringify({\n" +
                "                chatbotId: cId,\n" +
                "                message: msg,\n" +
                "                apiKey: aKey\n" +
                "            })\n" +
                "        })\n" +
                "        .then(res => res.json())\n" +
                "        .then(data => {\n" +
                "            if (data.success) {\n" +
                "                addMessage(data.data.response, 'bot');\n" +
                "            }\n" +
                "        })\n" +
                "        .catch(err => {\n" +
                "            console.error('Error:', err);\n" +
                "            addMessage('Sorry, there was an error. Please try again.', 'bot');\n" +
                "        });\n" +
                "    }\n" +
                "    \n" +
                "    inputContainer.appendChild(input);\n" +
                "    inputContainer.appendChild(sendBtn);\n" +
                "    container.appendChild(header);\n" +
                "    container.appendChild(messagesContainer);\n" +
                "    container.appendChild(inputContainer);\n" +
                "    document.body.appendChild(container);\n" +
                "};\n";
    }
}

