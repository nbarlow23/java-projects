package hangman;

import java.util.TreeSet;

/**
 * 1 data type
 * 1 constructor
 *      initialize
 * 3 functions
 *      addLetter
 *      hasLetter
 *      toString
 */
public class GuessedLetters {

    private TreeSet<Character> letters;

    /**
     * initialize data
     */
    public GuessedLetters() {
        letters = new TreeSet<>();
    }

    /**
     * add letter to data
     * @param c letter to add
     */
    protected void addLetter(char c) {
        letters.add(c);
    }

    /**
     * build new string
     * loop thru set of letters,
     * if it's not the first element, add a space
     * add the letter to string
     *
     * @return string of set of letters
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (char c : letters) {

            if (!first) {
                builder.append(" ");
            }

            builder.append(c);
            first = false;
        }

        return builder.toString();
    }

    /**
     *
     * @param c letter to check
     * @return if my set has c
     */
    protected boolean hasLetter(char c) {
        return letters.contains(c);
    }
}
