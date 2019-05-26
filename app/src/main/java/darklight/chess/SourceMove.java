package darklight.chess;


import java.io.Serializable;


public class SourceMove implements Serializable {

	private int sourceX, sourceY;
	private Move m;

	private static final long serialVersionUID = 1L;
	
	public SourceMove(int sourceX, int sourceY, int x, int y) 
	{
		this.m = new Move(x, y);
		this.sourceX = sourceX; 
		this.sourceY = sourceY;
	}
	public SourceMove(Point p, Move m)
	{
		this.m = m;
		this.sourceX = p.x;
		this.sourceY = p.y;
	}
	
	public SourceMove(int sourceX, int sourceY, Move m)
	{
		this.m = m;
		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}
	
	public Move getMove() { return this.m; }
	
	public Point getDestination()
	{
		return new Point(sourceX + m.getX(), sourceY + m.getY());
	}
	public Point getSource()
	{
		return new Point(sourceX, sourceY);
	}

	
	@Override
	public String toString()
	{
		String from = "Move from: (" + sourceX + ", " + sourceY + ")";
		if(!m.isRepeatable())
			return  from + " to (" + getDestination().x + ", " + getDestination().y + ")";
		else
			return from + " repeatable to [" + m.getX() + ", " + m.getY() + "]";
	}
	
	@Override
	public boolean equals(Object b)
	{
		if(!(b instanceof SourceMove))
			return false;
		else
		{
			SourceMove m2 = (SourceMove)b;
			if(m2.m.equals(this.m) && m2.sourceX == this.sourceX && m2.sourceY == this.sourceY)
				return true;
		}
		return false;
	}

}
