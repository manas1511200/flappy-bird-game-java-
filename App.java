

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
    //Here we just create a Frame and add flappyBird Panel to it
        flappyBird flap = new flappyBird();
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        flap.requestFocus();
        frame.add(flap,SwingConstants.CENTER);
        frame.setSize(1500, 750);
        frame.setVisible(true);
    }
}
