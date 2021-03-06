package com.example.pplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.entity.Nur;
import com.example.entity.NurAdapter;
import com.example.entity.Userdetails;
import com.example.zhouxu.customview.LineView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HealthWeekActivity extends AppCompatActivity {
    LineView chartView;
    List<String> xValues=new ArrayList<>();
    List<Float> yValues=new ArrayList<>();
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_week);


        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String a=postwebinfo();
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = a;
                    handler.sendMessage(msg);
                }
            }).start();
            chartView = findViewById(R.id.customView1);
            initData();
            chartView.setXValues(xValues);
            chartView.setYValues(yValues);
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(!msg.obj.toString().equals("empty"))
                    {
                        String[] spite1=msg.obj.toString().split(",");
                        xValues.clear();
                        yValues.clear();
                        for(int i=1;i<(int)spite1.length;i++)
                        {
                            xValues.add(String.valueOf(i));
                            yValues.add(Float.parseFloat(spite1[i]));
                            //final String ab=shop1.getShopName();
                            Toast.makeText(HealthWeekActivity.this,spite1[i],Toast.LENGTH_SHORT).show();
                        }

                        // xy轴集合自己添加数据
                        chartView.setXValues(xValues);
                        chartView.setYValues(yValues);
                    }
                }
            };
        }catch (Exception e){

        }





    }
    private void initData() {
        xValues.add("12.01");
        yValues.add(5f);
    }
    public static String inputStream2String (InputStream in) throws IOException {

        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        int n;
        while ((n = in.read(b)) != -1) {
            out.append(new String(b, 0, n));
        }
        Log.i("String的长度", new Integer(out.length()).toString());
        return out.toString();
    }

    public String postwebinfo(){
        String str="";String a = "";
        try {

            //1,找水源--创建URL
            URL url = new URL("http://62.234.119.213:8080/TomcatTest/GetWeekServlet");//放网站
            //URL url = new URL("http://192.168.91.1:8080/dyl/register");//放网站手机
            //URL url = new URL("http://"+MainActivity.URL_DOMAIN+":8080/dyl/buyticket");//放网站电脑
            //2,开水闸--openConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("POST");
            //设置超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            //设置允许输入
            conn.setDoInput(true);
            //设置允许输出
            conn.setDoOutput(true);
            //post方式不能设置缓存，需手动设置为false
            //子线程中显示Toast



            //我们请求的数据

            String data = "nickname="+ URLEncoder.encode(Userdetails.getname(),"UTF-8");
            //獲取輸出流
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            conn.connect();

            if (conn.getResponseCode() == 200 || 1 == 1) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                a = inputStream2String(is);
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }

                // 释放资源
                /*Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = a;
                handler.sendMessage(msg);
                Log.e("WangJ", message.toString());*/
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return a;
        }
    }



}
