import java.util.*;

public class Move {
    HashSet<Board.Pos> boxes;
    Board.Pos player;
    Move prev;
    boolean last;
    List<Board.Pos> visitedPlayers;

    public Move(Board.Pos[] boxes, Board.Pos player, Move prev) {
        this.boxes = new LinkedHashSet<>(List.of(boxes));
        this.player = player;
        this.prev = prev;

    }

    public Move(HashSet<Board.Pos> boxes, Board.Pos player, Move prev) {
        this.boxes = boxes;
        this.player = player;
        this.prev = prev;

    }
    public Move(Move prev, Board.Pos toMove, Board.Pos dest, Board.Pos player) {
        this.boxes = new LinkedHashSet<>(prev.boxes);
        this.boxes.remove(toMove);
        this.boxes.add(dest);
        this.player = player;
        this.prev = prev;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    // WARNING: Loose equality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move move)) return false;
        return Objects.equals(boxes, move.boxes) && Objects.equals(player, move.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxes, player);
    }
}
