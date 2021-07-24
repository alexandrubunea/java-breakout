import java.awt.*;
import java.util.Random;

public class Ball {
    // ball-props
    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private int centerX;
    private int centerY;
    private final int initX;
    private final int initY;
    private final int radius;
    private final static int BALL_VELOCITY_X = 3;
    private final static int BALL_VELOCITY_Y = 3;

    // game-props
    private int livesRemaining = 3;
    private int score = 0;
    private boolean gameOver = false;

    // constructor
    Ball(int x, int y, int radius, int score) {
        this.initX = x;
        this.initY = y;
        this.radius = radius;
        this.score = score;

        init();
    }

    // ball-init
    private void init() {
        // init-ball-position
        this.x = this.initX;
        this.y = this.initY;

        this.centerX = this.x + this.radius / 2;
        this.centerY = this.y + this.radius / 2;

        // init-ball-direction
        this.speedX = (int) (BALL_VELOCITY_X * Math.sin(Math.toRadians(Math.random() * 180)));
        this.speedY = (int) -(BALL_VELOCITY_Y * Math.cos(Math.toRadians(Math.random() * 60)));
    }

    // ball-render
    public void render(Graphics g, boolean AI) {
        // render-ball
        g.setColor(Color.WHITE);
        g.fillOval(this.x, this.y, this.radius, this.radius);

        if(!AI) {
            // render-lives
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("REMAINING LIVES: ", 10, 15);

            g.setColor(Color.RED);
            int location_x = 130;
            for (int i = 1; i <= livesRemaining; i++) {
                g.fillOval(location_x + 20, 3, 15, 15);
                location_x = location_x + 20;
            }

            // render-score
            g.setColor(Color.WHITE);
            g.drawString("SCORE: " + this.score, 10, 40);
        }
    }

    // ball-movement
    public void move() {
        this.x += this.speedX;
        this.y += this.speedY;

        this.centerX = this.x + this.radius / 2;
        this.centerY = this.y + this.radius / 2;
    }
    // reverse speed
    public void reverseSpeedY() { this.speedY = -this.speedY; }
    public void reverseSpeedX() { this.speedX = -this.speedX; }

    // ball-collisions
    public void collide(int RIGHT_LIMIT, Paddle paddle, boolean AI) {
        // with the walls
        if(this.centerX - this.radius / 2 < 0 && this.speedX < 0) this.speedX = -this.speedX;
        else if(this.centerX + this.radius / 2 > RIGHT_LIMIT && this.speedX > 0) this.speedX = -this.speedX;

        if(this.centerY - this.radius / 2 < 0 && this.speedY < 0) this.speedY = -this.speedY;
        else if(this.centerY + this.radius / 2 > paddle.centerY() + paddle.height() / 2 && this.speedY > 0) {
            init();
            if(!AI) {
                livesRemaining--;
                if (livesRemaining == 0) gameOver = true;
            }
        }

        // with the paddle
        if(this.centerY + this.radius / 2 >= paddle.centerY() - paddle.height() / 2 &&
        this.centerX + this.radius / 2 >= paddle.centerX() - paddle.width() / 2 &&
        this.centerX - this.radius / 2 <= paddle.centerX() + paddle.width() / 2 &&
        this.speedY > 0) {
            double offsetX = paddle.centerX() - this.centerX;
            double ratioX = offsetX / ((double) paddle.width() / 2);
            double bounceAngle = Math.toRadians(ratioX * 75);

            // inverting-x-axis-with-y-axis
            this.speedY = (int) -(BALL_VELOCITY_Y * Math.cos(bounceAngle));
            this.speedX = (int) -(BALL_VELOCITY_X * Math.sin(bounceAngle));

            if(this.speedY == 0) this.speedY = -BALL_VELOCITY_Y;
            if(this.speedX == 0 && AI) this.speedX = BALL_VELOCITY_X;
        }
    }

    // get-props
    public int centerX() { return this.centerX; }
    public int centerY() { return this.centerY; }
    public int radius() { return this.radius; }
    public int speedX() { return this.speedX; }
    public int speedY() { return this.speedY; }
    public int score() { return this.score; }
    public boolean isGameOver() { return this.gameOver; }

    // modify-props
    public void increaseScore() { this.score += 10; }
}
