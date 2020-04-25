package hangman;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 1 data type
 * 1 constructor
 *      initialize
 * 4 functions
 *      addWord,
 *      chooseBestWordGroup,
 *      partitionWords,
 *      getRandomWord
 *
 */
public class PossibleWords {

    private TreeSet<String> words;

    /**
     * initialize words
     */
    public PossibleWords() {
        this.words = new TreeSet<>();
    }

    /**
     *
     * @param word to add
     */
    public void addWord(String word) {
        words.add(word);
    }

    /**
     * create word groups
     * loop thru words
     * create new pattern based on cur word & letter
     * combine with current pattern
     * check if that pattern already in map
     *      if not:, put in new set, add word
     *      if so:, add word
     * return groups
     *
     * @param letter to use to build pattern
     * @param currentPattern used to combine each pattern
     * @return wordGroups map of Patterns to sets of words
     */
    public TreeMap<Pattern, TreeSet<String>> partitionWords(char letter, Pattern currentPattern) {
        TreeMap<Pattern, TreeSet<String>> wordGroups = new TreeMap<>();

        for (String word : words) {
            Pattern pattern = new Pattern(word, letter);
            pattern.combine(currentPattern);
            TreeSet<String> group = wordGroups.get(pattern);

            if (group == null) {
                wordGroups.put(pattern, new TreeSet<>());
                wordGroups.get(pattern).add(word);
            }
            else {
                group.add(word);
            }
        }

        return wordGroups;
    }

    /**
     * Initialize best pattern to null
     * create new keySet using copy construct of keySet
     * loop thru key set of patterns
     * if best pattern is null
     *      set to be current pattern
     * else
     *      if best pattern set size < cur pattern set size
     *          best pattern = current
     *      else if ==
     *           if best patter has letter > cur pattern has letter
     *                  best pattern = current
     *           else if ==
     *                  set index to length - 1
     *                  while index >= 0 and best not found
     *                      if best had letter at index & cur doesn't
     *                          best found
     *                      else if best doesn't have letter at index and cur does
     *                          best = cur
     *                          best found
     *                      else
     *                          index - 1
     * set words to group w/ best pattern
     * return best pattern
     *
     *
     * @param letter used to check letter count
     * @param wordGroups map of partitioned groups
     * @return best pattern
     *
     *
     *
     * if size <
     * else if size =
     */
    public Pattern chooseBestWordGroup(char letter, TreeMap<Pattern, TreeSet<String>> wordGroups) {
        Pattern bestPattern = null;

        if (wordGroups != null && wordGroups.size() != 0) {
            TreeSet<Pattern> keySet = new TreeSet<>(wordGroups.keySet());

            for (Pattern curPattern : keySet) {
                if (bestPattern == null) {
                    bestPattern = curPattern;
                }
                else {
                    if (wordGroups.get(bestPattern).size() < wordGroups.get(curPattern).size()) {
                        bestPattern = curPattern;
                    }
                    else if (wordGroups.get(bestPattern).size() == wordGroups.get(curPattern).size()) {

                        if (bestPattern.getLetterCount(letter) > curPattern.getLetterCount(letter)) {
                            bestPattern = curPattern;
                        }
                        else if (bestPattern.getLetterCount(letter) == curPattern.getLetterCount(letter)) {

                            int index = bestPattern.toString().length() - 1;
                            boolean bestPatternFound = false;

                            while (index >= 0 && !bestPatternFound) {

                                if (bestPattern.hasLetterAtIndex(letter, index) && !curPattern.hasLetterAtIndex(letter, index)) {
                                    bestPatternFound = true;
                                }
                                else if (!bestPattern.hasLetterAtIndex(letter, index) && curPattern.hasLetterAtIndex(letter, index)) {
                                    bestPattern = curPattern;
                                    bestPatternFound = true;
                                }
                                else {
                                    index--;
                                }
                            }
                        }

                    }
                }
            }

            this.words = wordGroups.get(bestPattern);
        }

        return bestPattern;
    }

    /**
     * create an iterator over set of words
     * @return iterator.next word
     */
    public String getRandomWord() {
        Iterator<String> it = words.iterator();
        return it.next();
    }

    public TreeSet<String> getWords() {
        return words;
    }
}
