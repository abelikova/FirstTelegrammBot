package com.company;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Character.toChars;

public class FirstBot extends TelegramLongPollingBot {

    public static String Token = "586964744:AAGoZlm9zmwchcdLUTKWtvE3QzGi-Y3rlCY"; //586964744:AAGoZlm9zmwchcdLUTKWtvE3QzGi-Y3rlCY
    public static String UserName = "hbvBot"; //hbvBot

    public static final String OPEN_MAIN = "OM";
    public static final String GET_ANSWER = "GA";

    final String party_popper = new String(toChars(0x1F389));
    final String hands = new String(toChars(0x1F44F));
    final String gift = new String(toChars(0x1F381));
    final String smile = new String(toChars(0x1F609));
    public static final String winking_face = new String(Character.toChars(0x1F609));

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
    public void onUpdateReceived(Update update) {
        // Тут будет то, что выполняется при получении сообщения
        Message msg = update.getMessage();

        if (msg != null) {
            try {
                String txt = msg.getText();
                String chatId = msg.getChatId().toString();
                Location location = msg.getLocation();
                if(location != null){
                    boolean isRight = false;
                    Float latitude = location.getLatitude();
                    Float longitude = location.getLongitude();
                    DBConnector dbConnector = new DBConnector();
                    Connection connection = dbConnector.getPostgresConnection();
                    try {
                        Statement stmt = connection.createStatement();
                        /*ResultSet rs = stmt.executeQuery("insert into geozones(primarykey, lat, lng, location) values (uuid_generate_v4(),"
                                + latitude + "," + longitude + ", ST_SetSRID(ST_MakePoint(" + longitude + "," + latitude +"),4326))");
                        rs.close();*/
                        String reqInsert = new String(
                                "insert into geozones(primarykey, lat, lng, location) values (uuid_generate_v4(),"
                                        + latitude + "," + longitude + ", ST_SetSRID(ST_MakePoint(" + longitude + "," + latitude +"),4326))");
                        System.out.println(reqInsert);
                        stmt.executeUpdate(reqInsert);

                        String selectReauest = "select q < 100 as result from(\n" +
                                "SELECT ST_DISTANCE_SPHERE(ST_AsText(location) , ST_GeomFromText('POINT(" + longitude + " " + latitude + ")',-1)) q " +
                                "from geozones_etalon " +
                                "where zoneNumber = 1) qq";
                        ResultSet rs = stmt.executeQuery(selectReauest);
                        System.out.println(selectReauest);
                        while (rs.next()) {
                            isRight = rs.getBoolean("result");
                            System.out.println("result = " + rs.getString("result"));
                        }
                        rs.close();
                        stmt.close();


                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    SendMessage answerMessage = null;
                    if(isRight){
                        answerMessage = _processCommand(update, rightFirstTask());
                    } else {
                        answerMessage = _processCommand(update, notRightFirstTask());
                    }
                    execute(answerMessage);
                }

                if(txt != null) {
                    if (txt.equals("/start")) {
                        SendMessage answerMessage = _processCommand(update, getStartMessage());
                        if (answerMessage != null) {
                            //answerMessage.setReplyMarkup(setButtons(getStartButton(), update));
                            execute(answerMessage);
                        }
                    } else if (txt.equals("/getImg")) {
                        sendImageFromUrl("1", chatId);
                    } else if (txt.equals("/getImgLocal")) {
                        sendImageLocal("2", chatId);
                /*}else if(txt.equals("/button")){
                    SendMessage answerMessage = _processCommand(update, getSecondMessage());
                    if (answerMessage != null) {
                        setButtons_(answerMessage);
                        //answerMessage.setReplyMarkup(setButtons(getStartButton(), update));
                        execute(answerMessage);
                    }*/
                    } else if (txt.equals("/LetsGo")) {
                        SendMessage answerMessage = _processCommand(update, getMenuMessage());
                        if (answerMessage != null) {
                            //answerMessage.setReplyMarkup(setButtons(getStartButton(), update));
                            execute(answerMessage);
                        }
                /*} else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("letsGo")) {
                    //answerCallbackQuery(update.getCallbackQuery().getId(), "callback!!!");
                    SendMessage answerMessage = _processCommand(update, getSecondMessage());
                    if (answerMessage != null) {
                        answerMessage.setReplyMarkup(ligar());
                        execute(answerMessage);
                    }
                } else if (update.hasInlineQuery() && update.getInlineQuery().getQuery().equals("letsGo")) {
                    //answerCallbackQuery(update.getCallbackQuery().getId(), "callback!!!");
                    answerCallbackQuery(update.getCallbackQuery().getId(), "callback!!!");
                    SendMessage answerMessage = _processCommand(update, getSecondMessage());
                    if (answerMessage != null) {
                        answerMessage.setReplyMarkup(ligar());
                        execute(answerMessage);
                    }*/
                    } else if (txt.equals("/button1")) {
                        SendMessage answerMessage = _processCommand(update, getSecondMessage());
                        if (answerMessage != null) {
                            answerMessage.setReplyMarkup(ligar());
                            execute(answerMessage);
                        }
                    } else if (txt.equals("/WantCongratulations1")) {
                        SendMessage answerMessage = _processCommand(update, wantCongratulations1Commands());
                        execute(answerMessage);
                    } else if (txt.equals("/pug")) {
                        sendImageFromUrl("1", chatId);
                    } else if (txt.equals("/owl")) {
                        sendImageFromUrl("2", chatId);
                    } else if (txt.equals("/WantCongratulations2")) {
                        SendMessage answerMessage = _processCommand(update, wantCongratulations2Commands());
                        execute(answerMessage);
                    } else if (txt.equals(("/HowWasBefore"))) {
                        SendMessage answerMessage = _processCommand(update, howWasBeforeCommands());
                        execute(answerMessage);
                    } else if (txt.equals("/1")) {
                        sendImageLocal("1", chatId);
                    } else if (txt.equals("/2")) {
                        sendImageLocal("2", chatId);
                    } else if (txt.equals("/3")) {
                        sendImageLocal("3", chatId);
                    } else if (txt.equals("/4")) {
                        sendImageLocal("4", chatId);
                    } else if (txt.equals("/Nastya")) {
                        sendAudio("Nastya", chatId);
                    } else if (txt.equals("/WantPresent")) {
                        SendMessage answerMessage = _processCommand(update, "Тестим кнопку отправки геолокации");
                        if (answerMessage != null) {
                            setButtons_(answerMessage);
                            //answerMessage.setReplyMarkup(setButtons(getStartButton(), update));
                            execute(answerMessage);
                        }
                    } else {
                        SendMessage answerMessage = _processCommand(update, "С днем рождения тебя!!!");
                        execute(answerMessage);
                    }
                }
            } catch (TelegramApiException ex) {
                Logger.getLogger(FirstBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getBotToken() {
        //Токен бота
        return Token;
    }

    private String getStartMessage(){
        return "<b>Привет, Вовчик! С днем рождения тебя!</b>" + party_popper + party_popper+ party_popper +
                "\nЯ - бот, который сегодня будет поздравлять тебя " + hands + " и дарить подарки!" + gift +
                "\n/LetsGo";
    }

    private String getSecondMessage(){
        return "Я пока совсем молодой и еще мало что умею, поэтому четко следуй моим подсказкам, пожалуйста " + smile;
    }

    private String rightFirstTask(){
        return "Ты молодец!!! Мои поздравления" + party_popper+party_popper+party_popper + "!!! \nПоехали дальше. Следующее задание:\n";
    }

    private String notRightFirstTask(){
        return "К сожалению, ты не в том месте... Если ты нашел шарик, то попробуй отойти метров на 10-20, я сверяю местоположение в радиусе 10 метров.";
    }

    private String getMenuMessage(){
        return "Я знаю следующие команды:" +
                "\n/WantCongratulations1" +
                "\n/WantCongratulations2" +
                "\n/HowWasBefore" +
                "\n/WantPresent";
    }

    private String wantCongratulations1Commands(){
        return "\n/pug" +
                "\n/owl" +
                "\n/HowWasBefore?" +
                "\n/WantPresent!";
    }

    private String wantCongratulations2Commands(){
        return "\n/Nastya" +
                "\n/Anya" +
                "\n/3" +
                "\n/4";
    }

    private String howWasBeforeCommands(){
        return "\n/1" +
                "\n/2" +
                "\n/3" +
                "\n/4";
    }

    public SendMessage _processCommand(Update update, String answer) {
        SendMessage answerMessage = null;
        String text = update.getMessage().getText();
        answerMessage = new SendMessage();
        answerMessage.setText(answer);
        answerMessage.setParseMode("HTML");
        answerMessage.setChatId(update.getMessage().getChatId());

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

    private void sendAudio(String number, String chatId) {
        File audio = chooseFile(number);
        SendAudio sendAudio = new SendAudio();
        sendAudio.setNewAudio(audio);
        sendAudio.setChatId(chatId);
        try {
            sendAudio(sendAudio);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private File chooseFile(String fileId) {
        switch (fileId){
            case "1":
                return new File("C:\\Users\\abelikova\\IdeaProjects\\FirstTelegrammBot\\src\\com\\company\\Images","1.jpg");
            case "2":
                return new File("C:\\Users\\abelikova\\IdeaProjects\\FirstTelegrammBot\\src\\com\\company\\Images","2.jpg");
            case "3":
                return new File("C:\\Users\\abelikova\\IdeaProjects\\FirstTelegrammBot\\src\\com\\company\\Images","3.jpg");
            case "4":
                return new File("C:\\Users\\abelikova\\IdeaProjects\\FirstTelegrammBot\\src\\com\\company\\Images","4.jpg");
            case "Nastya":
                return new File("C:\\Users\\abelikova\\IdeaProjects\\FirstTelegrammBot\\src\\com\\company\\Audio","Nastya.ogg");
            default:
                return null;
        }
    }

    private String chooseImg(String fileId) {
        switch (fileId) {
            case "1":
                return "http://s7.hostingkartinok.com/uploads/images/2015/01/28a8996fa2bdb851efd1cea13f4d454a.jpg";
            case "2":
                return "http://lenagold.ru/fon/clipart/s/sova/sova82.jpg";
            case "3":
                return new File("C:\\bot\\Images","photo_2018-01-26_15-29-05.jpg").getPath();
            default:
                return null;
        }
    }

    private List<List<InlineKeyboardButton>> getStartButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText("Поехали!").setCallbackData("letsGo"));
        //buttons1.add(new InlineKeyboardButton().setText("Поехали!").setSwitchInlineQuery("letsGo"));
        buttons.add(buttons1);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        /*markupKeyboard.setKeyboard(buttons);

        SendMessage answerMessage = null;

        answerMessage = new SendMessage();
        answerMessage.setText(getStartMessage());
        answerMessage.setParseMode("HTML");
        answerMessage.setChatId(update.getMessage().getChatId());*/

        return buttons;
    }

    public synchronized void answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setShowAlert(false);
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        //answer.setShowAlert(true);
        try {
            answerCallbackQuery(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<List<InlineKeyboardButton>> getMainMenuButtons(){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText("Хочу поздравляшек 1").setCallbackData("images"));
        buttons1.add(new InlineKeyboardButton().setText("Хочу поздравляшек 2").setCallbackData("voices"));
        buttons1.add(new InlineKeyboardButton().setText("А как было раньше?").setCallbackData("photo"));
        buttons1.add(new InlineKeyboardButton().setText("Хочу подарок!!!").setCallbackData("present"));
        buttons.add(buttons1);
        return buttons;
    }

    private InlineKeyboardMarkup setButtons(List<List<InlineKeyboardButton>> buttons, Update update){
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        SendMessage answerMessage = null;
        answerMessage = new SendMessage();
        answerMessage.setText(getStartMessage());
        answerMessage.setParseMode("HTML");
        answerMessage.setChatId(update.getMessage().getChatId());

        return markupKeyboard;
    }

    public synchronized void setButtons_(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        KeyboardButton setGeolocationBtn = new KeyboardButton("Отправить мместоположение");
        setGeolocationBtn.setRequestLocation(true);
        keyboardFirstRow.add(setGeolocationBtn);

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Помощь"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public InlineKeyboardMarkup ligar() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
        for (int i = 0; i < 9; i++) {
            if (i < 5 ) {
                row1.add(new InlineKeyboardButton().setText("liga led " + (i + 1)).setCallbackData(i + "on"));
            } else {
                row1.add(new InlineKeyboardButton().setText("desliga led " + (i + 1)).setCallbackData(i + "off"));
            }
            if (i % 2 == 0 || i == 0) {
                rowsInline.add(row1);
                row1 = new ArrayList<InlineKeyboardButton>();
            }
        }
        //rowsInline.add(acionaTodos());
        //rowsInline.add(desacionaTodos());
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}