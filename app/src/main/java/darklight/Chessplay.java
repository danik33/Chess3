package darklight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import darklight.chess.AI;
import darklight.chess.Game;
import darklight.chess.Move;
import darklight.chess.Piece;
import darklight.chess.Player;
import darklight.chess.Point;
import darklight.chess.R;
import darklight.chess.Side;
import darklight.chess.SourceMove;
import darklight.chess.Stats;
import darklight.chess.board.Tile;
import darklight.popup.Pop;
import darklight.popup.Popup;

public class Chessplay extends AppCompatActivity {


    Game game;
    Game loadedGame;
    Stats stats;
    Player p1, p2;
    AI Ai1, Ai2;

    File gameFile, multigame;
    File statsFile;
    Pop currentMessage;

    int tileSize;
    boolean autoRotate;
    int screenWidth, screenHeight;
    boolean bkSet;


    Bitmap bm;
    Bitmap[] textures;


    ImageView chessView;
    TextView turnLabel;
    View messageView;
    TextView msgTitle, msgText;
    Button[] msgButtons;
    Button undo;
    ImageView darkening;
    TextView timeLabel;
    ImageView bk;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Ai1 = (AI) getIntent().getSerializableExtra("Ai1");
        Ai2 = (AI) getIntent().getSerializableExtra("Ai2");
        currentMessage = Pop.NONE;
        autoRotate = false;
        textures = new Bitmap[12];
        p1 = new Player(Side.WHITE, Ai1);
        p2 = new Player(Side.BLACK, Ai2);
        bkSet = false;

        loadTextures();


        game = new Game(Ai1, Ai2);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chessplay);


        File f = getApplicationContext().getFilesDir();
        gameFile = new File(f.getAbsolutePath() + "//game.dat");
        multigame = new File(f.getAbsolutePath() + "//multigame.dat");
        statsFile = new File(f.getAbsolutePath() + "//stats.dat");

        initViews();
        loadStats();
        loadGame();


        Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        checkAIMove();
    }

    private void checkAIMove()
    {
        if(!game.hasEnded()) {
            System.out.println(Ai1 + ", " + Ai2);
            System.out.println(p1.getType() + ", " + p2.getType());
            Side curTurn = (game.whiteTurn) ? Side.WHITE : Side.BLACK;
            if (p1.getColor() == curTurn)
            {
                if (p1.getType() != AI.PLAYER) {
                    SourceMove m = p2.getNextMove(game);
                    game.getBoard().move(m.getSource(), m.getMove());
                    game.getShownMoves().clear();
                    game.setSelected(null);
                    game.whiteTurn = !game.whiteTurn;
                    checkAIMove();

                } else
                    return;
            } else if (p2.getColor() == curTurn) {
                if (p2.getType() != AI.PLAYER) {
                    SourceMove m = p2.getNextMove(game);
                    game.getBoard().move(m.getSource(), m.getMove());
                    game.getShownMoves().clear();
                    game.setSelected(null);
                    game.whiteTurn = !game.whiteTurn;

                    checkAIMove();
                } else
                    return;
            }
            refreshTurn();
        }

    }


    private void refreshTime()
    {
        if(game.movesCount > 0)
        {
            long time = game.getGameLength(true);
            timeLabel.setText("Time: " + timeToString(time));
        }

    }

    private String timeToString(long time)
    {

        int ms = (int) ((time/10)%100);
        int sc = (int) (time/1000)%60;
        int mn = (int) (time/60000)%60;
        return mn + ":" + sc + ":" + ms;
    }

    private void loadGame()
    {
        if(p1.getType() != AI.PLAYER || p2.getType() != AI.PLAYER)
            loadedGame = (Game) Saves.loadObject(gameFile);
        else
            loadedGame = (Game) Saves.loadObject(multigame);
        if(loadedGame != null && !loadedGame.initialGame())
        {
            if(game.autoResume)
            {
                System.out.println("It's autoresume");
                game = loadedGame;
                reDraw();
                refreshTurn();
                game.startGame = System.currentTimeMillis() - game.getGameLength(false);
            }
            else {
                System.out.println("It's not autoresume");
                showPop(Pop.SAVEDGAME);
            }
        }
    }

    private void loadStats()
    {
        Stats s1 = (Stats) Saves.loadObject(statsFile);
        if(s1 == null)
            stats = new Stats();
        else
            stats = s1;
    }

    private void processMessage(int ans)
    {
        System.out.println("Processing message: ans=" + ans);
        if(currentMessage == Pop.SAVEDGAME)
        {
            if(ans == 2)
            {
                game = loadedGame;
                reDraw();
                refreshTurn();
            }
            hidePopup();
        }
        if(currentMessage == Pop.ENDGAME2PL)
        {
            if(ans == 1)
                startActivity(new Intent(this, MainActivity.class));
            else if(ans == 2)
                hidePopup();
            else if(ans == 3)
            {
                game = new Game(Ai1, Ai2);
                hidePopup();
                reDraw();
                refreshTurn();
            }
        }
        if(currentMessage == Pop.SINGLEQUIT)
        {
            if(ans == 0)
            {
                hidePopup();
                return;
            }
            if(ans == 2)
                countGame(Result.DRAW, Side.NONE);
            if(ans == 3)
            {
                game.autoResume = true;
                super.onBackPressed();
            }
            if (ans == 4)
                countGame((p1.getColor() == Side.WHITE) ? Result.BLACKWIN : Result.WHITEWIN, p1.getColor());
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void countGame(Result res, Side s)
    {

        if(gameFile.delete())
        {
            System.out.println("Deleted save game");
        }
        stats.add(res, game.getGameLength(false), s);
    }


    @Override
    public void onBackPressed()
    {
        if (!shouldAllowBack())
        {
            showPop(Pop.SINGLEQUIT);
        }
        else
        {
            super.onBackPressed();
        }
    }

    private boolean shouldAllowBack()
    {
        return game.hasEnded();
    }

    private void hidePopup()
    {
        System.out.println("Hiding popup");
        messageView.setVisibility(View.GONE);
        darkening.setVisibility(View.GONE);
        undo.setEnabled(true);
        chessView.setEnabled(true);
        currentMessage = Pop.NONE;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews()
    {
        chessView = findViewById(R.id.chessGame);
        turnLabel = findViewById(R.id.turn);
        messageView = findViewById(R.id.message);
        darkening = findViewById(R.id.darkening);
        msgTitle = findViewById(R.id.MessageTitle);
        msgText = findViewById(R.id.MessageContent);
        msgButtons = new Button[5];
        msgButtons[0] = findViewById(R.id.btn1);
        msgButtons[1] = findViewById(R.id.btn2);
        msgButtons[2] = findViewById(R.id.btn3);
        msgButtons[3] = findViewById(R.id.btn4);
        msgButtons[4] = findViewById(R.id.btnCancel);
        undo = findViewById(R.id.retract);
        timeLabel = findViewById(R.id.gameTime);
        bk = findViewById(R.id.background);
        msgButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                processMessage(1);
            }
        });
        msgButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                processMessage(2);
            }
        });
        msgButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                processMessage(3);
            }
        });
        msgButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                processMessage(4);
            }
        });
        msgButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                processMessage(0);
            }
        });

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
                    {
                        if(game.processPress(xz, yz)) //Return true if moved
                        {
                            game.refreshMoves();
                            if (autoRotate)
                                game.board.rotate(game.whiteTurn);
                            refreshTurn();
                            if(p2.getType() != AI.PLAYER)
                                Saves.saveObject(gameFile, game);
                            else
                                Saves.saveObject(multigame, game);
                            checkAIMove();
                        }
                    }
                    if(game.hasEnded())
                        showPop(Pop.ENDGAME2PL);
                    reDraw();


                    return true;
                }
                return false;

            }
        });




        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                game.retractLastMove();

                if(game.movesCount > 0)
                    game.movesCount--;
                reDraw();
                refreshTurn();
            }
        });







        Thread refreshTime = new Thread(new Runnable() {
            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        runOnUiThread(new Runnable() {
                            public void run(){
                                refreshTime();
                            }
                        });
                        Thread.sleep(1000/60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        refreshTime.start();




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
    }

    private void showPop(Pop type)
    {
        Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        else
            v.vibrate(100);

        messageView.setVisibility(View.VISIBLE);
        darkening.setVisibility(View.VISIBLE);
        Popup p;
        if(type != Pop.ENDGAME2PL)
            p = new Popup(type);
        else
            p = new Popup(!game.whiteTurn, true);

        msgTitle.setText(p.getTitle());
        msgText.setText(p.getText());
        for (int i = 0; i < 5; i++)
        {
            msgButtons[i].setVisibility(View.VISIBLE);
            msgButtons[i].setText(p.getButtons()[i]);
            if (p.getButtons()[i] == null || p.getButtons()[i].equals(""))
                msgButtons[i].setVisibility(View.GONE);
        }


        undo.setEnabled(false);
        chessView.setEnabled(false);
        currentMessage = type;





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
//                    Rect rb = new Rect(i*tileSize, j*tileSize, i*(tileSize+1), j*(tileSize+1));
                    cv.drawBitmap(im, i*tileSize, j*tileSize, p);
                }
            }
            clr = !clr;
        }



        int darkeningColor = ResourcesCompat.getColor(getResources(), R.color.darkening, null);
        int enemyDarkening = ResourcesCompat.getColor(getResources(), R.color.enemyDarkening, null);;
        int black = ResourcesCompat.getColor(getResources(), R.color.black, null);

        try
        {
            if (game.getSelected() != null) {
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
        }
        catch (Exception ee)
        {
            System.out.println("Unexpcted null pointer ? idk how to solve it");
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
