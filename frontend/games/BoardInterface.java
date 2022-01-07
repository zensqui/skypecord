public interface BoardInterface{

    public boolean whiteToMove();
    public boolean isCheck();
    public boolean isCheckMate();

    public ChessTile enPassant();

    public void Capture(ChessTile p, ChessTile c);

    public String toFen();
    public ChessTile[][] getTilesFromFen();
}