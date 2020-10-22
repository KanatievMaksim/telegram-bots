package dao;

import entities.Word;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WordDAO {
    private Connection conn;
    private ResultSet rs;

    @SneakyThrows
    public WordDAO() {
        this.conn = DriverManager.getConnection("jdbc:sqlite:D:\\!Programming\\!JAVA_WORK\\botEnglish\\TelegramBotForEnglish\\englishBot");
    }

    @SneakyThrows
    private Word selectOne(ResultSet rs) {
        if (rs.next()) {
            int id = rs.getInt("id");
            String word = rs.getString("word");
            String wordType = rs.getString("word_type");
            String[] examples = rs.getString("examples").split("•");
            String[] translate = rs.getString("translation").split("•"); //TODO make sth w/ translation
            return new Word(id, word, wordType, examples, translate);
        }
        return new Word();
    }

    @SneakyThrows
    private void addQuery(PreparedStatement ps, Word word) {
        ps.setString(1, "translation");
        ps.setInt(2, word.getId());
        ps.setString(3, word.getWord());
        ps.setString(4, word.getType());
        ps.setString(5, word.getExamples().toString());
        ps.setString(6, word.getTranslation().toString());
        ps.executeUpdate();
    }

    @SneakyThrows
    private List<Word> selectAll(ResultSet rs) {
        List<Word> words = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String word = rs.getString("word");
            String wordType = rs.getString("word_type");
            String[] examples = rs.getString("examples").split("•");
            String[] translate = rs.getString("translation").split("•"); //TODO make sth w/ translation
            words.add(new Word(id, word, wordType, examples, translate));
        }
        return words;
    }

    @SneakyThrows
    public Word getRandomWordToLearn() {
        PreparedStatement ps = conn.prepareStatement("select * from oneWordGlossaryCopy order by random() limit 1");
        rs = ps.executeQuery();
        return selectOne(rs);
    }

    @SneakyThrows
    public Word getRandomPhraseToLearn() {
        PreparedStatement ps = conn.prepareStatement("select * from phrasalVerbsGlossaryCopy order by random() limit 1");
        rs = ps.executeQuery();
        return selectOne(rs);
    }

    @SneakyThrows
    public void deleteWord(Word word) {
        PreparedStatement ps = conn.prepareStatement("delete * from oneWordGlossaryCopy where word = ?, word_type = ?");
        ps.setString(1, word.getWord());
        ps.setString(2, word.getType());
        ps.executeUpdate();
    }

    @SneakyThrows
    public void deletePhrase(Word word) {
        PreparedStatement ps = conn.prepareStatement("delete * from phrasalVerbsGlossaryCopy where word = ?, word_type = ?");
        ps.setString(1, word.getWord());
        ps.setString(2, word.getType());
        ps.executeUpdate();
    }

    @SneakyThrows
    public void addWordToQuiz(Word word) {
        PreparedStatement ps = conn.prepareStatement("insert into wordQuiz (id,word,word_type,examples,?) values (?,?,?,?,?)");
        addQuery(ps, word);
    }

    @SneakyThrows
    public void addPhraseToQuiz(Word word) {
        PreparedStatement ps = conn.prepareStatement("insert into phrasalVerbsQuiz (id,word,word_type,examples?) values (?,?,?,?,?)");
        addQuery(ps, word);
    }

    @SneakyThrows
    public void deleteWordInGlossary() {
        PreparedStatement ps = conn.prepareStatement("delete from oneWordGlossaryCopy as a where a.id in (select id from wordQuiz)");
        ps.executeUpdate();
    }

    @SneakyThrows
    public void deletePhraseInGlossary() {
        PreparedStatement ps = conn.prepareStatement("delete from phrasalVerbsGlossaryCopy as a where a.id in (select id from phrasalVerbsQuiz)");
        ps.executeUpdate();
    }

    @SneakyThrows
    public List<Word> findAllWordQuiz() {
        PreparedStatement ps = conn.prepareStatement("select * from wordQuiz");
        ResultSet rs = ps.executeQuery();
        return selectAll(rs);
    }

    @SneakyThrows
    public List<Word> findAllPhraseQuiz() {
        PreparedStatement ps = conn.prepareStatement("select * from phrasalVerbsQuiz");
        ResultSet rs = ps.executeQuery();
        return selectAll(rs);
    }
}
