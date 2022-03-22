package com.firoz.mahmud.bauet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Attandace extends Fragment {
    public Attandace(){

    }
    private Home home;

    public Attandace(Home home){
    this.home=home;
    }
    ImageView iv;
    Button next;
    File file;
    TextView tv;
    ProgressDialog pd;
    ArrayList<Bitmap> faces;
    DatabaseReference dr= FirebaseDatabase.getInstance().getReference("Students");
    Thread th=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_attandace, container, false);
        next=view.findViewById(R.id.attandace_next_button);
        pd=new ProgressDialog(home);
        tv=view.findViewById(R.id.attandance_student_number_textview);
        tv.setText("");

        faces=new ArrayList<>();
        file=new File(home.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"image.jpg");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faces.size()<=0){
                    Toast.makeText(home, "Please capture an image first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(th==null){
                    th=new Thread(){
                        @Override
                        public void run() {
                            ArrayList<float[]> b=new ArrayList<>();




                        }
                    };
                    th.start();
                }



            }
        });
        view.findViewById(R.id.attandance_capture_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(home,"com.firoz.mahmud.bauet",file);
                in.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                pd.show();
                home.startActivityForResult(Intent.createChooser(in,"Take photo via:"),101);
            }
        });



        return view;
    }

    public void onActivity(int resultCode,int requestCode,Intent data) {

        if(resultCode!= Activity.RESULT_OK) {
            pd.dismiss();
            return;
        }
        if(requestCode!=101)return;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
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
                    if(task.isSuccessful()) {
                        List<Face> list = task.getResult();
                        if (list.size() <= 0) {
                            Toast.makeText(home, "No face found.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        next.setVisibility(View.VISIBLE);
                        faces=new ArrayList<>();
                        for(Face face:list){
                            Bitmap bit=FaceRecogniger.cropbit(bitmap,face.getBoundingBox());
                            faces.add(bit);
                        }


                        pd.dismiss();
                    }
                }

            });
        } catch (Exception e) {
        }

    }
}