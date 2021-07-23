import java.awt.*;

public class Brick {
    // brick-props
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int centerX;
    private final int centerY;

    // Constructor
    Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.centerX = this.x + this.width / 2;
        this.centerY = this.y + this.height / 2;
    }

    // render
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    // get-props
    public int centerX() { return this.centerX; }
    public int centerY() { return this.centerY; }
    public int height() { return this.height; }
    public int width() { return this.width; }
}
