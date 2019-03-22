package com.example.challenge_1_randall_larsen_pt2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap.CompressFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    SQLiteDatabase db;
    EditText num1_edit;
    int index = 1;
    private static final int CAMERA_REQUEST = 1888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        num1_edit = findViewById(R.id.editText);


        //database created
        db = this.openOrCreateDatabase("cam.db", Context.MODE_PRIVATE,null);
        //table created
       // db.execSQL("create table if not exists"+" imageTb"+"(KEY_IMAGE" + " BLOB)");
        String createTable = "create table if not exists picts(id INTEGER PRIMARY KEY AUTOINCREMENT, img blob not null)";
        db.execSQL(createTable);
        Button Button = (Button) this.findViewById(R.id.cam_but);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Matrix matrix = new Matrix();
            // matrix.preScale(-1.0f, 1.0f);
            matrix.preRotate(90);
            Bitmap bOutput = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);


            byte[] image = getBytesFromBitmap(bOutput);

            Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
            imageView.setImageBitmap(bmp);



            ContentValues values = new ContentValues();
            values.put("img", image);
            long result = db.insert("picts", null,values);

            Toast.makeText(this,String.valueOf(result),Toast.LENGTH_LONG).show();

        }
    }


    public void get(View view) {
        index = Integer.parseInt(num1_edit.getText().toString());
        Cursor c = db.rawQuery("select * from picts where id=?", new String[]{String.valueOf(index)});
        if(c.moveToNext())
        {
           byte[] image = c.getBlob(1);
            Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
            imageView.setImageBitmap(bmp);
            //Toast.makeText(this,"Done", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,String.valueOf(index),Toast.LENGTH_LONG).show();
        }

    }
}