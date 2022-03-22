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
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firoz.mahmud.bauet.Api.LoginApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FrontImageUpload extends AppCompatActivity {
    ImageView iv;
    Bitmap b=null;
    Bitmap bitmap;
    File file;
    ByteArrayOutputStream bos;
    Button upload;
    TextView tv;
    ProgressDialog pd;

DatabaseReference dr;

    float facedata[]=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_image_upload);
        dr= FirebaseDatabase.getInstance().getReference("Students");
        pd=new ProgressDialog(FrontImageUpload.this);
        pd.setCancelable(false);
        bos=new ByteArrayOutputStream();
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
                if(facedata==null){
                    Toast.makeText(FrontImageUpload.this, "You have to take your face photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                //bos variable is the image variable

//                try {
//                    String image = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
//                    LoginApi log = new LoginApi(FrontImageUpload.this);
//                    log.uploadFace(image, facedata);
//
//                }catch (Exception e){}
//                facedata
                EditText name,id,department,batch;
                name=findViewById(R.id.front_image_name_edittext);
                id=findViewById(R.id.front_image_id_edittext);
                department=findViewById(R.id.frontmageupload_department_edittext);
                batch=findViewById(R.id.front_image_upload_batch_edittext);
                if(name.getText().toString().isEmpty()){
                    name.setError("Fill it");
                    return ;
                }
                if(id.getText().toString().isEmpty()){
                    id.setError("Fill it");
                    return ;
                }
                if(department.getText().toString().isEmpty()){
                    department.setError("Fill it");
                    return ;
                }
                if(batch.getText().toString().isEmpty()){
                    batch.setError("Fill it");
                    return ;
                }

                StudentItem si=new StudentItem(id.getText().toString(),name.getText().toString(),batch.getText().toString(),department.getText().toString(),facedata);
                dr.push().setValue(si).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                        }else{
                            Toast.makeText(FrontImageUpload.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK) {
            pd.dismiss();
            return;
        }
        if(requestCode!=101)return;
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());



                iv.setImageBitmap(bitmap);
        FaceDetectorOptions f =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .build();
        try {
            InputImage image = InputImage.fromBitmap(bitmap,0);
            //can detect face but cann't recognige;
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
                            //Facerecgniger class made by firoz can genarate float array.....
                            FaceRecogniger fd = new FaceRecogniger(FrontImageUpload.this.getAssets());
                            facedata = fd.getimagedata(bit);
                            //setting into bit croped image by Facedetection rect
                            bit.compress(Bitmap.CompressFormat.JPEG,100,bos);
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