package org.honeypretty.honeyprettykidslove;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class ListViewActivity extends AppCompatActivity {
    ListView listView = null;

    private static String device_token = "";
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_1;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_2;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_3;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_4;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_folding;
    RelativeLayout titlePan;

    private static String str_hpkl_id = "";
    private static String str_hpkl_child_id = "";

    private static int int_hpkl_id = 0;

    ImageView parentImage;

    GestureDetector detector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    class Item {
        String hpkl_id;
        String hpkl_child_id;
        String hpkl_date;
        String hpkl_time;
        String hpkl_saving_love;
        String hpkl_praise_memo;
        String hpkl_praise_picture_url;
        String hpkl_sticker_name;
        String hpkl_child_name;
        String hpkl_child_picture_url;
        String hpkl_sayong_gbn;

        Item(String hpkl_id, String hpkl_child_id, String hpkl_date, String hpkl_time, String hpkl_saving_love, String hpkl_praise_memo, String hpkl_praise_picture_url, String hpkl_sticker_name,
             String hpkl_child_name, String hpkl_child_picture_url, String hpkl_sayong_gbn) {
            this.hpkl_id = hpkl_id;
            this.hpkl_child_id = hpkl_child_id;
            this.hpkl_date = hpkl_date;
            this.hpkl_time = hpkl_time;
            this.hpkl_saving_love = hpkl_saving_love;
            this.hpkl_praise_memo = hpkl_praise_memo;
            this.hpkl_praise_picture_url = hpkl_praise_picture_url;
            this.hpkl_sticker_name = hpkl_sticker_name;
            this.hpkl_child_name = hpkl_child_name;
            this.hpkl_child_picture_url = hpkl_child_picture_url;
            this.hpkl_sayong_gbn = hpkl_sayong_gbn;

        }
    }

    class Item2 {
        String hpkl_id;
        String device_token;
        String name;
        String birth;
        String gender;
        String picture_url;

        Item2(String hpkl_id, String device_token, String name, String birth, String gender, String picture_url) {
            this.hpkl_id = hpkl_id;
            this.device_token = device_token;
            this.name = name;
            this.birth = birth;
            this.gender = gender;
            this.picture_url = picture_url;
        }
    }

    ArrayList<Item> itemList = new ArrayList<Item>();
    ArrayList<Item2> itemList2 = new ArrayList<Item2>();
    class BookAdapter extends ArrayAdapter {
        public BookAdapter(Context context) {
            super(context, R.layout.list_item, itemList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, null);
            } else view = convertView;

            com.github.siyamed.shapeimageview.RoundedImageView roundedImageView_childImage = (com.github.siyamed.shapeimageview.RoundedImageView)view.findViewById(R.id.roundedImageView_childImage);
            com.github.siyamed.shapeimageview.RoundedImageView roundedImageView_sticker = (com.github.siyamed.shapeimageview.RoundedImageView)view.findViewById(R.id.roundedImageView_sticker);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView datetime = (TextView)view.findViewById(R.id.datetime);
            TextView sticker_name = (TextView)view.findViewById(R.id.sticker_name);
            ImageView praise_image = (ImageView)view.findViewById(R.id.praise_image);
            TextView praise_memo = (TextView)view.findViewById(R.id.praise_memo);
            TextView money = (TextView)view.findViewById(R.id.money);
            TextView sayong_gbn = (TextView)view.findViewById(R.id.sayong_gbn);

            File storageDir = Environment.getExternalStorageDirectory();
            File file = new File(storageDir.getAbsolutePath()+"/hpkl_"+itemList.get(position).hpkl_child_picture_url);

            Log.i("SJS path", storageDir.getAbsolutePath()+"/hpkl_"+itemList.get(position).hpkl_child_picture_url);

            if (!file.exists()) {

                //Toast.makeText(getApplicationContext(), "hpkl_child_picture_url 기존에 사진없음", Toast.LENGTH_LONG).show();

                if (roundedImageView_childImage != null) {
                    new ImageDownloaderTask(roundedImageView_childImage).execute(BasicInfo.restFulServer+"/upload_image/"+itemList.get(position).hpkl_child_picture_url,itemList.get(position).hpkl_child_picture_url,"1","1");
                    //new ImageDownloaderTask(roundedImageView_childImage).execute(itemList.get(position).hpkl_child_picture_url);
                }

            }else{

                //Toast.makeText(getApplicationContext(), "hpkl_child_picture_url  기존사진 있음", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap src = BitmapFactory.decodeFile(storageDir.getAbsolutePath()+"/hpkl_"+itemList.get(position).hpkl_child_picture_url);
                Bitmap bitmap = Bitmap.createScaledBitmap( src, src.getWidth(), src.getHeight(), true );

                roundedImageView_childImage.setImageBitmap(bitmap);

            }




            name.setText(itemList.get(position).hpkl_child_name);

            String praiseDateTime = "게시일 :" +
                    itemList.get(position).hpkl_date.substring(0,4) + "-" +
                    itemList.get(position).hpkl_date.substring(4,6) + "-" +
                    itemList.get(position).hpkl_date.substring(6,8) + " " +
                    itemList.get(position).hpkl_time.substring(0,2) + ":" +
                    itemList.get(position).hpkl_time.substring(2,4) + ":" +
                    itemList.get(position).hpkl_time.substring(4,6) + " ";

            datetime.setText(praiseDateTime);

            storageDir = Environment.getExternalStorageDirectory();
            file = new File(storageDir.getAbsolutePath()+"/hpkl_"+itemList.get(position).hpkl_praise_picture_url);

            Log.i("SJS path", storageDir.getAbsolutePath()+"/hpkl_"+itemList.get(position).hpkl_praise_picture_url);

            if (!file.exists()) {

               // Toast.makeText(getApplicationContext(), "hpkl_praise_picture_url 기존에 사진없음", Toast.LENGTH_LONG).show();

                if (praise_image != null) {
                    new ImageDownloaderTask(praise_image).execute(BasicInfo.restFulServer+"/upload_image/"+itemList.get(position).hpkl_praise_picture_url, itemList.get(position).hpkl_praise_picture_url, "1", "1");
                    //new ImageDownloaderTask(praise_image).execute(itemList.get(position).hpkl_praise_picture_url);
                }

            }else{

               // Toast.makeText(getApplicationContext(), "hpkl_praise_picture_url  기존사진 있음", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap src = BitmapFactory.decodeFile(storageDir.getAbsolutePath()+"/hpkl_"+itemList.get(position).hpkl_praise_picture_url);
                Bitmap bitmap = Bitmap.createScaledBitmap( src, src.getWidth(), src.getHeight(), true );

                praise_image.setImageBitmap(bitmap);

            }



            praise_memo.setText(itemList.get(position).hpkl_praise_memo);
            int formatMoney = Integer.parseInt(itemList.get(position).hpkl_saving_love);
            money.setText(String.format("%,d",formatMoney));

            if(itemList.get(position).hpkl_sayong_gbn.equals("1")){
                sayong_gbn.setText("적립");
                sticker_name.setText("칭찬스티커 : " + itemList.get(position).hpkl_sticker_name);
                roundedImageView_sticker.setImageResource(R.drawable.sticker_1);
            }else{
                sayong_gbn.setText("지출");
                sticker_name.setText("선물사용 : " + itemList.get(position).hpkl_sticker_name);
                roundedImageView_sticker.setImageResource(R.drawable.sticker_2);
            }

            return view;
        }
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0], params[1], params[2], params[3]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.images);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url, String filename, String height, String width) {
        HttpURLConnection urlConnection = null;
        try {

            Log.i("sjs bit 111",url);
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                Log.i("sjs bit 222","222");
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {

//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 8;
//
//                Bitmap src = BitmapFactory.decodeStream(inputStream);
//                Bitmap bitmap = Bitmap.createScaledBitmap( src, src.getWidth(), src.getHeight(), true );
                Log.i("sjs bit 222","111");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap bitmap2 = createThumbnail(bitmap, filename, height, width);
                return bitmap2;
            }
            Log.i("sjs bit 333","333");
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.i("sjs bit 444","444");
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
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


    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        roundedImageViewPic_1 = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_1);
        roundedImageViewPic_2 = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_2);
        roundedImageViewPic_3 = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_3);
        roundedImageViewPic_4 = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_4);
        roundedImageViewPic_folding = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_folding);
        titlePan = (RelativeLayout)findViewById(R.id.titlePan);
        parentImage = (ImageView)findViewById(R.id.parentImage);

//        listView = (ListView)findViewById(R.id.listview);
//        itemList.add(new Item(R.drawable.a_1, "1000원 적립", "2017.09.02"));
//        itemList.add(new Item(R.drawable.a_2, "2000원 적립", "2017.09.04"));
//        itemList.add(new Item(R.drawable.a_3, "1500원 적립", "2017.09.07"));
//        itemList.add(new Item(R.drawable.a_4, "1000원 적립", "2017.09.08"));
//        itemList.add(new Item(R.drawable.a_5, "500원 적립",  "2017.09.15"));
//        itemList.add(new Item(R.drawable.a_6, "1000원 적립", "2017.09.16"));
//        itemList.add(new Item(R.drawable.a_7, "1500원 적립", "2017.09.17"));
//        itemList.add(new Item(R.drawable.a_8, "2000원 적립", "2017.09.22"));

//        itemAdpater = new ItemAdapter(ListViewActivity.this, R.layout.list_item,
//                itemList);
//        listView.setAdapter(itemAdpater);

        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                        return false;

                    // right to left swipe
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                        int child_id = 1;
                        Log.i("SJS uuu child_id =", Integer.toString(child_id));

                        //Toast.makeText(getApplicationContext(), "List View str_hpkl_id : " + str_hpkl_id + ", child_id =" + child_id, Toast.LENGTH_LONG).show();

                        try {
                            new goFindChild().execute(
                                    BasicInfo.restFulServer+"/child/",   // 회사
                                    //BasicInfo.restFulServer+"/child", // 집
                                    str_hpkl_id, Integer.toString(child_id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
                    }
                    // left to right swipe
                    else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {



                        Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
                    }
                    // down to up swipe
                    else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                        if (titlePan.getVisibility() != View.GONE) {
                            titlePan.setVisibility(View.GONE);
                            roundedImageViewPic_folding.setImageResource(R.drawable.down_key);
                        } else {
                            titlePan.setVisibility(View.VISIBLE);
                            roundedImageViewPic_folding.setImageResource(R.drawable.top_key);
                        }

                        Toast.makeText(getApplicationContext(), "Swipe up", Toast.LENGTH_SHORT).show();
                    }
                    // up to down swipe
                    else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                        if (titlePan.getVisibility() != View.GONE) {
                            titlePan.setVisibility(View.GONE);
                            roundedImageViewPic_folding.setImageResource(R.drawable.down_key);
                        } else {
                            titlePan.setVisibility(View.VISIBLE);
                            roundedImageViewPic_folding.setImageResource(R.drawable.top_key);
                        }

                        Toast.makeText(getApplicationContext(), "Swipe down", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return super.onDown(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                showDialog(BasicInfo.SELECT_ITEM);

                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                return super.onContextClick(e);
            }
        });

        //교재 p707~p713 Firebase 설정 적용
        //Registration ID
        //최초 접속시에 토큰아이디로 가입여부 체크
        Log.i("restFulServer",  BasicInfo.restFulServer+"/parent");
        try {
            device_token = FirebaseInstanceId.getInstance().getToken();
            Log.i("device_token", device_token);
            new gaipCheck().execute(
                    BasicInfo.restFulServer+"/parent", device_token); // 회사
            //BasicInfo.restFulServer+"/parent", device_token);  // 집
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 아이 이미지 터치시에 터치 이벤트 감지
        parentImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                detector.onTouchEvent(motionEvent);
                return true;
            }
        });

        roundedImageViewPic_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ListViewActivity.this,
                        ListViewActivity.class);
                startActivity(intent);

            }
        });

        roundedImageViewPic_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                Intent intent1 = new Intent(ListViewActivity.this, ChildViewListActivity.class);
//
//                intent1.putExtra("parent_hpkl_id",str_hpkl_id);
//                intent1.putExtra("child_hpkl_id", "1");
//
//                startActivity(intent1);

                int child_id = 1;
                Log.i("SJS uuu child_id =", Integer.toString(child_id));

                //Toast.makeText(getApplicationContext(), "List View str_hpkl_id : " + str_hpkl_id + ", child_id =" + child_id, Toast.LENGTH_LONG).show();

                try {
                    new goFindChild().execute(
                            BasicInfo.restFulServer+"/child/",   // 회사
                            //BasicInfo.restFulServer+"/child", // 집
                            str_hpkl_id, Integer.toString(child_id));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        roundedImageViewPic_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ListViewActivity.this,
                        KidsRegActivity.class);
                startActivity(intent);

            }
        });

        roundedImageViewPic_4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ListViewActivity.this,
                        KidsStatisticsActivity.class);
                startActivity(intent);

            }
        });

        roundedImageViewPic_folding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (titlePan.getVisibility() != View.GONE) {
                    titlePan.setVisibility(View.GONE);
                    roundedImageViewPic_folding.setImageResource(R.drawable.down_key);
                } else {
                    titlePan.setVisibility(View.VISIBLE);
                    roundedImageViewPic_folding.setImageResource(R.drawable.top_key);
                }
            }
        });


    }

    //로그아웃
    public void logout(View view) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("token");
        editor.commit();
        finish();
    }

    class gaipCheck extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ListViewActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                // GET방식
                Log.i("params" , params[0]);
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
            View view = null;
            super.onPostExecute(s);
            dialog.dismiss();
            Log.i("result", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {// 가입 되어 있음

                    //hpkl_id = json.getString("hpkl_id");
                    str_hpkl_id = json.getString("hpkl_id");
//                    Toast.makeText(ListViewActivity.this,
//                            "가입 되어 있음",
//                            Toast.LENGTH_SHORT).show();

                    //Log.i("hpkl_id SJS", hpkl_id);
                    Log.i("str_hpkl_id SJS", str_hpkl_id);

                    JSONArray item = json.getJSONArray("db_result");
                    itemList2.clear();
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject obj = item.getJSONObject(i);
                        String hpkl_id = obj.getString("hpkl_id");
                        String device_token = obj.getString("device_token");
                        String name = obj.getString("name");
                        String birth = obj.getString("birth");
                        String gender = obj.getString("gender");
                        String picture_url = obj.getString("picture_url");
                        itemList2.add(new Item2(hpkl_id, device_token, name, birth,gender, picture_url));
                    }

                    File storageDir = Environment.getExternalStorageDirectory();
                    File file = new File(storageDir.getAbsolutePath()+"/hpkl_"+itemList2.get(0).picture_url);

                    Log.i("SJS path", storageDir.getAbsolutePath()+"/hpkl_"+itemList2.get(0).picture_url);
                    ImageView parentImage = (ImageView)findViewById(R.id.parentImage);


                    if (!file.exists()) {

                       // Toast.makeText(getApplicationContext(), "picture_url 기존에 사진없음", Toast.LENGTH_LONG).show();

                        new ImageDownloaderTask(parentImage).execute(BasicInfo.restFulServer+"/upload_image/"+itemList2.get(0).picture_url,itemList2.get(0).picture_url,"1","1");
                        //new ImageDownloaderTask(parentImage).execute(itemList2.get(0).picture_url);

                    }else{

                      //  Toast.makeText(getApplicationContext(), "picture_url  기존사진 있음", Toast.LENGTH_LONG).show();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

                        Bitmap src = BitmapFactory.decodeFile(storageDir.getAbsolutePath()+"/hpkl_"+itemList2.get(0).picture_url);
                        Bitmap bitmap = Bitmap.createScaledBitmap( src, src.getWidth(), src.getHeight(), true );

                        parentImage.setImageBitmap(bitmap);

                    }

                    TextView parent_name = (TextView)findViewById(R.id.parent_name);
                    parent_name.setText(itemList2.get(0).name);

                    new goPraiseList().execute(
                            BasicInfo.restFulServer+"/child/praise",   // 회사
                            //BasicInfo.restFulServer+"/child", // 집
                            str_hpkl_id);



                } else {//가입 안되어 있음
//                    Toast.makeText(ListViewActivity.this,
//                            "가입 안되어 있음",
//                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ListViewActivity.this,
                            ParentRegActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    class goPraiseList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ListViewActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                // GET방식
                URL url = new URL(params[0]+"?hpkl_id="+params[1]);

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
            Log.i("SJS 여기왔니?? => result", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {// 가입 되어 있음

//                    Toast.makeText(ListViewActivity.this,
//                            "칭찬내역있음",
//                            Toast.LENGTH_SHORT).show();
                    JSONArray item = json.getJSONArray("db_result");
                    itemList.clear();
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject obj = item.getJSONObject(i);
                        String hpkl_id = obj.getString("hpkl_id");
                        String hpkl_child_id = obj.getString("hpkl_child_id");
                        String hpkl_date = obj.getString("hpkl_date");
                        String hpkl_time = obj.getString("hpkl_time");
                        String hpkl_saving_love = obj.getString("hpkl_saving_love");
                        String hpkl_praise_memo = obj.getString("hpkl_praise_memo");
                        String hpkl_praise_picture_url = obj.getString("hpkl_praise_picture_url");
                        String hpkl_sticker_name = obj.getString("hpkl_sticker_name");
                        String hpkl_child_name = obj.getString("hpkl_child_name");
                        String hpkl_child_picture_url = obj.getString("hpkl_child_picture_url");
                        String hpkl_sayong_gbn = obj.getString("hpkl_sayong_gbn");

                        itemList.add(new Item(hpkl_id, hpkl_child_id, hpkl_date, hpkl_time,
                                hpkl_saving_love, hpkl_praise_memo, hpkl_praise_picture_url, hpkl_sticker_name, hpkl_child_name, hpkl_child_picture_url,hpkl_sayong_gbn
                        ));

                        try {
                            BookAdapter adapter = new BookAdapter(ListViewActivity.this);
                            ListView listView = (ListView)findViewById(R.id.listview);
                            listView.setAdapter(adapter);
                        } catch (Exception e) {
                            Log.i("DDDDDDDDDDDDDDDDD00","11");
                            e.printStackTrace();
                        }

                    }

                } else {//가입 안되어 있음
//                    Toast.makeText(ListViewActivity.this,
//                            "칭찬내역없음",
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

    class goFindChild extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ListViewActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                // GET방식
                URL url = new URL(params[0]+"?hpkl_id="+params[1]+"&hpkl_child_id="+params[2]);

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
            Log.i("SJS itemList3 => result", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {// 가입 되어 있음

//                       Toast.makeText(ListViewActivity.this,
//                            "아동 있음",
//                            Toast.LENGTH_SHORT).show();

                //str_hpkl_id = json.getString("hpkl_id");
                //Toast.makeText(getApplicationContext(), "List View str_hpkl_id : " + str_hpkl_id , Toast.LENGTH_LONG).show();
                JSONArray item = json.getJSONArray("db_result");
                for (int i = 0; i < item.length(); i++) {
                    JSONObject obj = item.getJSONObject(i);
                    str_hpkl_id = obj.getString("hpkl_id");
                }

                Intent intent1 = new Intent(ListViewActivity.this, ChildViewListActivity.class);

                //intent1.putExtra("parent_hpkl_id",str_hpkl_id);
                intent1.putExtra("parent_hpkl_id", str_hpkl_id);
                intent1.putExtra("child_hpkl_id", "1");

                startActivity(intent1);

                finish();

                } else {//

                    Toast.makeText(ListViewActivity.this,
                            "아동 없음",
                            Toast.LENGTH_SHORT).show();

                    Intent intent2 = new Intent(ListViewActivity.this,
                            KidsRegActivity.class);

                    startActivity(intent2);

                    finish();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public void goRecyclerView(View view) {
        Intent intent = new Intent(ListViewActivity.this,
                            RecyclerViewActivity.class);
                startActivity(intent);
   }

}

