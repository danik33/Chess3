package darklight.chess.pieces;

import java.util.ArrayList;

import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Side;


public class Queen extends ChessPiece{

	@Override
	public int getValue() {
		return 9;
	}

	public Queen(Side color)
	{
		super(color);
		this.pieceType = Piece.QUEEN;
	
		
		
	}

	@Override
	public ArrayList<Move> possibleMoves() 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(new Move(-1, -1, true));
		moves.add(new Move(-1, 0, true));
		moves.add(new Move(-1, 1, true));
		moves.add(new Move(0, -1, true));
		moves.add(new Move(0, 1, true));
		moves.add(new Move(1, -1, true));
		moves.add(new Move(1, 0, true));
		moves.add(new Move(1, 1, true));
		
		return moves;
	}

}
