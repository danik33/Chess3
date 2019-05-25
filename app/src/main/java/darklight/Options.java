package darklight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import darklight.chess.AI;
import darklight.chess.R;

public class Options extends AppCompatActivity {

    Button ai, sound;
    boolean a;
    boolean b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        a = false;
        b = true;


        ai = findViewById(R.id.aidiff);
        sound = findViewById(R.id.sound);

        refreshText();

        ai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(a)
                    Chessplay.Aidiff = AI.HUNGRYAI;
                else
                    Chessplay.Aidiff = AI.RANDOMAI;
                refreshText();
            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(b)
                    Chessplay.playSound = false;
                else
                    Chessplay.playSound = true;
                refreshText();
            }
        });
    }

    private void refreshText() {
        if (!Chessplay.playSound)
        {
            sound.setText("Play sound: No");
            b = false;
        }
        else
        {
            sound.setText("Play sound: Yes");
            b = true;
        }

        if (Chessplay.Aidiff == AI.HUNGRYAI)
        {
            ai.setText("AI Difficulty: Very Easy");
            a = false;
        }
        else
        {
            a = true;
            ai.setText("AI Difficulty: Even easier");
        }
    }
}
