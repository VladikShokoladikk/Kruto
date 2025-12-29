package engine;

import model.*;
import exception.GameActionException;
import java.util.*;

public class Game {
    private final int width, height;
    private final List<Entity> entities = new ArrayList<>();
    private final FireShrimp shrimp;
    private final Shark shark;
    private final Ball ball;
    private final Hoop leftHoop, rightHoop;
    private Player ballOwner = null;
    private final Random rng = new Random();

    public Game(int w, int h) {
        this.width = w % 2 == 0 ? w + 1 : w;
        this.height = h;
        int midY = h / 2;

        shrimp = new FireShrimp(new Point(1, midY));
        shark = new Shark(new Point(width - 2, midY));
        ball = new Ball(new Point(width / 2, midY));
        leftHoop = new Hoop(new Point(0, midY));
        rightHoop = new Hoop(new Point(width - 1, midY));

        entities.addAll(List.of(shrimp, shark, ball, leftHoop, rightHoop));
    }

    public void render() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("--- BARKETBALL OCEAN ---");
        System.out.println("Score: Shrimp [" + shrimp.getScore() + "] | Shark [" + shark.getScore() + "]");
        System.out.println("Ball owner: " + (ballOwner == null ? "none" : ballOwner.getName()));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point p = new Point(x, y);
                char sym = '~';
                if (shrimp.getPos().equals(p)) sym = shrimp.getSymbol();
                else if (shark.getPos().equals(p)) sym = shark.getSymbol();
                else if (ball.getPos().equals(p)) sym = ball.getSymbol();
                else if (leftHoop.getPos().equals(p) || rightHoop.getPos().equals(p)) sym = 'R';
                System.out.print(sym + " ");
            }
            System.out.println();
        }
    }

    public void processMove(Player p, String dir) throws GameActionException {
        int dx = 0, dy = 0;
        switch (dir.toLowerCase()) {
            case "w" -> dy = -1;
            case "s" -> dy = 1;
            case "a" -> dx = -1;
            case "d" -> dx = 1;
            default -> throw new GameActionException("Invalid WASD direction");
        }
        executePhysicsMove(p, new Point(p.getPos().x() + dx, p.getPos().y() + dy));
    }

    public void executePhysicsMove(Player p, Point newPos) {
        if (newPos.x() < 0 || newPos.x() >= width || newPos.y() < 0 || newPos.y() >= height) return;

        Player enemy = (p == shrimp) ? shark : shrimp;
        if (newPos.equals(enemy.getPos())) {
            if (ballOwner == enemy) {
                ballOwner = p;
                System.out.println("!!! BALL STOLEN !!!");
            }
            return;
        }

        p.setPos(newPos);
        if (p.getPos().equals(ball.getPos()) && ballOwner == null) ballOwner = p;
        if (ballOwner == p) ball.setPos(p.getPos());
    }

    public void processThrow(Player p, char dir) throws GameActionException {
        if (ballOwner != p) throw new GameActionException("No ball in hands!");

        int dx = 0, dy = 0;
        switch (String.valueOf(dir).toLowerCase()) {
            case "w" -> dy = -1;
            case "s" -> dy = 1;
            case "a" -> dx = -1;
            case "d" -> dx = 1;
            default -> throw new GameActionException("Invalid direction! Use tw, ta, ts, td.");
        }

        ballOwner = null;

        int dist = rng.nextInt(4) + 2;

        int targetX = Math.max(0, Math.min(width - 1, p.getPos().x() + dx * dist));
        int targetY = Math.max(0, Math.min(height - 1, p.getPos().y() + dy * dist));
        Point landing = new Point(targetX, targetY);

        ball.setPos(landing);
        boolean goalScored = (p instanceof FireShrimp && landing.equals(rightHoop.getPos())) ||
                (p instanceof Shark && landing.equals(leftHoop.getPos()));

        if (goalScored) {
            p.addGoal();
            render();
            AsciiArtService.printGoal();
            System.out.println("Press Enter to continue...");
            new Scanner(System.in).nextLine();
            resetRound();
            return;
        }

        Player enemy = (p instanceof FireShrimp) ? shark : shrimp;
        if (landing.equals(enemy.getPos())) {
            ballOwner = enemy;
            System.out.println("!!! Intercepted by " + enemy.getName() + " !!!");
        }
    }

    private void resetRound() {
        shrimp.setPos(new Point(1, height/2));
        shark.setPos(new Point(width-2, height/2));
        ball.setPos(new Point(width/2, height/2));
        ballOwner = null;
    }

    public boolean isFinished() { return shrimp.getScore() >= 10 || shark.getScore() >= 10; }
    public Player getWinner() { return shrimp.getScore() >= 10 ? shrimp : shark; }
    public FireShrimp getShrimp() { return shrimp; }
    public Shark getShark() { return shark; }
    public Ball getBall() { return ball; }
    public Player getBallOwner() { return ballOwner; }
    public Point getLeftHoopPos() { return leftHoop.getPos(); }
}