package ui;

import javax.swing.*;
import java.awt.*;

// Represents a graphical rectangle with
// a constant height but varying width
public class Bar extends JPanel {
    static final int POS_X = 125;
    static final int POS_Y = 25;
    static final int HEIGHT = 20;

    int width;
    Color colour;

    // EFFECTS:  creates a filled rectangle with a certain width and color
    public Bar(int width, Color colour) {
        this.width = width;
        this.colour = colour;
    }

    // EFFECTS:  creates a filled rectangle
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(colour);
        g.fillRect(POS_X, POS_Y, width, HEIGHT);

    }
}
