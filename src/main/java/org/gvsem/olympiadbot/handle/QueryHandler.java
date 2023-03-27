package org.gvsem.olympiadbot.handle;

import org.gvsem.olympiadbot.controller.MessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class QueryHandler {

    protected final CommandHandler commandHandler;

    @Autowired
    public QueryHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        String chatId = buttonQuery.getMessage().getChatId().toString();
        try {
            return commandHandler.handle(
                    chatId,
                    buttonQuery.getData()
            );
        } catch (IllegalArgumentException e) {
            return new SendMessage(chatId, MessageEnum.ERROR.toString());
        } catch (Exception e) {
            return new SendMessage(chatId, MessageEnum.INTERNAL_ERROR.toString());
        }
    }

}
