package com.company;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class FirstBot extends TelegramLongPollingBot {

    private static final String Token = "";
    private static final String UserName = "hbvBot";

    public FirstBot() {
    }

    public FirstBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotUsername() {
        return UserName;
        //возвращаем юзера
    }

    @Override
    public void onUpdateReceived(Update e) {
        // Тут будет то, что выполняется при получении сообщения
        Message msg = e.getMessage(); // Это нам понадобится
        String txt = msg.getText();
        if (txt.equals("/start")) {
            SendMessage(msg, "Hello, world! This is simple bot!");
        }
    }

    @Override
    public String getBotToken() {
        return Token;
        //Токен бота
    }

    @SuppressWarnings("deprecation") // Означает то, что в новых версиях метод уберут или заменят
    private void SendMessage(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            sendMessage(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}