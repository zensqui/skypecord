import javax.swing.JPanel;

//chessboard that will control all of the logic on the client's end.
// when it comes time to send, i will send it in a fen string that will not only send the board, but also some logic for example,
//whether en passant is possible, whos turn it is, which sides can each play castle.
public class ChessBoard extends JPanel{
    public ChessTile[][] mainBoard = getTilesFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    private ChessTile[][] getTilesFromFen(String s){
        ChessTile[][] tiles = new ChessTile[8][8];
        int row = 0;
        int col = 0;
        char[] pieces = {'r', 'n', 'b', 'q', 'k', 'p', 'R', 'N', 'B', 'Q', 'K', 'P'};
        for (int i = 0; i < s.length(); i++) {
            if(row < 8)
            {
                boolean isPiece = false;
                if(Character.isDigit(s.charAt(i)))
                {
                    int digit = Character.digit(s.charAt(i), 10);
                    for (int j = 0; j < digit; j++) {
                        
                    }
                }
                for (int j = 0; j < pieces.length; j++) {
                    if(s.charAt(i) == pieces[j])
                    {
                        isPiece = true;
                    }
                }
                if(isPiece == true)
                {
                    tiles[row][col] = new ChessTile(s.charAt(i));
                } 
                else 
                {
                    if(s.charAt(i) == ' ' || s.charAt(i) == '/')
                    {
                        row+=1;
                        col = 0;
                    }
                }
                
            }
        }
        return tiles;
    }
    private boolean whiteToMove = true;
    public String toFen(){
        return "";
    }
    public String toString(){
        String string = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                string = string + mainBoard[i][j];
            }
            string = string + "\n";
        }
        return string;
    }
}

