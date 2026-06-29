import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    static int rangeMin;
    static int rangeMax;
    static int maxAttempts;
    static int maxRounds;

    static int totalScore = 0;
    static int roundsWon = 0;
    static int totalGuesses = 0;

    public static void main(String[] args) {
        printBanner();
        configure();
        for (int round = 1; round <= maxRounds; round++) {
            playRound(round);
            if (round < maxRounds) {
                System.out.println("\nPress ENTER for next round...");
                scanner.nextLine();
            }
        }
        printFinalSummary();
        scanner.close();
    }

    static void printBanner() {
        System.out.println("+======================================+");
        System.out.println("|      NUMBER  GUESSING  GAME          |");
        System.out.println("+======================================+");
        System.out.println();
    }

    static void configure() {
        System.out.println("--- Game Setup ---");
        rangeMin    = readInt("Range minimum        : ", 0, Integer.MAX_VALUE - 1);
        rangeMax    = readInt("Range maximum        : ", rangeMin + 1, Integer.MAX_VALUE);
        maxAttempts = readInt("Max attempts / round : ", 1, 50);
        maxRounds   = readInt("Number of rounds     : ", 1, 20);
        System.out.println();
    }

    static void playRound(int round) {
        int secret = rangeMin + random.nextInt(rangeMax - rangeMin + 1);
        int attemptsUsed = 0;
        boolean won = false;

        System.out.println();
        System.out.println("+-------------------------------------+");
        System.out.printf ("|  ROUND %d / %-27d|%n", round, maxRounds);
        System.out.println("+-------------------------------------+");
        System.out.printf ("Guess a number between %d and %d.%n", rangeMin, rangeMax);
        System.out.printf ("You have %d attempt(s).%n%n", maxAttempts);

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.printf("[Attempt %d/%d | %d left] Your guess: ",
                              attempt, maxAttempts, (maxAttempts - attempt + 1));

            int guess = readGuess();
            attemptsUsed++;
            totalGuesses++;

            if (guess == secret) {
                won = true;
                System.out.println("\nCorrect! The number was " + secret + ".");
                break;
            } else if (guess > secret) {
                System.out.println("Too HIGH! Try a lower number.");
            } else {
                System.out.println("Too LOW! Try a higher number.");
            }

            printBar(attempt, maxAttempts);

            if (attempt == maxAttempts - 1) {
                System.out.println("WARNING: Last attempt!");
            }
        }

        if (won) {
            int points = (maxAttempts - attemptsUsed + 1) * 10;
            totalScore += points;
            roundsWon++;
            System.out.printf("Points earned: +%d  (total: %d)%n", points, totalScore);
        } else {
            System.out.println("\nOut of attempts! The number was " + secret + ".");
            System.out.println("No points this round.");
        }

        printRoundStats(round, attemptsUsed, won);
    }

    static void printBar(int used, int total) {
        System.out.print("Progress: [");
        for (int i = 1; i <= total; i++) {
            System.out.print(i <= used ? "X" : "-");
        }
        System.out.println("]");
    }

    static void printRoundStats(int round, int attemptsUsed, boolean won) {
        System.out.println();
        System.out.println("--- Round " + round + " Summary ---");
        System.out.println("Attempts used : " + attemptsUsed + " / " + maxAttempts);
        System.out.println("Result        : " + (won ? "WIN" : "LOSS"));
        System.out.println("Running score : " + totalScore);
        System.out.println("Rounds won    : " + roundsWon + " / " + round);
    }

    static void printFinalSummary() {
        System.out.println();
        System.out.println("+======================================+");
        System.out.println("|          FINAL  SUMMARY              |");
        System.out.println("+======================================+");
        System.out.printf ("|  Rounds played : %-19d|%n", maxRounds);
        System.out.printf ("|  Rounds won    : %-19d|%n", roundsWon);
        System.out.printf ("|  Total guesses : %-19d|%n", totalGuesses);
        System.out.printf ("|  Final score   : %-19d|%n", totalScore);
        System.out.println("+--------------------------------------+");

        double pct = (double) roundsWon / maxRounds * 100;
        String grade;
        if      (pct == 100) grade = "Perfect! Flawless victory!";
        else if (pct >= 66)  grade = "Great job!";
        else if (pct >= 33)  grade = "Good effort!";
        else                 grade = "Better luck next time!";

        System.out.printf ("|  %-36s|%n", grade);
        System.out.println("+======================================+");
    }

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= min && v <= max) return v;
                System.out.println("Please enter a value between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }
    }

    static int readGuess() {
        while (true) {
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= rangeMin && v <= rangeMax) return v;
                System.out.printf("Out of range! Enter between %d and %d: ", rangeMin, rangeMax);
            } catch (NumberFormatException e) {
                System.out.print("Not a number. Try again: ");
            }
        }
    }
}
