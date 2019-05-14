package darklight.chess;

import java.io.Serializable;

public enum Piece implements Serializable
{
	KING,
	QUEEN,
	ROOK,
	KNIGHT,
	BISHOP,
	PAWN;

	public static int getValue(Piece p)
	{
		if(p == KING)
			return 100;
		if(p == QUEEN)
			return 9;
		if(p == ROOK)
			return 5;
		if(p == KNIGHT)
			return 3;
		if(p == BISHOP)
			return 3;
		return 1;
	}

	public int getValue()
	{
		if(this == KING)
			return 100;
		if(this == QUEEN)
			return 9;
		if(this == ROOK)
			return 5;
		if(this == KNIGHT)
			return 3;
		if(this == BISHOP)
			return 3;
		return 1;
	}


}
