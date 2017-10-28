package org.honeypretty.honeyprettykidslove;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    ListView listView = null;
    class Item {
        int image;
        String money;
        String date;
        Item(int image, String money, String date) {
            this.image = image;
            this.money = money;
            this.date = date;
        }
    }
    ArrayList<Item> itemList = new ArrayList<Item>();
    class ItemAdapter extends ArrayAdapter {
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
            TextView text1View = (TextView)convertView.findViewById(R.id.money);
            TextView text2View = (TextView)convertView.findViewById(R.id.date);
            Item item = itemList.get(position);
            imageView.setImageResource(item.image);
            text1View.setText(item.money);
            text2View.setText(item.date);
            return convertView;
        }

        public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
        }
    }

    ItemAdapter itemAdpater = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = (ListView)findViewById(R.id.listview);
        itemList.add(new Item(R.drawable.a_1, "1000원 적립", "2017.09.02"));
        itemList.add(new Item(R.drawable.a_2, "2000원 적립", "2017.09.04"));
        itemList.add(new Item(R.drawable.a_3, "1500원 적립", "2017.09.07"));
        itemList.add(new Item(R.drawable.a_4, "1000원 적립", "2017.09.08"));
        itemList.add(new Item(R.drawable.a_5, "500원 적립",  "2017.09.15"));
        itemList.add(new Item(R.drawable.a_6, "1000원 적립", "2017.09.16"));
        itemList.add(new Item(R.drawable.a_7, "1500원 적립", "2017.09.17"));
        itemList.add(new Item(R.drawable.a_8, "2000원 적립", "2017.09.22"));
        itemAdpater = new ItemAdapter(ListViewActivity.this, R.layout.list_item,
                itemList);
        listView.setAdapter(itemAdpater);

        //교재 p707~p713 Firebase 설정 적용
        //Registration ID
        String regId = FirebaseInstanceId.getInstance().getToken();
        Log.i("regId", regId);
        new Nologin().execute(
                "http://172.16.2.11:52273/user/nologin", regId);
    }

    //로그아웃
    public void logout(View view) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("token");
        editor.commit();
        finish();
    }

    class Nologin extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ListViewActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {

                URL url = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("device_token", params[1]);


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
            Log.i("s111111", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//로그인 성공
                } else {//로그인 실패
                    Toast.makeText(ListViewActivity.this,
                            json.getString("err"),
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
}