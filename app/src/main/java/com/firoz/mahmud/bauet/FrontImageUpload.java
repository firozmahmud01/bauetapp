package com.firoz.mahmud.bauet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FrontImageUpload extends AppCompatActivity {
    ImageView iv;
    Bitmap b=null;
    Bitmap bitmap;
    File file;
    Button upload;
    TextView tv;
    ProgressDialog pd;



    float facedata[]=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_image_upload);
        pd=new ProgressDialog(FrontImageUpload.this);
        pd.setCancelable(false);
        iv=findViewById(R.id.front_image_upload_imageview);
        upload=findViewById(R.id.front_image_upload_button);
        tv=findViewById(R.id.front_image_upload_text_view);
        file=new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"image.jpg");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        findViewById(R.id.front_image_upload_take_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(FrontImageUpload.this,"com.firoz.mahmud.bauet",file);
                in.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                pd.show();
                startActivityForResult(Intent.createChooser(in,"Take photo via:"),101);
            }
        });
//        findViewById(R.id.front_image_upload_choose_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in=new Intent(Intent.ACTION_PICK);
//                in.setType("image/*");
//                pd.show();
//                startActivityForResult(Intent.createChooser(in,"Pick via:"),102);
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK) {
            pd.dismiss();
            return;
        }
        if(requestCode==101){
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

//        }else if(requestCode==102){
//            try {
//                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
//            } catch (IOException e) {
//
//            }

        }else{
            return ;
        }
                iv.setImageBitmap(bitmap);
        FaceDetectorOptions f =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .build();
        try {
            InputImage image = InputImage.fromBitmap(bitmap,0);
            FaceDetection.getClient(f).process(image).addOnCompleteListener(new OnCompleteListener<List<Face>>() {
                @Override
                public void onComplete(@NonNull Task<List<Face>> task) {
                    if(task.isSuccessful()){
                        List<Face> list=task.getResult();
                        if(list.size()==0){
                            pd.dismiss();
                            Toast.makeText(FrontImageUpload.this, "No face found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(list.size()!=1) {
                            Toast.makeText(FrontImageUpload.this, "Only one face should be in your picture", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Face f=list.get(0);
                        Bitmap bit=FaceRecogniger.cropbit(bitmap,f.getBoundingBox());
                        iv.setImageBitmap(bit);
                        try {
                            FaceRecogniger fd = new FaceRecogniger(FrontImageUpload.this.getAssets());
                            facedata = fd.getimagedata(bit);
                            bit.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
                            upload.setVisibility(View.VISIBLE);
                        }catch (Exception e) {

                        }
                    }else{
                        Toast.makeText(FrontImageUpload.this, "No face detected.", Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
        }

    }
}