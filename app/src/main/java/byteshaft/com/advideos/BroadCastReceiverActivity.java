package byteshaft.com.advideos;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadCastReceiverActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "BOOT okay");
        Intent bootIntent = new Intent(context, MainActivity.class);
        bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bootIntent.putExtra("play", true);
        context.startActivity(bootIntent);

    }
}
