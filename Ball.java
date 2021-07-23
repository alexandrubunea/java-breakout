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

    // constructor
    Ball(int x, int y, int radius) {
        this.initX = x;
        this.initY = y;
        this.radius = radius;

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
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(this.x, this.y, this.radius, this.radius);
    }

    // ball-movement
    public void move() {
        this.x += this.speedX;
        this.y += this.speedY;

        this.centerX = this.x + this.radius / 2;
        this.centerY = this.y + this.radius / 2;
    }

    // ball-collisions
    public void collide(int RIGHT_LIMIT, Paddle paddle) {
        // with the walls
        if(this.centerX - this.radius / 2 < 0 && this.speedX < 0) this.speedX = -this.speedX;
        else if(this.centerX + this.radius / 2 > RIGHT_LIMIT && this.speedX > 0) this.speedX = -this.speedX;

        if(this.centerY - this.radius / 2 < 0 && this.speedY < 0) this.speedY = -this.speedY;
        else if(this.centerY + this.radius / 2 > paddle.centerY() + paddle.height() / 2 && this.speedY > 0) init();

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
        }
    }
}
