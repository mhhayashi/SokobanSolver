import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BFSThread implements Runnable {
    private final Board board;
    ConcurrentLinkedQueue<Move> queue;
    ConcurrentHashMap<Move, Boolean> visited;
    Move win;
    boolean won;

    public BFSThread(Board board, ConcurrentLinkedQueue<Move> queue, ConcurrentHashMap<Move, Boolean> visited) {
        this.board = board;
        this.queue = queue;
        this.visited = visited;
    }

    // Valid cell for player to be in?
    // May return true in out of bounds cases.
    private boolean playerBFSCell(Board.Pos p, Move m) {
//        return this.timer1(p, m) && this.timer2(p, m);
        if (p.y < 0 || p.y >= board.walls.length || p.x < 0 || p.x >= board.walls[p.y].length) {
            return false;
        }
        if (board.walls[p.y][p.x]) {
            return false;
        }
        return !m.boxes.contains(p);
    }

    // search up down left right
    private void playerBFSQueue(Board.Pos p, Move m, HashSet<Board.Pos> visited, Queue<Board.Pos> todo) {
        Board.Pos up = new Board.Pos(p.x, (short) (p.y - 1));
        Board.Pos down = new Board.Pos(p.x, (short) (p.y + 1));
        Board.Pos left = new Board.Pos((short) (p.x - 1), p.y);
        Board.Pos right = new Board.Pos((short) (p.x + 1), p.y);
        Board.Pos[] dirs = new Board.Pos[]{up, down, left, right};

        for (Board.Pos dir : dirs) {
            if (this.playerBFSCell(dir, m) && visited.add(dir)) {
//                visited.add(dir);
                todo.add(dir);
            }
        }
    }

    // Hashset of possible player positions
    public HashSet<Board.Pos> playerBFS(Move m) {
        HashSet<Board.Pos> visited = new HashSet<>();
        Queue<Board.Pos> todo = new LinkedList<>();
        this.playerBFSQueue(m.player, m, visited, todo);
        while (!todo.isEmpty()) {
            this.playerBFSQueue(todo.poll(), m, visited, todo);
        }
        return visited;
    }

    private boolean wins(Move m) {
        for (Board.Pos box : m.boxes) {
            if (!this.board.targets[box.y][box.x]) {
                return false;
            }
        }
        return true;
    }

    // returns true on win
    private boolean bfsQueue(Board.Pos p, Move m, ConcurrentHashMap<Move,Boolean> visited, Queue<Move> todo) {
        LinkedList<Move> moves = new LinkedList<>();
        Board.Pos up = new Board.Pos(p.x, (short) (p.y - 1));
        Board.Pos down = new Board.Pos(p.x, (short) (p.y + 1));
        Board.Pos left = new Board.Pos((short) (p.x - 1), p.y);
        Board.Pos right = new Board.Pos((short) (p.x + 1), p.y);
        Board.Pos up2 = new Board.Pos(p.x, (short) (p.y - 2));
        Board.Pos down2 = new Board.Pos(p.x, (short) (p.y + 2));
        Board.Pos left2 = new Board.Pos((short) (p.x - 2), p.y);
        Board.Pos right2 = new Board.Pos((short) (p.x + 2), p.y);
        HashSet<Board.Pos> playerSquares = this.playerBFS(m);

        if (this.wins(m) && playerSquares.contains(this.board.player)) {
            return true;
        }

        if (playerSquares.contains(up) && playerSquares.contains(up2)) {
            Move next = new Move(m, p, up, up2);
            if (!visited.containsKey(next)) {
                moves.add(next);
            }
        }
        if (playerSquares.contains(down) && playerSquares.contains(down2)) {
            Move next = new Move(m, p, down, down2);
            if (!visited.containsKey(next)) {
                moves.add(next);
            }
        }
        if (playerSquares.contains(left) && playerSquares.contains(left2)) {
            Move next = new Move(m, p, left, left2);
            if (!visited.containsKey(next)) {
                moves.add(next);
            }
        }
        if (playerSquares.contains(right) && playerSquares.contains(right2)) {
            Move next = new Move(m, p, right, right2);
            if (!visited.containsKey(next)) {
                moves.add(next);
            }
        }
        if (moves.size() > 0) {
            moves.getLast().setLast(true);
        }
        for (Move mm : moves) {
            visited.put(mm, Boolean.TRUE);
        }
        todo.addAll(moves);
        return false;
    }

    @Override
    public void run() {
        int tries = 0;
        while (tries < 2) {
            Move m = queue.poll();
            if (m == null) {
                tries ++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            for (Board.Pos box : m.boxes) {
                if (this.bfsQueue(box, m, visited, queue)) {
                    queue.clear();
                    this.won = true;
                    this.win = m;
                    return;
                }
            }
            tries = 0;
        }

    }
}
