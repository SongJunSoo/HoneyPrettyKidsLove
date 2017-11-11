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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class KidsRegActivity extends AppCompatActivity {

    public static final String TAG = "KidsRegActivity";

    File file = null;
    ImageView imageView;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic;
    TextView textView;
    GestureDetector detector;
    Button reg;
    RadioGroup choiceWay;
    int selectedValueId;
    RadioButton oneWay;


    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    private static final int REQUEST_IMAGE_CAMERA = 11;
    private static final int REQUEST_IMAGE_ALBUM = 12;
    private static final int CROP_FROM_CAMERA = 13;

    private static String device_token = "";
    private static String mCurrentPhotoPath = "";
    private static String image_url = "";
    private static String hpkl_id = "";
    private static int int_hpkl_id = 0;

    boolean isPhotoCaptured;
    boolean isPhotoFileSaved;
    int mSelectdContentArray;
    int mChoicedArrayItem;
    boolean isPhotoCanceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_reg);

        imageView = (ImageView)findViewById(R.id.imageViewTest);
        roundedImageViewPic = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic);
//        textView = (TextView) findViewById(R.id.textView);
        reg = (Button)findViewById(R.id.reg);

        // 카메라 관련 기능 사용
        try {
            file = createFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        reg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //OnCLick Stuff
                TextView name = (TextView)findViewById(R.id.name);
                TextView birth = (TextView)findViewById(R.id.birth);

                if (TextUtils.isEmpty(name.getText())) {

                    Toast.makeText(KidsRegActivity.this,
                            "아이 이름을 등록하세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(birth.getText())) {

                    Toast.makeText(KidsRegActivity.this,
                            "생년월일을 등록하세요.",
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
            "http://172.16.2.11:52274/user/picture",   // 회사
            //"http://172.16.2.11:52274/user/picture", // 집
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
        try {
            device_token = FirebaseInstanceId.getInstance().getToken();
            Log.i("device_token", device_token);
            new gaipCheck().execute(
                    "http://172.16.2.11:52274/parent", device_token); // 회사
                    //"http://192.168.0.25:52274/parent", device_token);  // 집
        } catch (Exception e) {
            e.printStackTrace();
        }


        //외장 메모리 접근 권한 요청
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
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
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
                    imageView.setImageBitmap(bitmap);
                    //roundedImageViewPic.setVisibility(View.GONE);
                    roundedImageViewPic.setVisibility(View.VISIBLE);
                    Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
                    Uri mImageCaptureUri = data.getData();
//                    new ImageUpload().execute(
//                            "http://172.16.2.11:52274/user/picture",   // 회사
//                            //"http://172.16.2.11:52274/user/picture", // 집
//                            mCurrentPhotoPath, "DESCRIPTION");
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                Log.i("resultCode", resultCode+"");
                if (resultCode == RESULT_OK)
                {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    if (file != null) {
                        //roundedImageViewPic.setVisibility(View.GONE);
                        roundedImageViewPic.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        imageView.setImageBitmap(bitmap);

                        mCurrentPhotoPath = file.getAbsolutePath();
                        Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
//                        new ImageUpload().execute(
//                                "http://172.16.2.11:52274/user/picture",   // 회사
//                                //"http://172.16.2.11:52274/user/picture", // 집
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

    class goChildGaip extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(KidsRegActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {

                URL url = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("hpkl_id", params[1]);
                postDataParams.put("name", params[2]);
                postDataParams.put("birth", params[3]);
                postDataParams.put("gender", params[4]);
                postDataParams.put("picture_url", params[5]);


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
            dialog.setMessage("로그인 중...");
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
                    Toast.makeText(KidsRegActivity.this,
                            "아이 정보 저장 성공",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(KidsRegActivity.this,
                            ListViewActivity.class);

                    startActivity(intent);

                } else {
                    Toast.makeText(KidsRegActivity.this,
                            "아이 정보 저장 실패",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    class ImageUpload extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(KidsRegActivity.this);
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
            dialog.setMessage("이미지 업로드 중...");
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
                    Toast.makeText(KidsRegActivity.this,
                            "이미지 서버 저장 성공",
                            Toast.LENGTH_SHORT).show();
                    TextView name = (TextView)findViewById(R.id.name);
                    TextView birth = (TextView)findViewById(R.id.birth);

                    String gender = "";
                    //라디오 그룹
                    choiceWay = (RadioGroup) findViewById(R.id.radioGroup1);
                    //라디오버튼
                    oneWay = (RadioButton) findViewById(R.id.radio_man);

                    selectedValueId = choiceWay.getCheckedRadioButtonId();
                    //checking the id of the selected radio
                    if(selectedValueId == oneWay.getId()) {
                        gender = "M";
                    }else{
                        gender = "W";
                    }

//                    TextView gender = (TextView)findViewById(R.id.gender);
                    image_url = "http://172.16.2.11:52274/upload_image/"+json.getString("url").toString();
                    Log.i("name",name.getText().toString());
                    Log.i("birth",birth.getText().toString());
  //                  Log.i("gender",gender.getText().toString());
                    Log.i("url", image_url);
                    Log.i("hpkl_id", Integer.toString(int_hpkl_id));
                    Log.i("gender", gender);
                    new goChildGaip().execute(
                            "http://172.16.2.11:52274/child",   // 회사
                            //"http://172.16.2.11:52274/child", // 집
                            Integer.toString(int_hpkl_id), name.getText().toString(), birth.getText().toString(), gender, image_url);

                } else {//가입 안되어 있음
                    Toast.makeText(KidsRegActivity.this,
                            "이미지 서버 저장 실패",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    class gaipCheck extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(KidsRegActivity.this);
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
            dialog.setMessage("가입여부 체크 중...");
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
                    Toast.makeText(KidsRegActivity.this,
                            "가입 되어 있음",
                            Toast.LENGTH_SHORT).show();

                    //Log.i("hpkl_id SJS", hpkl_id);
                    Log.i("int_hpkl_id SJS", Integer.toString(int_hpkl_id));

                } else {//가입 안되어 있음
                    Toast.makeText(KidsRegActivity.this,
                            "가입 안되어 있음",
                            Toast.LENGTH_SHORT).show();
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
}
