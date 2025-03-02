import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public abstract class LadderGame {

    protected ArrayList<ArrayList<String>> sortedWords;
    public LadderGame(String dictionaryFile) {
        readDictionary(dictionaryFile);
    }

    /**
     * Find the best word ladder between start and end
     * @param start The starting word for the word ladder
     * @param end The target word (end word) for the word ladder
     */
    public abstract void play(String start, String end);

    /**
     * Returns a list of all strings in the dictionary that differ from
     * word by one letter
     * @param word The word to get 'one-aways' of
     * @param withRemoval Whether to remove the words returned from the
     *                    dictionary
     * @return A list of words that differ from word by one letter
     */
    public ArrayList<String> oneAway(String word, boolean withRemoval) {
        ArrayList<String> words = new ArrayList<>();

        for (String testWord : sortedWords.get(word.length() - 1)) {
            int wordDiff = diff(testWord, word);
            boolean invalidWord = wordDiff > 1;
            if (invalidWord) {
                continue;
            }

            words.add(testWord);
        }

        if (withRemoval) {
            for (String removeWord : words) {
                sortedWords.get(word.length() - 1).remove(removeWord);
            }
        }

        return words;
    }

    /**
     * Reads a list of words from a file, putting all words of the same length into the same array.
     */
    protected void readDictionary(String dictionaryFile) {
        File file = new File(dictionaryFile);
        ArrayList<String> allWords = new ArrayList<>();

        //
        // Track the longest word, because that tells us how big to make the array.
        int longestWord = 0;
        try (Scanner input = new Scanner(file)) {
            //
            // Start by reading all the words into memory.
            while (input.hasNextLine()) {
                String word = input.nextLine().toLowerCase();
                allWords.add(word);
                longestWord = Math.max(longestWord, word.length());
            }

            sortedWords = new ArrayList<>(longestWord - 1);
            for (int i = 0; i < longestWord; i++) {
                sortedWords.add(new ArrayList<>());
            }

            for (String word : allWords) {
                sortedWords.get(word.length() - 1).add(word);
            }
        }
        catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the dictionary: " + ex);
        }
    }

    /**
     * Gets the difference between two words
     * @param word1 The first word to compare
     * @param word2 The second word to compare
     * @return The integer difference (in letters) between the two
     * words, or Integer.MAX_VALUE if the words differ in length
     */
    private int diff(String word1, String word2) {
        if (word1.length() != word2.length())
            return Integer.MAX_VALUE;
        int diff = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diff++;
            }
        }

        return diff;
    }
}