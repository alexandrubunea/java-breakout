import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends JPanel implements Runnable {
    // panel-props
    private static final int SCREEN_HEIGHT = 800;
    private static final int SCREEN_WIDTH = 1000;
    private static final int BOTTOM_LIMIT = SCREEN_HEIGHT - (SCREEN_HEIGHT / 4);

    // paddle-props
    private static final int PADDLE_WIDTH = 200;
    private static final int PADDLE_HEIGHT = 10;
    Paddle paddle;

    // ball-props
    private static final int BALL_RADIUS = 10;
    Ball ball;

    // bricks-props
    private static final int BRICK_WIDTH = 55;
    private static final int BRICK_HEIGHT = 25;
    private static final int BRICK_GAP = 5;
    private static final int TOP_GAP = 100;
    private ArrayList<Brick> bricks;

    // game-props
    private boolean gameOver = false;
    private boolean levelWon = false;
    private boolean gameWon = false;
    private int currentLevel = 4;
    private int score = 0;
    private final Random random = new Random();
    private boolean AI = true;
    private Thread gameThread;

    // constructor
    Panel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        initGame();
    }

    // game-init
    private void initGame() {
        // paddle
        paddle = new Paddle(SCREEN_WIDTH / 2 - PADDLE_WIDTH / 2, BOTTOM_LIMIT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

        // ball
        ball = new Ball(SCREEN_WIDTH / 2 - BALL_RADIUS / 2, BOTTOM_LIMIT - 4*BALL_RADIUS, BALL_RADIUS, score);

        // bricks
        bricks = new ArrayList<>();
        int location_y = TOP_GAP - BRICK_HEIGHT;
        int location_x = 0;
        for(int i = 0; i < currentLevel; i++) {
            for(int j = 0; j < 17; j++) {
                boolean hardBrick = false;
                int chance = random.nextInt(100);
                if(chance <= 5 && currentLevel >= 3) hardBrick = true;
                bricks.add(new Brick(location_x, location_y, BRICK_WIDTH, BRICK_HEIGHT, hardBrick));
                location_x = bricks.get(bricks.size() - 1).centerX() + bricks.get(bricks.size() - 1).width() / 2 + BRICK_GAP;
            }
            location_x = 0;
            location_y = bricks.get(bricks.size() - 1).centerY() + bricks.get(bricks.size() - 1).height() / 2 + BRICK_GAP;
        }

        gameThread = new Thread(this);
        gameThread.start();
    }

    // render
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g) {
        if(!gameOver) {
            if(!levelWon) {
                if(!gameWon) {
                    // render-paddle
                    paddle.render(g);

                    // render-ball
                    ball.render(g, AI);

                    // render-bricks
                    bricks.forEach(brick -> brick.render(g));
                    if(AI) {
                        g.setColor(Color.BLUE);
                        g.setFont(new Font("Arial", Font.BOLD, 40));
                        g.drawString("BREAKOUT", SCREEN_WIDTH / 2 - 110, SCREEN_HEIGHT / 2 - 110);

                        g.setColor(Color.YELLOW);
                        g.setFont(new Font("Arial", Font.BOLD, 16));
                        g.drawString("PRESS SPACE TO START", SCREEN_WIDTH / 2 - 100, SCREEN_HEIGHT / 2 - 80);
                    }
                } else {
                    g.setColor(Color.ORANGE);
                    g.setFont(new Font("Arial", Font.BOLD, 40));
                    g.drawString("GAME WON", SCREEN_WIDTH / 2 - 110, SCREEN_HEIGHT / 2 - 110);
                    g.setFont(new Font("Arial", Font.BOLD, 16));
                    g.drawString("SCORE: " + ball.score(), SCREEN_WIDTH / 2 - 30, SCREEN_HEIGHT / 2 - 80);
                    g.setColor(Color.WHITE);
                    g.drawString("NICE JOB", SCREEN_WIDTH / 2 - 30, SCREEN_HEIGHT / 2 - 50);
                }
            } else {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("LEVEL PASSED", SCREEN_WIDTH / 2 - 110, SCREEN_HEIGHT / 2 - 110);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("PRESS SPACE TO CONTINUE", SCREEN_WIDTH / 2 - 85, SCREEN_HEIGHT / 2 - 50);
            }
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", SCREEN_WIDTH / 2 - 110, SCREEN_HEIGHT / 2 - 110);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("SCORE: " + ball.score(), SCREEN_WIDTH / 2 - 30, SCREEN_HEIGHT / 2 - 80);
            g.setColor(Color.BLUE);
            g.drawString("PRESS SPACE TO RESTART", SCREEN_WIDTH / 2 - 100, SCREEN_HEIGHT / 2 - 50);
        }
    }
    private void checkCurrentLevel() {
        if(!gameOver) {
            int bricksRemaining = 0;
            for (Brick brick : bricks)
                if (brick.status() && !brick.isHardBrick()) bricksRemaining++;

            // player-has-won-the-current-level
            if (bricksRemaining == 0 && !AI) {
                currentLevel++;
                score = ball.score();
                if (currentLevel > 15) gameWon = true;
                else levelWon = true;
                repaint();
                gameThread.stop();
            } else if (bricksRemaining == 0) initGame();
        } else {
            repaint();
            gameThread.stop();
        }
    }
    // key-adapter
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> {
                    if(paddle.getDirection() == 's' && !AI) paddle.changeDirection('l');
                }
                case KeyEvent.VK_D -> {
                    if(paddle.getDirection() == 's' && !AI) paddle.changeDirection('r');
                }
                case KeyEvent.VK_SPACE -> {
                    if(!AI) {
                        if (gameOver) {
                            initGame();
                            gameOver = false;
                        } else if (levelWon) {
                            initGame();
                            levelWon = false;
                        }
                    } else {
                        currentLevel = 1;
                        AI = false;
                        gameThread.stop();
                        initGame();
                    }
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A, KeyEvent.VK_D -> {
                    if(paddle.getDirection() != 's') paddle.changeDirection('s');
                }
            }
        }
    }

    // game-loop
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 144.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                // paddle-movement
                paddle.move(SCREEN_WIDTH);

                // ball-movement
                ball.move();

                // ball-collisions
                ball.collide(SCREEN_WIDTH, paddle, AI);

                // bricks-collisions
                bricks.forEach(brick -> brick.collide(ball));

                // get-game-status
                gameOver = ball.isGameOver();

                // check-current-level-status
                checkCurrentLevel();

                // ai-paddle
                if(AI) paddle.ai(ball);

                repaint();
                delta --;
            }
        }
    }
}
