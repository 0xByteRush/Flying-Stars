package flyingstars;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Reads and writes the leaderboard as "name,score" lines.
// The default location is in the user's home directory, so the game works from any checkout.
final class ScoreFile {
    static final Path DEFAULT = Path.of(System.getProperty("user.home"), ".flying-stars", "scores.csv");

    private final Path path;

    ScoreFile(Path path) {
        this.path = path;
    }

    // Returns the saved players sorted by score; a missing file or malformed lines are skipped
    List<Player> load() {
        List<Player> players = new ArrayList<>();
        if (!Files.exists(path)) {
            return players;
        }
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                int comma = line.lastIndexOf(',');
                if (comma <= 0) {
                    continue;
                }
                try {
                    Player player = new Player();
                    player.setName(line.substring(0, comma));
                    player.setScore(Integer.parseInt(line.substring(comma + 1).trim()));
                    players.add(player);
                } catch (NumberFormatException e) {
                    // Skip lines with a non-numeric score
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read scores from " + path + ": " + e.getMessage());
        }
        players.sort(null);
        return players;
    }

    // Overwrites the file with the given players, creating the directory if needed
    void save(List<Player> players) {
        List<String> lines = new ArrayList<>();
        for (Player player : players) {
            lines.add(player.getName().replace(",", " ") + "," + player.getScore());
        }
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Could not save scores to " + path + ": " + e.getMessage());
        }
    }
}
