package byteshaft.com.advideos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PasswordActivity extends Activity {

    private String userEntered;
    private String userPin;
    final int PIN_LENGTH = 4;
    boolean keyPadLockedFlag = false;
    private Context appContext;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonExit;
    private ImageView buttonDelete;
    private EditText passwordInput;
    private ImageView backSpace;
    private ImageView buttonOk;

    private CustomVideoView customVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customVideoView = new CustomVideoView();
        MainActivity.getInstance().openedOnce = false;
        appContext = this;
        userEntered = "";
        userPin = Helpers.getPassword();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_password);
        passwordInput = (EditText) findViewById(R.id.editText);
        passwordInput.setInputType(InputType.TYPE_NULL);
        backSpace = (ImageView) findViewById(R.id.imageView);
        buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonDelete = (ImageView) findViewById(R.id.buttonDeleteBack);
        buttonOk = (ImageView) findViewById(R.id.buttonOK);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Helpers.isPasswordSet()) {
                    if (userEntered.length() < 4 || userEntered.length() > 4) {
                        Toast.makeText(PasswordActivity.this,
                                "password must be 4 character", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (userEntered.length() == 4) {
                        Helpers.savePassword(userEntered);
                        Helpers.passwordStatus(true);
                        Toast.makeText(PasswordActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    // check the password and exit the user
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordInput.getText().toString().length() > 0) {
                    passwordInput.setText(passwordInput.getText().toString().substring(0,
                            passwordInput.getText().toString().length() - 1));
                    userEntered = userEntered.substring(0,
                            userEntered.length() - 1);
//                    if (userEntered.length() > 0) {
//                        userEntered = userEntered.substring(0,
//                                passwordInput.getText().toString().length() - 1);
//                    }
                    Log.e("PASS", userEntered);
                }
            }
        });
        buttonExit.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              //Exit app
                                              finish();

                                          }

                                      }
        );

        View.OnClickListener pinButtonHandler = new View.OnClickListener() {
            public void onClick(View v) {
                if (keyPadLockedFlag == true) {
                    return;
                }
                Button pressedButton = (Button) v;
                if (userEntered.length() < PIN_LENGTH) {
                    userEntered = userEntered + pressedButton.getText();
                    Log.v("PinView", "User entered=" + userEntered);
                    passwordInput.setText(passwordInput.getText().toString() + "*");
                    passwordInput.setSelection(passwordInput.getText().toString().length());

                    if (userEntered.length() == PIN_LENGTH) {
                        //Check if entered PIN is correct
                        if (userEntered.equals(userPin)) {
                            Log.v("PinView", "Correct PIN");
                            CustomVideoView.customVideoView.finish();
                            finish();
                        } else {
                            keyPadLockedFlag = true;
                            Log.v("PinView", "Wrong PIN");

                        }
                    }
                } else {
                    passwordInput.setText("");
                    userEntered = "";
                    userEntered = userEntered + pressedButton.getText();
                    Log.v("PinView", "User entered=" + userEntered);
                }
                Log.e("PASS", userEntered);
            }
        };

        button0 = (Button) findViewById(R.id.button0);
        button0.setOnClickListener(pinButtonHandler);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(pinButtonHandler);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(pinButtonHandler);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(pinButtonHandler);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(pinButtonHandler);
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(pinButtonHandler);
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(pinButtonHandler);
        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(pinButtonHandler);
        button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(pinButtonHandler);
        button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener(pinButtonHandler);
        buttonDelete = (ImageView) findViewById(R.id.buttonDeleteBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_pin_entry_view, menu);
        return true;
    }

}
