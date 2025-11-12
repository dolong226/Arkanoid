import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.HighScoreTable;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HighScoreTableTest {
    private static final String FILENAME = "highscores.txt";
    private static final int MAX_SCORES = 5;

    @Before
    @After
    public void cleanupTestFile() {
        File file = new File(FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Kiểm tra thứ tự sắp xếp điểm từ cao đến thấp
     */
    @Test
    public void testAddScoreSorting() {
        HighScoreTable table = new HighScoreTable();
        table.addScore("A", 100);
        table.addScore("B", 200);
        table.addScore("C", 300);
        List<HighScoreTable.ScoreEntry> scores = table.getTopScores();
        assertEquals(3, scores.size());
        assertEquals("C", scores.get(0).name);
        assertEquals(300, scores.get(0).score);
        assertEquals("B", scores.get(1).name);
        assertEquals(200, scores.get(1).score);
        assertEquals("A", scores.get(2).name);
        assertEquals(100, scores.get(2).score);
    }

    /**
     * Đẩy điểm thấp nhất ra khỏi danh sách.
     * Điểm cao nhất được thêm vào.
     */
    @Test
    public void testAddScoreTruncation() {
        HighScoreTable table = new HighScoreTable();
        table.addScore("A", 100);
        table.addScore("B", 200);
        table.addScore("C", 300);
        table.addScore("D", 400);
        table.addScore("E", 500);
        table.addScore("F", 600); // Đẩy điểm thấp nhất ra khỏi danh sách
        List<HighScoreTable.ScoreEntry> scores = table.getTopScores();
        assertEquals(MAX_SCORES, scores.size());
        assertEquals("F", scores.get(0).name);   // Điểm cao nhất
        assertEquals(600, scores.get(0).score);
        assertEquals("B", scores.get(4).name);   // Điểm thấp nhất
        assertEquals(200, scores.get(4).score);
    }

    /**
     * Kiểm tra điểm thấp quá không được thêm vào.
     */
    @Test
    public void testAddScore_NotMakingTop5() {
        HighScoreTable table = new HighScoreTable();
        table.addScore("A", 100);
        table.addScore("B", 200);
        table.addScore("C", 300);
        table.addScore("D", 400);
        table.addScore("E", 500);
        table.addScore("Z", 50);  // Điểm thấp quá không được thêm vào
        List<HighScoreTable.ScoreEntry> scores = table.getTopScores();
        assertEquals(MAX_SCORES, scores.size());
        for (HighScoreTable.ScoreEntry entry : scores) {
            assertNotEquals("Z", entry.name);
        }
        assertEquals("A", scores.get(4).name); // Điểm thấp nhất khi chưa thêm Z
        assertEquals(100, scores.get(4).score);
    }

    @Test
    public void testSaveAndLoad() {
        HighScoreTable table1 = new HighScoreTable();
        table1.addScore("C", 300);
        table1.addScore("A", 100);
        table1.addScore("B", 200);
        HighScoreTable table2 = new HighScoreTable();
        List<HighScoreTable.ScoreEntry> loadedScores = table2.getTopScores();

        // Kiểm tra xem table2 đã tải đúng dữ liệu chưa
        assertEquals(3, loadedScores.size());
        assertEquals("C", loadedScores.get(0).name);
        assertEquals(300, loadedScores.get(0).score);
        assertEquals("B", loadedScores.get(1).name);
        assertEquals(200, loadedScores.get(1).score);
        assertEquals("A", loadedScores.get(2).name);
        assertEquals(100, loadedScores.get(2).score);
    }

    /**
     * kiểm tra giới hạn.
     */
    @Test
    public void testLoadFromFile_SortsAndTruncates() throws IOException {
        try (PrintWriter in = new PrintWriter(new FileWriter(FILENAME))) {
            in.println("Z,10");
            in.println("A,100");
            in.println("C,300");
            in.println("F,600");
            in.println("B,200");
            in.println("E,500");
            in.println("D,400");
        }
        HighScoreTable table = new HighScoreTable();
        List<HighScoreTable.ScoreEntry> scores = table.getTopScores();
        assertEquals(MAX_SCORES, scores.size());
        assertEquals("F", scores.get(0).name);
        assertEquals("E", scores.get(1).name);
        assertEquals("D", scores.get(2).name);
        assertEquals("C", scores.get(3).name);
        assertEquals("B", scores.get(4).name);
    }

    @Test
    public void testGetTopScores_ReturnsDefensiveCopy() {
        HighScoreTable table = new HighScoreTable();
        table.addScore("A", 200);
        List<HighScoreTable.ScoreEntry> scores = table.getTopScores();
        scores.add(new HighScoreTable.ScoreEntry("Hacker", 9999));
        // Lấy lại danh sách điểm từ table để kiểm tra
        List<HighScoreTable.ScoreEntry> originalScores = table.getTopScores();
        assertEquals(1, originalScores.size());
        assertEquals("A", originalScores.get(0).name);
        assertNotEquals(scores.size(), originalScores.size());
    }
}