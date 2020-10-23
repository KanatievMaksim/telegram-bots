import dao.WordDAO;
import entities.Word;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private static SendMessage sendMessage;
    private static long chat_id;
    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private CallbackQuery callbackQuery;
    private WordDAO wordDAO;
    private Word word;

    public Bot(){
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        wordDAO = new WordDAO();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            update.getUpdateId();
            chat_id = update.getMessage().getChatId();
            sendMessage = new SendMessage().setChatId(chat_id);
            System.out.println("get message(from update) -> " + update.getMessage());
            startCommandSwitcher(update);
        } else if (update.hasCallbackQuery()) {
            difficultCommandSwitcher(update);
        }
    }

    @Override
    public String getBotUsername() {
        return "English vocabulary";
    }

    @Override
    public String getBotToken() {
        return "913726033:AAFE_RYPjagMFpdBGLdczRRKtjQbABDf-jE";
    }

    private void startCommandSwitcher(Update update){
        String text = update.getMessage().getText();
        switch(text){
            case "/start":
                sayHelloAndSetButtons();
                break;
            case "Тест":
                setDifficultButtons();
                sendMsg("Выберите сложность:");
                System.out.println("get message from TEST CASE -> " + update.getMessage().getText());
                break;
            case "Обучение":
                setDifficultButtons();
                sendMsg("Выберите сложность:");
                System.out.println("Learn -> " + text);
                break;
            case "Повторить слова":
                //TODO
                break;
            case "Поиск слова":
                //TODO
                break;
            case "Помощь":
                sendMsg(help());
                break;
            default:
                sendMsg("Выберите один из вариантов!");
        }
    }

    private void difficultCommandSwitcher(Update update) {
        String difficult = update.getCallbackQuery().getData();
        switch (difficult){
            case "Просто слова":
                setTestButtons();
                sendMsg("Приступим!");
                startWordQuiz(update);
                break;
            case "Фразовые глаголы":
                break;
            case "Все вместе":
                break;
        }
    }

    private  void startWordQuiz(Update update) {

        word = wordDAO.getRandomWordToLearn();
        System.out.println("word toString -> " + word.toString());
        sendMsg(word.toString());
    }

    private void startPhraseQuiz(){

    }

    private String showWord(){

        return word.getWord();
    }

    private void sayHelloAndSetButtons(){
        setMainButtons();
        sendMsg("Здравствуйте! Чем могу помочь?");
    }

    private void setKeyboardProps(){
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
    }

    private void setMainButtons(){
        setKeyboardProps();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();

        keyboardFirstRow.add("Повторить слова");
        keyboardFirstRow.add("Тест");
        keyboardSecondRow.add("Поиск слова");
        keyboardSecondRow.add("Обучение");
        keyboardThirdRow.add("Помощь");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private void setTestButtons(){
        setKeyboardProps();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add("Пропустить слово");
        keyboardFirstRow.add("Стоп");

        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private void setDifficultButtons(){

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardFirstRow = new ArrayList<>();
        List<InlineKeyboardButton> keyboardSecondRow = new ArrayList<>();

        keyboardFirstRow.add(new InlineKeyboardButton().setText("Просто слова").setCallbackData("Просто слова"));
        keyboardFirstRow.add(new InlineKeyboardButton().setText("Фразовые глаголы").setCallbackData("Фразовые глаголы"));
        keyboardSecondRow.add(new InlineKeyboardButton().setText("Все вместе").setCallbackData("Все вместе"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    private void sendMsg(String message){
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chat_id);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String help(){
        return "Нажмите \"Тест\" чтобы проверить свои знания." +
                "\nНажмите \"Обучение\" чтобы начать учить слова." +
                "\nНажмите \"Повторить слова\" чтобы вспомнить какие слова вы учили до этого." +
                "\nНажмите \"Поиск слова\" чтобы найти перевод интересующего вас слова.";
    }

    private void dictation(){

    }
}
