import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/*
 * Tbh this isn't the most performant application and took me a little bit longer as I haven't created an application such as this in Java before.
 * As such, I want to give a little disclaimer that I did use Ai to help me construct this. However, I did NOT use it to just write the code for me.
 * I used it for help and advice.
 * 
 * If I am accepted or not, I would like to thank you guys for the opportunity and would wish for an email back.
 */

public class App extends JFrame {
    private DrawCanvas canvas;
    private ArrayList<Target> targetList;
    private Thread gameThread;
    private Timer gameTimer;
    private int numTargets = 5000;
    private Boolean gameOn = true;
    private int clickCount = 0;
    private int targetsDestroyed = 0;

    public App() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1900, 1000);
        canvas = new DrawCanvas();
        canvas.setSize(1900,1000);
        canvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("click");
                clickCount++;
                for(int i = 0; i < targetList.size(); i++) {
                    if(targetList.get(i).getBounds().intersects(e.getX(), e.getY(),1,1)) {
                        targetList.get(i).isActive = false;
                        targetList.remove(i);
                        targetsDestroyed++;
                        System.out.println("destroyed");
                    }
                }
            }
        });
        setContentPane(canvas);
        // Populate targetList with targets
        targetList = new ArrayList<Target>();
        for(int i = 0; i < numTargets; i++) {
            Target t = new Target();
            targetList.add(t);
        }
        setVisible(true);
        requestFocus();
        // Implement new thread to update game animation
        gameThread = new Thread(new Runnable() {
            public void run() {
                while(gameOn) {
                    for(int i = 0; i < targetList.size(); i++) {
                        targetList.get(i).update();
                    }
                    repaint();
                    try {
                        Thread.sleep(35);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } // When game is no longer active, update all of the targets to make them disappear
                for(int i = 0; i < targetList.size(); i++) {
                    targetList.get(i).isActive = false;
                }
                repaint();
            }
        });
        // Implement a new timer thread
        gameTimer = new Timer(60000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("timer ended");
                gameOn = false;
                endGame();
            }
        });
        gameThread.start();
        gameTimer.start();
    }
    
    // Start app
    public static void main(String[] args) {
        new App();
    }

    private class DrawCanvas extends JPanel {
        private BufferedImage buffer;
        public DrawCanvas() {
            buffer = new BufferedImage(1900, 1000, BufferedImage.TYPE_INT_ARGB);
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Graphics2D bufferGraphics = buffer.createGraphics();
            bufferGraphics.setBackground(getBackground());
            bufferGraphics.clearRect(0, 0, getWidth(), getHeight());
            for(int i = 0; i < targetList.size(); i++) {
                targetList.get(i).draw(bufferGraphics);
            }
            g2.drawImage(buffer, 0, 0, null);
        }
    }

    public void endGame() {
        gameTimer.stop();
        Double hitPercent = (Double)(100.0 *  ((double)targetsDestroyed / clickCount));
        Double missPercent = (Double)(100.0 * (numTargets - targetsDestroyed) / numTargets);
        getContentPane().remove(canvas);
        JPanel results = new JPanel();
        results.setLayout(null);
        results.setBounds(0,0, getWidth(), getHeight());
        JLabel clicks = new JLabel("Clicks: " + clickCount);
        JLabel hits = new JLabel("Hits: " + targetsDestroyed);
        JLabel pctHit = new JLabel(String.format("%% Hit: %.0f", hitPercent));
        JLabel pctMiss = new JLabel(String.format("%% of total targets missed: %.0f", missPercent));
        clicks.setBounds(getWidth() / 2 - 75, getHeight() / 2 - 150, 150, 50);
        hits.setBounds(getWidth() / 2 - 75, getHeight() / 2 - 75, 150, 50);
        pctHit.setBounds(getWidth() / 2 - 75, getHeight() / 2, 150, 50);
        pctMiss.setBounds(getWidth() / 2 - 75, getHeight() / 2 + 75, 250, 50);
        results.add(clicks);
        results.add(hits);
        results.add(pctHit);
        results.add(pctMiss);
        add(results);
    }
}
