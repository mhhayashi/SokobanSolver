import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
    final char[][] board;
    final boolean[][] walls;
    final boolean[][] targets;
    final Board.Pos player;

    public static class Pos {
        public final short x;
        public final short y;
        public Pos(short x, short y) {
            this.x = x;
            this.y = y;
        }

        public Pos(int x, int y) {
            this.x = (short) x;
            this.y = (short) y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pos pos)) return false;
            return x == pos.x && y == pos.y;
        }

        @Override
        public int hashCode() {
            return (x << 16) + y;
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public Board (String path) {
        this.board = readFile(path);
        this.walls = findWalls(this.board);
        this.targets = findGoals(this.board);
        this.player = findPlayer(this.board);
    }

    private static char[][] readFile(String path) {
        char[][] board = new char[0][0];
        try {
            Scanner sc = new Scanner(new File(path));
            List<char[]> b = new ArrayList<>();
//            int len = 0;
            while (sc.hasNextLine()) {
                char[] line = sc.nextLine().toCharArray();
                b.add(line);
//                if (line.length > len){
//                    len = line.length;
//                }
            }
            board = b.toArray(board);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        return board;
    }

    private static boolean[][] findWalls(char[][] board) {
        boolean[][] walls = new boolean[board.length][];
        for (int i = 0; i < board.length; i++) {
            walls[i] = new boolean[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '#') {
                    walls[i][j] = true;
                }
            }
        }
        return walls;
    }

    private static boolean[][] findGoals(char[][] board) {
        boolean[][] targets = new boolean[board.length][];
        for (int i = 0; i < board.length; i++) {
            targets[i] = new boolean[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                if ("$*".contains(String.valueOf(board[i][j]))) {
                    targets[i][j] = true;
                }
            }
        }
        return targets;
    }

    static boolean[][] findTargets(char[][] board) {
        boolean[][] targets = new boolean[board.length][];
        for (int i = 0; i < board.length; i++) {
            targets[i] = new boolean[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                if (".*+".contains(String.valueOf(board[i][j]))) {
                    targets[i][j] = true;
                }
            }
        }
        return targets;
    }

    private static Board.Pos findPlayer(char[][] board) {
        boolean[][] targets = new boolean[board.length][];
        for (int i = 0; i < board.length; i++) {
            targets[i] = new boolean[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                if ("@+".contains(String.valueOf(board[i][j]))) {
                    return new Board.Pos(j, i);
                }
            }
        }
        return null;
    }

    public void print() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                System.out.print(this.board[i][j]);
            }
            System.out.println();
        }
    }

}
