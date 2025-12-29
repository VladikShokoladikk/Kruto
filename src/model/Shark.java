package model;

import engine.Game;
import exception.GameActionException;

public class Shark extends Player {
    public Shark(Point pos) {
        super("Shark", pos);
        resetAP();
    }

    @Override public char getSymbol() { return 'A'; }
    @Override public void resetAP() { this.ap = 1; }

    @Override
    public void makeAction(String input, Game game) throws GameActionException {
        Point b = game.getBall().getPos();
        Point hoop = game.getLeftHoopPos();
        if (game.getBallOwner() != this) {
            moveToTarget(b, game);
            return;
        }
        boolean sameX = (pos.x() == hoop.x());
        boolean sameY = (pos.y() == hoop.y());

        if (sameX || sameY) {
            char dir;
            if (sameX) {
                dir = (hoop.y() > pos.y()) ? 's' : 'w';
            } else {
                dir = (hoop.x() > pos.x()) ? 'd' : 'a';
            }
            game.processThrow(this, dir);
        } else {
            moveToTarget(hoop, game);
        }
    }

    // Вспомогательный метод для движения (чтобы не дублировать код)
    private void moveToTarget(Point target, Game game) {
        int dx = 0, dy = 0;
        // Сначала выравниваемся по вертикали (как ты просил в условии)
        if (pos.y() != target.y()) {
            dy = (target.y() > pos.y()) ? 1 : -1;
        } else if (pos.x() != target.x()) {
            dx = (target.x() > pos.x()) ? 1 : -1;
        }
        game.executePhysicsMove(this, new Point(pos.x() + dx, pos.y() + dy));
    }
}