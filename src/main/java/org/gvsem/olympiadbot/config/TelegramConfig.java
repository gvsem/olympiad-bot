package org.gvsem.olympiadbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TelegramConfig {

    @Autowired
    private Environment env;

    @Value("${telegram.api-url}")
    protected String apiUrl;


    public String getWebhookPath() {
        return env.getProperty("WEBHOOK_PATH");
    }

    public String getBotToken() {
        return env.getProperty("BOT_TOKEN");
    }

    public String getBotUsername() {
        return env.getProperty("BOT_USERNAME");
    }


}
