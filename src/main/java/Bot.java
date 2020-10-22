import dao.WordDAO;
import entities.Word;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private static SendMessage sendMessage;
    private static long chat_id;
    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private WordDAO wordDAO;
    private Word word;

    public Bot(){
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        wordDAO = new WordDAO();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            update.getUpdateId();
            chat_id = update.getMessage().getChatId();
            sendMessage = new SendMessage().setChatId(chat_id);
            sendMessage.enableMarkdown(true);
            String text = update.getMessage().getText();
            commandSwitcher(text);
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

    private void commandSwitcher(String text){
        switch(text){
            case "/start":
                sayHelloAndSetButtons();
                break;
            case "Тест":
                sendMsg("Выберите сложность:");
                setDifficultButtons();
                break;

            case "Обучение":
                sendMsg("Выберите сложность:");
                setDifficultButtons();
                break;
            case "Просто слова":
                sendMsg("Приступим!");
                setTestButtons();
                word = wordDAO.getRandomWordToLearn();

                break;
            case "Фразовые глаголы":
                //TODO
                break;
            case "Все вместе":
                //TODO
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
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();

        keyboardFirstRow.add("Старт");
        keyboardFirstRow.add("Стоп");
        keyboardSecondRow.add("Перевод слова");
        keyboardSecondRow.add("Пропустить слово");
        keyboardThirdRow.add("Помощь");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private void setDifficultButtons(){
        setKeyboardProps();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add("Просто слова");
        keyboardFirstRow.add("Фразовые глаголы");
        keyboardSecondRow.add("Все вместе");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
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
