package com.company;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, world");
        ApiContextInitializer.init(); // Инициализируем апи
        TelegramBotsApi botApi = new TelegramBotsApi();

        HttpHost proxy = new HttpHost("proxy2.ics.perm.ru", 8080, "http");

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        HttpGet request = new HttpGet("/");
        request.setConfig(config);

        Runnable r = () -> {
            FirstBot bot = null;
            if (proxy == null) {
                bot = new FirstBot();
            } else {
                DefaultBotOptions instance = ApiContext
                        .getInstance(DefaultBotOptions.class);
                RequestConfig rc = RequestConfig.custom()
                        .setProxy(proxy).build();
                instance.setRequestConfig(rc);
                bot = new FirstBot(instance);
            }

            try {
                botApi.registerBot(bot);
                //AppEnv.getContext().getMenuManager().setBot(bot);
            } catch (TelegramApiRequestException ex) {
                ex.printStackTrace();
                Logger.getLogger(Main.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        };

        new Thread(r).start();

        while (true) {
            try {
                Thread.sleep(80000L);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}



