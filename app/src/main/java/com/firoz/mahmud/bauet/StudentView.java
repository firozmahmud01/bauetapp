package com.firoz.mahmud.bauet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.renderscript.Sampler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;

public class StudentView extends AppCompatActivity {
    TextView name,id,batch,department;
    DatabaseReference dr,data;
    ArrayList<ReportItem> list;
    String reports="";
    BaseAdapter ba;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        setTitle("Office of the Proctor,BAUET");
        data=FirebaseDatabase.getInstance().getReference("Reports")
                .child(getIntent().getStringExtra("id"));
        name=findViewById(R.id.student_view_name_textview);
        id=findViewById(R.id.student_view_id_textview);
        batch=findViewById(R.id.student_view_batch_textview);
        department=findViewById(R.id.student_view_department_textview);



        name.setText(getIntent().getStringExtra("name"));
        id.setText(getIntent().getStringExtra("id"));
        batch.setText(getIntent().getStringExtra("batch"));
        department.setText(getIntent().getStringExtra("department"));


        ListView lv=findViewById(R.id.student_view_reportlist_listview);
        list=new ArrayList<>();

        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }
            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                ViewGroup vg=lv;
                int limit=ba.getCount();
                int height=0;
                height+=lv.getDividerHeight()*limit;
                for(int x=0;x<limit;x++) {
                    View v= ba.getView(x,null,vg);
                    v.measure(0,0);
                    height+=v.getMeasuredHeight();
                }
                ViewGroup.LayoutParams lp = lv.getLayoutParams();
                lp.height = height;
                lv.setLayoutParams(lp);
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
                    v=getLayoutInflater().inflate(R.layout.listitem,null);
                }
                TextView date,details;
                ImageView tv=v.findViewById(R.id.list_item_edit_imageview);
                date=v.findViewById(R.id.list_item_date_textview);
                details=v.findViewById(R.id.list_item_description_textview);
                date.setText(list.get(i).getDate());
                details.setText(list.get(i).getDetails());
                tv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent me) {
                        if(me.getAction()==MotionEvent.ACTION_DOWN){
                            view.setAlpha(0.5f);
                        }else if(me.getAction()==MotionEvent.ACTION_UP){
                            view.setAlpha(1f);
                            ReportItem ri=list.get(i);
                            View v=getLayoutInflater().inflate(R.layout.reportdialog,null);
                            EditText r=v.findViewById(R.id.alertdialog_message_edittext);
                            r.setText(ri.getDetails());
                            new AlertDialog.Builder(StudentView.this).setView(v).setMessage("Edit report")
                                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface di, int i) {
                                            if(r.getText().toString().isEmpty()){
                                                r.setError("Fill it before add");
                                                return;
                                            }
                                            ri.setDetails(r.getText().toString());
                                            data.child(ri.getId()).setValue(ri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(StudentView.this, "Edited", Toast.LENGTH_SHORT).show();
                                                        di.dismiss();
                                                    }else{
                                                        Toast.makeText(StudentView.this, task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    di.dismiss();
                                }
                            }).show();
                        }
                        return true;
                    }
                });

                return v;
            }
        };
        lv.setAdapter(ba);

        data.keepSynced(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                list=new ArrayList<>();
                reports="[";
                for(DataSnapshot d:ds.getChildren()){
                    ReportItem ri=d.getValue(ReportItem.class);
                    list.add(ri);
                    reports+="\n{\n\tDate:"+ri.getDate()+"\n"+"\tDetails:"+ri.getDetails()+"\n}";
                }

                reports+="\n]";

                ba.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.student_view_add_report_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v=getLayoutInflater().inflate(R.layout.reportdialog,null);
                new AlertDialog.Builder(StudentView.this).setView(v).setMessage("What is the report?")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {
                                EditText r=v.findViewById(R.id.alertdialog_message_edittext);
                                if(r.getText().toString().isEmpty()){
                                    r.setError("Fill it before add");
                                    return;
                                }
                                Date da=new Date();
                                ReportItem ri=new ReportItem(r.getText().toString(),""+System.currentTimeMillis(),
                                        DateFormat.format("dd/MM/yy hh:mm a",da).toString());
                                data.child(ri.getId()).setValue(ri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(StudentView.this, "Added", Toast.LENGTH_SHORT).show();
                                            di.dismiss();
                                        }else{
                                            Toast.makeText(StudentView.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int i) {
                        di.dismiss();
                    }
                }).show();


            }
        });



    }
}