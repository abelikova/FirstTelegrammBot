package com.company;

import com.company.util.ActionBuilder;
import com.company.util.InlineKeyboardButtonBuilder;
import com.company.util.UpdateUtil;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Character.toChars;

public class FirstBot extends TelegramLongPollingBot {

    private static final String Token = "";
    private static final String UserName = "";

    public static final String OPEN_MAIN = "OM";
    public static final String GET_ANSWER = "GA";

    final String party_popper = new String(toChars(0x1F389));
    final String hands = new String(toChars(0x1F44F));
    final String gift = new String(toChars(0x1F381));
    public static final String winking_face = new String(Character.toChars(0x1F609));

    private DocumentMarshaller marshaller;
    //private QuestStateHolder questStateHolder;

    public FirstBot() {
    }

    public FirstBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotUsername() {
        //возвращаем юзера
        return UserName;
    }

    @Override
    public void onUpdateReceived(Update e) {
        // Тут будет то, что выполняется при получении сообщения
        Message msg = e.getMessage(); // Это нам понадобится
        String txt = msg.getText();
        String chatId = msg.getChatId().toString();

        if (txt.equals("/start")) {
            processCommand(e);
        }
        if(txt.equals("/getImg")){
            sendImageFromUrl("1", chatId);
        }

        if (txt.equals("/getImgLocal")) {
            sendImageLocal("2", chatId);
        }
    }

    @Override
    public String getBotToken() {
        //Токен бота
        return Token;
    }

    /*@SuppressWarnings("deprecation") // Означает то, что в новых версиях метод уберут или заменят
    private void SendStartMessage(Update update){
        SendMessage answerMessage = new SendMessage();
        answerMessage.setText(getStartMessage());
        answerMessage.setParseMode("HTML");
        answerMessage.setChatId(update.getMessage().getChatId().toString());
        InlineKeyboardMarkup markup = keyboard(update);
        answerMessage.setReplyMarkup(markup);

        try {
            //Чтобы не крашнулась программа при вылете Exception
            sendMessage(answerMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }*/

    private void processCommand(Update update) {
        SendMessage answerMessage = null;
        try {
            answerMessage = _processCommand(update);
            if (answerMessage != null) {
                execute(answerMessage);
            }

        } catch (TelegramApiException ex) {
            Logger.getLogger(FirstBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getStartMessage(){
        return "<b>Привет, Вовчик! С днем рождения тебя!</b>" + party_popper + party_popper+ party_popper +
                "\\nЯ - бот, который сегодня будет поздравлять тебя " + hands + " и дарить подарки!" + gift;
    }

    public SendMessage _processCommand(Update update) {
        SendMessage answerMessage = null;
        String text = update.getMessage().getText();
        if ("/start".equalsIgnoreCase(text)) {
            answerMessage = new SendMessage();
            answerMessage.setText(getStartMessage());
            answerMessage.setParseMode("HTML");
            answerMessage.setChatId(update.getMessage().getChatId());
            InlineKeyboardMarkup markup = keyboard(update);

            answerMessage.setReplyMarkup(markup);
        }
        return answerMessage;
    }

    public void sendImageFromUrl(String imgNumb, String chatId) {
        String url = chooseImg(imgNumb);
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        sendPhotoRequest.setPhoto(url);
        try {
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendImageLocal(String imgNumb, String chatId) {
        File photo = chooseFile(imgNumb);
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        sendPhotoRequest.setNewPhoto(photo);
        try {
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private File chooseFile(String fileId) {
        switch (fileId){
            case "1":
                return new File("C:\\bot\\Images","photo_2018-01-26_15-29-05.jpg");
            case "2":
                return new File("C:\\bot\\Images","photo_2018-01-26_15-29-05.jpg");
            default:
                return null;
        }
    }

    private String chooseImg(String fileId) {
        switch (fileId) {
            case "1":
                return "http://s7.hostingkartinok.com/uploads/images/2015/01/28a8996fa2bdb851efd1cea13f4d454a.jpg";
            case "2":
                return new File("C:\\bot\\Images","photo_2018-01-26_15-29-05.jpg").getPath();
            default:
                return null;
        }
    }

    // Все что ниже - с гитхаба
    public InlineKeyboardMarkup keyboard(Update update) {
        final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(buttonMain()));
        markup.setKeyboard(keyboard);
        return markup;
    }

    public InlineKeyboardButton buttonMain() {
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Начать!" + winking_face)
                .setCallbackData(new ActionBuilder(marshaller)
                        .setName(OPEN_MAIN)
                        .asString())
                .build();
        return button;
    }

    public List<SendMessage> _processCallbackQuery(Update update) {
        List<SendMessage> answerMessages = new ArrayList<>();
        try {
            Action action = new ActionBuilder(marshaller).buld(update);
            String data = update.getCallbackQuery().getData();
            Long chatId = UpdateUtil.getChatFromUpdate(update).getId();//update.getCallbackQuery().getMessage().getChatId();
            if (data == null || action == null) {
                return null;
            }

            /*if (OPEN_MAIN.equals(action.getName())) {
                initQuests(update);

                sendQuest(update);
            }*/

            /*if (GET_ANSWER.equals(action.getName())) {
                Long answId = Long.parseLong(action.getValue());
                ClsAnswer answ = classifierRepository.find(ClsAnswer.class, answId);
                SendMessage comment = new SendMessage();
                comment.setParseMode("HTML");
                comment.setText("<b>Твой ответ:</b> "
                        + answ.getAnswerText()
                        + "\n<b>Комментарий к ответу:</b> "
                        + answ.getAnswerComment()
                        + "\n");
                comment.setChatId(chatId);
                execute(comment);

                sendQuest(update);
            }*/

        } catch (Exception ex) {
            Logger.getLogger(FirstBot.class.getName()).log(Level.SEVERE, null, ex);
            answerMessages.add(errorMessage());
        }
        return answerMessages;
    }

    public void setMarshaller(DocumentMarshaller marshalFactory) {
        this.marshaller = marshalFactory;
    }

    public SendMessage errorMessage() {
        SendMessage answerMessage = new SendMessage();
        answerMessage.setText("Ой, что-то пошло не так, попробуйте еще раз или перейдите в главное меню");
        InlineKeyboardMarkup keyboardMain = keyboard(null);
        answerMessage.setReplyMarkup(keyboardMain);
        return answerMessage;
    }
}