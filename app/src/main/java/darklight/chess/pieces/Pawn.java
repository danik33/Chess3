package darklight.chess.pieces;


import java.util.ArrayList;

import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Side;


public class Pawn extends ChessPiece
{

	boolean justDoubleMoved;
	int offsetfordm;

	@Override
	public int getValue() {
		return 1;
	}

	public Pawn(Side color)
	{
		super(color);
		this.justDoubleMoved = false;
		
		if(this.color == Side.BLACK)
			offsetfordm = 2;
		else
			offsetfordm = -2;
		this.pieceType = Piece.PAWN;
	}

	public Pawn(Pawn m) 
	{
		super(m);
		this.justDoubleMoved = m.justDoubleMoved();
		this.offsetfordm = m.offsetfordm;
		this.pieceType = Piece.PAWN;
	}

	@Override
	public ArrayList<Move> possibleMoves() //Done in Game.process() somewhy
	{
		ArrayList<Move> moves = new ArrayList<Move>(); 
//		int oneFw = (this.color == Side.BLACK) ? 1 : -1;
//		if(this.color == Side.BLACK)
//		{
//			moves.add(new Move(false, 0, oneFw));
//			if(!this.moved)
//			{
//				moves.add(new Move(false, 0, oneFw));
//			}
//		}
//		
		return moves;
	}
	
	public void dbMove() { this.justDoubleMoved = true; }
	public void clearDbMove() { this.justDoubleMoved = false;} 
	public boolean justDoubleMoved() { return this.justDoubleMoved; }

}
