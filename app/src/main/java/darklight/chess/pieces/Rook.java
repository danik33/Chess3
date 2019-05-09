package darklight.chess.pieces;


import java.util.ArrayList;

import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Side;

public class Rook extends ChessPiece
{

	public Rook(Side color) 
	{
		super(color);
		this.pieceType = Piece.ROOK;
	}



	public Rook(Rook rook) 
	{
		super(rook);
		this.pieceType = Piece.ROOK;
	}



	@Override
	public ArrayList<Move> possibleMoves() 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(new Move(1, 0, true));
		moves.add(new Move(-1, 0, true));
		moves.add(new Move(0, 1, true));
		moves.add(new Move(0, -1, true));
		
		return moves;
	}
	


}
