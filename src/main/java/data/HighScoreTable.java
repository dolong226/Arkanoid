
package data;

import java.io.*;
import java.util.*;

public class HighScoreTable {
    private static final int MAX_SCORES = 5;
    private static final String FILENAME = "highscores.txt";
    private List<ScoreEntry> scores;

    public static class ScoreEntry {
        public final String name;
        public final int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    public HighScoreTable() {
        this.scores = new ArrayList<>();
        loadFromFile();
    }

    public void addScore(String name, int score) {
        scores.add(new ScoreEntry(name, score));
        scores.sort((a, b) -> Integer.compare(b.score, a.score));
        if (scores.size() > MAX_SCORES) {
            scores = new ArrayList<>(scores.subList(0, MAX_SCORES));
        }
        saveToFile();
    }

    public List<ScoreEntry> getTopScores() {
        return new ArrayList<>(scores);
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        scores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
                    } catch (NumberFormatException ignored) {}
                }
            }
            // sort và giữ 5 cao nhất
            scores.sort((a, b) -> Integer.compare(b.score, a.score));
            if (scores.size() > MAX_SCORES) {
                scores = new ArrayList<>(scores.subList(0, MAX_SCORES));
            }
        } catch (IOException e) {
            // File không tồn tại -> tạo file mưới
            System.out.println("Tạo file highscores mới");
        }
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            for (ScoreEntry entry : scores) {
                writer.println(entry.name + "," + entry.score);
            }
        } catch (IOException e) {
            System.err.println("Lỗi lưu highscore: " + e.getMessage());
        }
    }
}