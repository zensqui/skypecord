package deprecated.games;

public class ChessTile {
    public int val;
    public char fen;
    public boolean hasPiece = true;

    public ChessTile(char type) {
        fen = type;
        if (fen == ' ') {
            hasPiece = false;
        }
    }

    public String toString() {
        String s = "";
        return s + fen;
    }
}