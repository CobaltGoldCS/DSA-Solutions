import java.util.ArrayList;
import java.util.Collections;

public class LadderGameExhaustive extends LadderGame {

    public LadderGameExhaustive(String dictionaryFile) {
        super(dictionaryFile);
    }

    @Override
    public void play(String start, String end) {
        start = start.toLowerCase();
        end = end.toLowerCase();

        System.out.printf("Seeking exhaustive solution from %s -> %s : ", start, end);

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
                    System.out.printf("[%s] total enqueues %d\n",
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
}
