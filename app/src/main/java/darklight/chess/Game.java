package darklight.chess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import darklight.chess.board.Board;
import darklight.chess.board.Tile;
import darklight.chess.pieces.ChessPiece;
import darklight.chess.pieces.King;
import darklight.chess.pieces.Pawn;

public class Game implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Player p1, p2;
	public Board board;


	public boolean whiteTurn;
	public long gameLength, startGame;
	public int movesCount;
	public boolean autoResume;



	Stack<Board> boardSnaps;

	private Point selected;

	private ArrayList<Point> shownMoves;


	public Game(AI p1, AI p2)
	{
		autoResume = false;
		boardSnaps = new Stack<>();
		board = new Board();
		this.p1 = new Player(Side.WHITE, p1);
		this.p2 = new Player(Side.BLACK, p2);
		board.initPieces();
		shownMoves = new ArrayList<>();
		whiteTurn = true;
		movesCount = 0;
		gameLength = 0;


	}


	public Game(Game game, Board copy)
	{
		this.board = copy;
		p1 = new Player(game.p1);
		p2 = new Player(game.p2);
		boardSnaps = new Stack<Board>();
		whiteTurn = game.whiteTurn;
	}

	public Board getBoard() { return this.board; }


	public ArrayList<SourceMove> process(ArrayList<Move> moves, int x, int y, Board b)
	{
		if(board.rotated)
		{
			for(int i = 0; i < moves.size(); i++)
			{
				Move nm = new Move(moves.get(i).getX(), -moves.get(i).getY());
				moves.remove(moves.get(i));
				moves.add(nm);
			}
		}
		ArrayList<SourceMove> avMoves = new ArrayList<SourceMove>();
		int enemy = (b.occupied(x, y) == 1) ? 2 : 1;
		ChessPiece piece = b.getBoard()[x][y].getChessPiece();
		Tile[][] br = b.getBoard();


		if(piece.getType() == Piece.PAWN)                //TODO if on last tile action, meanwhile does a queen
		{
			int oneFw = (enemy == 1) ? 1 : -1;
			if(board.rotated)
				oneFw = -oneFw;
			if(br[x][y+oneFw].getOccupied() == 0 )
			{
				avMoves.add(new SourceMove(x, y, 0, oneFw));
				if(!piece.hasMoved() && !(y == 1 || y == 6) && br[x][y+oneFw*2].getOccupied() == 0)
				{
					avMoves.add(new SourceMove(x, y, 0, oneFw*2));
				}
			}
			if(x > 0 && br[x-1][y+oneFw].getOccupied() == enemy)
			{
				avMoves.add(new SourceMove(x, y, -1, oneFw));
			}
			if(x < 7 &&br[x+1][y+oneFw].getOccupied() == enemy)
			{
				avMoves.add(new SourceMove(x, y, 1, oneFw));
			}

			return avMoves;
		}





		for(Move m : moves)
		{
			SourceMove sm = new SourceMove(x, y, m);
			if(!m.isRepeatable())
			{
				if(b.occupied(sm.getDestination()) == 0 || (m.doesEats() && (b.occupied(sm.getDestination()) == enemy)))
				{
					if(!(board.getBoard()[x][y].getType() == Piece.KING && (m.getX() > 1 || m.getX() < 1)))
					{
						if(m.canMove(this, x, y))
						{
							avMoves.add(sm);
						}
					}
				}

			}
			else
			{
				Point a = new Point(x + m.getX(), y + m.getY());
				while(b.occupied(a) == 0)
				{
					avMoves.add(new SourceMove(x, y, new Move(a.x-x, a.y-y)));
					a = new Point(a.x + m.getX(), a.y + m.getY());
				}
				if(m.doesEats() && b.occupied(a) == enemy)
				{
					avMoves.add(new SourceMove(x, y, new Move(a.x-x, a.y-y)));
				}
			}
		}
		return avMoves;
	}


	public ArrayList<Point> process(ArrayList<Move> moves, int x, int y)
	{

		if(board.rotated)
		{
			for(Move m : moves)
			{
				m.invertY();
			}
		}
		ArrayList<Point> avMoves = new ArrayList<Point>();
		int enemy = (board.occupied(x, y) == 1) ? 2 : 1;
		ChessPiece piece = board.getBoard()[x][y].getChessPiece();
		Tile[][] br = board.getBoard();


		if(piece instanceof Pawn)                //TODO if on last tile action, meanwhile crashes the shownmoves
		{
			int oneFw = (enemy == 1) ? 1 : -1;
			if(board.rotated)
				oneFw = -oneFw;
			if(br[x][y+oneFw].getOccupied() == 0 )
			{
				addTo(new Move(0, oneFw), x, y, avMoves);
				if(!piece.hasMoved() &&!(y < 1 || y > 6) && br[x][y+oneFw*2].getOccupied() == 0)
				{
					addTo(new Move(0, oneFw*2), x, y, avMoves);
				}
			}
			if(x > 0 && br[x-1][y+oneFw].getOccupied() == enemy)
			{
				addTo(new Move(-1, oneFw), x, y, avMoves);
			}
			if(x < 7 && br[x+1][y+oneFw].getOccupied() == enemy)
			{
				addTo(new Move(1, oneFw), x, y, avMoves);
			}
			if(x > 0 && (br[x-1][y].getOccupied() == enemy && br[x-1][y].getType() == Piece.PAWN && ((Pawn)(br[x-1][y].getChessPiece())).justDoubleMoved()))
			{
				addTo(new Move(-1, oneFw), x, y, avMoves);
			}
			if(x < 7 && br[x+1][y].getOccupied() == enemy && br[x+1][y].getType() == Piece.PAWN && ((Pawn)(br[x+1][y].getChessPiece())).justDoubleMoved())
			{
				addTo(new Move(1, oneFw), x, y, avMoves);
			}
			return avMoves;
		}

		if(piece.getType() == Piece.KING)
		{
			for(Move m : moves)
			{
				Point pn = new Point(x + m.getX(), y + m.getY());
				if(board.occupied(pn) == enemy || board.occupied(pn) == 0)
				{
					if(m.canMove(this, x, y))
						addTo(pn, x, y, avMoves);
				}
			}
			return avMoves;
		}




		for(Move m : moves)
		{
			if(!m.isRepeatable())
			{
				Point a = new Point(x + m.getX(), y + m.getY());
				if(board.occupied(a) == 0 || (m.doesEats() && board.occupied(a) == enemy))
				{

					if(m.canMove(this, x, y))
						addTo(m, x, y, avMoves);
				}

			}
			else
			{
				Point a = new Point(x + m.getX(), y + m.getY());
				while(board.occupied(a) == 0)
				{
					addTo(a, x, y, avMoves);
					a = new Point(a.x + m.getX(), a.y + m.getY());
				}
				if(m.doesEats() && board.occupied(a) == enemy)
				{
					addTo(a, x, y, avMoves);
				}
			}
		}
		return avMoves;
	}

	public Game simMove(Point p1, Move m)
	{
		Board copy = new Board(board);
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
		return new Game(this, copy);
	}

	public ArrayList<SourceMove> getMoves(Side s)
	{
		ArrayList<SourceMove> arr = new ArrayList<SourceMove>();
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				Tile tl = board.getBoard()[i][j];
				if(tl.getOccupied() > 0 && tl.getSide() == s)
				{
					for(Point pn : process(tl.getChessPiece().possibleMoves(),i, j))
					{
						arr.add(new SourceMove(i, j, pn.x-i, pn.y-j));
					}
				}
			}
		}
		return arr;
	}

	private boolean addTo(Point a2, int x, int y, ArrayList<Point> avMoves)
	{
		Move mv = new Move(a2.x-x, a2.y-y);
		return addTo(mv, x, y, avMoves);

	}

	private boolean addTo(Move mv, int x, int y, ArrayList<Point> avMoves)
	{
		Board sim = board.simMove(new Point(x,y), mv);
		sim.real = false;
		Point king = null;
		Side s = board.getBoard()[x][y].getSide();
		if(board.getBoard()[x][y].getType() == Piece.KING)
		{
			king = new Point(x + mv.getX(), y + mv.getY());
		}
		else
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(sim.getBoard()[i][j].getOccupied() != 0 && sim.getBoard()[i][j].getType() == Piece.KING)
				{

					if(s == Side.WHITE && sim.getBoard()[i][j].getSide() == Side.WHITE)
					{
						king = sim.wKingLocation;
					}
					else if(s == Side.BLACK && sim.getBoard()[i][j].getSide() == Side.BLACK)
					{
						king = sim.bKingLocation;
					}
				}
			}
		}



		if(!isAttacked(king, s, sim))
		{
			if(!avMoves.contains(new Point(x + mv.getX(), y + mv.getY())))
				avMoves.add(new Point(x + mv.getX(), y + mv.getY()));
			return true;
		}
		return false;

	}

	public boolean processPress(int xz, int yz)
	{
		boolean flag = false;

		if(Board.inBoard(xz, yz))
		{
			int selc; //Index of selected tile if pressed on one of them
			if(getSelected() != null && (selc = getShownMoves().indexOf(new Point(xz, yz))) > -1)
			{
				Point b = getShownMoves().get(selc);
				boardSnaps.push(new Board(board));
				board.move(new Point(getSelected().x, getSelected().y), new Move(b.x - getSelected().x, b.y - getSelected().y));

				getShownMoves().clear();
				setSelected(null);
				whiteTurn = !whiteTurn;
				flag = true;
			}
			else if(board.occupied(xz, yz) == 1 && whiteTurn)
			{
				if(getSelected() != null && getSelected().equals(new Point(xz, yz)))
					setSelected(null);
				else
					setSelected(new Point(xz, yz));
//				System.out.println(xz + "," + yz);
			}
			else if(board.occupied(xz, yz) == 2 && !whiteTurn)
			{
				if(getSelected() != null && getSelected().equals(new Point(xz, yz)))
					setSelected(null);
				else
					setSelected(new Point(xz, yz));
			}
			else
			{
				setSelected(null);

			}
			if(getSelected() != null)
			{
				refreshMoves();
			}
			if(flag == true && movesCount == 0)
				startGame = System.currentTimeMillis();

		}


		refreshThings(flag);


		return flag;

	}

	private void refreshThings(final boolean flag)
	{
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run()
			{

				if(flag)
					movesCount++;

				if(movesCount > 1 && !hasEnded())
				{
					long curTime = System.currentTimeMillis();
					gameLength = curTime-startGame;
				}
			}
		});
		t1.start();
	}

	public long getGameLength(boolean refresh)
	{
		if(refresh)
		{
			long curTime = System.currentTimeMillis();
			gameLength = curTime-startGame;
		}
		return gameLength;
	}


	public void retractLastMove()
	{
		if(!boardSnaps.empty())
		{
			board = boardSnaps.pop();
			if(p2.getType() == AI.PLAYER)
				whiteTurn = !whiteTurn;
			getShownMoves().clear();
		}

	}


	public boolean isAttacked(Point point, Side color, Board br)
	{
		for(int i = 0; i < br.getBoard().length; i++)
		{
			for(int j = 0; j < br.getBoard()[i].length; j++)
			{
				Tile b = br.getBoard()[i][j];
				if(b.getOccupied() != 0)
				{
					ChessPiece ch = b.getChessPiece();
					if(ch.getColor() != color)
					{
						for(SourceMove sm : process(ch.possibleMoves(), i, j, br))
						{
							if(!sm.getMove().isSpecial())
							{
								Point pn = sm.getDestination();
								if(pn.equals(point))
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public void refreshMoves()
	{
		if(getSelected() != null)
		{
			ArrayList<Move> moves = board.getBoard()[getSelected().x][getSelected().y].getChessPiece().possibleMoves();
			this.shownMoves = process(moves, getSelected().x, getSelected().y);
		}
	}


	public Point getSelected() {
		return selected;
	}

	public void setSelected(Point selected) {
		this.selected = selected;
	}

	public ArrayList<Point> getShownMoves() {
		return shownMoves;
	}

	public boolean initialGame()
	{
		if(this.whiteTurn == true)
		{
			if(this.board.getMoveAmount() == 0)
			{
				return true;
			}
		}
		return false;
	}

    public boolean hasEnded()
	{

		Side s = (whiteTurn) ? Side.WHITE : Side.BLACK;
		System.out.println("Has ended, side: " + s + " moves: " + getMoves(s).size());
		if(getMoves(s).size() == 0)
			return true;
		return false;
    }
}
