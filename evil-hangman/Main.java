package hangman;

import java.io.File;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    private static void printSummary(int numGuesses, Game game) {
        System.out.println("You have " + numGuesses + " guesses left");
        System.out.println("Used letters: " + game.getGuessedLetters().toString());
        System.out.println("Word: " + game.getPattern().toString());
    }

    private static char getValidInput(Scanner in) {
        String input;
        char guess;
        boolean validInput = true;

        do {

            if (!validInput) {
                System.out.println("Invalid input");
            }

            System.out.print("Enter guess: ");
            input = in.nextLine().toLowerCase();
            guess = input.charAt(0);
            validInput = Character.isLetter(guess) && input.length() == 1;

        } while (!validInput);

        return guess;
    }

    private static char makeGuess(Game game, Scanner in) {
        boolean needAnotherGuess;
        char guess = ' ';

        do {
            needAnotherGuess = false;

            try {
                guess = getValidInput(in);
                game.makeGuess(guess);
            }
            catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                System.out.println("You already used that letter");
                needAnotherGuess = true;
            }

        } while (needAnotherGuess);

        return guess;
    }

    private static boolean playGame(int numGuesses, Scanner in, Game game) {
        Pattern curPattern = game.getPattern();
        boolean gameWon = false;

        while (numGuesses > 0 && !gameWon) {
            printSummary(numGuesses, game);
            char guess = makeGuess(game, in);
            gameWon = game.getPattern().wordGuessed();
            Pattern newPattern = game.getPattern();

            if (curPattern.equals(newPattern)) {
                numGuesses--;
                System.out.println("Sorry, there are no " + guess + "'s");
            }
            else {
                System.out.println("Yes, there are " + newPattern.getLetterCount(guess) + " " + guess + "'s");
            }
            System.out.println();

            curPattern = newPattern;
        }

        System.out.println();
        return gameWon;
    }

    private static void printFinal(Game game, boolean wordGuessed) {
        if (wordGuessed) {
            System.out.println("You win!");
            System.out.println("The word is " + game.getPattern().toString());
        }
        else {
            System.out.println("You lose!");
            System.out.println("The word is " + game.getPossibleWords().getRandomWord());
        }
    }

    /**
     *
     * @param args args
     */
    public static void main(String[] args) throws Exception {  //todo: delete this
        Game g = new Game();
        g.startGame(new File("my.txt"), 5);
        int guess = 10;
        Scanner s = new Scanner(System.in);
        TreeSet<String> possible;
        while (guess > 0) {
            char g1 = s.next().charAt(0);
            possible = g.makeGuess(g1);
            System.out.println(possible);
            guess--;
        }

//        if (args.length != 3) {
//            System.out.println("Wrong number of args");
//            return;
//        }
//
//        int wordLength;
//        int numGuesses;
//
//        try {
//            wordLength = Integer.parseInt(args[1]);
//            numGuesses = Integer.parseInt(args[2]);
//        }
//        catch (NumberFormatException e) {
//            System.out.println("Word length and number of guesses arguments must be integers");
//            return;
//        }
//
//        if (wordLength < 2) {
//            System.out.println("Word length must be at least 2");
//            return;
//        }
//        if (numGuesses < 1) {
//            System.out.println("Number of guesses must be at least 1");
//            return;
//        }
//
//        File dictionary = new File(args[0]);
//
//        if (dictionary.length() == 0) {
//            System.out.println("File is empty");
//            System.exit(1);
//        }
//
//        Scanner in = new Scanner(System.in);
//
//        Game game = new Game();
//        game.startGame(dictionary, wordLength);
//        boolean gameWon = playGame(numGuesses, in, game);
//        printFinal(game, gameWon);
    }

}



