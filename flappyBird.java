


import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

class flappyBird extends JPanel {

    private final Timer pipeTimer, birdTimer;
    private int score = -3;
    private int attempt_no;
    private double rotation = 0;
    private JList<String> scoreList;
    private int popupState = 0;
    private int pipefix = 0;
    private final Image topPipe, bottomPipe, background, birdImage;
    private final Random random = new Random();
    private int birdX = 150, birdY = 300, birdVelocity = 0;
    private final int birdWidth = 80, birdHeight = 60, gravity = 1, jumpHeight = -15, pipeGap = 720;
    private final int[] pipeX = new int[3], pipeY = new int[3];

    private final boolean[] pipePassed = new boolean[3];
     File file = new File("FlappyBird.txt");
    File attempt = new File("Attempt.txt");

    public flappyBird() {
       background = new ImageIcon(Objects.requireNonNull(getClass().getResource("flappybirdbg.png"))).getImage();
birdImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("flappybird.png"))).getImage();
topPipe = new ImageIcon(Objects.requireNonNull(getClass().getResource("toppipe.png"))).getImage();
bottomPipe = new ImageIcon(Objects.requireNonNull(getClass().getResource("bottompipe.png"))).getImage();

        pipeX[0] = getWidth();
        pipeX[1] = getWidth() + getWidth()/3;
        pipeX[2] = getWidth() + 2*(getWidth()/3);
        setFocusable(true);
      addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            birdVelocity = jumpHeight;
            Clip clip = getSound("jump.wav");
            if (clip != null) {
                clip.start();  
            }
		 if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        // Dispose the game window (close it)
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(flappyBird.this);
        if (frame != null) {
            frame.dispose();
        }
    }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Restart the game when Enter key is pressed
            restartGame();
        }
    }
});



        birdTimer = new Timer(1000 / 50, this::updateBird);
        pipeTimer = new Timer(15, this::updatePipes);
        birdTimer.start();
        pipeTimer.start();

        initializePipes();
    }


        private void initializePipes() {
        pipeX[0] = getWidth();

        for (int i = 0; i < 3; i++) {
            pipeY[i] = random.nextInt(-300, 0);
            pipePassed[i] = false;
        }

    }
public static Clip getSound(String path) {
    File file = new File(path);
    Clip clip = null;
    try {
        clip = AudioSystem.getClip();

        // Open the sound file if it's not already open
        if (file.exists()) {
            clip.open(AudioSystem.getAudioInputStream(file));
        } else {
            path = path.substring(path.indexOf("/") + 1);
            clip.open(AudioSystem.getAudioInputStream(ClassLoader.getSystemClassLoader().getResource(path)));
        }

        // Stop and reset the clip if it's already playing
        if (clip.isRunning()) {
            clip.stop();
            clip.flush();
        }

        // Start the clip to play the sound
        clip.start();

    } catch (LineUnavailableException e) {
        System.out.println("Audio line format not supported: " + e.getMessage());
        e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
        System.out.println("Audio file format not supported: " + e.getMessage());
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return clip;
}


	
    private void updatePipes(ActionEvent e) {
        for (int i = 0; i < 3; i++) {
            pipeX[i] -= 2;

            if (pipeX[i] + topPipe.getWidth(null) / 7 < 0) {
                pipeX[i] = getWidth();
                pipeY[i] = random.nextInt(-300, 0);
                pipePassed[i] = false;
            }

            if(pipeX[0] == 2*(getWidth()/3)){
                pipeX[1] = getWidth();
            }
            if(pipeX[1] == 2*(getWidth()/3)){
                pipeX[2] = getWidth();
            }


        }
        repaint();
    }

    private void updateBird(ActionEvent e) {
        birdVelocity += gravity;
        birdY += birdVelocity;

        if (birdY < 0) {
            birdY = 0;
            birdVelocity = 0;
        }

        if (birdY > getHeight() - birdHeight) {
            birdY = getHeight() - birdHeight;
            birdVelocity = 0;
        }
        rotation = (90.0 * (birdVelocity) / 60) ;
        rotation = -Math.toRadians(rotation);
        if (rotation > Math.PI / 3) {
            rotation = Math.PI / 3;
        } else if (rotation < -Math.PI / 3) {
            rotation = -Math.PI / 3;
        }


        checkCollision();
        repaint();
    }

    private void checkCollision() {
        Rectangle birdRect = new Rectangle(birdX, birdY, birdWidth, birdHeight);

        for (int i = 0; i < 3; i++) {
            Rectangle topPipeRect = new Rectangle(pipeX[i], pipeY[i], topPipe.getWidth(null) / 7, topPipe.getHeight(null) / 7);
            Rectangle bottomPipeRect = new Rectangle(pipeX[i], pipeY[i] + pipeGap, bottomPipe.getWidth(null) / 7, bottomPipe.getHeight(null) / 7);

            if (birdRect.intersects(topPipeRect) || birdRect.intersects(bottomPipeRect)) {
            Clip clip = getSound("hit.wav");
            if (clip != null) {
                clip.start();  
            }
                birdTimer.stop();
                pipeTimer.stop();
                popupState = 0;

                saveScore();
                showPopup();


            }

            if (!pipePassed[i] && birdX > pipeX[i] + topPipe.getWidth(null) / 7) {
             Clip clip = getSound("point.wav");
            if (clip != null) {
                clip.start();  
            }
                score++;
                pipePassed[i] = true;
            }
           if(birdX <= pipeX[i] + topPipe.getWidth(null)/7) {
               pipePassed[i] = false;
           }
        }
    }

    private void saveScore() {

        try {
            if (!attempt.exists()) {
                attempt.createNewFile();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            Path att = Paths.get("Attempt.txt");
            if (attempt.exists()) {
                String attemptNumber = Files.readString(att).trim();
                 if(attemptNumber.isEmpty()){
                     attemptNumber = "0";
                 }
                attempt_no = Integer.parseInt(attemptNumber) + 1;
                Files.write(att, String.valueOf(attempt_no).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }

            Path path = file.toPath();
            List<String> lines = new ArrayList<>();
            lines.add("Attempt "+ attempt_no +  " : " + score);
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {
        }
    }

    private void loadScores() {
        try {
            List<String> scores = Files.readAllLines(Paths.get("FlappyBird.txt"));
            Collections.sort(scores);
            Collections.reverse(scores);
            scoreList = new JList<>(scores.toArray(new String[0]));
        } catch (IOException ignored) {
        }
    }

    private void showPopup() {
    loadScores();
    if (popupState == 0) {
        popupState++;
        JFrame frame = new JFrame();
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        Button exit  = new Button("EXIT");
	    exit.addActionListener(e -> {
    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(frame);
    if (parentFrame != null) {
        parentFrame.dispose();  // Close the window
    }
});

        Button clearAll = new Button("Clear All");
        clearAll.addActionListener(e -> {
            file.delete();
            attempt.delete();
            saveScore();
            loadScores();
        });

        JLabel scoreLabel = new JLabel("Your Current Score: " + score, SwingConstants.CENTER);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            frame.dispose();
            restartGame();
        });

        // Add replay functionality when Enter is pressed
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                    frame.dispose();
                }
            }
        });

        JPanel bpanel = new JPanel();
        bpanel.add(okButton);
        bpanel.add(clearAll);
        
        frame.add(new JScrollPane(scoreList), BorderLayout.CENTER);
        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.add(bpanel, BorderLayout.SOUTH);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}


    private void restartGame() {
        birdX = 150;
        birdY = 300;
        score = 0;
        birdVelocity = 0;
        pipeX[0] = getWidth();
        pipeX[1] = getWidth() + getWidth() / 3;
        pipeX[2] = getWidth() + 2 * (getWidth() / 3);
        initializePipes();
        birdTimer.restart();
        pipeTimer.restart();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);


        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString(String.valueOf(score), 50, 50);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        AffineTransform birdTransform = new AffineTransform();

        birdTransform.translate(birdX + birdWidth / 2, birdY + birdHeight / 2);
        birdTransform.rotate(rotation);
        birdTransform.translate(-birdWidth / 2, -birdHeight / 2);

        g2d.setTransform(birdTransform);
        g2d.drawImage(birdImage, 0, 0, birdWidth, birdHeight, null);
        g2d.setTransform(originalTransform);


        for (int i = 0; i < 3; i++) {
            g.drawImage(topPipe, pipeX[i], pipeY[i], topPipe.getWidth(null) / 7, topPipe.getHeight(null) / 7, null);
            g.drawImage(bottomPipe, pipeX[i], pipeY[i] + pipeGap, bottomPipe.getWidth(null) / 7, bottomPipe.getHeight(null) / 7, null);
        }
    }
}
