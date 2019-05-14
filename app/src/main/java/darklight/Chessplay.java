package darklight;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import darklight.chess.Game;
import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.R;
import darklight.chess.Side;
import darklight.chess.SourceMove;
import darklight.chess.board.Tile;

public class Chessplay extends AppCompatActivity {

    ImageView chessView;
    TextView turnLabel;
    Game game;
    int tileSize;
    Bitmap[] textures;
    boolean autoRotate;
    Bitmap bm;
    int screenWidth, screenHeight;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        autoRotate = false;
        textures = new Bitmap[12];

        loadTextures();

        game = new Game();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chessplay);


        chessView = findViewById(R.id.chessGame);
        turnLabel = findViewById(R.id.turn);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;





        ViewTreeObserver viewTreeObserver = chessView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    chessView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    tileSize = chessView.getWidth()/8;
                    resizeImages();
                    bm = Bitmap.createBitmap(chessView.getWidth(), chessView.getWidth(), Bitmap.Config.ARGB_8888);
                    reDraw();
                    chessView.setImageBitmap(bm);
                    chessView.invalidate();
                    refreshTurn();
                    System.out.println("tatarara" + chessView.getWidth());
                }
            });
        }




        chessView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent m)
            {

                if(m.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) m.getX();
                    int y = (int) m.getY();
                    int xz = x / tileSize;
                    int yz = y / tileSize;
                    System.out.println("Click on, (" + xz + ", " + yz + ")");
                    if (x > 0 && y > 0)
                        game.processPress(xz, yz);
                    game.refreshMoves();


                    if (autoRotate)
                        game.board.rotate(game.whiteTurn);

                    reDraw();
                    refreshTurn();
//                    isAttacked(Point point, Side color, Board br);
                    System.out.println(game.isAttacked(new Point(0, 5), Side.BLACK, game.getBoard()));
                    System.out.println(game.isAttacked(new Point(0, 5), Side.WHITE, game.getBoard()));


                    return true;
                }
                return false;

            }

        });


        Button undo = findViewById(R.id.retract);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                game.retractLastMove();
                reDraw();
            }
        });

    }

    private void refreshTurn()
    {
        String turn = "Black";
        if(game.whiteTurn)
            turn = "White";
        turn += "'s turn";
        turnLabel.setText(turn);

    }

    private void reDraw()
    {
        Paint p = new Paint();
        Canvas cv = new Canvas(bm);





        int darktile = ResourcesCompat.getColor(getResources(), R.color.darkTile, null);
        int lighttile = ResourcesCompat.getColor(getResources(), R.color.lightTile, null);
        p.setColor(darktile);





        boolean clr = false;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(clr)
                    p.setColor(darktile);
                else
                    p.setColor(lighttile);
                clr = !clr;

                cv.drawRect(new Rect(i*tileSize, j*tileSize, i*tileSize + tileSize, j*tileSize + tileSize), p);
                if(game.board.occupied(i, j) > 0)
                {
                    Bitmap im = getTexture(game.board.getBoard()[i][j]);

                    Rect rb = new Rect(i*tileSize, j*tileSize, i*(tileSize+1), j*(tileSize+1));
                    cv.drawBitmap(im, i*tileSize, j*tileSize, p);
                }
            }
            clr = !clr;
        }



        int darkeningColor = ResourcesCompat.getColor(getResources(), R.color.darkening, null);
        int enemyDarkening = ResourcesCompat.getColor(getResources(), R.color.enemyDarkening, null);;
        int black = ResourcesCompat.getColor(getResources(), R.color.black, null);;

        if(game.getSelected() != null) {
            int i = game.getSelected().x, j = game.getSelected().y;
            if (game.getBoard().getBoard()[i][j].getOccupied() >= 0) {
                int enemy = (game.board.occupied(i, j) == 1) ? 2 : 1;


                p.setColor(ResourcesCompat.getColor(getResources(), R.color.selectedDarkening, null));
                cv.drawRect(new Rect(i * tileSize, j * tileSize, i * tileSize + tileSize, j * tileSize + tileSize), p);

                p.setColor(darkeningColor);

                ArrayList<Move> moves = game.getBoard().getBoard()[i][j].getChessPiece().possibleMoves();
                for (Point m : game.getShownMoves()) {
                    if (game.board.getBoard()[m.x][m.y].getOccupied() == enemy) {
                        p.setColor(enemyDarkening);
                    } else {
                        p.setColor(darkeningColor);
                    }
                    cv.drawRect(new Rect(m.x * tileSize, m.y * tileSize, m.x * tileSize + tileSize, m.y * tileSize + tileSize), p);
                }
            }
        }

        if(game.getBoard().getLastMove() != null)
        {
            p.setColor(ResourcesCompat.getColor(getResources(), R.color.blue , null));
            p.setStrokeWidth(10);
            SourceMove sc = game.getBoard().getLastMove();

            cv.drawRect(new Rect(sc.getSource().x * tileSize, sc.getSource().y * tileSize, sc.getSource().x * tileSize + tileSize, sc.getSource().y * tileSize + tileSize), p);
            cv.drawRect(new Rect(sc.getDestination().x * tileSize, sc.getDestination().y * tileSize, sc.getDestination().x * tileSize + tileSize, sc.getDestination().y * tileSize + tileSize), p);
            System.out.println();
        }

        if(chessView != null)
            chessView.invalidate();
    }

    private void resizeImages()
    {
        for(int i = 0; i < 12; i++)
        {
            textures[i] = Bitmap.createScaledBitmap(textures[i], tileSize, tileSize, false);
        }

    }

    public Bitmap getTexture(Tile tile)
    {
        Side s = tile.getSide();
        Piece p = tile.getType();
        if(s == Side.BLACK)
        {
            switch(p)
            {
                case BISHOP:
                    return textures[5];
                case KING:
                    return textures[7];
                case KNIGHT:
                    return textures[3];
                case PAWN:
                    return textures[1];
                case QUEEN:
                    return textures[9];
                case ROOK:
                    return textures[11];

            }
        }
        else
        {
            switch(p)
            {
                case BISHOP:
                    return textures[4];
                case KING:
                    return textures[6];
                case KNIGHT:
                    return textures[2];
                case PAWN:
                    return textures[0];
                case QUEEN:
                    return textures[8];
                case ROOK:
                    return textures[10];

            }
        }
        return null;
    }

    private void loadTextures()
    {
        textures[0] = BitmapFactory.decodeResource(getResources(), R.drawable.pawnw);
        textures[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pawnb);
        textures[2] = BitmapFactory.decodeResource(getResources(), R.drawable.knw);
        textures[3] = BitmapFactory.decodeResource(getResources(), R.drawable.knb);
        textures[4] = BitmapFactory.decodeResource(getResources(), R.drawable.bw);
        textures[5] = BitmapFactory.decodeResource(getResources(), R.drawable.bb);
        textures[6] = BitmapFactory.decodeResource(getResources(), R.drawable.kw);
        textures[7] = BitmapFactory.decodeResource(getResources(), R.drawable.kb);
        textures[8] = BitmapFactory.decodeResource(getResources(), R.drawable.qw);
        textures[9] = BitmapFactory.decodeResource(getResources(), R.drawable.qb);
        textures[10] = BitmapFactory.decodeResource(getResources(), R.drawable.rw);
        textures[11] = BitmapFactory.decodeResource(getResources(), R.drawable.rb);
    }
}
