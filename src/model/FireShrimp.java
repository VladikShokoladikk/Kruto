package model;

import exception.GameActionException;
import engine.Game;

public class FireShrimp extends Player {
    public FireShrimp(Point pos) {
        super("Shrimp", pos);
        resetAP();
    }

    @Override public char getSymbol() { return 'G'; }
    @Override public void resetAP() { this.ap = 2; }

    @Override
    public void makeAction(String input, Game game) throws GameActionException {
        if (input.startsWith("t")) {
            char dir = input.length() > 1 ? input.charAt(1) : ' ';
            game.processThrow(this, dir);
        } else {
            game.processMove(this, input);
        }
    }
}