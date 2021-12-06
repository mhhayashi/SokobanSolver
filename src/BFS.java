import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BFS {
    private final Board board;

    public BFS(Board board) {
        this.board = board;
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
        visited.add(m.player);
        todo.add(m.player);
//        this.playerBFSQueue(m.player, m, visited, todo);
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
    private boolean bfsQueue(Board.Pos p, Move m, HashSet<Move> visited, Queue<Move> todo) {
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
            if (!visited.contains(next)) {
                moves.add(next);
            }
        }
        if (playerSquares.contains(down) && playerSquares.contains(down2)) {
            Move next = new Move(m, p, down, down2);
            if (!visited.contains(next)) {
                moves.add(next);
            }
        }
        if (playerSquares.contains(left) && playerSquares.contains(left2)) {
            Move next = new Move(m, p, left, left2);
            if (!visited.contains(next)) {
                moves.add(next);
            }
        }
        if (playerSquares.contains(right) && playerSquares.contains(right2)) {
            Move next = new Move(m, p, right, right2);
            if (!visited.contains(next)) {
                moves.add(next);
            }
        }
        if (moves.size() > 0) {
            moves.getLast().setLast(true);
        }
        visited.addAll(moves);
        todo.addAll(moves);
        return false;
    }

    // queue the first moves
    // player can start next to any box
    private boolean bfsQueue1(Board.Pos p, Move m, HashSet<Move> visited, Queue<Move> todo) {
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

        if (this.playerBFSCell(up, m) && this.playerBFSCell(up2, m)) {
            Move next = new Move(m, p, up, up2);
                moves.add(next);
        }
        if (this.playerBFSCell(down, m) && this.playerBFSCell(down2, m)) {
            Move next = new Move(m, p, down, down2);
                moves.add(next);
        }
        if (this.playerBFSCell(left, m) && this.playerBFSCell(left2, m)) {
            Move next = new Move(m, p, left, left2);
                moves.add(next);
        }
        if (this.playerBFSCell(right, m) && this.playerBFSCell(right2, m)) {
            Move next = new Move(m, p, right, right2);
                moves.add(next);
        }
        // TODO: doesn't go here, instead check last of all boxes
//        if (moves.size() > 0) {
//            moves.getLast().setLast(true);
//        }
        visited.addAll(moves);
        todo.addAll(moves);
        return false;
    }

    // queue the first moves
    // player can start next to any box
    private boolean bfsQueue1Multi(Board.Pos p, Move m, ConcurrentHashMap<Move, Boolean> visited, Queue<Move> todo) {
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

        if (this.playerBFSCell(up, m) && this.playerBFSCell(up2, m)) {
            Move next = new Move(m, p, up, up2);
                moves.add(next);
        }
        if (this.playerBFSCell(down, m) && this.playerBFSCell(down2,m)) {
            Move next = new Move(m, p, down, down2);
                moves.add(next);
        }
        if (this.playerBFSCell(left, m) && this.playerBFSCell(left2, m)) {
            Move next = new Move(m, p, left, left2);
                moves.add(next);
        }
        if (this.playerBFSCell(right, m) && this.playerBFSCell(right2, m)) {
            Move next = new Move(m, p, right, right2);
                moves.add(next);
        }
        // TODO: doesn't go here, instead check last of all boxes
//        if (moves.size() > 0) {
//            moves.getLast().setLast(true);
//        }
        for (Move mm : moves) {
            visited.put(mm, Boolean.TRUE);
        }
        todo.addAll(moves);
        return false;
    }

    // returns the winning move or null
    public Move bfs(Move start) {
        // Visited moves: exact player pos + exact boxes pos
        HashSet<Move> visited = new HashSet<>();
        // Visited moves: similar player pos + exact boxes pos
        HashSet<Move> visited2 = new HashSet<>();

        Queue<Move> todo = new LinkedList<>();
        int i = 0;

        for (Board.Pos box : start.boxes) {
            if (this.bfsQueue1(box, start, visited, todo)) {
                return start;
            }
        }
        while (!todo.isEmpty()) {
//            if (i % 1000 == 0) {
//                System.out.println(todo.size());
//            }
//            i++;
            Move m = todo.poll();
            for (Board.Pos box : m.boxes) {
                if (this.bfsQueue(box, m, visited, todo)) {
                    return m;
                }
            }

        }

        return null;
    }

    public Move bfs() {
        boolean[][] targets = Board.findTargets(this.board.board);
        HashSet<Board.Pos> t = new HashSet<>();
        for (int i = 0; i < targets.length; i++) {
            for (int j = 0; j < targets[i].length; j++) {
                if (targets[i][j]) {
                    t.add(new Board.Pos(j, i));
                }
            }
        }
        return this.bfs(new Move(t, this.board.player, null));
    }

    public Move bfsMulti(int numThreads) throws InterruptedException {
        boolean[][] targets = Board.findTargets(this.board.board);
        HashSet<Board.Pos> t = new HashSet<>();
        for (int i = 0; i < targets.length; i++) {
            for (int j = 0; j < targets[i].length; j++) {
                if (targets[i][j]) {
                    t.add(new Board.Pos(j, i));
                }
            }
        }

        ConcurrentLinkedQueue<Move> queue = new ConcurrentLinkedQueue<>();
        ConcurrentHashMap<Move, Boolean> visited = new ConcurrentHashMap<>();
        Move m = new Move(t, this.board.player, null);
        for (Board.Pos box : t) {
            if (this.bfsQueue1Multi(box, m, visited, queue)) {
                return m;
            }
        }
        BFSThread[] bfss = new BFSThread[numThreads];
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            bfss[i] = new BFSThread(this.board, queue, visited);
            threads[i] = new Thread(bfss[i]);
            threads[i].start();
        }
        for (Thread td : threads) {
            td.join();
        }
        for (BFSThread td : bfss) {
            if (td.won) {
                return td.win;
            }
        }

       return null;
    }

    // Moves don't store enough information
    public Pair<Character, Board.Pos> betweenMoves(Move m) {
        Move l = m.prev;
        HashSet<Board.Pos> mp = new HashSet<>(m.boxes);
        mp.removeAll(l.boxes);
        Board.Pos change = (Board.Pos) mp.toArray()[0];
        Board.Pos player = m.player;
        if (player.x == change.x + 1) {
            return new Pair<>('l', change);
        }
        if (player.x == change.x - 1) {
            return new Pair<>('r', change);
        }
        if (player.y == change.y + 1) {
            return new Pair<>('u', change);
        } else {
            return new Pair<>('d', change);
        }
    }

    public List<Character> backtraceBFS(Board.Pos from, Board.Pos to, Move m) {
        LinkedList<Board.Pos> todo = new LinkedList<>();
        LinkedList<List<Character>> paths = new LinkedList<>();
        HashSet<Board.Pos> visited = new HashSet<>();

        todo.add(from);
        visited.add(from);
        paths.add(new ArrayList<>());

        while (!todo.isEmpty()) {
            Board.Pos p = todo.poll();
            List<Character> path = paths.poll();
            assert path != null;

            if (p.equals(to)) {
                return path;
            }
            Board.Pos up = new Board.Pos(p.x, (short) (p.y - 1));
            Board.Pos down = new Board.Pos(p.x, (short) (p.y + 1));
            Board.Pos left = new Board.Pos((short) (p.x - 1), p.y);
            Board.Pos right = new Board.Pos((short) (p.x + 1), p.y);
            if (this.playerBFSCell(up, m) && !visited.contains(up)) {
                todo.add(up);
                visited.add(up);
                List<Character> path1 = new ArrayList<>(path);
                path1.add('u');
                paths.add(path1);
            }
            if (this.playerBFSCell(down, m) && !visited.contains(down)) {
                todo.add(down);
                visited.add(down);
                List<Character> path1 = new ArrayList<>(path);
                path1.add('d');
                paths.add(path1);
            }
            if (this.playerBFSCell(left, m) && !visited.contains(left)) {
                todo.add(left);
                visited.add(left);
                List<Character> path1 = new ArrayList<>(path);
                path1.add('l');
                paths.add(path1);
            }
            if (this.playerBFSCell(right, m) && !visited.contains(right)) {
                todo.add(right);
                visited.add(right);
                List<Character> path1 = new ArrayList<>(path);
                path1.add('r');
                paths.add(path1);
            }
        }

        throw new RuntimeException("fix your code");
    }

    public List<Character> backtrace(Move m) {
        List<Character> finalPath = new ArrayList<>();
        Board.Pos player = this.board.player;
        while (m.prev != null) {
            finalPath.addAll(this.backtraceBFS(player, m.player, m));
//            System.out.println(this.backtraceBFS(player, m.player, m));
            Pair<Character, Board.Pos> pr = this.betweenMoves(m);
            finalPath.add(pr.a);
            m = m.prev;
            player = pr.b;
        }
        // TODO ?????
//        finalPath.addAll(this.backtraceBFS(player, m.player, m));
        return finalPath;
    }

}
