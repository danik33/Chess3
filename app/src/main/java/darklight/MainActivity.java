package darklight;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import darklight.chess.AI;
import darklight.chess.R;

public class MainActivity extends AppCompatActivity {

    Button single, mp, settings;
    Intent intnt;
    Reciever b = new Reciever();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(b, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        intnt = new Intent(this, Chessplay.class);






        single = findViewById(R.id.singleplay);
        mp = findViewById(R.id.multiplay);
        settings = findViewById(R.id.options);

        mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                unregisterReceiver(b);
                intnt.putExtra("Ai1", AI.PLAYER);
                intnt.putExtra("Ai2", AI.PLAYER);
                startActivity(intnt);
            }
        });
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                unregisterReceiver(b);
                intnt.putExtra("Ai1", AI.PLAYER);
                intnt.putExtra("Ai2", AI.RANDOMAI);

                startActivity(intnt);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent sett = new Intent(getApplicationContext(), Options.class );
                startActivity(sett);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(b, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    @Override
    public void onBackPressed(){}
}
