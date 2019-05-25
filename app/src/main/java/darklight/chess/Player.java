package darklight.chess;

import java.io.Serializable;
import java.util.ArrayList;

import darklight.Chessplay;
import darklight.chess.board.Tile;
import darklight.chess.pieces.ChessPiece;

public class Player implements Serializable{
	
	ArrayList<ChessPiece> eatenPieces;
	Side sd;
	AI type;

	private static final long serialVersionUID = 1L;
	
	public Player(Side sd)
	{
		this.sd = sd;
		this.type = AI.PLAYER;
		eatenPieces = new ArrayList<ChessPiece>();
	}

    public Player(Side sd, AI type)
	{
		this(sd);
		this.type = type;
    }

	public Player(Player p1)
	{
		this.sd = p1.sd;
		this.type = p1.getType();
		this.eatenPieces = new ArrayList<>();
	}

	public SourceMove getNextMove(Game g)
	{
		ArrayList<SourceMove> arr = g.getMoves(sd);
		for(SourceMove m : arr)
		{
			Tile dest = g.getBoard().getBoard()[m.getDestination().x][m.getDestination().y];
			if(dest.getChessPiece() != null && dest.getSide() != sd)
				return m;
		}

		return arr.get(rand(0, arr.size()-1));
	}

	public int rand(int min, int max)
	{
		return (int) (Math.random()*(max-min+1)+min);
	}

	public AI getType() { return this.type; }


    public Side getColor() { return this.sd;}
}
