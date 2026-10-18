package flyingstars;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

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
