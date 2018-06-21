package jp.techacademy.hiromu.kikuchi.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    ContentResolver resolver;
    Cursor cursor;

    Handler mHandler = new Handler();

    Timer mTimer;
    double mTimerSec = 0.0;
    TextView mTextView;


    Button mAdvanceButton;
    Button mBackButton;
    Button mMainButton;


    private static final int PERMISSIONS_REQUEST_CODE = 0;





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Handler mHandler = new Handler();

        mAdvanceButton = (Button) findViewById(R.id.Advance_button);
        mBackButton = (Button) findViewById(R.id.Back_button);
        mMainButton = (Button) findViewById(R.id.Main_button);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                getContentsInfo();

            } else {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);

            }

        }

    }
    public void getContentsInfo() {
        resolver = getContentResolver();
        cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {

            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        }





            mAdvanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cursor.moveToNext()) {
                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        Long id = cursor.getLong(fieldIndex);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageURI(imageUri);


                    }
                    if (cursor.moveToNext() == false) {

                        cursor.moveToFirst();

                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        Long id = cursor.getLong(fieldIndex);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageURI(imageUri);
                    }
                }

            });

            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cursor.moveToPrevious()) {
                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        Long id = cursor.getLong(fieldIndex);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageURI(imageUri);
                    }
                    if (cursor.moveToPrevious() == false) {

                        cursor.moveToLast();

                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        Long id = cursor.getLong(fieldIndex);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageURI(imageUri);
                    }
                }
            });

            mMainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mTimer == null) {
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {

                            @Override
                            public void run() {

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (cursor.moveToNext()) {
                                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                            Long id = cursor.getLong(fieldIndex);
                                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                            imageView.setImageURI(imageUri);

                                            mAdvanceButton.setEnabled(false);
                                            mBackButton.setEnabled(false);

                                            TextView mTextView = (TextView) findViewById(R.id.Main_button);
                                            mTextView.setText("停止");

                                        } else if (cursor.moveToNext() == false) {

                                            cursor.moveToFirst();

                                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                            Long id = cursor.getLong(fieldIndex);
                                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                            imageView.setImageURI(imageUri);

                                        }
                                    }
                                });
                            }
                        }, 2000, 2000);

                        //停止時の処理
                    } else if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;

                        TextView mTextView = (TextView) findViewById(R.id.Main_button);
                        mTextView.setText("再生");

                        mAdvanceButton.setEnabled(true);
                        mBackButton.setEnabled(true);

                    }

                }

            });

        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }
}





