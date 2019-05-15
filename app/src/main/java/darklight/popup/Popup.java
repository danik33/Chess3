package darklight.popup;

public class Popup {

    private String title;
    private String text;
    private String btn1Name, btn2Name;

    public Popup(String title, String text, String n1, String n2)
    {
        this.title = title;
        this.text = text;
        this.btn1Name = n1;
        this.btn2Name = n2;
    }

    public Popup(Pop predefined)
    {
        if(predefined == Pop.SAVEDGAME)
        {
            title = "Found saved game";
            text = "Load saved game ?";
            btn1Name = "New Game";
            btn2Name = "Load";
        }
    }


    public String getTitle(){ return this.title;   }
    public String getText() { return this.text;    }
    public String getBtn1() { return this.btn1Name;}
    public String getBtn2() { return this.btn2Name;}
}
