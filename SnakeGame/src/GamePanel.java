import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean isRunning = false;
    Timer timer;
    Random random;
    boolean isAppleEpic = false;
    boolean epicMode = false;
    int applesEpic;
    boolean pause = false;
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newApple();
        isRunning = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if(isRunning) {

            if(isAppleEpic) {
                g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));;
                g.fillOval(appleX,appleY, UNIT_SIZE, UNIT_SIZE);
            }
            else {
                g.setColor(Color.red);
                g.fillOval(appleX,appleY, UNIT_SIZE, UNIT_SIZE);
            }

            if(epicMode) {
                g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                g.drawLine(0, 0, 0, SCREEN_HEIGHT);
                g.drawLine(SCREEN_WIDTH-1, 0, SCREEN_WIDTH-1, SCREEN_HEIGHT);
                g.drawLine(0, 0, SCREEN_WIDTH-1, 0);
                g.drawLine(0, SCREEN_HEIGHT-1, SCREEN_WIDTH-1, SCREEN_HEIGHT-1);
            }

            for(int i=0;i<bodyParts;i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i],y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
        if(pause) {
            pause(g);
        }
    }
    public void newApple() {
        if(random.nextInt(10) == 1) {
            isAppleEpic = true;
        }
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move() {
        for(int i=bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple() {
        if((x[0] == appleX)&& (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            if(isAppleEpic) {
                applesEpic = applesEaten + 5;
                isAppleEpic = false;
                epicMode = true;
            }
            if(applesEaten == applesEpic) {
                epicMode = false;
            }
            newApple();
        }
    }
    public void checkCollision() {
        for(int i=bodyParts;i>0;i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                isRunning = false;
                break;
            }
        }
        if(x[0] < 0) {
            if(epicMode) {
                x[0] = SCREEN_WIDTH-UNIT_SIZE;
            }
            else {
                isRunning = false;
            }
        }

        if(x[0] > SCREEN_WIDTH-UNIT_SIZE) {
            if(epicMode) {
                x[0] = 0;
            }
            else {
                isRunning = false;
            }
        }

        if(y[0] < 0) {
            if(epicMode) {
                y[0] = SCREEN_HEIGHT-UNIT_SIZE;
            }
            else {
                isRunning = false;
            }
        }

        if(y[0] > SCREEN_HEIGHT-UNIT_SIZE) {
            if(epicMode) {
                y[0] = 0;
            }
            else {
                isRunning = false;
            }
        }
        if(!isRunning) {
            epicMode = false;
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

        g.setFont(new Font("Ink Free",Font.BOLD, 35));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press 'R' to restart", (SCREEN_WIDTH - metrics3.stringWidth("Press 'R' to restart"))/2, ((SCREEN_HEIGHT)/2)+75);

        g.setFont(new Font("Ink Free",Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
    }
    public void pause(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 55));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME PAUSED", (SCREEN_WIDTH - metrics1.stringWidth("GAME PAUSED"))/2, SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(isRunning) {
            checkApple();
            move();
            checkCollision();

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R' && !pause) {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L' && !pause) {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D' && !pause) {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U' && !pause) {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_R:
                    if(!isRunning) {
                        for(int i=0;i<bodyParts;i++) {
                            x[i] = 0;
                            y[i] = 0;
                        }
                        direction = 'R';
                        bodyParts = 6;
                        applesEaten = 0;
                        startGame();
                    }
                    break;
                case KeyEvent.VK_P:
                    if(isRunning) {
                        if (!pause) {
                            pause = true;
                            timer.stop();
                        } else {
                            pause = false;
                            timer.start();
                        }
                    }
                    break;
            }
        }
    }
}
