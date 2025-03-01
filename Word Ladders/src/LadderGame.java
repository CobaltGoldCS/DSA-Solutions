import java.io.File;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class LadderGame {

    private ArrayList<ArrayList<String>> sortedWords;
    public LadderGame(String dictionaryFile) {
        readDictionary(dictionaryFile);
    }

    public void play(String start, String end) {
        start = start.toLowerCase();
        end = end.toLowerCase();

        System.out.printf("%s -> %s : ", start, end);

        if (start.length() != end.length()) {
            return;
        }


        ArrayList<String> required = new ArrayList<>();
        required.add(start);
        required.add(end);
        if (!sortedWords.get(start.length() - 1).containsAll(required)) {
            return;
        }

        ArrayList<ArrayList<String>> tempList = new ArrayList<>(sortedWords.size());
        for (int i = 0; i < sortedWords.size(); i++) {
            tempList.add(new ArrayList<>());
            for (String word : sortedWords.get(i)) {
                tempList.get(i).add(word);
            }
        }

        Queue<WordInfo> wordInfoQueue = new Queue<>(new WordInfo(start, 0, start));
        while (!wordInfoQueue.isEmpty()) {
            WordInfo currentWord = wordInfoQueue.dequeue();
            String currentString = currentWord.getWord();

            for (String word : oneAway(currentString, true)) {

                WordInfo newInfo = new WordInfo(
                        word,
                        currentWord.getMoves() + 1,
                        currentWord.getHistory() + " " + word);

                if (word.equalsIgnoreCase(end)) {
                    System.out.printf("%d Moves [%s] total enqueues %d\n",
                            newInfo.getMoves(),
                            newInfo.getHistory(),
                            wordInfoQueue.totalEnqueues);
                    // We use withRemoval = true as an optimization, which will persist past this call,
                    // so we need to reset sortedWords afterward so subsequent calls will hold fresh dictionaries
                    Collections.copy(sortedWords, tempList);
                    return;
                }
                wordInfoQueue.enqueue(newInfo);
            }
        }
        System.out.println("No ladder was found");

        // We use withRemoval = true as an optimization, which will persist past this call,
        // so we need to reset sortedWords afterward so subsequent calls will hold fresh dictionaries

        // note: assigning the list to another variable only creates a shallow copy, which doesn't help
        Collections.copy(sortedWords, tempList);
    }

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

    public void listWords(int length, int howMany) {
        ArrayList<String> wordsOfProperLength = sortedWords.get(length - 1);
        for (int i = 0; i < howMany; i++) {
            System.out.println(wordsOfProperLength.get(i));
        }
    }

    /*
        Reads a list of words from a file, putting all words of the same length into the same array.
     */
    private void readDictionary(String dictionaryFile) {
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