package org.honeypretty.honeyprettykidslove;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChildViewListActivity extends AppCompatActivity {
    ListView listView = null;
    Intent intent;

    private static String device_token = "";
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_1;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_2;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_3;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_folding;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_NextChild;
    com.github.siyamed.shapeimageview.RoundedImageView roundedImageViewPic_PrevChild;

    ImageView parentImage;
    RelativeLayout titlePan;

    private static String hpkl_id = "";
    private static int int_hpkl_id = 0;

    private static String str_hpkl_id = "";
    private static String str_hpkl_child_id = "";

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

        Item(String hpkl_id, String hpkl_child_id, String hpkl_date, String hpkl_time, String hpkl_saving_love, String hpkl_praise_memo, String hpkl_praise_picture_url, String hpkl_sticker_name,
             String hpkl_child_name, String hpkl_child_picture_url) {
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


    class Item3 {

        String hpkl_id;
        String hpkl_child_id;
        String hpkl_name;
        String hpkl_birth;
        String hpkl_gender;
        String hpkl_total_saving_love;
        String hpkl_child_picture_url;

        Item3(String hpkl_id, String hpkl_child_id, String hpkl_name, String hpkl_birth,
              String hpkl_gender, String hpkl_total_saving_love, String hpkl_child_picture_url)
        {
            this.hpkl_id = hpkl_id;
            this.hpkl_child_id = hpkl_child_id;
            this.hpkl_name = hpkl_name;
            this.hpkl_birth = hpkl_birth;
            this.hpkl_gender = hpkl_gender;
            this.hpkl_total_saving_love = hpkl_total_saving_love;
            this.hpkl_child_picture_url = hpkl_child_picture_url;
        }
    }

    ArrayList<Item> itemList = new ArrayList<Item>();
    ArrayList<Item2> itemList2 = new ArrayList<Item2>();
    ArrayList<Item3> itemList3 = new ArrayList<Item3>();
    ArrayList<Item3> itemList4 = new ArrayList<Item3>();
    class BookAdapter extends ArrayAdapter {
        public BookAdapter(Context context) {
            super(context, R.layout.child_list_item, itemList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.child_list_item, null);
            } else view = convertView;

            com.github.siyamed.shapeimageview.RoundedImageView roundedImageView_childImage = (com.github.siyamed.shapeimageview.RoundedImageView)view.findViewById(R.id.roundedImageView_childImage);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView datetime = (TextView)view.findViewById(R.id.datetime);
            TextView sticker_name = (TextView)view.findViewById(R.id.sticker_name);
            ImageView praise_image = (ImageView)view.findViewById(R.id.praise_image);
            TextView praise_memo = (TextView)view.findViewById(R.id.praise_memo);
            TextView money = (TextView)view.findViewById(R.id.money);

            if (roundedImageView_childImage != null) {
                new ImageDownloaderTask(roundedImageView_childImage).execute(itemList.get(position).hpkl_child_picture_url);
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

            sticker_name.setText(itemList.get(position).hpkl_sticker_name);

            if (praise_image != null) {
                new ImageDownloaderTask(praise_image).execute(itemList.get(position).hpkl_praise_picture_url);
            }

            praise_memo.setText(itemList.get(position).hpkl_praise_memo);
            if(notEmpty(itemList.get(position).hpkl_saving_love)) {
                money.setText(itemList.get(position).hpkl_saving_love);
            }else{
                money.setText("0");
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
            return downloadBitmap(params[0]);
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
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static Boolean empty(Object obj) {
        if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List) return obj == null || ((List) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }

    /**
     * Object type 변수가 비어있지 않은지 체크
     *
     * @param obj
     * @return Boolean : true / false
     */
    public static Boolean notEmpty(Object obj) {
        return !empty(obj);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view_list);
        roundedImageViewPic_1 = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_1);
        roundedImageViewPic_2 = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_2);
        roundedImageViewPic_3 = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_3);
        roundedImageViewPic_folding = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_folding);
        roundedImageViewPic_NextChild = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_NextChild);
        roundedImageViewPic_PrevChild = (com.github.siyamed.shapeimageview.RoundedImageView)findViewById(R.id.roundedImageViewPic_PrevChild);
        parentImage = (ImageView)findViewById(R.id.parentImage);
        titlePan = (RelativeLayout)findViewById(R.id.titlePan);

        intent = getIntent();

        int childCnt = itemList3.size();

        str_hpkl_id = intent.getStringExtra("parent_hpkl_id");
        str_hpkl_child_id = intent.getStringExtra("child_hpkl_id");

        Toast.makeText(getApplicationContext(), "Child View str_hpkl_id : " + str_hpkl_id + ",  str_hpkl_child_id =" +str_hpkl_child_id, Toast.LENGTH_LONG).show();

        Log.i("SJS www parent_hpkl_id", str_hpkl_id);
        Log.i("SJS www str_hpkl_id", notEmpty(str_hpkl_id).toString());
        Log.i("SJS www child_hpkl_id", str_hpkl_child_id);
        Log.i("SJS www hpkl_child_id", notEmpty(str_hpkl_child_id).toString());

        // true : 있는 것
        // fasle : 없는 것
        if(notEmpty(str_hpkl_child_id)){

            Log.i("SJS www 여기오니 111","2222");

            try {
                new goFindChild().execute(
                        "http://172.16.2.11:52274/child/",   // 회사
                        //"http://172.16.2.11:52274/child", // 집
                        str_hpkl_id, str_hpkl_child_id);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "str_hpkl_child_id 있음", Toast.LENGTH_LONG).show();

        }else{

            try {
                new goChildList().execute(
                        "http://172.16.2.11:52274/childlist", str_hpkl_id); // 회사
                //"http://192.168.0.25:52274/parent", device_token);  // 집
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        roundedImageViewPic_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ChildViewListActivity.this,
                        ListViewActivity.class);
                startActivity(intent);

            }
        });

        roundedImageViewPic_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ChildViewListActivity.this,
                        ChildViewListActivity.class);
                intent.putExtra("parent_hpkl_id",str_hpkl_id);
                intent.putExtra("child_hpkl_id",str_hpkl_child_id);
                startActivity(intent);

            }
        });

        roundedImageViewPic_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ChildViewListActivity.this,
                        KidsRegActivity.class);
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


        roundedImageViewPic_NextChild.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ChildViewListActivity.this,
                        ChildViewListActivity.class);

                int next_child_id = Integer.parseInt(str_hpkl_child_id) + 1;
                Log.i("SJS uuu child_id =", Integer.toString(next_child_id));
                intent.putExtra("parent_hpkl_id",str_hpkl_id);
                intent.putExtra("child_hpkl_id",Integer.toString(next_child_id));

                Toast.makeText(getApplicationContext(), "str_hpkl_id : " + str_hpkl_id + ", str_hpkl_child_id =" + Integer.toString(next_child_id), Toast.LENGTH_LONG).show();

                startActivity(intent);
            }
        });

        roundedImageViewPic_PrevChild.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ChildViewListActivity.this,
                        ChildViewListActivity.class);

                int prev_child_id = Integer.parseInt(str_hpkl_child_id) - 1;
                Log.i("SJS uuu child_id =", Integer.toString(prev_child_id));
                intent.putExtra("parent_hpkl_id",str_hpkl_id);
                intent.putExtra("child_hpkl_id",Integer.toString(prev_child_id));

                Toast.makeText(getApplicationContext(), "str_hpkl_id : " + str_hpkl_id + ", str_hpkl_child_id =" + Integer.toString(prev_child_id), Toast.LENGTH_LONG).show();

                startActivity(intent);
            }
        });

        parentImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ChildViewListActivity.this,
                        PraiseStickerChoice.class);
                intent.putExtra("parent_hpkl_id",str_hpkl_id);
                intent.putExtra("child_hpkl_id",str_hpkl_child_id);

                //Toast.makeText(getApplicationContext(), "parent_hpkl_id : " + Integer.toString(int_hpkl_id) + ", child_hpkl_id =" + str_hpkl_child_id, Toast.LENGTH_LONG).show();
                Log.i("KKKKK parent_hpkl_id", str_hpkl_id);
                Log.i("KKKKK str_hpkl_child_id", str_hpkl_child_id);

                startActivity(intent);

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

    class goFindChild extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ChildViewListActivity.this);
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
            dialog.setMessage("가입여부 체크 중...");
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

                    JSONArray item = json.getJSONArray("db_result");
                    itemList4.clear();
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject obj = item.getJSONObject(i);
                        String hpkl_id = obj.getString("hpkl_id");
                        String hpkl_child_id = obj.getString("hpkl_child_id");
                        String hpkl_name = obj.getString("name");
                        String hpkl_birth = obj.getString("birth");
                        String hpkl_gender = obj.getString("gender");
                        String hpkl_total_saving_love = obj.getString("hpkl_total_saving_love");
                        String hpkl_child_picture_url = obj.getString("picture_url");

                        itemList4.add(new Item3(hpkl_id, hpkl_child_id, hpkl_name, hpkl_birth,hpkl_gender,
                                hpkl_total_saving_love, hpkl_child_picture_url
                        ));
                    }

                    int childCnt = itemList4.size();
                    Log.i("itemList3 Count SJS ", Integer.toString(itemList4.size()));

                    if(childCnt > 0 ) {

                        ImageView parentImage = (ImageView) findViewById(R.id.parentImage);
                        new ImageDownloaderTask(parentImage).execute(itemList4.get(0).hpkl_child_picture_url);

                        TextView money  = (TextView) findViewById(R.id.money );
                        TextView parent_name = (TextView) findViewById(R.id.parent_name);
                        TextView birth = (TextView) findViewById(R.id.birth);
                        money.setText(itemList4.get(0).hpkl_total_saving_love);
                        parent_name.setText(itemList4.get(0).hpkl_name);

                        str_hpkl_child_id = itemList4.get(0).hpkl_child_id;

                        String birth_date = itemList4.get(0).hpkl_birth.substring(0,4) + "년" + " " +
                                itemList4.get(0).hpkl_birth.substring(4,6) + "월" + " " +
                                itemList4.get(0).hpkl_birth.substring(6,8) + "일";

                        birth.setText(birth_date);

                        new goPraiseList().execute(
                                "http://172.16.2.11:52274/child/praise/child",   // 회사
                                //"http://172.16.2.11:52274/child", // 집
                                itemList4.get(0).hpkl_id, itemList4.get(0).hpkl_child_id);

                        Log.i("SJS 111", itemList4.get(0).hpkl_id);
                        Log.i("SJS 222", itemList4.get(0).hpkl_child_id);

                    }



                } else {//

                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }


    class goChildList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ChildViewListActivity.this);
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
            dialog.setMessage("가입여부 체크 중...");
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

                    JSONArray item = json.getJSONArray("db_result");
                    itemList3.clear();
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject obj = item.getJSONObject(i);
                        String hpkl_id = obj.getString("hpkl_id");
                        String hpkl_child_id = obj.getString("hpkl_child_id");
                        String hpkl_name = obj.getString("name");
                        String hpkl_birth = obj.getString("birth");
                        String hpkl_gender = obj.getString("gender");
                        String hpkl_total_saving_love = obj.getString("hpkl_total_saving_love");
                        String hpkl_child_picture_url = obj.getString("picture_url");

                        itemList3.add(new Item3(hpkl_id, hpkl_child_id, hpkl_name, hpkl_birth,hpkl_gender,
                                hpkl_total_saving_love, hpkl_child_picture_url
                        ));
                    }

                    Log.i("itemList3 Count SJS ", Integer.toString(itemList3.size()));


                        ImageView parentImage = (ImageView) findViewById(R.id.parentImage);
                        new ImageDownloaderTask(parentImage).execute(itemList3.get(0).hpkl_child_picture_url);

                        TextView money  = (TextView) findViewById(R.id.money );
                        TextView parent_name = (TextView) findViewById(R.id.parent_name);
                        TextView birth = (TextView) findViewById(R.id.birth);
                        if(notEmpty(itemList3.get(0).hpkl_total_saving_love)) {
                            money.setText(itemList3.get(0).hpkl_total_saving_love);
                        }else{
                            money.setText("0");
                        }

                        parent_name.setText(itemList3.get(0).hpkl_name);

                        str_hpkl_child_id = itemList3.get(0).hpkl_child_id;

                        String birth_date = itemList3.get(0).hpkl_birth.substring(0,4) + "년" + " " +
                                itemList3.get(0).hpkl_birth.substring(4,6) + "월" + " " +
                                itemList3.get(0).hpkl_birth.substring(6,8) + "일";

                        Log.i("SJS hpkl_birth", itemList3.get(0).hpkl_birth);

                        birth.setText(birth_date);

                        new goPraiseList().execute(
                                "http://172.16.2.11:52274/child/praise/child",   // 회사
                                //"http://172.16.2.11:52274/child", // 집
                                itemList3.get(0).hpkl_id, itemList3.get(0).hpkl_child_id);

                        Log.i("SJS 111", itemList3.get(0).hpkl_id);
                        Log.i("SJS 222", itemList3.get(0).hpkl_child_id);



                } else {//가입 안되어 있음

                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    class goPraiseList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ChildViewListActivity.this);
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
            dialog.setMessage("가입여부 체크 중...");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.i("SJS => result", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {// 가입 되어 있음

                    Toast.makeText(ChildViewListActivity.this,
                            "칭찬내역있음",
                            Toast.LENGTH_SHORT).show();
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
                        itemList.add(new Item(hpkl_id, hpkl_child_id, hpkl_date, hpkl_time,
                                hpkl_saving_love, hpkl_praise_memo, hpkl_praise_picture_url, hpkl_sticker_name, hpkl_child_name, hpkl_child_picture_url
                        ));

                        BookAdapter adapter = new BookAdapter(ChildViewListActivity.this);
                        ListView listView = (ListView)findViewById(R.id.listview);
                        listView.setAdapter(adapter);

                    }

                } else {//가입 안되어 있음

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

    public void goRecyclerView(View view) {
        Intent intent = new Intent(ChildViewListActivity.this,
                RecyclerViewActivity.class);
        startActivity(intent);
    }

}

