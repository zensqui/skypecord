import javax.swing.JPanel;

//chessboard that will control all of the logic on the client's end.
// when it comes time to send, i will send it in a fen string that will not only send the board, but also some logic for example,
//whether en passant is possible, whos turn it is, which sides can each play castle.
public class ChessBoard extends JPanel{

    private boolean whiteToMove = true;
    public ChessTile[][] mainBoard = getTilesFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    private ChessTile[][] getTilesFromFen(String s){
        ChessTile[][] tempTiles = new ChessTile[8][8];
        int row = 0;
        int col = 0;
        char[] pieces = {'r', 'n', 'b', 'q', 'k', 'p', 'R', 'N', 'B', 'Q', 'K', 'P'};
        String[] str = s.split(" ");
        for (int i = 0; i < str[0].length(); i++) {
            if(str[0].charAt(i) == '/')
            {
                row += 1;
                col = 0;
            } else if(Character.isDigit(str[0].charAt(i))) {
                for (int j = 0; j < Character.digit(str[0].charAt(i), 10); j++) {
                    tempTiles[row][col] = new ChessTile(' ');
                    col += 1;
                }
            } else {
                tempTiles[row][col] = new ChessTile(str[0].charAt(i));
                col += 1;
            }

            
        }
        return tempTiles;
    }
    public String toFen(){
        return "";
    }
    public String toString(){
        String string = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                string = string + mainBoard[i][j].fen;
            }
            string = string + "\n";
        }
        return string;
    }
}

