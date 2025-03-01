import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WritePoetry {
    public String writePoem(String file, String startWord, int length, boolean printHashtable) {
        StringBuilder builder = new StringBuilder(startWord);

        HashTable<String, WordFreqInfo> poemTable;
        try {
            poemTable = buildHashTable(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String currentWord = startWord;
        Random rand = new Random();
        for(int i = 0; i < length - 1; i++) {
            WordFreqInfo wordFreqInfo = poemTable.find(currentWord);

            currentWord = wordFreqInfo.getFollowWord(rand.nextInt(wordFreqInfo.getOccurCount()));

            boolean isPunctuation = !Character.isLetter(lastCharacter(currentWord));

            if (Character.isLetter(lastCharacter(builder.toString())) && !isPunctuation) {
                builder.append(' ');
            }
            builder.append(currentWord);

            if (isPunctuation) {
                builder.append('\n');
            }
        }

        if (Character.isLetter(lastCharacter(builder.toString()))) {
            builder.append('.');
        }

        if (printHashtable) {
            System.out.println(poemTable.toString(poemTable.size()));
        }

        return builder.toString();
    }

    private HashTable<String, WordFreqInfo> buildHashTable(String file) throws FileNotFoundException {
        File data = new File(file);
        Scanner fileScanner = new Scanner(data);

        HashTable<String, WordFreqInfo> poemTable = new HashTable<>();

        ArrayList<String> wordFIFO = new ArrayList<>();

        // Create the FIFO
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();

            if (line.isEmpty()) {
                continue;
            }
            wordFIFO.addAll(List.of(line.toLowerCase().split(" ")));
        }


        for (int i = 0; i < wordFIFO.size(); i++) {
            String currentWord = wordFIFO.get(i);
            if (currentWord.isEmpty()) {
                continue;
            }
            if (Character.isLetter(currentWord.charAt(currentWord.length() - 1))) {
                // If there is no punctuation
                insertStandardWord(currentWord, poemTable, i, wordFIFO);
            }

            else {
                // Dealing with punctuation
                insertPunctuationWord(currentWord, poemTable, i, wordFIFO);
            }
        }

        return poemTable;
    }


    /**
     * Inserts a word of the form [word][punctuation], such as am. or leave!
     * properly into the poemTable
     * **/
    private void insertPunctuationWord(String currentWord, HashTable<String, WordFreqInfo> poemTable, int i, ArrayList<String> wordFIFO) {
        String cleanWord = removePunctuation(currentWord);
        if (!poemTable.contains(cleanWord)) {
            poemTable.insert(cleanWord, new WordFreqInfo(cleanWord, 0));
        }
        WordFreqInfo frequency = poemTable.find(cleanWord);

        String punctuation = currentWord.substring(currentWord.length() - 1);
        frequency.updateFollows(punctuation);

        if (i < wordFIFO.size() - 1) {
            // Add punctuation into the poemTable with word frequency, as long as it is
            // not the last word in the FIFO
            if (!poemTable.contains(punctuation)) {
                poemTable.insert(punctuation, new WordFreqInfo(punctuation, 0));
            }
            poemTable.find(punctuation).updateFollows(wordFIFO.get(i + 1));
        }
    }

    /**
     * Inserts a word properly into the poemTable
     * **/
    private void insertStandardWord(String currentWord, HashTable<String, WordFreqInfo> poemTable, int i, ArrayList<String> wordFIFO) {
        String nextWord = wordFIFO.get(i + 1);
        if (!poemTable.contains(currentWord)) {
            poemTable.insert(currentWord, new WordFreqInfo(currentWord, 0));
        }

        WordFreqInfo frequency = poemTable.find(currentWord);

        frequency.updateFollows(removePunctuation(nextWord));
    }

    private String removePunctuation(String word) {
        if (!Character.isAlphabetic(word.charAt(word.length() - 1))) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    private char lastCharacter(String word) {
        return word.charAt(word.length() - 1);
    }
}
