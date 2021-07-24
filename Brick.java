import java.awt.*;

public class Brick {
    // brick-props
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int centerX;
    private final int centerY;
    private boolean doesExist;
    private final boolean hardBrick;

    // Constructor
    Brick(int x, int y, int width, int height, boolean hardBrick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.centerX = this.x + this.width / 2;
        this.centerY = this.y + this.height / 2;

        this.doesExist = true;
        this.hardBrick = hardBrick;
    }

    // render
    public void render(Graphics g) {
        if(!this.doesExist) return;
        g.setColor((this.hardBrick) ? Color.BLUE : Color.ORANGE);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    // kill-revive
    private void kill() { this.doesExist = false; }
    private void revive() { this.doesExist = true; }

    // collisions-with-the-ball
    public void collide(Ball ball) {
        if(!this.doesExist) return;
        // bottom-side-of-the-brick
        if(ball.centerY() - ball.radius() / 2 <= this.centerY + this.height / 2 &&
        ball.centerY() - ball.radius() / 2 > this.centerY &&
        ball.centerX() + ball.radius() / 2 >= this.centerX - this.width / 2 &&
        ball.centerX() - ball.radius() / 2 <= this.centerX + this.width / 2 &&
        ball.speedY() < 0) {
            ball.reverseSpeedY();
            if(!this.hardBrick) {
                ball.increaseScore();
                kill();
            }
            return;
        }
        // top-side-of-the-brick
        if(ball.centerY() + ball.radius() / 2 >= this.centerY - this.height / 2 &&
        ball.centerY() + ball.radius() / 2 < this.centerY &&
        ball.centerX() + ball.radius() / 2 >= this.centerX - this.width / 2 &&
        ball.centerX() - ball.radius() / 2 <= this.centerX + this.width / 2 &&
        ball.speedY() > 0) {
            ball.reverseSpeedY();
            if(!this.hardBrick) {
                ball.increaseScore();
                kill();
            }
            return;
        }
        // left-side-of-the-brick
        if(ball.centerY() + ball.radius() / 2 >= this.centerY - this.height / 2 &&
        ball.centerY() - ball.radius() / 2 <= this.centerY + this.height / 2 &&
        ball.centerX() + ball.radius() / 2 >= this.centerX - this.width / 2 &&
        ball.centerX() + ball.radius() / 2 < this.centerX &&
        ball.speedX() > 0) {
            ball.reverseSpeedX();
            if(!this.hardBrick) {
                ball.increaseScore();
                kill();
            }
            return;
        }
        // right-side-of-the-brick
        if(ball.centerY() + ball.radius() / 2 >= this.centerY - this.height / 2 &&
        ball.centerY() - ball.radius() / 2 <= this.centerY + this.height / 2 &&
        ball.centerX() - ball.radius() / 2 <= this.centerX + this.width / 2 &&
        ball.centerX() - ball.radius() / 2 > this.centerX &&
        ball.speedX() < 0) {
            ball.reverseSpeedX();
            if(!this.hardBrick) {
                ball.increaseScore();
                kill();
            }
        }
    }

    // get-props
    public int centerX() { return this.centerX; }
    public int centerY() { return this.centerY; }
    public int height() { return this.height; }
    public int width() { return this.width; }
    public boolean status() { return this.doesExist; }
    public boolean isHardBrick() { return this.hardBrick; }
}
