package com.nsind.widget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WidgetScriptRequest {
    private String chatbotId;
    private String apiKey;
    private String widgetPosition;
    private String widgetTheme;
}

