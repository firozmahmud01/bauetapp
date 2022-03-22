package com.firoz.mahmud.bauet.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.firoz.mahmud.bauet.MKeys;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LoginApi {
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;
    public LoginApi(Context con){
        sp=con.getSharedPreferences(MKeys.sp.database,Context.MODE_PRIVATE);
        spe=sp.edit();
        client=new OkHttpClient();
    }
    private String POST(String url,String json) throws Exception{
        RequestBody body = RequestBody.create(JSON, json);
        Request r=new Request.Builder().url(url).post(body).build();
        return client.newCall(r).execute().body().string();
    }
    public void login(String user,String pass) throws Exception {
        JSONObject ob=new JSONObject();
        ob.put("user",user);
        ob.put("pass",pass);
        String res=POST(AllUrl.login,ob.toString());
        ob=new JSONObject(res);
        if(ob.has("err")){
            throw new Exception(ob.getString("err"));
        }else if(ob.has("token")){
            spe.putString(MKeys.sp.mainauthtoken,ob.getString("token"));
            spe.commit();

        }
    }


    public void signupstudent(String name,String email,String pass,String id,String regid,
                              String phone,String batch,String department,String session,
                              String program,String hallname,String roomno) throws Exception{
        JSONObject ob=new JSONObject();
        ob.put("name",name);
        ob.put("email",email);
        ob.put("pass",pass);
        ob.put("id",id);
        ob.put("regid",regid);
        ob.put("phone",phone);
        ob.put("batch",batch);
        ob.put("department",department);
        ob.put("session",session);
        ob.put("program",program);
        ob.put("hallname",hallname);
        ob.put("roomno",roomno);
        String res=POST(AllUrl.signupstudent,ob.toString());
        ob=new JSONObject(res);
        if(ob.has("err")){
            throw new Exception(ob.getString("err"));
        }else{
            spe.putString(MKeys.sp.occupation,"student");
            spe.putString(MKeys.sp.tmptoken,ob.getString("token"));
            spe.commit();
            setPosition("1st");
        }

    }

    public void signupteacher(String name,String email,String pass,String degignation,String phone,String department) throws Exception{
        JSONObject ob=new JSONObject();
        ob.put("name",name);
        ob.put("email",email);
        ob.put("pass",pass);
        ob.put("degignation",degignation);
        ob.put("phone",phone);
        ob.put("department",department);
        String res=POST(AllUrl.signupteacher,ob.toString());
        ob=new JSONObject(res);
        if(ob.has("err")){
            throw new Exception(ob.getString("err"));
        }else{
            spe.putString(MKeys.sp.occupation,"teacher");
            spe.putString(MKeys.sp.tmptoken,ob.getString("token"));
            spe.commit();
            setPosition("1st");
        }

    }
    public void verifycode(String code) throws Exception{
        JSONObject ob=new JSONObject();
        ob.put("token",true);
        ob.put("job",sp.getString(MKeys.sp.occupation,""));
        ob.put("code",code);
        String res=POST(AllUrl.verifycode,ob.toString());
        ob=new JSONObject(res);
        if(ob.has("err")){
            throw new Exception(ob.getString("err"));
        }else{
            setPosition("2nd");
        }

    }
    public void uploadFace(String image,float facecode[])throws Exception{

    }
    public String getToken(boolean tmp){
        return tmp?sp.getString(MKeys.sp.tmptoken,""):sp.getString(MKeys.sp.mainauthtoken,"");
    }
    public void setPosition(String position){
        spe.putString(MKeys.sp.currentpos,position);
        spe.commit();
    }
    public String getPosition(){
        return sp.getString(MKeys.sp.currentpos,"");
    }


}
