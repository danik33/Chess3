package darklight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;

import darklight.chess.R;

public class MainActivity extends AppCompatActivity {

    Button single;
    Intent intnt;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intnt = new Intent(this, Chessplay.class);




        single = findViewById(R.id.singleplay);
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(intnt);
            }
        });


    }
}
