public class WordInfoPriority extends WordInfo implements Comparable<WordInfoPriority> {
    private final int priority;
    public WordInfoPriority(String word, int moves, int estimatedWork) {
        super(word, moves);
        priority = estimatedWork + moves;
    }

    public WordInfoPriority(String word, int moves, int estimatedWork, String history) {
        super(word, moves, history);
        priority = estimatedWork + moves;
    }

    @Override
    public int compareTo(WordInfoPriority wordInfo) {
        return priority - wordInfo.priority;
    }
}
