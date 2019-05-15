package darklight.chess.board;

import java.io.Serializable;

import darklight.chess.Piece;
import darklight.chess.Side;
import darklight.chess.pieces.ChessPiece;

public class Tile implements Serializable
{
	private ChessPiece chessPiece;
	private int occupied;

	private static final long serialVersionUID = 1L;
	
	public Tile()
	{
		chessPiece = null;
		occupied = 0;
	}
	
	public Tile(ChessPiece piece)
	{
		chessPiece = piece;
		occupied = piece.getColor().ordinal()+1;
	}
	
	public Tile(Tile tile) 
	{
		if(tile != null)
		{
//			ChessPiece m = tile.getChessPiece();
			if((this.occupied = tile.occupied) != 0)
			{
				this.chessPiece = tile.getChessPiece().getCopy();
			}
			else
				this.chessPiece = null;
		}
		
	}

	public Piece getType()
	{
		return this.chessPiece.getType();
	}
	
	public Side getSide()
	{
		return this.chessPiece.getColor();
	}
	
	public void clear()
	{
		this.chessPiece = null;
		occupied = 0;
	}
		
	
	public void setChessPiece(ChessPiece piece)
	{
		this.chessPiece = piece;
		occupied = piece.getColor().ordinal()+1;
	}
	
	@Override
	public String toString()
	{
		return "Tile, occupied: " + getOccupied() + " ChessPiece:" + this.chessPiece; 
	}
	
	
	public ChessPiece getChessPiece() { return this.chessPiece;}
	public int getOccupied() { return this.occupied;}

}
