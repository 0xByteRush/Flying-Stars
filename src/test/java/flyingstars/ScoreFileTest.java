package flyingstars;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreFileTest {
    @TempDir
    Path dir;

    private static Player player(String name, int score) {
        Player p = new Player();
        p.setName(name);
        p.setScore(score);
        return p;
    }

    @Test
    void missingFileLoadsAsEmpty() {
        assertTrue(new ScoreFile(dir.resolve("none.csv")).load().isEmpty());
    }

    @Test
    void saveThenLoadRoundTripsSortedByScore() {
        ScoreFile file = new ScoreFile(dir.resolve("nested/scores.csv"));
        file.save(List.of(player("Ana", 3), player("Bo", 12), player("Cy", 7)));

        List<Player> loaded = file.load();
        assertEquals(List.of("Bo 12", "Cy 7", "Ana 3"), loaded.stream().map(Player::toString).toList());
    }

    @Test
    void malformedLinesAreSkipped() throws IOException {
        Path path = dir.resolve("scores.csv");
        Files.writeString(path, "Ana,5\nno-comma\nBo,abc\n,4\nCy,9\n");

        assertEquals(List.of("Cy 9", "Ana 5"), new ScoreFile(path).load().stream().map(Player::toString).toList());
    }

    @Test
    void commasInNamesDoNotBreakTheFormat() {
        ScoreFile file = new ScoreFile(dir.resolve("scores.csv"));
        file.save(List.of(player("Smith, J", 4)));

        assertEquals("Smith  J 4", file.load().get(0).toString());
    }
}
