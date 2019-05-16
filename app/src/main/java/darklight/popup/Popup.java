package darklight.popup;

public class Popup {

    private String title;
    private String text;
    private String[] btnName = new String[5];

    public Popup(String title, String text, String... buttonNames)
    {
        this.title = title;
        this.text = text;
        for(int i = 0; i < buttonNames.length || i < 5; i++)
        {
            btnName[i] = buttonNames[i];
        }
    }

    public Popup(Pop predefined)
    {
        if(predefined == Pop.SAVEDGAME)
        {
            title = "Found saved game";
            text = "Load saved game ?";
            btnName[0] = "New Game";
            btnName[1] = "Load";
        }
        else if(predefined == Pop.SINGLEQUIT)
        {
            title = "Are you sure you want to quit ?";
            text = "You want to pause the game or abandon ?";
            btnName[0] = "Abandon game";
            btnName[1] = "Draw game";
            btnName[2] = "Win game";
            btnName[3] = "Lose game";
            btnName[4] = "Cancel";
        }
    }

    public Popup(boolean win)
    {
        String msg;
        if(win)
        {
            msg = "Victory.";
        }
        else
        {
            msg = "Checkmate. Defeat.";
        }
        title = "Game finished";
        text = "Game has finished, play again ?";
        btnName[0] = "Quit";
        btnName[1] = "Look at the board";
        btnName[2] = "Play again";


    }



    public String getTitle(){ return this.title;   }
    public String getText() { return this.text;    }
    public String[] getButtons() { return this.btnName;}

}
