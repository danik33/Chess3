package darklight.chess.board;

import java.io.Serializable;

import darklight.chess.Game;

public interface Condition extends Serializable
{
	
//	public boolean canMove(Board b, int x, int y); // chess piece x ,y 

	boolean canMove(Game b, int x, int y);

}
