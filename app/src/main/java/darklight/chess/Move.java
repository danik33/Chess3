package darklight.chess;


import java.io.Serializable;

import darklight.chess.board.Condition;

public class Move implements Serializable
{
	
//	this.con = new Condition() { public boolean canMove(Board b) {return true;}};
	private static final long serialVersionUID = 1L;

	private int xoff, yoff;
	private Condition con;
	private boolean repeatable;
	private boolean eats;
	private boolean specialMove;


	public Move()
	{
		this(0,0);
	}

	public Move(int x, int y, boolean repeatable)
	{
		this(x, y);
		this.repeatable = repeatable;
		
	}
	public Move(boolean eats, int x, int y)
	{
		this(x, y);
		this.eats = eats;
	}
	
	public Move(boolean eats, int x, int y, Condition con)
	{
		this(eats, x, y);
		
		this.con = con;
		this.specialMove = true;
	}

	public Move(int x, int y) 
	{
		this.xoff = x;
		this.yoff = y;
		this.repeatable = false;
		this.eats = true;
		this.specialMove = false;
	}
	
	
	public void invertY() { yoff = -yoff; }
	public int getX() { return xoff; }
	public int getY() { return yoff; }
	public boolean canMove(Game g, int x, int y)
	{ 
		if(con != null)
			return con.canMove(g, x, y); 
		else
			return true;
	}
	public boolean isRepeatable(){ return this.repeatable; }
	public boolean doesEats() { return this.eats; }
	public boolean isSpecial() { return this.specialMove; }
	
	
	@Override
	public String toString()
	{
		return "(" + xoff + ", " + yoff + "). Eats: " + this.eats + ", Repeatable: " + this.repeatable;
	}
	
	@Override
	public boolean equals(Object b)
	{
		if(b instanceof Move)
		{
			Move m2 = (Move)b;
			if(m2.xoff == this.xoff && this.yoff == m2.yoff)
				return true;
		}
		return false;
	}
	

	
	

}
