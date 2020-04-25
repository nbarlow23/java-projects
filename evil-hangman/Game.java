package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 3 data types: guessedLetters, possibleWords, Pattern
 * 1 constructor: empty
 * 2 functions
 *      startGame
 *      makeGuess
 *
 */
public class Game implements IEvilHangmanGame {

        private GuessedLetters guessedLetters;
    private PossibleWords possibleWords;
    private Pattern pattern;

    /**
     * empty
     */
    public Game() {

    }

    /**
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     */
    public void startGame(File dictionary, int wordLength) {
        pattern = new Pattern(wordLength);
        guessedLetters = new GuessedLetters();
        possibleWords = new PossibleWords();

        try (Scanner in = new Scanner(dictionary)){
            while (in.hasNext()) {
                String word = in.next().toLowerCase();

                if (word.length() == wordLength) {
                    possibleWords.addWord(word);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     *
     * @param guess The character being guessed
     *
     * @return set of Possible words
     * @throws GuessAlreadyMadeException
     */
    public TreeSet<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        if (guessedLetters.hasLetter(guess)) {
            throw new GuessAlreadyMadeException();
        }

        guessedLetters.addLetter(guess);
        TreeMap<Pattern, TreeSet<String>> wordGroups = possibleWords.partitionWords(guess, pattern);
        pattern = possibleWords.chooseBestWordGroup(guess, wordGroups);

        return possibleWords.getWords();
    }


    public GuessedLetters getGuessedLetters() {
        return guessedLetters;
    }

    public PossibleWords getPossibleWords() {
        return possibleWords;
    }

    public Pattern getPattern() {
        return pattern;
    }

}
