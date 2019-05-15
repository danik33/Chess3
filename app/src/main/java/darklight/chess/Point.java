package darklight.chess;

import java.io.Serializable;

public class Point implements Serializable {

    public int x, y;

    private static final long serialVersionUID = 1L;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point()
    {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public boolean equals(Object p2)
    {
        if(!(p2 instanceof Point))
            return false;
        Point p = (Point)p2;
        if(p.x == this.x && p.y == this.y)
            return true;
        return false;
    }






}
