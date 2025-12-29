package app;

import engine.Game;
import engine.AsciiArtService;
import exception.GameActionException;
import model.Player;
import model.FireShrimp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game(15, 7);

        System.out.println("=== FIRE SHRIMP BASKETBALL ===");
        System.out.println("1. Single Player (vs AI Shark)");
        System.out.println("2. Two Players");
        System.out.print("Select mode: ");

        int mode = 1;
        if (scanner.hasNextInt()) {
            mode = scanner.nextInt();
            scanner.nextLine();
        }

        while (!game.isFinished()) {
            game.render();

            // Shrimp Turn
            game.getShrimp().resetAP();
            handleTurn(game.getShrimp(), game, scanner);
            if (game.isFinished()) break;

            // Shark Turn
            game.getShark().resetAP();
            if (mode == 1) {
                try {
                    game.getShark().makeAction(null, game);
                } catch (GameActionException e) {
                    // Если ИИ совершил ошибку (например, бросил без мяча), просто пишем в консоль
                    System.out.println("Shark AI error: " + e.getMessage());
                }
            }
        }

        if (game.getWinner() instanceof FireShrimp) {
            AsciiArtService.printCool();
        } else {
            AsciiArtService.printNotCool();
        }
    }

    private static void handleTurn(Player p, Game game, Scanner sc) {
        while (p.getAP() > 0) {
            System.out.print("[" + p.getName() + " AP:" + p.getAP() + "] Input: ");
            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                p.makeAction(input, game);
                p.consumeAP();
            } catch (GameActionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}