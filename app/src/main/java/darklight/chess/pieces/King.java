package darklight.chess.pieces;

import android.graphics.Point;

import java.util.ArrayList;

import darklight.chess.Game;
import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Side;
import darklight.chess.board.Condition;
import darklight.chess.board.Tile;

public class King extends ChessPiece 
{

	@Override
	public int getValue() {
		return 100;
	}

	public King(Side color)
	{
		super(color);
		this.pieceType = Piece.KING;
	}

	public King(King king) 
	{
		super(king);
		this.pieceType = Piece.KING;
	}

	@Override
	public ArrayList<Move> possibleMoves() 
	{
		ArrayList<Move> mv = new ArrayList<Move>();
		
		for(int i = -1; i <= 1; i++)           
		{
			for(int j = -1; j <= 1; j++)
			{
				if(j != 0 || i != 0)
					mv.add(new Move(j, i));
			}
		}
		
		if(!this.moved)
		{
			Move rightCastling = new Move(false, 2, 0, new Condition() 
			{
	
				@Override
				public boolean canMove(Game b, int x, int y) 
				{
					try
					{
						if(hasMoved())
							return false;
						Tile[][] brd = b.getBoard().getBoard();
						if(brd[7][y].getOccupied() != 0 && brd[7][y].getSide() == color && brd[7][y].getChessPiece().hasMoved())
							return false;
						if(brd[x+1][y].getOccupied() > 0)
							return false;
						for(int i = 0; i <= 2; i++)
						{
							if((b.isAttacked(new Point(x+i, y), color, b.getBoard())))
							{
								return false;
							}
						}
						
						if(!(brd[x+3][y].getChessPiece() instanceof Rook))
							return false;
						return true;
					} 
					catch(Exception ee)
					{
						ee.printStackTrace();
						return false;
					}
				}
			});
			
			Move leftCastling = new Move(false, -2, 0, new Condition() 
			{
	
				@Override
				public boolean canMove(Game b, int x, int y) 
				{
					try
					{
						if(hasMoved())
							return false;
						Tile[][] brd = b.getBoard().getBoard();
						if(brd[0][y].getChessPiece().hasMoved())
							return false;
						if(brd[x-1][y].getOccupied() > 0 || brd[x-2][y].getOccupied() > 0)
							return false;
						for(int i = 1; i <= 3; i++)
						{
							if((b.isAttacked(new Point(x-i, y), color, b.getBoard())))
							{
								return false;
							}
						}
						if(!(brd[0][y].getChessPiece() instanceof Rook))
							return false;
						return true;
					} 
					catch(Exception ee)
					{
						ee.printStackTrace();
						return false;
					}
				}
			});

			mv.add(rightCastling);
			mv.add(leftCastling);
		}
		return mv;
	}



}
