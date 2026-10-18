package flyingstars;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Random;

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
