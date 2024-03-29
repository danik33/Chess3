package darklight.chess.pieces;

import java.util.ArrayList;

import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Side;

public class Bishop extends ChessPiece
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getValue()
	{
		return 3;
	}

	public Bishop(Side color)
	{
		super(color);
		this.pieceType = Piece.BISHOP;
	}

	@Override
	public ArrayList<Move> possibleMoves() 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(new Move(1, 1, true));
		moves.add(new Move(-1,-1, true));
		moves.add(new Move(1, -1, true));
		moves.add(new Move(-1, 1, true));
		return moves;
	}

}
