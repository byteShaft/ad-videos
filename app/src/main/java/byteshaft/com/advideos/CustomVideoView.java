package byteshaft.com.advideos;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class CustomVideoView extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public VideoView videoView;
    public int position;
    public int file = 0;
    private MediaController mediaController;
    public static CustomVideoView customVideoView;

    // to bring application on top

    private Timer timer;
    MyTimerTask myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        customVideoView = this;
        String path = getIntent().getStringExtra(MainActivity.KEY);
        position = getIntent().getIntExtra(MainActivity.POSITION, 0);
        startVideo(path);

    }

    private void startVideo(String path) {
        videoView = null;
        videoView = (VideoView) findViewById(R.id.view);
        videoView.setVideoPath(path);
        videoView.setBackground(getResources().getDrawable(R.drawable.ipro));
        Helpers.setScreenBrightness(getWindow(), Screen.Brightness.HIGH);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        mediaController = new MediaController(CustomVideoView.this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.setBackground(null);
                mp.start();
            }
        }, 1000);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        videoView.refreshDrawableState();
        Log.i("TAG", String.valueOf(position));
        if ((position+1) < MainActivity.sFilesInFolder.size()) {
            position = position+1;
            Log.i("TAG", "position ++");
        } else {
            position = 0;
            Log.i("TAG", "position");
        }
        startVideo(MainActivity.path + File.separator +
                AppGlobals.FOLDER + File.separator + MainActivity.sFilesInFolder.get(position));

    }

    @TargetApi(19)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    protected void onPause() {
        if (timer == null) {
            myTimerTask = new MyTimerTask();
            timer = new Timer();
            timer.schedule(myTimerTask, 100, 100);
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void bringApplicationToFront() {
        KeyguardManager myKeyManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (myKeyManager.inKeyguardRestrictedInputMode())
            return;

        Log.d("TAG", "====Bringging Application to Front====");

        Intent notificationIntent = new Intent(this, CustomVideoView.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            bringApplicationToFront();
        }
    }

    private void enterPasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomVideoView.this);
        // Setting Dialog Title
        alertDialog.setTitle("Enter Password");

        // outside touch disable
        alertDialog.setCancelable(true);

        final EditText input = new EditText(CustomVideoView.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter.LengthFilter(4)});
        input.setHint("Enter pin code ");

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        input.setLayoutParams(lp);
        alertDialog.setView(input);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        // Showing Alert Message
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = input.getText().toString().trim();

                String savedPassword =  Helpers.getPreferenceManager().getString("password", "");

                if (savedPassword.equals(password)) {
                    // kill app
                    android.os.Process.killProcess(android.os.Process.myPid());
                    finish();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static class Screen {
        static class Brightness {
            static final float HIGH = 1f;
            static final float LOW = 0f;
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("TAG", "onBackPressed");
        enterPasswordDialog();
//        startActivity(new Intent(this, PasswordActivity.class));
    }
}
