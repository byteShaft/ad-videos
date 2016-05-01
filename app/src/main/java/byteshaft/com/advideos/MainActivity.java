package byteshaft.com.advideos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    public File path = Environment.getExternalStorageDirectory();
    public static ArrayList<String> filesInFolder;
    private CustomVideoView customVideoView;
    public static final String KEY = "path";
    public static final String POSITION = "position";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // calling set password dialog tod
//        setPasswordDialog(); //
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            loadVideosAndSetAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission granted");
                    loadVideosAndSetAdapter();
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied!"
                            , Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void loadVideosAndSetAdapter() {
        mListView = (ListView) findViewById(R.id.video_list);
        filesInFolder = GetFiles(path + "/Videos");
        ArrayAdapter<String> arrayAdapter = new VideoListAdapter(this,
                android.R.layout.simple_list_item_1, filesInFolder);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new ListItemCLickListener());

    }

    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<>();
        File file = new File(DirectoryPath);
        file.mkdirs();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++)
            MyFiles.add(files[i].getName());
        return MyFiles;
    }

    private class ListItemCLickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), CustomVideoView.class);
            intent.putExtra(KEY, path + "/Videos/" + parent.getItemAtPosition(position).toString());
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }


    class VideoListAdapter extends ArrayAdapter<String> {
        public VideoListAdapter(Context context, int resource, ArrayList<String> videos) {
            super(context, resource, videos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, parent, false);
                holder = new ViewHolder();
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail
                    (path + "/Videos/" + filesInFolder.get(position), MediaStore.Video.Thumbnails.MICRO_KIND);
            holder.thumbnail.setImageBitmap(bitmap);
            return convertView;
        }

    }

    class ViewHolder {
        public ImageView thumbnail;
    }

    private void setPasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Set Password");

        // outside touch disable
        alertDialog.setCancelable(false);

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Save",
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

                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter password", Toast.LENGTH_SHORT).show();

                } else {
                    Helpers.getPreferenceManager().edit().putString("password", password).commit();
                    System.out.println("save password");
                    dialog.dismiss();
                }
            }
        });
    }
}
