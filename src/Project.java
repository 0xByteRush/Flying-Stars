// Importing necessery Libraries
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// Creating class called Star
class Star {
    // This is made for a surprise rainbow line that will show up
    static int count = 1;
    // Growth factor varible that controls the growth of the star
    private double growthFactor = 1.0;
    // array of starting points
    //here we are adjusting 10 different  points in order to make the star
    private final double[] startingPoints = {
            400.0, 300.0,  // Top point
            370.0, 350.0,  // Right upper point
            320.0, 350.0,  // Right point
            370.0, 380.0,  // Right lower point
            350.0, 430.0,  // Bottom right point
            400.0, 400.0,  // Bottom point
            450.0, 430.0,  // Bottom left point
            430.0, 380.0,  // Left lower point
            480.0, 350.0,  // Left point
            430.0, 350.0   // Left upper point
    };
    // Here we are making a list of colors in order to loop over specific colors and prevent lacking the ball color in the star.
    private Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.FORESTGREEN,
            Color.DARKBLUE, Color.INDIGO, Color.VIOLET, Color.BROWN,
            Color.DARKCYAN, Color.PURPLE
    };
    // We used LinearGradient Library to make the rainbow edge of the star
    LinearGradient rainbowGradient = new LinearGradient(0, 0, 1, 0, true, null,
            new Stop(0, Color.RED),
            new Stop(0.14, Color.ORANGE),
            new Stop(0.28, Color.YELLOW),
            new Stop(0.42, Color.GREEN),
            new Stop(0.57, Color.CYAN),
            new Stop(0.71, Color.BLUE),
            new Stop(0.85, Color.INDIGO),
            new Stop(1, Color.VIOLET)
    );
    // Random class to choose a random color
    private Random rand = new Random();

    // Array list to save lines
    ArrayList<Line> lines = new ArrayList<>();
    // Variable to hold the state of interaction with the star
    private boolean touched = false;
    // Constructor of class Star
    public Star() {

        for (int i = 0; i < startingPoints.length; i += 2) {
            int nextIndex = (i + 2) % startingPoints.length;
            Line line = new Line(startingPoints[i], startingPoints[i + 1], startingPoints[nextIndex], startingPoints[nextIndex + 1]);
            // Setting a random color for each edge of the star
            line.setStroke(colors[rand.nextInt(10)]);
            line.setStrokeWidth(2);
            lines.add(line);  // Add the line to the ArrayList
        }
        if (count%10==0){
            int index = rand.nextInt(10);
            lines.get(index).setStroke(rainbowGradient);
        }
        count++;
    }

    // function to return lines to be shown on the pane
    public ArrayList<Line> getLines() {
        return lines;
    }

    public void grow() {
        double centerX = 400.0;  // Center X of the screen (middle of the 800x800 scene)
        double centerY = 380.0;  // Center Y of the screen (middle of the 800x800 scene)

        // Loop through each pair of points to scale them outward
        for (int i = 0; i < startingPoints.length; i += 2) {
            double x = startingPoints[i];      // Get the current X coordinate of the point
            double y = startingPoints[i + 1];  // Get the current Y coordinate of the point

            // Calculate the vector from the center to this point
            double dx = x - centerX;
            double dy = y - centerY;

            // Scale the vector outward by the growth factor
            double newX = centerX + dx * growthFactor;
            double newY = centerY + dy * growthFactor;

            // Update the starting points with the new scaled values
            startingPoints[i] = newX;
            startingPoints[i + 1] = newY;
        }

        // Update the lines to reflect the new positions
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);

            // Get the start and end point indices for the current line
            int startIdx = i * 2;  // Each line has two consecutive points
            int endIdx = (startIdx + 2) % startingPoints.length;  // Wrap around to the first point

            // Get the new start and end coordinates for the line
            double startX = startingPoints[startIdx];
            double startY = startingPoints[startIdx + 1];
            double endX = startingPoints[endIdx];
            double endY = startingPoints[endIdx + 1];

            // Update the line's start and end coordinates
            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX(endX);
            line.setEndY(endY);
        }

        // Increase the growth factor to continue expanding
        growthFactor += 0.0008; // control the growing of the growth factor
    }
    // A method that makes the stars disappear
    public void setVisible(boolean state){
        if (!state) {
            for(Line line: lines){
                line.setVisible(false);

            }
        }
    }
    // A method that ensures the Star is touched
    public void setTouched(boolean touched){
        this.touched = touched; //
    }
    // A method that tests if the Star is touched
    public boolean isTouched(){
        return touched;
    }
}
// Creating class called Ball
// Ball class represents the player-controlled ball in the game
class Ball {
    // Random number generator for color selection
    private Random rand = new Random();

    // Circle representing the ball
    private Circle circle;

    // Array of possible ball colors
    private Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.FORESTGREEN,
            Color.DARKBLUE, Color.INDIGO, Color.VIOLET, Color.BROWN,
            Color.DARKCYAN, Color.PURPLE
    };

    // Flag to control if the ball can be dragged
    private boolean canDrag = true;

    // Constructor to create a ball at specific coordinates with given radius
    public Ball(double x, double y, double radius) {
        this.circle = new Circle(x, y, radius);
    }

    // Assigns a random color to the ball
    public void setColorRandom() {
        this.circle.setFill(colors[rand.nextInt(10)]);
    }

    // Returns the circle representing the ball
    public Circle getCircle() {
        return this.circle;
    }

    // Sets the x-coordinate of the ball's center
    public void setCenterX(double x) {
        this.circle.setCenterX(x);
    }

    // Sets the y-coordinate of the ball's center
    public void setCenterY(double y) {
        this.circle.setCenterY(y);
    }

    // Checks if the ball is currently draggable
    public boolean isDragable() {
        return canDrag;
    }

    // Sets the draggable state of the ball
    public void setDragable(boolean state) {
        canDrag = state;
    }

    // Resets the ball to a random corner of the screen
    public void reInitiate() {
        canDrag = false;
        // Predefined corner coordinates
        int[] RandomX = {100, 750, 100, 750}; //800-50 = 750
        int[] RandomY = {100, 100, 740, 740};

        // Select a random corner
        int index = rand.nextInt(4);
        setCenterX(RandomX[index]);
        setCenterY(RandomY[index]);

        // Assign a new random color
        setColorRandom();
    }
}

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
// Main game class extending JavaFX Application
public class Project extends Application {
    // Timeline for game mechanics
    private Timeline gameTimeLine;
    private Timeline timeline;

    // Method to initialize players by reading from a CSV file
    public void intializing(ArrayList<Player> playersList) throws IOException {
        try {
            FileInputStream fileByteStream = null;
            Scanner inFS = null;

            // Open the CSV file containing player names and scores
            fileByteStream = new FileInputStream("C://Users/yasee/IdeaProjects/FinalProject/src/namesScores.csv");
            inFS = new Scanner(fileByteStream);

            // Read each line from the file
            while (inFS.hasNextLine()) {
                // Split the line into name and score
                String[] columns = inFS.nextLine().split(",");
                String name = columns[0];
                int score = Integer.parseInt(columns[1]);

                // Create a new Player object and add to the list
                Player newPlayer = new Player();
                newPlayer.setName(name);
                newPlayer.setScore(score);
                playersList.add(newPlayer);
            }

            // Sort the players list by score
            playersList.sort(null);

            // Close the file stream
            fileByteStream.close();
        } catch(Exception e) {
            // Silently handle any initialization errors
            return;
        }
    }

    // Creates the start page of the game
    public void startPage(Stage primaryStage) {
        // Create various UI elements for the start page
        Text welcomeText = new Text("Welcome To The Game!!!");
        Text dashboardTitle = new Text("Go to the LeaderBoard");
        Button dashboard = new Button(" LeaderBoard");
        Text gameName = new Text("Flying Stars Game ");
        TextField playerName = new TextField();
        Text madeBy = new Text("Made By:\n *Abdulaaziz AlKhatib \n *Yaseen AlYaseen \n *Hamza Ammous ");

        // Style and position various text elements
        madeBy.setFill(Color.FORESTGREEN);
        madeBy.setFont(Font.font("Impact", 20));
        madeBy.setX(600);
        madeBy.setY(700);

        gameName.setFill(Color.BISQUE);
        gameName.setFont(Font.font("Impact", 50));
        gameName.setX(10);
        gameName.setY(50);

        // Configure dashboard and welcome text
        dashboardTitle.setFill(Color.RED);
        dashboardTitle.setFont(Font.font(20));
        dashboardTitle.setX(590);
        dashboardTitle.setY(40);

        welcomeText.setFill(Color.FORESTGREEN);
        welcomeText.setFont(Font.font("Lucida Calligraphy", 25));
        welcomeText.setX(255);
        welcomeText.setY(350);

        // Style dashboard button
        dashboard.setTextFill(Color.RED);
        dashboard.setFont(Font.font(15));
        dashboard.setLayoutX(630);
        dashboard.setLayoutY(50);

        // Create start game button
        Button starting = new Button("Start the game");
        starting.setTextFill(Color.BLUE);
        starting.setFont(Font.font("Garamond", 20));
        starting.setLayoutX(318);
        starting.setLayoutY(470);

        // Configure player name input
        playerName.setPrefColumnCount(15);
        playerName.setEditable(true);
        playerName.setPromptText("Enter Your Name Here ");
        playerName.setLayoutX(295);
        playerName.setLayoutY(400);

        // Initialize players list from file
        ArrayList<Player> players = new ArrayList<Player>();
        try {
            intializing(players);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        TextField finalPlayerName = playerName;
        // Set up event handlers for buttons
        starting.setOnAction(event -> {gamePage(primaryStage, players, finalPlayerName.getText());});
        dashboard.setOnAction(event -> {leaderBoardPage(primaryStage, players);});

        // Create scene and set up UI
        Pane startRoot = new Pane();
        startRoot.getChildren().addAll(welcomeText, starting, dashboardTitle, dashboard, gameName, playerName, madeBy);
        Scene startScene = new Scene(startRoot, 800, 780);
        startScene.getRoot().setStyle("-fx-background-color: lightblue;");
        primaryStage.setScene(startScene);
    }
    // Method to update player scores and save to CSV file
    public void update(ArrayList<Player> players, int[] score, String playerName) {
        // Find the player in the existing list
        int newScore;
        int index = -1;
        for (Player p : players) {
            if (playerName.equals(p.getName())) {
                index = players.indexOf(p);
                break;
            }
        }

        // Update or add player score
        if (index != -1) {
            // Existing player: update score if new score is higher
            newScore = players.get(index).getScore();
            if (score[0] > newScore) {
                newScore = score[0];
            }
            players.get(index).setScore(newScore);
        } else {
            // New player: create and add to list
            Player newPlayer = new Player();
            newPlayer.setName(playerName);
            newPlayer.setScore(score[0]);
            players.add(newPlayer);
        }

        // Sort players by score
        players.sort(null);

        // Write updated scores to CSV file
        FileOutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream("C://Users/yasee/IdeaProjects/FinalProject/src/namesScores.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PrintWriter filePrinter = new PrintWriter(fileStream);
        for (int i = 0; i < players.size(); ++i) {
            filePrinter.println(players.get(i).getName() + "," + players.get(i).getScore());
        }
        filePrinter.close();
    }

    // Game page method
    public void gamePage(Stage primaryStage, ArrayList<Player> players, String playerName) {
        // Initialize player score
        final int[] playerScore = {0};
        Pane gameRoot = new Pane();

        // Create and configure the ball
        Ball ball = new Ball(400, 740, 20);
        ball.setColorRandom();
        gameRoot.getChildren().add(ball.getCircle());

        // Set up ball dragging mechanics
        ball.getCircle().setOnMousePressed(even -> {
            ball.setDragable(true);
            ball.getCircle().toFront();
        });
        ball.getCircle().setOnMouseDragged(even1 -> {
            if (ball.isDragable() && (even1.getX() >= 0 && even1.getX() <= 800) && (even1.getY() >= 0 && even1.getY() <= 780)) {
                ball.setCenterX(even1.getX());
                ball.setCenterY(even1.getY());
            } else if (ball.isDragable()) {
                ball.setDragable(false);
            }
        });

        // Create try indicators (lives)
        ArrayList<Circle> tries = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            Circle babyCircle = new Circle(740 + i * 20, 40, 5);
            babyCircle.setFill(ball.getCircle().getFill());//Yaseen
            tries.add(babyCircle);
            gameRoot.getChildren().add(babyCircle);
        }

        // Create score and reaction time labels
        Label score = new Label();
        Label reactionTimeLabel = new Label();
        reactionTimeLabel.setText("Your Average Reaction time: 0 sec");
        score.setText("Your score: 0");
        reactionTimeLabel.setLayoutX(10);
        reactionTimeLabel.setLayoutY(30);
        score.setLayoutX(10);
        score.setLayoutY(10);
        score.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        reactionTimeLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        reactionTimeLabel.setPickOnBounds(false);
        score.setPickOnBounds(false);
        gameRoot.getChildren().addAll(score, reactionTimeLabel);

        // Track reaction time
        double[] reactionTime = {0.0, 0.0}; // index 0 = total time, index 1 = number of stars touched

        // Main game timeline for spawning stars
        gameTimeLine = new Timeline(new KeyFrame(Duration.seconds(1.75), eveynt -> {
            // Create a new star
            Star star = new Star();
            for (Line line : star.getLines()) {
                gameRoot.getChildren().add(line);
            }

            // Star movement and interaction timeline
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
                star.grow();
                reactionTime[0] += 0.05; // increase reaction time

                // Check for star-ball interactions
                for (Line starLine : star.getLines()) {
                    Shape intersection = Shape.intersect(starLine, ball.getCircle());
                    if (intersection.getBoundsInLocal().getWidth() > 0 && intersection.getBoundsInLocal().getHeight() > 0) {
                        // Color match scoring
                        if (starLine.getStroke().equals(ball.getCircle().getFill()) && !star.isTouched()) {
                            star.setVisible(false);
                            playerScore[0] += 1;
                            star.setTouched(true);
                            score.setText("Your score: " + playerScore[0]);
                            reactionTime[1]++; // increase stars touched
                            reactionTimeLabel.setText(String.format("Your Average Reaction time: %.2f sec", reactionTime[0] / reactionTime[1]));
                        }
                        // Rainbow star scoring
                        else if (!(starLine.getStroke() instanceof Color) && !star.isTouched()) {
                            star.setVisible(false);
                            playerScore[0] += 5;
                            star.setTouched(true);
                            score.setText("Your score: " + playerScore[0]);
                            reactionTime[1]++;
                            reactionTimeLabel.setText(String.format("Your Average Reaction time: %.2f sec", reactionTime[0] / reactionTime[1]));
                        }
                        // Incorrect color handling
                        else if (!(starLine.getStroke().equals(ball.getCircle().getFill())) && !star.isTouched()) {
                            reactionTime[1]++;
                            reactionTimeLabel.setText(String.format("Your Average Reaction time: %.2f sec", reactionTime[0] / reactionTime[1]));

                            // Game over logic
                            if (tries.size() == 1) {
                                timeline.stop();
                                gameTimeLine.stop();
                                update(players, playerScore, playerName);
                                endPage(primaryStage, players, playerScore, playerName);
                            }
                            // Lose a life
                            else if (!star.isTouched()) {
                                gameRoot.getChildren().remove(tries.get(tries.size() - 1));
                                tries.removeLast();
                                star.setVisible(false);
                                star.setTouched(true);
                                ball.reInitiate();
                            }
                        }
                    }
                }

                // Remove star if it goes too high
                if (star.getLines().get(0).getStartY() <= -220) {
                    star.setVisible(false);
                    star.setTouched(true);
                }
            }));
            timeline.setCycleCount(-1);
            timeline.play();
        }));
        gameTimeLine.setCycleCount(-1);
        gameTimeLine.play();
        gameRoot.setStyle("-fx-background-color: black;");
        // Create and set game scene
        Scene scene = new Scene(gameRoot, 800, 780, Color.BLACK);
        primaryStage.setScene(scene);
    }
    // Leaderboard page method
    public void leaderBoardPage(Stage primaryStage, ArrayList<Player> players) {
        // Get top 5 players
        ArrayList<Player> topPlayers = new ArrayList<>(players.subList(0, 5));//Math.min(5, players.size())

        Pane leaderBoardRoot = new Pane();

        // Create return button to start page
        Button returnButton = new Button("Start Page");
        returnButton.setOnAction(event -> {startPage(primaryStage);});

        // Create leaderboard headers and labels
        Label nameHeader = new Label("Name");
        nameHeader.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        Label scoreHeader = new Label("Score");
        scoreHeader.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        Label leaderBoard = new Label("Leader Board Of Top 5 Achievers");
        leaderBoard.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        leaderBoard.setLayoutX(20);
        leaderBoard.setLayoutY(0);

        Label back = new Label("Back to Start Page");
        back.setLayoutX(500);
        back.setLayoutY(650);
        back.setStyle("-fx-font-size: 30px;");

        // Position headers
        nameHeader.setLayoutX(225);
        nameHeader.setLayoutY(100);
        scoreHeader.setLayoutX(525);
        scoreHeader.setLayoutY(100);
        returnButton.setLayoutX(600);
        returnButton.setLayoutY(700);
        leaderBoardRoot.getChildren().addAll(nameHeader, scoreHeader, returnButton, leaderBoard, back);

        // Display top players
        for (int i = 0; i < topPlayers.size(); i++) {
            Label nameLabel = new Label(topPlayers.get(i).getName());
            nameLabel.setStyle("-fx-font-size: 30px;");

            Label scoreLabel = new Label(String.valueOf(topPlayers.get(i).getScore()));
            scoreLabel.setStyle("-fx-font-size: 30px;");
            nameLabel.setLayoutX(230);
            nameLabel.setLayoutY(150 + i * 100);
            scoreLabel.setLayoutX(550);
            scoreLabel.setLayoutY(150 + i * 100);

            leaderBoardRoot.getChildren().add(nameLabel);
            leaderBoardRoot.getChildren().add(scoreLabel);
        }
        leaderBoardRoot.setStyle("-fx-background-color: gold;");

        // Create and set leaderboard scene
        Scene leaderBoardScene = new Scene(leaderBoardRoot, 800, 780);
        primaryStage.setScene(leaderBoardScene);
    }

    // Game over page method
    public void endPage(Stage primaryStage, ArrayList<Player> players, int[] score, String name) {
        // Create game over text
        Text gameOver = new Text("Game Over \n       *_*");
        gameOver.setFont(Font.font(60));
        gameOver.setFill(Color.RED);
        gameOver.setX(250);
        gameOver.setY(300);
        Label nameScore= new Label(name+" your score is "+score[0]);
        nameScore.setLayoutX(275);
        nameScore.setLayoutY(400);
        nameScore.setTextFill(Color.RED);
        nameScore.setStyle("-fx-font-size: 30px;");

        // Create buttons for leaderboard and restart
        Button lbButton = new Button("Leaderboard");
        Button resetButton = new Button("Restart");
        lbButton.setPrefSize(100, 50);

        lbButton.setLayoutX(250);
        lbButton.setLayoutY(500);
        resetButton.setPrefSize(100, 50);
        resetButton.setLayoutX(500);
        resetButton.setLayoutY(500);

        // Set button actions
        resetButton.setOnAction(event -> {startPage(primaryStage);});
        lbButton.setOnAction(event -> {leaderBoardPage(primaryStage, players);});

        // Create end page scene
        Pane endPageRoot = new Pane();
        endPageRoot.getChildren().addAll(gameOver, lbButton, resetButton,nameScore);
        Scene scene = new Scene(endPageRoot, 800, 780, Color.BLACK);
        primaryStage.setScene(scene);
    }

    // Application start method
    public void start(Stage primaryStage) {
        // Begin with start page
        startPage(primaryStage);

        // Set window title
        primaryStage.setTitle("Flying Stars Game");
        primaryStage.show();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
