import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Solver {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -Xmx1G prog boardName #threads");
            return;
        }
        Board b = new Board(args[0]);
        b.print();
        BFS x = new BFS(b);
        if (args.length < 2) {
            System.out.println(x.backtrace(x.bfs()));
        }
        else {
            try {
                Move win = x.bfsMulti(Integer.parseInt(args[1]));
                System.out.println(x.backtrace(win));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
