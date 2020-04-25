package hangman;

/**
 * implements Comparable
 * 1 data type
 * 2 constructors
 *      one initialize to ---- on length
 *      one initialize to --c--c on word and letter
 *
 * 7 functions
 *      hasLetterAtIndex
 *      getLetterCount
 *      toString
 *      wordGuessed
 *      combine
 *      equals
 *      compareTo
 *
 */
public class Pattern implements Comparable<Pattern> {

    private String pattern;

    /**
     * build pattern with all '-'
     * set this pattern
     *
     * @param numLetters length of pattern
     */
    public Pattern(int numLetters) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < numLetters; i++) {
            builder.append("-");
        }

        pattern = builder.toString();
    }

    /**
     * build a new string
     * if the word at any index matches the letter, set index to letter
     * otherwise, set to '-'
     * set this pattern to builder
     * @param word i'm using to construct
     * @param letter i'm using to construct
     */
    public Pattern(String word, char letter) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {

            if (word.charAt(i) == letter) {
                builder.append(letter);
            }
            else {
                builder.append("-");
            }

        }

        pattern = builder.toString();
    }

    /**
     *
     * @param letter i'm checking
     * @param index i'm checking
     * @return if the letter at that index == letter
     */
    public boolean hasLetterAtIndex(char letter, int index) {

        if (index >= 0 && index < pattern.length()) {
            return pattern.charAt(index) == letter;
        }

        return false;
    }

    /**
     *
     * @param letter I'm checking
     * @return count of times letter appears in word
     */
    public int getLetterCount(char letter) {
        int count = 0;

        for (int i = 0; i < pattern.length(); i++) {

            if (pattern.charAt(i) == letter) {
                count++;
            }

        }

        return count;
    }

    /**
     * create new builder of this pattern
     * loop thru, if this has a '-', override with p at index
     * set pattern to builder
     *
     * @param
     */
    public void combine(Pattern p) {
        StringBuilder builder = new StringBuilder(this.pattern);

        for (int i = 0; i < this.pattern.length(); i++) {
            if (this.pattern.charAt(i) == '-') {
                builder.setCharAt(i, p.pattern.charAt(i));
            }
        }

        this.pattern = builder.toString();
    }

    /**
     * loop thru pattern, if any char is '-', false, else true
     *
     * @return true | false
     */
    public boolean wordGuessed() {
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '-') {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param o object to compare
     * @return if pattern equals o's pattern
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        Pattern p = (Pattern)o;
        return p.pattern.equals(this.pattern);
    }

    /**
     *
     * @return string pattern
     */
    @Override
    public String toString() {
        return pattern;
    }

    /**
     *
     * @param p
     * @return compareToInt
     */
    @Override
    public int compareTo(Pattern p) {
        return this.pattern.compareTo(p.pattern);
    }



}
