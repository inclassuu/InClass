/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;  
      
    import java.io.BufferedReader;  
import java.io.DataOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  
import java.util.UUID;  

import android.util.Log;


      
      
    public class HttpAssist {  
        private static final String TAG = "uploadFile";  
        private static final int TIME_OUT = 10 * 10000000; // ������������  
        private static final String CHARSET = "utf-8"; // ������������  
        public static final String SUCCESS = "1";  
        public static final String FAILURE = "0";  
        static String uid;
        
         static String RequestURL="" ;
        
        
        public void uploadphoto()
        {
        	RequestURL = "http://senior-07.eng.utah.edu:8080/UploadPhoto/UploadPhoto1";
        }
        
        public void uploadument()
        {
        	RequestURL = "http://senior-07.eng.utah.edu:8080/UploadFile/Uploadfile1";
        }
      
        public static String uploadFile(File file) {  
            String BOUNDARY = UUID.randomUUID().toString(); // ������������ ������������  
            String PREFIX = "--", LINE_END = "\r\n";  
            String CONTENT_TYPE = "multipart/form-data"; // ������������  
            //String RequestURL = "http://senior-07.eng.utah.edu:8080/UploadPhoto/UploadPhoto1";
            //String RequestURL = "http://senior-07.eng.utah.edu:8080/UploadFile/Uploadfile1";
           // String RequestURL="http://senior-07.eng.utah.edu/Ument/";
            try {  
                URL url = new URL(RequestURL);  
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                conn.setReadTimeout(TIME_OUT);  
                conn.setConnectTimeout(TIME_OUT);  
                conn.setDoInput(true); // ���������������  
                conn.setDoOutput(true); // ���������������  
                conn.setUseCaches(false); // ���������������������  
                conn.setRequestMethod("POST"); // ������������  
                conn.setRequestProperty("Charset", CHARSET); // ������������  
                conn.setRequestProperty("connection", "keep-alive");  
                conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="  
                        + BOUNDARY);  
                if (file != null) {  
                    /** 
                     * ������������������������������������������������ 
                     */  
                    OutputStream outputSteam = conn.getOutputStream();  
      
                    DataOutputStream dos = new DataOutputStream(outputSteam);  
                    StringBuffer sb = new StringBuffer();  
                    sb.append(PREFIX);  
                    sb.append(BOUNDARY);  
                    sb.append(LINE_END);  
                    /** 
                     * ��������������������� name���������������������������������key ������������key ������������������������������ 
                     * filename��������������������������������������� ������:abc.png 
                     */  
                    
                    String exten=getExtensionName(file.getName());
                    
                    Log.e("exten", exten);
      
                    sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""  
                            + uid+ "\"" + LINE_END);  
                    sb.append("Content-Type: application/octet-stream; charset="  
                            + CHARSET + LINE_END);  
                    sb.append(LINE_END);  
                    dos.write(sb.toString().getBytes());  
                    InputStream is = new FileInputStream(file);  
                    byte[] bytes = new byte[1024];  
                    int len = 0;  
                    while ((len = is.read(bytes)) != -1) {  
                        dos.write(bytes, 0, len);  
                    }  
                    is.close();  
                    dos.write(LINE_END.getBytes());  
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)  
                            .getBytes();  
                    dos.write(end_data);  
                    dos.flush();  
                    /** 
                     * ��������������� 200=������ ������������������������������������ 
                     */  
                    int res = conn.getResponseCode();  
                    if (res == 200) {  
                    	Log.i("success","iiii");
                        return SUCCESS;  
                        
                    }  
                }  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
                
                
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            
            Log.i("faill","faill");
            return FAILURE;  
        }  
        
        public static String getExtensionName(String filename1) { 
        	//String ext="";
        	String filename=filename1;
            if ((filename != null) && (filename.length() > 0)) {   
                int dot = filename.lastIndexOf('.');   
                if ((dot >-1) && (dot < (filename.length() - 1))) {   
                    return filename.substring(dot + 1);   
                }   
            }
            filename = filename1.replaceAll(filename ,"");
            return filename; 
        }
        
        public void  setid(String uid1)
        {
        uid=uid1;
        }
        
    }  