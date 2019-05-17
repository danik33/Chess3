package darklight.chess.board;


import java.io.Serializable;


import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Point;
import darklight.chess.Side;
import darklight.chess.SourceMove;
import darklight.chess.pieces.Bishop;
import darklight.chess.pieces.ChessPiece;
import darklight.chess.pieces.King;
import darklight.chess.pieces.Knight;
import darklight.chess.pieces.Pawn;
import darklight.chess.pieces.Queen;
import darklight.chess.pieces.Rook;

public class Board implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean rotated;
	public boolean real;
	private int moveAmount;

	public SourceMove lastMove;
	public Tile[][] board;
	public Point bKingLocation, wKingLocation;


	public Board()
	{
		rotated = false;
		board = new Tile[8][8];
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				board[i][j] = new Tile();
			}
		}
		real = true;
		moveAmount = 0;
	}
	
	public Board(Board b)
	{
		board = new Tile[8][8];
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				board[i][j] = new Tile(b.getBoard()[i][j]);
				
				if(board[i][j].getOccupied() != 0 && board[i][j].getType() == Piece.KING)
				{
					if(board[i][j].getSide() == Side.WHITE)
					{
						wKingLocation = new Point(i, j);
					}
					else
					{
						bKingLocation = new Point(i, j);
					}
				}
			}
		}
		rotated = b.rotated;
		real = b.real;
		lastMove = b.lastMove;
		moveAmount = b.moveAmount;
	}
	
	
	public void setPiece(ChessPiece piece, int x, int y)
	{
		if(inBoard(x,y))
		{
			board[x][y].setChessPiece(piece);
		}
	}
	
	public static boolean inBoard(int x, int y){return !(x < 0 || x > 7 || y < 0 || y > 7);	}
	
	public static boolean inBoard(Point p)
	{
		if(p == null)
			return false;
		
		return !(p.x < 0 || p.x > 7 || p.y < 0 || p.y > 7);		
	}
	
	public Tile[][] getBoard() { return this.board;}

	public int occupied(int x, int y) 
	{
		if(!inBoard(x, y))
			return -1;
		return board[x][y].getOccupied(); 
	}

	public int occupied(Point a) 
	{
		
		if(a == null || !inBoard(a))
			return -1;
		
		return board[a.x][a.y].getOccupied();
	}

	public SourceMove getLastMove() { return lastMove; }


	public void move(Point p1, Move m)
	{
		Tile destTile = board[p1.x + m.getX()][p1.y + m.getY()];
		Tile initTile = board[p1.x][p1.y];
		lastMove = new SourceMove(p1.x, p1.y, m);
		if(initTile.getChessPiece().getType() == Piece.PAWN) 
		{
			int oneFw = (initTile.getSide() == Side.BLACK) ? 1 : -1;
			int enemy = (initTile.getOccupied() == 1) ? 2 : 1;
			
			if(p1.x > 0 && m.equals(new Move(-1, oneFw)) && board[p1.x-1][p1.y].getOccupied() == enemy && board[p1.x-1][p1.y+oneFw].getOccupied() == 0)
			{
				board[p1.x-1][p1.y].clear();
			}
			if(p1.x < 7 && m.equals(new Move(1, oneFw)) && board[p1.x+1][p1.y].getOccupied() == enemy && board[p1.x+1][p1.y+oneFw].getOccupied() == 0)
			{
				board[p1.x+1][p1.y].clear();
			}
		}
		destTile.setChessPiece(initTile.getChessPiece());
		
		if((initTile.getChessPiece() instanceof King)) 
		{
			int unit = 1;
			if(rotated)
				unit = -unit;
			if(m.equals(new Move(unit*2, 0)))						//Right castling 
				move(new Point(7, p1.y), new Move(-2*unit, 0));
			else if(m.equals(new Move(-2*unit, 0))) 					//Left one
				move(new Point(0, p1.y), new Move(3*unit, 0));
			if(initTile.getSide() == Side.WHITE)
			{
				wKingLocation = new Point(p1.x + m.getX(), p1.y + m.getY());
			}
			else
			{
				bKingLocation = new Point(p1.x + m.getX(), p1.y + m.getY());
			}
		}
		destTile.getChessPiece().setMoved();
		initTile.clear();
		if(destTile.getChessPiece().getType() == Piece.PAWN && (p1.y+m.getY() == 7 || p1.y+m.getY() == 0))  //TODO A CHOICE HERE
		{
			int ans;
			ans = 4;
			
			ChessPiece p;
			Side s = destTile.getSide();
			switch(ans)
			{
				case 0:
					p = new Queen(s);
					break;
				case 1:
					p = new Knight(s);
					break;
				case 2:
					p = new Bishop(s);
					break;
				case 3:
					p = new Rook(s);
					break;
				default:
					p = new Queen(s);
			}
			destTile.setChessPiece(p);
		}
		
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(board[i][j].getOccupied() != 0 && board[i][j].getType() == Piece.PAWN)
				{
					((Pawn)board[i][j].getChessPiece()).clearDbMove();
				}
			}
		}
		if(destTile.getChessPiece().getType() == Piece.PAWN && (m.equals(new Move(0, 2)) || m.equals(new Move(0, -2))))
		{
			((Pawn)destTile.getChessPiece()).dbMove();
		}

		++moveAmount;

		
	}
	
	public Board simMove(Point p1, Move m)
	{
		Board copy = new Board(this);
		if((copy.board[p1.x][p1.y].getChessPiece() instanceof King)) //Right castling i guess
		{
			if(m.equals(new Move(2, 0)))
				copy.move(new Point(7, p1.y), new Move(-2, 0));
			else if(m.equals(new Move(-2, 0)))
				copy.move(new Point(0, p1.y), new Move(3, 0));
		}
		copy.board[p1.x + m.getX()][p1.y + m.getY()].setChessPiece(copy.board[p1.x][p1.y].getChessPiece());
		if(!copy.board[p1.x + m.getX()][p1.y + m.getY()].getChessPiece().hasMoved() && copy.board[p1.x + m.getX()][p1.y + m.getY()].getChessPiece() instanceof Pawn)
		{
			Pawn b = (Pawn) copy.board[p1.x + m.getX()][p1.y + m.getY()].getChessPiece();
			b.dbMove();
		}
		copy.board[p1.x + m.getX()][p1.y + m.getY()].getChessPiece().setMoved();
		copy.board[p1.x][p1.y].clear();
		return copy;
	}
	
	
	

	
	
	

	public void initPieces() 
	{
		Side s = Side.BLACK;
		board[0][0].setChessPiece(new Rook(s));
		board[1][0].setChessPiece(new Knight(s));
		board[2][0].setChessPiece(new Bishop(s));
		board[3][0].setChessPiece(new Queen(s));
		board[4][0].setChessPiece(new King(s));
		board[5][0].setChessPiece(new Bishop(s));
		board[6][0].setChessPiece(new Knight(s));
		board[7][0].setChessPiece(new Rook(s));
		for(int i = 0; i < 8; i++)
		{
			board[i][1].setChessPiece(new Pawn(s));
		}
		
		s = Side.WHITE;
		
		board[0][7].setChessPiece(new Rook(s));
		board[1][7].setChessPiece(new Knight(s));
		board[2][7].setChessPiece(new Bishop(s));
		board[3][7].setChessPiece(new Queen(s));
		board[4][7].setChessPiece(new King(s));
		board[5][7].setChessPiece(new Bishop(s));
		board[6][7].setChessPiece(new Knight(s));
		board[7][7].setChessPiece(new Rook(s));
		for(int i = 0; i < 8; i++)
		{
			board[i][6].setChessPiece(new Pawn(s));
		}
		
		wKingLocation = new Point(4, 7);
		bKingLocation = new Point(4, 0);
		
		
	}
	
	public void rotate(boolean whiteTurn)
	{
		if((whiteTurn && rotated) || (!whiteTurn && !rotated))
		{
			Board nb = new Board();
			for(int i = 0; i < 8; i++)
			{
				for(int j = 0; j < 8; j++)
				{
					nb.getBoard()[i][j] = new Tile(getBoard()[i][j]);
				}
			}
			
			for(int i = 0; i < 8; i++)
			{
				for(int j = 0; j < 8; j++)
				{
					board[i][j] = new Tile(nb.getBoard()[7-i][7-j]);
				}
			}
			rotated = !rotated;
		
		}
		
	}


	public int getMoveAmount()
	{
		return this.moveAmount;
	}
}
