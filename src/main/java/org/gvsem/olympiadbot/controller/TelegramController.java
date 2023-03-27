package org.gvsem.olympiadbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.gvsem.olympiadbot.config.TelegramConfig;
import org.gvsem.olympiadbot.handle.MessageHandler;
import org.gvsem.olympiadbot.handle.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import javax.annotation.PostConstruct;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class TelegramController extends SpringWebhookBot {

    protected final TelegramConfig telegramConfig;
    protected final MessageHandler messageHandler;
    protected final QueryHandler queryHandler;

    public TelegramController(@Autowired TelegramBotsApi telegramBotsApi,
                              @Autowired TelegramConfig telegramConfig,
                              @Autowired DefaultBotOptions options,
                              @Autowired SetWebhook setWebhook,
                              @Autowired MessageHandler messageHandler,
                              @Autowired QueryHandler queryHandler) throws TelegramApiException {
        super(options, setWebhook, telegramConfig.getBotToken());
        this.telegramConfig = telegramConfig;
        this.messageHandler = messageHandler;
        this.queryHandler = queryHandler;
        telegramBotsApi.registerBot(this, setWebhook);
    }

    @Override
    public String getBotPath() {
        return telegramConfig.getWebhookPath();
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotUsername();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            return queryHandler.processCallbackQuery(
                    update.getCallbackQuery()
            );
        } else if (update.hasMessage()) {
            return messageHandler.processMessage(
                    update.getMessage()
            );
        }
        return null;
    }

    @PostConstruct
    public void registerWebhook() {
        try {
            URL url = new URL(telegramConfig.getApiUrl() + "/bot" + telegramConfig.getBotToken() + "/setWebhook" + "?url=" + URLEncoder.encode(telegramConfig.getWebhookPath(), StandardCharsets.UTF_8));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() / 100 != 2) {
                throw new IllegalArgumentException("Failed to register webhook: got " + con.getResponseCode());
            }
            log.info("Registered webhook for telegram.");
        } catch (Exception e) {
            log.error("Failed to register webhook.");
            throw new RuntimeException(e);
        }
    }

}
