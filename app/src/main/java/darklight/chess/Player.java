package darklight.chess;

import java.io.Serializable;
import java.util.ArrayList;

import darklight.chess.pieces.ChessPiece;

public class Player implements Serializable{
	
	ArrayList<ChessPiece> eatenPieces;
	Side sd;
	AI type;
	
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
}
