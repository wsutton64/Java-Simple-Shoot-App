import java.awt.*;

public class Target {
    private int size = 25;
    private int xPos, yPos, xVel, yVel;
    public Boolean isActive;

    public Target() {
        // Initialize Target object with random properties
        xPos = (int) (Math.random() * 1800) + 50;
        yPos = (int) (Math.random() * 900) + 50;
        xVel = (int) (Math.random() * 11) - 5;
        yVel = (int) (Math.random() * 11) - 5;
        isActive = true;
    }

    public void update() {
        setXPos(xPos += xVel);
        setYPos(yPos += yVel);
        if((getXPos() < 0) || (getXPos() > 1900 - size)) {
            setXVel(xVel *= -1);
        }
        if((getYPos() < 0) || (getYPos() > 1000 - size)) {
            setYVel(yVel *= -1);
        }
    }

    public void draw(Graphics g) {
        if(isActive) {
            g.setColor(Color.BLACK);
            g.fillRect(xPos, yPos, size, size);
        }
    }

    public boolean contains(int x, int y) {
        return (x >= getXPos() && x <= getXPos() - getSize() && y >= getYPos() && y <= getYPos() - getSize());
    }

    public void destroy() {
        this.isActive = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(getXPos(), getYPos(), getSize(), getSize());
    }

    // Setters and Getters
    public int getXPos() {
        return this.xPos;
    }
    public int getYPos() {
        return this.yPos;
    }
    public int getSize() {
        return this.size;
    }
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }
    public void setXVel(int xVel) {
        this.xVel = xVel;
    }
    public void setYVel(int yVel) {
        this.yVel = yVel;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    
}
