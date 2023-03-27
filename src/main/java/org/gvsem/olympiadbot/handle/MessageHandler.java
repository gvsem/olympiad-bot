package org.gvsem.olympiadbot.handle;

import org.gvsem.olympiadbot.controller.MessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

    protected final CommandHandler commandHandler;

    @Autowired
    public MessageHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public BotApiMethod<?> processMessage(@Nullable Message message) {

        if (message == null) {
            return null;
        }

        String chatId = message.getChatId().toString();
        try {
            return commandHandler.handle(
                    chatId,
                    message.getText()
            );
        } catch (IllegalArgumentException e) {
            return new SendMessage(chatId, MessageEnum.ERROR.toString());
        } catch (Exception e) {
            return new SendMessage(chatId, MessageEnum.INTERNAL_ERROR.toString());
        }
    }

}
