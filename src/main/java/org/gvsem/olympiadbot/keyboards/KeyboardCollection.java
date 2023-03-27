package org.gvsem.olympiadbot.keyboards;

import org.gvsem.olympiadbot.controller.KeyboardEnum;
import org.gvsem.olympiadbot.controller.QueryEnum;
import org.gvsem.olympiadbot.model.Olympiad;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KeyboardCollection {

    public InlineKeyboardMarkup getDemoKeyboard() {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        Map<String, String> buttons = new HashMap<>();
        buttons.put(KeyboardEnum.DEMO_FIND_ALL.toString(), "/" + QueryEnum.GET_ALL_OLYMPIADS);
        buttons.put(KeyboardEnum.DEMO_FIND_OLYMPIAD.toString(), "/" + QueryEnum.GET_OLYMPIAD + " 52");
        buttons.put(KeyboardEnum.DEMO_FIND_SUBJECT.toString(), "/" + QueryEnum.GET_OLYMPIADS_BY_PROFILE + " математика");

        for (var b : buttons.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(b.getKey());
            button.setCallbackData(b.getValue());
            rowList.add(List.of(button));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getOlympiadsKeyboard(List<Olympiad> olympiads, @Nullable String nextPageCmd) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (var o : olympiads) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(o.getTitle());
            button.setCallbackData(QueryEnum.GET_OLYMPIAD + " " + o.getId());
            rowList.add(List.of(button));
        }

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(KeyboardEnum.NEXT_PAGE.toString());
        button.setCallbackData(nextPageCmd);
        rowList.add(List.of(button));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }

    public ReplyKeyboardMarkup getMenu() {

        KeyboardRow[] rows = new KeyboardRow[]{new KeyboardRow(), new KeyboardRow()};

        Map<String, String> buttons = new HashMap<>();
        buttons.put(KeyboardEnum.MENU_FIND_ALL.toString(), "/" + QueryEnum.GET_ALL_OLYMPIADS);
        buttons.put(KeyboardEnum.MENU_FIND_SUBJECT.toString(), "/" + QueryEnum.GET_OLYMPIADS_BY_PROFILE + " математика");

        int i = 0;
        for (var b : buttons.entrySet()) {
            KeyboardButton btn = new KeyboardButton(b.getKey());
            rows[(i++) % 2].add(btn);
        }

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(List.of(rows));
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }


}
