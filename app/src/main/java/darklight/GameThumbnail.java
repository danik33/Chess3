package darklight;

import java.io.Serializable;

import darklight.chess.Side;

public class GameThumbnail implements Serializable {



    long gameLength;
    Result gameResult;
    Side pSide;
    private static final long serialVersionUID = 1L;

    public GameThumbnail(long len, Result res, Side pSide)
    {
        this.gameLength = len;
        this.gameResult = res;
        this.pSide = pSide;
    }



}
