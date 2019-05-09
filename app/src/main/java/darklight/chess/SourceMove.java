package darklight.chess;

import android.graphics.Point;


public class SourceMove extends Move{

	private int sourceX, sourceY;


	public SourceMove(int sourceX, int sourceY, int x, int y)
	{
		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}
	public SourceMove(Point p, Move m)
	{
		this.sourceX = p.x;
		this.sourceY = p.y;
	}

	public SourceMove(int sourceX, int sourceY, Move m)
	{
		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}

	public Move getMove() { return this; }

	public Point getDestination()
	{
		return new Point(sourceX + getX(), sourceY + getY());
	}
	public Point getSource()
	{
		return new Point(sourceX, sourceY);
	}

	public int getX() { return this.sourceX; }
	public int getY() { return this.sourceY; }

	@Override
	public String toString()
	{
		String from = "Move from: (" + sourceX + ", " + sourceY + ")";
		if(!isRepeatable())
			return  from + " to (" + getDestination().x + ", " + getDestination().y + ")";
		else
			return from + " repeatable to [" + getX() + ", " + getY() + "]";
	}

	@Override
	public boolean equals(Object b)
	{
		if(!(b instanceof SourceMove))
			return false;
		else
		{
			SourceMove m2 = (SourceMove)b;
			if(m2.sourceX == this.sourceX && m2.sourceY == this.sourceY && m2.getX() == getX() && m2.getY() == getY())
				return true;
		}
		return false;
	}

}
