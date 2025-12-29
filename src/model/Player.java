package model;

import exception.GameActionException;
import engine.Game;

public abstract class Player extends Entity {
    protected String name;
    protected int score = 0;
    protected int ap;

    public Player(String name, Point pos) {
        super(pos);
        this.name = name;
    }

    public abstract void makeAction(String input, Game game) throws GameActionException;
    public abstract void resetAP();

    public int getAP() { return ap; }
    public void consumeAP() { this.ap--; }
    public int getScore() { return score; }
    public void addGoal() { this.score++; }
    public String getName() { return name; }
}