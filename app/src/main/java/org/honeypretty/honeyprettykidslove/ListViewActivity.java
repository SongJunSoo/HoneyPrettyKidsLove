package org.honeypretty.honeyprettykidslove;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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
    }
}