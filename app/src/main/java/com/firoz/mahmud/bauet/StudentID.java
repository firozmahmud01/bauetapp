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
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentID extends Fragment {
    public StudentID(){

    }
    private Home home;

    public StudentID(Home home){
        this.home=home;
    }
    ImageView iv;
    Button next;
    File file;
    TextView tv;
    ProgressDialog pd;
    ListView studentview;
    ArrayList<Bitmap> faces;
    DatabaseReference dr;
    ArrayList<StudentItem> si;
    Thread th=null;
    BaseAdapter ba;
    ProgressDialog loading;
    Handler h;
    ArrayList<StudentItem>present;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_attandace, container, false);
        h=new Handler();
        try {
            FirebaseDatabase fb=FirebaseDatabase.getInstance();
            fb.setPersistenceEnabled(true);
            fb.setPersistenceCacheSizeBytes(1024*100);
        }catch (Exception e){}
        next=view.findViewById(R.id.attandace_next_button);
        pd=new ProgressDialog(home);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        loading=new ProgressDialog(home);
        loading.setMessage("Please wait.Loading...");
        loading.setCancelable(false);
        loading.show();
        si=new ArrayList<>();
        present=new ArrayList<>();
        studentview=view.findViewById(R.id.attandance_student_view_listview);
        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return present.size();
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View v, ViewGroup viewGroup) {
                if(v==null){
                    v=getLayoutInflater().inflate(R.layout.studentitem,null);
                }
                TextView name,id,batch,department;
                name=v.findViewById(R.id.studentitem_name_textview);
                id=v.findViewById(R.id.studentitem_student_id_textview);
                batch=v.findViewById(R.id.studentitem_batch_textview);
                department=v.findViewById(R.id.studentitem_department_textivew);
                name.setText(present.get(i).getName());
                id.setText(present.get(i).getId());
                batch.setText(present.get(i).getBatch());
                department.setText(present.get(i).getDepartment());

                return v;
            }
        };
        studentview.setAdapter(ba);

        dr=FirebaseDatabase.getInstance().getReference("Students");

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
                            present=new ArrayList<>();
                            for(Bitmap bi:faces) {
                                try {
                                    //Facerecgniger class made by firoz can genarate float array.....
                                    FaceRecogniger fd = new FaceRecogniger(home.getAssets());
                                    float by[] = fd.getimagedata(bi);
                                    StudentItem s=si.get(0);
                                    double dis=fd.caldis(by,FrontImageUpload.getFaceinf(s.getFace()));
                                    double min=dis;
                                    int ind=0;
                                    for(int i=1;i<si.size();i++){
                                        s=si.get(i);
                                        dis=fd.caldis(by,FrontImageUpload.getFaceinf(s.getFace()));
                                        if(dis<min){
                                            min=dis;
                                            ind=i;
                                        }
                                    }
                                    present.add(si.get(ind));

                                } catch (Exception e) {

                                }
                            }
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    ba.notifyDataSetChanged();
                                }
                            });


                        }
                    };
                    th.start();
                }



            }
        });
        view.findViewById(R.id.attandance_capture_image_button).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                home.startActivity(new Intent(home,FrontImageUpload.class));


                return true;
            }
        });
        view.findViewById(R.id.attandance_capture_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(home,"com.firoz.mahmud.bauet",file);
                in.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                pd.show();
                home.startActivityForResult(Intent.createChooser(in,"Take photo via:"),102);
            }
        });
        dr.keepSynced(true);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                si=new ArrayList<>();
                for(DataSnapshot d:ds.getChildren() ){
                    StudentItem s=d.getValue(StudentItem.class);
                    si.add(s);
                }
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    public void onActivity(int resultCode,int requestCode,Intent data) {

        if(resultCode!= Activity.RESULT_OK) {
            pd.dismiss();
            return;
        }
        if(requestCode!=102)return;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        iv.setImageBitmap(bitmap);
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