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
    private ArrayList<Brick> bricks = new ArrayList<>();

    // game-props
    private Thread gameThread = new Thread(this);

    // constructor
    Panel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        initGame();

        this.addKeyListener(new MyKeyAdapter());
        gameThread.start();

    }

    // game-init
    private void initGame() {
        // paddle
        paddle = new Paddle(SCREEN_WIDTH / 2 - PADDLE_WIDTH / 2, BOTTOM_LIMIT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

        // ball
        ball = new Ball(SCREEN_WIDTH / 2 - BALL_RADIUS / 2, BOTTOM_LIMIT - 4*BALL_RADIUS, BALL_RADIUS);

        // bricks
        int location_y = TOP_GAP - BRICK_HEIGHT;
        int location_x = 0;
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 20; j++) {
                bricks.add(new Brick(location_x, location_y, BRICK_WIDTH, BRICK_HEIGHT));
                location_x = bricks.get(bricks.size() - 1).centerX() + bricks.get(bricks.size() - 1).width() / 2 + BRICK_GAP;
            }
            location_x = 0;
            location_y = bricks.get(bricks.size() - 1).centerY() + bricks.get(bricks.size() - 1).height() / 2 + BRICK_GAP;
        }
    }

    // render
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g) {
        // render-paddle
        paddle.render(g);

        // render-ball
        ball.render(g);

        // render-bricks
        bricks.forEach(brick -> brick.render(g));
    }

    // key-adapter
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> {
                    if(paddle.getDirection() == 's') paddle.changeDirection('l');
                }
                case KeyEvent.VK_D -> {
                    if(paddle.getDirection() == 's') paddle.changeDirection('r');
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
                ball.collide(SCREEN_WIDTH, paddle);

                repaint();
                delta --;
            }
        }
    }
}
