package org.honeypretty.honeyprettykidslove;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class PresentStickerChoice extends AppCompatActivity {
    EditText editText;

    GridView gridView;
    SingerAdapter adapter;
    Intent intent;

    private static String str_hpkl_id = "";
    private static String str_hpkl_child_id = "";
    private static int int_hpkl_child_total_saving_love = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_sticker_choice);

        gridView = (GridView) findViewById(R.id.gridView);

        intent = getIntent();

        str_hpkl_id = intent.getStringExtra("parent_hpkl_id");
        str_hpkl_child_id = intent.getStringExtra("child_hpkl_id");
        int_hpkl_child_total_saving_love = intent.getIntExtra("child_total_saving_love",0);

        Toast.makeText(getApplicationContext(), "int_hpkl_child_total_saving_love : " + int_hpkl_child_total_saving_love, Toast.LENGTH_LONG).show();


        adapter = new SingerAdapter();

        adapter.addItem(new PresentStickerItem("책 읽어주기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("산책/운동하기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("함께 놀이터 가기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("키즈 카페가기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("외식 하기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("영화 보러 가기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("놀이동산 가기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("여행 가기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("과자 먹기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("TV 보기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("친구들과 놀기", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("장난감/인형 선물", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("스포츠 용품 선물", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("예쁜/멋진 옷 선물", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("책 선물", R.drawable.sticker_2));
        adapter.addItem(new PresentStickerItem("용돈 주기", R.drawable.sticker_2));


        gridView.setAdapter(adapter);

        //editText = (EditText) findViewById(R.id.editText);

//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = editText.getText().toString();
//                String mobile = "010-1000-1000";
//                int age = 20;
//
//                adapter.addItem(new PresentStickerItem(name, mobile, age, R.drawable.singer3));
//                adapter.notifyDataSetChanged();
//            }
//        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PresentStickerItem item = (PresentStickerItem) adapter.getItem(position);

                Intent intent = new Intent(PresentStickerChoice.this,
                        KidsPresentRegActivity.class);

                intent.putExtra("parent_hpkl_id",str_hpkl_id);
                intent.putExtra("child_hpkl_id",str_hpkl_child_id);
                intent.putExtra("praise_sticker_name",item.getName());
                intent.putExtra("child_total_saving_love",int_hpkl_child_total_saving_love);

                Toast.makeText(getApplicationContext(), "int_hpkl_child_total_saving_love2 : " + int_hpkl_child_total_saving_love, Toast.LENGTH_LONG).show();
                startActivity(intent);

            }
        });

    }

    class SingerAdapter extends BaseAdapter {
        ArrayList<PresentStickerItem> items = new ArrayList<PresentStickerItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(PresentStickerItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            PresentStickerItemView view = new PresentStickerItemView(getApplicationContext());

            PresentStickerItem item = items.get(position);
            view.setName(item.getName());
            view.setImage(item.getResId());

            int numColumns = gridView.getNumColumns();
            int rowIndex = position / numColumns;
            int columnIndex = position % numColumns;
            Log.d("SingerAdapter", "index : " + rowIndex + ", " + columnIndex);

            return view;
        }
    }

}
