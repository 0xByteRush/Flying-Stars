package flyingstars;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

// Main game class extending JavaFX Application
public class FlyingStars extends Application {
    // Timeline for game mechanics
    private Timeline gameTimeLine;
    private Timeline timeline;
    // Leaderboard storage (~/.flying-stars/scores.csv)
    private final ScoreFile scoreFile = new ScoreFile(ScoreFile.DEFAULT);

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
        ArrayList<Player> players = new ArrayList<>(scoreFile.load());

        TextField finalPlayerName = playerName;
        // Set up event handlers for buttons
        starting.setOnAction(event -> {
            String name = finalPlayerName.getText().isBlank() ? "Anonymous" : finalPlayerName.getText().trim();
            gamePage(primaryStage, players, name);
        });
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

        // Save updated scores
        scoreFile.save(players);
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
        ArrayList<Player> topPlayers = new ArrayList<>(players.subList(0, Math.min(5, players.size())));

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
