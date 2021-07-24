import java.awt.*;

public class Paddle {
    // paddle-props
    private int x;
    private int centerX;
    private final int y;
    private final int centerY;
    private final int width;
    private final int height;

    private char dir = 's';
    private static final int PADDLE_VELOCITY = 7;

    // constructor
    Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.centerX = this.x + this.width / 2;
        this.centerY = this.y + this.height / 2;
    }

    // movement
    public void move(int RIGHT_LIMIT) {
        if(dir == 'l' && this.centerX - this.width / 2 > 0) {
            this.x -= PADDLE_VELOCITY;
            this.centerX = this.x + this.width / 2;
        } else if(dir == 'r' && this.centerX + this.width / 2 < RIGHT_LIMIT) {
            this.x += PADDLE_VELOCITY;
            this.centerX = this.x + this.width / 2;
        }
    }
    public void changeDirection(char d) { this.dir = d; }
    public char getDirection() { return this.dir; }

    // render
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    // AI
    private int modulo(int x) {
        if(x < 0) x = -x;
        return x;
    }
    public void ai(Ball ball) {
        if(modulo(ball.centerY() - this.centerY) < 300 && ball.speedY() > 0) {
            if(!(ball.centerX() + ball.radius() / 2 >= this.centerX - this.width / 4 &&
            ball.centerX() - ball.radius() / 2 <= this.centerX + this.width / 4)) {
                if(ball.centerX() > this.centerX) changeDirection('r');
                else if(ball.centerX() < this.centerX) changeDirection('l');
            } else changeDirection('s');
        }
    }

    // get-props
    public int centerX() { return this.centerX; }
    public int centerY() { return this.centerY; }
    public int height() { return this.height; }
    public int width() { return this.width; }
}
