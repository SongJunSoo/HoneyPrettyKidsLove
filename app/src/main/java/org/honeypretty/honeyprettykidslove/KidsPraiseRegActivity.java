package org.honeypretty.honeyprettykidslove;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import static org.honeypretty.honeyprettykidslove.KidsRegActivity.rotateBitmap;


public class KidsPraiseRegActivity extends AppCompatActivity {

    public static final String TAG = "KidsPraiseRegActivity";
    long mNow;
    Date mDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");


    File file = null;
    ImageView imageView;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic;
    TextView textView;
    TextView sticker_name;
    GestureDetector detector;
    Button reg;
    TextView mEtPrice;


    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    private static final int REQUEST_IMAGE_CAMERA = 11;
    private static final int REQUEST_IMAGE_ALBUM = 12;
    private static final int CROP_FROM_CAMERA = 13;

    private static String device_token = "";
    private static String mCurrentPhotoPath = "";
    private static String image_url = "";
    private static String hpkl_id = "";
    private static int int_hpkl_id = 0;

    private static String str_hpkl_id = "";
    private static String str_hpkl_child_id = "";
    private static String str_praise_sticker_name = "";

    boolean isPhotoCaptured;
    boolean isPhotoFileSaved;
    int mSelectdContentArray;
    int mChoicedArrayItem;
    boolean isPhotoCanceled;

    Intent intent;
    Spinner spinner;

    /**
     * 스피너를 위한 아이템 정의
     */
    String[] items = {"적립할 러브를 선택하세요", "500 러브 적립", "1,000 러브 적립", "1,500 러브 적립", "2,000 러브 적립", "2,500 러브 적립", "3,000 러브 적립"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_praise_reg);

        intent = getIntent();

        str_hpkl_id = intent.getStringExtra("parent_hpkl_id");
        str_hpkl_child_id = intent.getStringExtra("child_hpkl_id");
        str_praise_sticker_name = intent.getStringExtra("praise_sticker_name");

//        Toast.makeText(getApplicationContext(), "str_hpkl_id : " + str_hpkl_id + ", str_hpkl_child_id =" + str_hpkl_child_id + ", str_praise_sticker_name =" + str_praise_sticker_name, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "getDate() : " + getDate() + ", getTime() =" + getTime(), Toast.LENGTH_LONG).show();


        imageView = (ImageView)findViewById(R.id.imageViewTest);
        roundedImageViewPic = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic);
//        textView = (TextView) findViewById(R.id.textView);
        reg = (Button)findViewById(R.id.reg);
        sticker_name = (TextView)findViewById(R.id.sticker_name);
        spinner = (Spinner)findViewById(R.id.spinner);

        sticker_name.setText("칭찬스티커 : " + str_praise_sticker_name);
        // 어댑터 객체 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.custom_simple_dropdown_item_1line, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 어댑터 설정
        spinner.setAdapter(adapter);

        // 아이템 선택 이벤트 처리
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 아이템이 선택되었을 때 호출됨
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(KidsPraiseRegActivity.this,"선택된 아이템 : "+items[position],Toast.LENGTH_SHORT).show();
            }

            // 아무것도 선택되지 않았을 때 호출됨
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textView.setText("");
            }
        });

        // 카메라 관련 기능 사용
        try {
            file = createFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        reg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //OnCLick Stuff
                TextView memo = (TextView)findViewById(R.id.memo);
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                String saving_love = spinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(memo.getText())) {

                    Toast.makeText(KidsPraiseRegActivity.this,
                            "메모 내용을 등록하세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                if (saving_love.equals("적립할 러브를 선택하세요")) {

                    Toast.makeText(KidsPraiseRegActivity.this,
                            "적립할 러브를 선택하세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


//                if (TextUtils.isEmpty(image_url)) {
//
//                    Toast.makeText(KidsRegActiviaty.this,
//                            "이미지경로가 없습니다. 이미지를 다시 선택하세요",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }


                new ImageUpload().execute(
                        BasicInfo.restFulServer+"/user/picture",   // 회사
                        //BasicInfo.restFulServer+"/user/picture", // 집
                        mCurrentPhotoPath, "DESCRIPTION");

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isPhotoCaptured || isPhotoFileSaved) {
                    showDialog(BasicInfo.CONTENT_PHOTO_EX);
                } else {
                    showDialog(BasicInfo.CONTENT_PHOTO);
                }
            }
        });


        roundedImageViewPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isPhotoCaptured || isPhotoFileSaved) {
                    showDialog(BasicInfo.CONTENT_PHOTO_EX);
                } else {
                    showDialog(BasicInfo.CONTENT_PHOTO);
                }
            }
        });

        // 잠만보 이미지 터치시에 터치 이벤트 감지
//        roundedImageViewPic.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = motionEvent.getAction();
//
//                float curX = motionEvent.getX();
//                float curY = motionEvent.getY();
//
//                if (action == MotionEvent.ACTION_DOWN) {
//                    //onButton1Click();
//                    //println("손가락 눌림 : " + curX + ", " + curY);
//                } else if (action == MotionEvent.ACTION_MOVE) {
//                    //println("손가락 움직임 : " + curX + ", " + curY);
//                } else if (action == MotionEvent.ACTION_UP) {
//                    //println("손가락 뗌 : " + curX + ", " + curY);
//                }
//
//                return true;
//            }
//        });

        //교재 p707~p713 Firebase 설정 적용
        //Registration ID
        //아이정보 등록을 위해 토큰 값으로 부모 정보를 읽어 온다.
//        try {
//            device_token = FirebaseInstanceId.getInstance().getToken();
//            Log.i("device_token", device_token);
//            new gaipCheck().execute(
//                    BasicInfo.restFulServer+"/parent", device_token); // 회사
//            //BasicInfo.restFulServer+"/parent", device_token);  // 집
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //외장 메모리 접근 권한 요청
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    private String getDate(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return dateFormat.format(mDate);
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return timeFormat.format(mDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_ALBUM:
                Log.i("resultCode", resultCode+"");
                if (resultCode == RESULT_OK)
                {
                    mCurrentPhotoPath = getPathFromUri(data.getData());

                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(mCurrentPhotoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);



                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;

                    Bitmap src = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
                    Bitmap bitmap = Bitmap.createScaledBitmap( src, src.getWidth(), src.getHeight(), true );

                    bitmap = rotateBitmap(bitmap, orientation);
                    Bitmap bitmap2 = createThumbnail(bitmap, "test.jpg", "1", "1");

                    imageView.setImageBitmap(bitmap2);
                    File storageDir = Environment.getExternalStorageDirectory();
                    mCurrentPhotoPath = storageDir.getAbsolutePath() + "/hpkl_test.jpg";

                    roundedImageViewPic.setVisibility(View.VISIBLE);
                    Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
                    Uri mImageCaptureUri = data.getData();
//                    new ImageUpload().execute(
//                            BasicInfo.restFulServer+"/user/picture",   // 회사
//                            //BasicInfo.restFulServer+"/user/picture", // 집
//                            mCurrentPhotoPath, "DESCRIPTION");
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                Log.i("resultCode", resultCode+"");
                if (resultCode == RESULT_OK)
                {
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    if (file != null) {
                        //roundedImageViewPic.setVisibility(View.GONE);
                        roundedImageViewPic.setVisibility(View.VISIBLE);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;

                        Bitmap src = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        Bitmap bitmap = Bitmap.createScaledBitmap( src, src.getWidth(), src.getHeight(), true );

                        bitmap = rotateBitmap(bitmap, orientation);
                        Bitmap bitmap2 = createThumbnail(bitmap, "test.jpg", "1", "1");

                        imageView.setImageBitmap(bitmap2);
                        File storageDir = Environment.getExternalStorageDirectory();
                        mCurrentPhotoPath = storageDir.getAbsolutePath() + "/hpkl_test.jpg";

                        Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
//                        new ImageUpload().execute(
//                                BasicInfo.restFulServer+"/user/picture",   // 회사
//                                //BasicInfo.restFulServer+"/user/picture", // 집
//                                mCurrentPhotoPath, "DESCRIPTION");
                    } else {
                        Toast.makeText(getApplicationContext(), "File is null.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return path;
    }

    public void showPhotoCaptureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

//    public void onButton1Clicked(View v) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

    private File createFile() throws IOException {
        String imageFileName = "test.jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File curFile = new File(storageDir, imageFileName);

        return curFile;
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

    class goChildPraiseReg extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(KidsPraiseRegActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {

                URL url = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("hpkl_id", params[1]);
                postDataParams.put("hpkl_child_id", params[2]);
                postDataParams.put("hpkl_date", params[3]);
                postDataParams.put("hpkl_time", params[4]);
                postDataParams.put("hpkl_saving_love", params[5]);
                postDataParams.put("hpkl_praise_memo", params[6]);
                postDataParams.put("hpkl_praise_picture_url", params[7]);
                postDataParams.put("hpkl_sticker_name", params[8]);


                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true); conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) { e.printStackTrace(); }
            return output.toString();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("처리중 입니다.");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.i("result", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {

                    str_hpkl_id = json.getString("hpkl_id");
                    str_hpkl_child_id = json.getString("hpkl_child_id");

//                    Toast.makeText(KidsPraiseRegActivity.this,
//                            "칭찬 정보 저장 성공",
//                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(KidsPraiseRegActivity.this,
                            ChildViewListActivity.class);

                    intent.putExtra("parent_hpkl_id", str_hpkl_id);
                    intent.putExtra("child_hpkl_id", str_hpkl_child_id);
                    Log.i("SJS qqq parent_hpkl_id", str_hpkl_id);
                    Log.i("SJS qqq child_hpkl_id", str_hpkl_child_id);

                    startActivity(intent);


                } else {
//                    Toast.makeText(KidsPraiseRegActivity.this,
//                            "칭찬 정보 저장 실패",
//                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    class ImageUpload extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(KidsPraiseRegActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            DataOutputStream dos = null;
            ByteArrayInputStream bis = null;
            ByteArrayInputStream bis2 = null;
            InputStream is = null;

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            try{
                URL url = new URL(params[0]);
                FileInputStream fstrm = new FileInputStream(params[1]);
                String filename = new File(params[1]).getName();
                String description = params[2];

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true); conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                // write data
                dos = new DataOutputStream(conn.getOutputStream()) ;

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"description\"");
                dos.writeBytes(lineEnd + lineEnd + description + lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition:form-data;name=\"image\";filename=\"" + filename + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                int bytesAvailable = fstrm.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fstrm.read( buffer , 0 , bufferSize);
                Log.e("File Up", "text byte is " + bytesRead );
                while(bytesRead > 0 ){
                    dos.write(buffer , 0 , bufferSize);
                    bytesAvailable = fstrm.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fstrm.read(buffer,0,bufferSize);
                }

                fstrm.close();

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                Log.e("File Up" , "File is written");
                dos.flush();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line = null;
                while(true) {
                    line = reader.readLine();
                    if (line == null) break;
                    output.append(line);
                }
                reader.close();
                conn.disconnect();

            } catch(Exception e) {
                e.printStackTrace();
            }
            return output.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("처리중 입니다.");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.i("result json", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {// 가입 되어 있음
//                    Toast.makeText(KidsPraiseRegActivity.this,
//                            "이미지 서버 저장 성공",
//                            Toast.LENGTH_SHORT).show();
                    TextView memo = (TextView)findViewById(R.id.memo);
                    Spinner spinner = (Spinner)findViewById(R.id.spinner);
                    String saving_love = spinner.getSelectedItem().toString();
                    //image_url = BasicInfo.restFulServer+"/upload_image/"+json.getString("url").toString();
                    image_url = json.getString("url").toString();

                    if(saving_love.equals("500 러브 적립")) {
                        saving_love = "500";
                    }else if(saving_love.equals("1,000 러브 적립")) {
                        saving_love = "1000";
                    }else if(saving_love.equals("1,500 러브 적립")) {
                        saving_love = "1500";
                    }else if(saving_love.equals("2,000 러브 적립")) {
                        saving_love = "2000";
                    }else if(saving_love.equals("2,500 러브 적립")) {
                        saving_love = "2500";
                    }else if(saving_love.equals("3,000 러브 적립")) {
                        saving_love = "3000";
                    }

//                    Log.i("KKK name",memo.getText().toString());
//                    Log.i("KKK url", image_url);
//                    Log.i("KKK str_hpkl_id", str_hpkl_id);
//                    Log.i("KKK str_hpkl_child_id", str_hpkl_child_id);
//                    Log.i("str_praise_sticker_name", str_praise_sticker_name);

                    new goChildPraiseReg().execute(
                            BasicInfo.restFulServer+"/child/praise",   // 회사
                            //BasicInfo.restFulServer+"/child", // 집
                            str_hpkl_id, str_hpkl_child_id, getDate(), getTime(), saving_love, memo.getText().toString(), image_url, str_praise_sticker_name );

                } else {//가입 안되어 있음
                    Toast.makeText(KidsPraiseRegActivity.this,
                            "이미지 서버 저장 실패",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    class gaipCheck extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(KidsPraiseRegActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                // GET방식
                URL url = new URL(params[0]+"?device_token="+params[1]);

                // POST방식
                //URL url = new URL(params[0]);
                //JSONObject postDataParams = new JSONObject();
                //postDataParams.put("device_token", params[1]);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);

                    // GET방식
                    conn.setRequestMethod("GET");

                    // POST 방식
                    //conn.setRequestMethod("GET");
                    /*conn.setDoInput(true); conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();*/
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) { e.printStackTrace(); }
            return output.toString();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("처리중 입니다.");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.i("result", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {// 가입 되어 있음

                    //hpkl_id = json.getString("hpkl_id");
                    int_hpkl_id = json.getInt("hpkl_id");
//                    Toast.makeText(KidsPraiseRegActivity.this,
//                            "가입 되어 있음",
//                            Toast.LENGTH_SHORT).show();

                    //Log.i("hpkl_id SJS", hpkl_id);
                    Log.i("int_hpkl_id SJS", Integer.toString(int_hpkl_id));

                } else {//가입 안되어 있음
//                    Toast.makeText(KidsPraiseRegActivity.this,
//                            "가입 안되어 있음",
//                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = null;

        switch(id) {
            case BasicInfo.CONFIRM_TEXT_INPUT:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("메모");
                builder.setMessage("텍스트를 입력하세요.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                break;

            case BasicInfo.CONTENT_PHOTO:
                builder = new AlertDialog.Builder(this);

                mSelectdContentArray = R.array.array_photo;
                builder.setTitle("선택하세요");
                builder.setSingleChoiceItems(mSelectdContentArray, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mChoicedArrayItem = whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(mChoicedArrayItem == 0 ) {
                            showPhotoCaptureActivity(); // 사진찍기
                        } else if(mChoicedArrayItem == 1) {
                            showPhotoSelectionActivity();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.d(TAG, "whichButton3        ======        " + whichButton);
                    }
                });

                break;

            case BasicInfo.CONTENT_PHOTO_EX:
                builder = new AlertDialog.Builder(this);

                mSelectdContentArray = R.array.array_photo_ex;
                builder.setTitle("선택하세요");
                builder.setSingleChoiceItems(mSelectdContentArray, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mChoicedArrayItem = whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(mChoicedArrayItem == 0) {
                            showPhotoCaptureActivity();
                        } else if(mChoicedArrayItem == 1) {
                            showPhotoSelectionActivity();
                        } else if(mChoicedArrayItem == 2) {
                            isPhotoCanceled = true;
                            isPhotoCaptured = false;

                            //mPhoto.setImageResource(R.drawable.person_add);
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                break;

            default:
                break;
        }

        return builder.create();
    }

    public void showPhotoSelectionActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_ALBUM);
    }

    // Bitmap to File
    //bitmap에는 비트맵, strFilePath에 는 파일을 저장할 경로, strFilePath 에는 파일 이름을 할당해주면 됩니다.
    public Bitmap createThumbnail(Bitmap bitmap, String filename, String height, String width) {

        File storageDir = Environment.getExternalStorageDirectory();
        File file = new File(storageDir.getAbsolutePath());
        Log.i("bitmap.storageDir " , storageDir.getAbsolutePath());
        Log.i("bitmap.filename " , filename);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            Log.i("bitmap.filename 000" , "000");
        }else{
            Log.i("bitmap.filename 001" , "001");
        }

        Log.i("bitmap.filename 000" , storageDir.getAbsolutePath() + "/hpkl_"+filename);
        File fileCacheItem = new File(storageDir.getAbsolutePath() + "/hpkl_"+filename);
        OutputStream out = null;

        Log.i("bitmap.filename 111" , "111");
        try {

            int height_get = Integer.parseInt(height);
            int width_get = Integer.parseInt(width);

            int height_imgget=bitmap.getHeight();
            int width_imgget=bitmap.getWidth();

            int height_final = 0;
            int width_final = 0;

            if(height.equals("1")) {
                height_final = height_imgget;
            }else {
                height_final = height_get;
            }

            if(width.equals("1")) {
                width_final = width_imgget;
            }else {
                width_final = width_get;
            }



            Log.i("bitmap height_get" , Integer.toString(height_get));
            Log.i("bitmap height_imgget" , Integer.toString(height_imgget));
            fileCacheItem.createNewFile();
            Log.i("bitmap width_get" , Integer.toString(width_get));
            Log.i("bitmap width_imgget" , Integer.toString(width_imgget));
            out = new FileOutputStream(fileCacheItem);
            Log.i("bitmap height_final" , Integer.toString(height_final));
            Log.i("bitmap width_final" , Integer.toString(width_final));
            Log.i("bitmap.filename 666" , "666");
            //160 부분을 자신이 원하는 크기로 변경할 수 있습니다.
            bitmap = Bitmap.createScaledBitmap(bitmap, width_final, height_final, true);
            //bitmap = Bitmap.createScaledBitmap(bitmap, 160, height/(width/160), true);
            //bitmap = Bitmap.createScaledBitmap(bitmap, 10, 20, true);
            bitmap.compress(CompressFormat.JPEG, 50, out);



            Log.i("bitmap.compress " , storageDir.getAbsolutePath() + "/hpkl_"+filename);

        } catch (Exception e) {
            Log.i("bitmap.filename 222" , "222");
            e.printStackTrace();
        } finally {
            try {
                Log.i("bitmap.filename 333" , "333");
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }
}
