package com.company.util;

import com.company.Action;
import com.company.DocumentMarshaller;
import org.telegram.telegrambots.api.objects.Update;

public class ActionBuilder {

    private final DocumentMarshaller marshaller;
    private Action action = new Action();

    public ActionBuilder(DocumentMarshaller marshaller) {
        this. marshaller = marshaller;
    }

    public ActionBuilder setName(String name) {
        action.setName(name);
        return this;
    }

    public ActionBuilder setValue(String name) {
        action.setValue(name);
        return this;
    }

    public String asString() {
        return marshaller.<Action>marshal(action);
    }

    public Action build() {
        return action;
    }

    public Action buld(Update update) {
        String data = update.getCallbackQuery().getData();
        if (data == null) {
            return null;
        }

        action = marshaller.<Action>unmarshal(data);

        if (action == null) {
            return null;
        }
        return action;
    }
}
