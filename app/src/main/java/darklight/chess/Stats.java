package darklight.chess;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import darklight.GameThumbnail;
import darklight.Result;

public class Stats implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    ArrayList<GameThumbnail> gt;
    public int gamesPlayed;
    public int gamesWon;
    public int gamesDraw;


    public Stats()
    {
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesDraw = 0;
        gt = new ArrayList<>();
    }




    public void add(Result res, long gameLength, Side s)
    {
        gt.add(new GameThumbnail(gameLength, res, s));
        gamesPlayed++;
        if((res == Result.WHITEWIN && s == Side.WHITE) || (res == Result.BLACKWIN && s == Side.BLACK))
        {
            gamesWon++;
        }
        else if(res == Result.DRAW)
        {
            gamesDraw++;
        }

    }
}
