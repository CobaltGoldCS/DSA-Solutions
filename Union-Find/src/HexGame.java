import javax.swing.text.Position;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class HexGame {
    public final int GRID_SIZE;
    private final int TOP_EDGE;
    private final int BOTTOM_EDGE;
    private final int LEFT_EDGE;
    private final int RIGHT_EDGE;

    private final Color[] board;

    private final DisjointSet paths;
    public HexGame(int grid_size) {
        GRID_SIZE = grid_size;
        board = new Color[grid_size * grid_size];
        Arrays.fill(board, Color.NONE);

        TOP_EDGE = GRID_SIZE * GRID_SIZE + 1;
        BOTTOM_EDGE = GRID_SIZE * GRID_SIZE + 2;
        LEFT_EDGE = GRID_SIZE * GRID_SIZE + 3;
        RIGHT_EDGE = GRID_SIZE * GRID_SIZE + 4;

        paths = new DisjointSet(GRID_SIZE * GRID_SIZE + 4);
    }

    public boolean playBlue(int position, boolean displayNeighbors) {
        if (isOccupied(position)) {
            return false;
        }

        board[position - 1] = Color.BLUE;


        ArrayList<Integer> neighbors = getNeighbors(position);
        if (displayNeighbors) {
            System.out.printf("Cell %d: %s\n", position, neighbors);
        }

        for (int neighbor : neighbors) {
            if (getColorAt(neighbor) == Color.BLUE) {
                paths.union(position, neighbor);
            }
        }

        return paths.find(LEFT_EDGE) == paths.find(RIGHT_EDGE);
    }

    public boolean playRed(int position, boolean displayNeighbors) {
        if (isOccupied(position)) {
            return false;
        }

        board[position - 1] = Color.RED;

        ArrayList<Integer> neighbors = getNeighbors(position);
        if (displayNeighbors) {
            System.out.printf("Cell %d: %s\n", position, neighbors);
        }

        for (int neighbor : neighbors) {
            if (getColorAt(neighbor) == Color.RED) {
                paths.union(position, neighbor);
            }
        }

        return paths.find(TOP_EDGE) == paths.find(BOTTOM_EDGE);
    }

    public Color[] getGrid() {
        return board;
    }

    private boolean isOccupied(int location) {
        return location > GRID_SIZE * GRID_SIZE || board[location - 1] != Color.NONE;
    }

    private Color getColorAt(int position) {
        if (position <= GRID_SIZE * GRID_SIZE) {
            return board[position - 1];
        }

        if (position == TOP_EDGE || position == BOTTOM_EDGE) {
            return Color.RED;
        }
        if (position == LEFT_EDGE || position == RIGHT_EDGE) {
            return Color.BLUE;
        }
        throw new InvalidParameterException("position should be within board");
    }

    private ArrayList<Integer> getNeighbors(int position) {
        ArrayList<Integer> returnList = new ArrayList<>();

        int boardPosition = position - 1;

        int col = (boardPosition % GRID_SIZE) + 1;
        int row = Math.floorDiv(boardPosition, GRID_SIZE) + 1;

        // Left neighbor
        if (col > 1) {
            returnList.add(position - 1);
        }


        // Right Neighbor
        if (col < GRID_SIZE) {
            returnList.add(position + 1);
        }

        // top Left neighbor
        if (row > 1) {
            returnList.add(position - GRID_SIZE);
        }

        // top right neighbor
        if (row > 1 && col < GRID_SIZE) {
            returnList.add(position - GRID_SIZE + 1);
        }

        // Bottom Right Neighbor
        if (row < GRID_SIZE) {
            returnList.add(position + GRID_SIZE);
        }

        // Bottom Left Neighbor
        if (row < GRID_SIZE && col > 1) {
            returnList.add(position + GRID_SIZE - 1);
        }

        if (col == 1) {
            returnList.add(LEFT_EDGE);
        }

        if (col == GRID_SIZE) {
            returnList.add(RIGHT_EDGE);
        }

        if (row == 1) {
            returnList.add(TOP_EDGE);
        }

        if (row == GRID_SIZE) {
            returnList.add(BOTTOM_EDGE);
        }

        return returnList;
    }

    public enum Color {
        NONE,
        RED,
        BLUE,
    }
}