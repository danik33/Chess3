package darklight.chess;

import java.io.Serializable;
import java.util.ArrayList;

import darklight.chess.pieces.ChessPiece;

public class Player implements Serializable{
	
	ArrayList<ChessPiece> pieces;
	Side sd;
	
	public Player(Side sd)
	{
		this.sd = sd;

	}

    public Player(Side black, AI type)
	{

    }
}
