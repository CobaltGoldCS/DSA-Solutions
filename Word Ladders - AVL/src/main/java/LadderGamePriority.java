import java.util.ArrayList;

public class LadderGamePriority extends LadderGame{

    /**
     * A helper class for comparing words, and storing their best word ladder lengths
     */
    private static class WordComparator implements Comparable<WordComparator> {
        private final String word;
        private int length;

        /**
         * @param word The word to store and compare against
         * @param length The length of the best word ladder for the word
         **/
        public WordComparator(String word, int length) {
            this.word = word;
            this.length = length;
        }

        @Override
        public int compareTo(WordComparator o) {
            return word.compareTo(o.word);
        }

    }
    public LadderGamePriority(String dictionary) {
        super(dictionary);
    }

    @Override
    public void play(String start, String end) {
        start = start.toLowerCase();
        end = end.toLowerCase();

        System.out.printf("Seeking A* solution from %s -> %s : ", start, end);

        if (start.length() != end.length()) {
            return;
        }


        ArrayList<String> required = new ArrayList<>();
        required.add(start);
        required.add(end);
        if (!sortedWords.get(start.length() - 1).containsAll(required)) {
            return;
        }


        AVLTree<WordComparator> previousWords = new AVLTree<>();
        
        int totalEnqueues = 1;

        AVLTree<WordInfoPriority> wordSteps = new AVLTree<>();

        wordSteps.insert(new WordInfoPriority(start, 0, calculateRemainingWork(start, end)));
        previousWords.insert(new WordComparator(start, 0));

        while (!wordSteps.isEmpty()) {
            WordInfoPriority priorityWord = wordSteps.deleteMin();


            for (String oneAway : oneAway(priorityWord.getWord(), false)) {
                WordInfoPriority newInfo = new WordInfoPriority(
                        oneAway.toLowerCase(),
                        priorityWord.getMoves() + 1,
                        calculateRemainingWork(oneAway, end),
                        priorityWord.getHistory() +  " " + oneAway
                );

                if (oneAway.equals(end)) {
                    System.out.printf("[%s] total enqueues %d\n",
                            newInfo.getHistory(),
                            totalEnqueues);
                    return;
                }

                WordComparator oneAwayComparator = new WordComparator(oneAway, newInfo.getMoves());
                if (!previousWords.contains(oneAwayComparator)) {
                    wordSteps.insert(newInfo);
                    previousWords.insert(oneAwayComparator);
                    totalEnqueues++;
                }

                if (previousWords.find(oneAwayComparator).length > newInfo.getMoves()) {
                    WordComparator treeComparator = previousWords.find(oneAwayComparator);
                    treeComparator.length = newInfo.getMoves();
                    wordSteps.insert(newInfo);
                    totalEnqueues++;
                }

            }

        }
    }

    /** Find the hamming distance between two words
    *
     * @param current the word to compare
     * @param target The goal word to compare current against
     * @return The total difference in letters between the two words, or
     * Integer.MAX_VALUE
    **/
    private int calculateRemainingWork(String current, String target) {
        if (current.length() != target.length()) {
            return Integer.MAX_VALUE;
        }
        int work = 0;
        for (int i = 0; i < current.length(); i++) {
            if (current.charAt(i) != target.charAt(i)) {
                work++;
            }
        }
        return work;
    }
}
