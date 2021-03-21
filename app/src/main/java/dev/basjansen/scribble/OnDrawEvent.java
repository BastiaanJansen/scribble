package dev.basjansen.scribble;

public class OnDrawEvent {
    private float x;
    private float y;
    private int color;

    public OnDrawEvent() { }

    public OnDrawEvent(float x, float y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
