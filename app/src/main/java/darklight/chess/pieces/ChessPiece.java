package darklight.chess.pieces;

import android.graphics.Point;

import java.io.Serializable;
import java.util.ArrayList;

import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Side;
import darklight.chess.board.Board;

public abstract class ChessPiece implements Serializable
{
	protected Piece pieceType;
	protected Side color;
	protected boolean moved;
	protected boolean alive; 
	
	public ChessPiece(Side color)
	{
		this.color = color;
		this.moved = false;
	}
	
	public ChessPiece(ChessPiece piece) 
	{
		this.color = piece.color;
		this.moved = piece.moved;
	}

	public abstract ArrayList<Move> possibleMoves();
	
	

	public static void checkMoves(Point[] points)
	{
		for(int i = 0; i < points.length; i++)
		{
			if(!Board.inBoard(points[i]))
				points[i] = null;
		}
		
	}
	
	public static void checkMoves(ArrayList<Point> points) 
	{
		for(int i = 0; i < points.size(); i++)
		{
			if(!Board.inBoard(points.get(i)))
				points.remove(i);
		}
	}
	
	public boolean isAlive() { return this.alive; }
	public Side getColor(){ return this.color; }
	
	public boolean hasMoved() { return this.moved; }
	public void setMoved() { this.moved = true; }
	
	public Piece getType() { return this.pieceType; }
	
	
	@Override
	public String toString()
	{
		return "" + this.pieceType;
	}
	
	public ChessPiece getCopy()
	{
		if(this instanceof Pawn)
		{
			return new Pawn((Pawn)this);
		}
		else if (this instanceof King)
		{
			return new King((King)this);
		}
		else if ( this instanceof Rook)
		{
			return new Rook((Rook)this);
		}
		return this;
	}

	
	
	

}
