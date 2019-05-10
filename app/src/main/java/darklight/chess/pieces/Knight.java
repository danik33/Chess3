package darklight.chess.pieces;

import java.util.ArrayList;

import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Side;

public class Knight extends ChessPiece{

	@Override
	public int getValue() {
		return 3;
	}

	public Knight(Side color)
	{
		super(color);
		this.pieceType = Piece.KNIGHT;
	}

	@Override
	public ArrayList<Move> possibleMoves() 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(new Move(-1, 2));
		moves.add(new Move(1, 2));
		moves.add(new Move(-1, -2));
		moves.add(new Move(1, -2));
		moves.add(new Move(2, 1));
		moves.add(new Move(2, -1));
		moves.add(new Move(-2, 1));
		moves.add(new Move(-2, -1));
		return moves;
	}
	
	

}
