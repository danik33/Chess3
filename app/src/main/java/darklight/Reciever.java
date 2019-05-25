package darklight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class Reciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;



        if(batteryPct < 0.15)
            Toast.makeText(context, "Battery is low, be careful. (" + batteryPct*100+ "%)", Toast.LENGTH_LONG).show();

    }
}
