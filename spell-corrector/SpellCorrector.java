package spell;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * 1 data types
 *      trie dictionary
 *  1 constructor
 *
 *  8 functions
 *      useDictionary
 *      suggestSimilarWord
 *      getBestWord
 *      fillSet
 *      alteration
 *      transposition
 *      insertion
 *      deletion
 */
public class SpellCorrector implements ISpellCorrector {

    private Trie dictionary;

    /**
     * initialize trie
     */
    public SpellCorrector() {
        dictionary = new Trie();
    }

    /**
     * create scanner
     * try:
     *      Create file reader
     *      scanner = new Scanner(reader);
     *
     *      while (scanner has next)
     *          add next to dictionary
     * catch: file not found
     *      print out
     *
     * @param dictionaryFileName File containing the words to be used
     */
    public void useDictionary(String dictionaryFileName) {

        try {
            FileReader r = new FileReader(dictionaryFileName);
            Scanner in = new Scanner(r);

            while (in.hasNext()) {
                dictionary.add( in.next() );
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("file not found");
        }

    }

    /**
     *  set Similar word to null
     *  best count to 0
     *  loop thru set of possible words
     *      call trie.find on possible word
     *      if resulting node is not null (it was found) & that node has a better count
     *          set similar word to that word
     *          set best count to that count
     *
     *  return similar word
     *
     * @param set i'm searching
     * @return best word
     */
    private String getBestWord(TreeSet<String> set) {
        String similarWord = null;
        int bestCount = 0;

        for (String possible : set) {
            Node n = dictionary.find(possible);

            if (n != null && n.getValue() > bestCount) {
                similarWord = possible;
                bestCount = n.getValue();
            }
        }

        return similarWord;
    }

    /**
     * set inputWord to lowercase
     * call find on input word; if not null
     *      it was found, return input word
     *
     * create primary treeSett
     * call fillSet on word and primary
     * set string similarword to getBestWord on primary
     * if it's not null, return that
     *
     * create secondary treeset
     * loop through first set
     *      call fillSet on that word and secondary
     *
     *  return getBestWord on secondary
     *
     *
     */
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();

        if (dictionary.find(inputWord) != null) {
            return inputWord;
        }

        TreeSet<String> primary = new TreeSet<>();
        fillSet(inputWord, primary);
        String similarWord = getBestWord(primary);

        if (similarWord != null) {
            return similarWord;
        }

        TreeSet<String> secondary = new TreeSet<>();

        for (String primaryWord : primary) {
            fillSet(primaryWord, secondary);
        }

        return getBestWord(secondary);
    }

    /**
     *  deletionWords
     *  insertionWords
     *  transpositionWords
     *  alterationWords
     *
     * @param word i'm editing
     * @param set i'm adding to
     */
    private void fillSet(String word, TreeSet<String> set) {
        deletionWords(word, set);
        insertionWords(word, set);
        transpositionWords(word, set);
        alterationWords(word, set);
    }

    /**
     * loop thru word
     *      create a builder based on input word
     *      delete the char at that index
     *      add that new word to set
     *
     * @param inputWord i'm editing
     * @param set i'm adding to
     */
    private void deletionWords(String inputWord, TreeSet<String> set) {

        for (int index = 0; index < inputWord.length(); index++) {
            StringBuilder builder = new StringBuilder(inputWord);
            builder.deleteCharAt(index);
            String newWord = builder.toString();
            set.add(newWord);
        }

    }

    /**
     * loop thru insertion, including the index after final (index i)
     *      loop through possible letters (index j)
     *          create a builder based on inputWord
     *          create letter to add based on index j and offset
     *          add letter to builder at index i
     *          add that to set
     *
     * @param inputWord i'm editing
     * @param set i'm adding to
     */
    private void insertionWords(String inputWord, TreeSet<String> set) {

        for (int i = 0; i <= inputWord.length(); i++) {

            for (int j = 0; j < Node.NUM_LETTERS; j++) {
                StringBuilder builder = new StringBuilder(inputWord);
                char letter = (char)(Trie.OFFSET + j);
                builder.insert(i, letter);
                String newWord = builder.toString();
                set.add(newWord);
            }

        }

    }

    /**
     * loop thru word to < inputword length - 1
     *      create builder on input word
     *      char first = word letter at i
     *      char second = word letter at i + 1
     *      set builder char at i to be second
     *      and builder char at i + 1 to be first
     *      add to set
     * @param inputWord i'm editing
     * @param set i'm adding to
     */
    private void transpositionWords(String inputWord, TreeSet<String> set) {

        for (int i = 0; i < inputWord.length() - 1; i++) {
            StringBuilder builder = new StringBuilder(inputWord);
            char first = inputWord.charAt(i);
            char second = inputWord.charAt(i + 1);
            builder.setCharAt(i, second);
            builder.setCharAt(i + 1, first);
            String newWord = builder.toString();
            set.add(newWord);
        }

    }

    /**
     *
     * loop through word normally
     *      loop through letters
     *           char newLetter = OFFSET + j
     *           char oldLetter = word letter at i
     *           if they're not the same
     *              create new builder on word
     *              set char at i to be new letter
     *              add to set
     *
     * @param inputWord i'm editing
     * @param set i'm adding to
     */
    private void alterationWords(String inputWord, TreeSet<String> set) {

        for (int i = 0; i < inputWord.length(); i++) {

            for (int j = 0; j < Node.NUM_LETTERS; j++) {
                char newLetter = (char)(Trie.OFFSET + j);
                char oldLetter = inputWord.charAt(i);

                if (oldLetter != newLetter) {
                    StringBuilder builder = new StringBuilder(inputWord);
                    builder.setCharAt(i, newLetter);
                    String newWord = builder.toString();
                    set.add(newWord);
                }

            }

        }

    }

}
