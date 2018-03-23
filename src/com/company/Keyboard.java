package com.company;

import com.company.util.ActionBuilder;
import com.company.util.InlineKeyboardButtonBuilder;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Keyboard {
    private InlineKeyboardMarkup keyboard(Update update) {
        final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(buttonMain()));
        markup.setKeyboard(keyboard);
        return markup;
    }

    private InlineKeyboardButton buttonMain() {
        final String OPEN_MAIN = "OM";

        /////////////////////////////////////////////////////////
        DocumentMarshaller marshaller = new DocumentMarshaller() {
            @Override
            public <T> String marshal(T document) {
                return null;
            }

            @Override
            public <T> T unmarshal(String str) {
                return null;
            }

            @Override
            public <T> T unmarshal(String str, Class clazz) {
                return null;
            }

            @Override
            public <T> T unmarshal(String str, String clazz) {
                return null;
            }
        };
        ///////////////////////////////////////////////////////////////

        final String winking_face = new String(Character.toChars(0x1F609));
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Начать!" + winking_face)
                .setCallbackData(new ActionBuilder(marshaller)
                        .setName(OPEN_MAIN)
                        .asString())
                .build();
        return button;
    }
}
