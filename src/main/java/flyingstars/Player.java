package flyingstars;

// Player class to store player information and support sorting
class Player implements Comparable<Player> {
    // Player's name
    private String name;
    // Player's score
    private int score;

    // Sets the player's name
    public void setName(String name) {
        this.name = name;
    }

    // Sets the player's score
    public void setScore(int score) {
        this.score = score;
    }

    // Retrieves the player's name
    public String getName() {
        return name;
    }

    // Retrieves the player's score
    public int getScore() {
        return score;
    }

    // Custom comparison method for sorting players by score (descending order)
    @Override
    public int compareTo(Player otherPlayer) {
        if (this.score > otherPlayer.score) {
            return -1;
        } else if (this.score == otherPlayer.score) {
            return 0;
        } else {
            return 1;
        }
    }

    // String representation of the player (used for display)
    @Override
    public String toString() {
        return name + " " + score;
    }
}
