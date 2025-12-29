package model;

public class Ball extends Entity {
    public Ball(Point pos) { super(pos); }
    @Override public char getSymbol() { return 'O'; }
}