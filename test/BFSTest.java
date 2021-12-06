import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BFSTest {
    @Test
    public void testBoardLoading() throws Exception {
        Board b = new Board("../screens/bing.1");
        for (int i = 0; i < b.board.length; i++) {
            for (int j = 0; j < b.board[i].length; j++) {
                System.out.print(b.board[i][j]);
            }
            System.out.println();
        }

        for (int i = 0; i < b.board.length; i++) {
            for (int j = 0; j < b.board[i].length; j++) {
                if (b.walls[i][j]) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        for (int i = 0; i < b.board.length; i++) {
            for (int j = 0; j < b.board[i].length; j++) {
                if (b.targets[i][j]) {
                    System.out.print(".");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        // Verify board and walls print correctly
    }

    @Test
    public void testPlayerBFS() throws Exception {
        Board b = new Board("../screens/bing.1");
        Move m = new Move(new Board.Pos[]{new Board.Pos(3,1), new Board.Pos(3,2)},
                new Board.Pos(1, 1), null);
        BFS x = new BFS(b);
        HashSet<Board.Pos> vis = x.playerBFS(m);
        for (Board.Pos p : vis) {
            System.out.println(p);
        }
        assertEquals(vis.size(), 4);

        // see output
    }

    @Test
    public void testPlayerBFSSpeed() throws Exception {
        Board b = new Board("../screens/screen.1");
        Move m = new Move(new Board.Pos[]{new Board.Pos(1, 3), new Board.Pos(2, 3)},
                new Board.Pos(1, 1), null);
        BFS x = new BFS(b);
        for (int i = 0; i < 100000; i++) {
            x.playerBFS(m);
        }

        // <2 sec
    }

    @Test
    public void testBFS() throws Exception {
        Board b = new Board("../screens/bing.1");
        Move m = new Move(new Board.Pos[]{new Board.Pos(4,1), new Board.Pos(4,2)},
                new Board.Pos(1, 1), null);
        BFS x = new BFS(b);
        Move win = x.bfs(m);
        for (Board.Pos p : win.boxes) {
            System.out.println(p);
        }

        // see output
    }

    @Test
    public void testBFS1() throws Exception {
        Board b = new Board("../screens/bing.1");
        BFS x = new BFS(b);
        Move win = x.bfs();
        for (Board.Pos p : win.boxes) {
            System.out.println(p);
        }
        System.out.println(x.backtrace(win));

        // see output
    }
    @Test
    public void testBFS2() throws Exception {
        Board b = new Board("../screens/micro.133");
        BFS x = new BFS(b);
        Move win = x.bfs();
        for (int i = 0; i < b.board.length; i++) {
            for (int j = 0; j < b.board[i].length; j++) {
                System.out.print(b.board[i][j]);
            }
            System.out.println();
        }
        System.out.println(x.backtrace(win));

        for (int i = 0; i < 50; i++) {
            x.bfs();
        }
        // see output
    }
    @Test
    public void bfsBench() throws Exception {
        Board b = new Board("../microban1/screen.150from(Original47)");
        BFS x = new BFS(b);
        Move win = x.bfs();

//        for (int i = 0; i < 5; i++) {
//            x.bfs();
//        }
        // see output
    }

    @Test
    public void bfsBenchMulti() throws Exception {
        Board b = new Board("../microban1/screen.144");
        BFS x = new BFS(b);
        Move win = x.bfsMulti(3);

//        for (int i = 0; i < 5; i++) {
//            x.bfs();
//        }
        // see output
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread.sleep(10000);
        Board b = new Board("../screens/screen.1");
        BFS x = new BFS(b);
//       Move win = x.bfs();
        Move win = x.bfsMulti(3);
    }
}