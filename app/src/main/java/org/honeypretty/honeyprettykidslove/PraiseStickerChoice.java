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

public class PraiseStickerChoice extends AppCompatActivity {
    EditText editText;

    GridView gridView;
    SingerAdapter adapter;
    Intent intent;

    private static String str_hpkl_id = "";
    private static String str_hpkl_child_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise_sticker_choice);

        gridView = (GridView) findViewById(R.id.gridView);

        intent = getIntent();

        str_hpkl_id = intent.getStringExtra("parent_hpkl_id");
        str_hpkl_child_id = intent.getStringExtra("child_hpkl_id");

        Toast.makeText(getApplicationContext(), "str_hpkl_id : " + str_hpkl_id + ", str_hpkl_child_id =" + str_hpkl_child_id, Toast.LENGTH_LONG).show();


        adapter = new SingerAdapter();

        adapter.addItem(new PraiseStickerItem("일찍 자기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("일찍 일어나기", R.drawable.sticker_2));
        adapter.addItem(new PraiseStickerItem("등하원 잘하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("질서 지키기", R.drawable.sticker_2));
        adapter.addItem(new PraiseStickerItem("떠들지 않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("뛰어 다니지 않기", R.drawable.sticker_2));
        adapter.addItem(new PraiseStickerItem("떼스지 않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("울음 그치기", R.drawable.sticker_2));
        adapter.addItem(new PraiseStickerItem("책 읽기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("TV보는 시간 지키기", R.drawable.sticker_2));


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
//                adapter.addItem(new PraiseStickerItem(name, mobile, age, R.drawable.singer3));
//                adapter.notifyDataSetChanged();
//            }
//        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PraiseStickerItem item = (PraiseStickerItem) adapter.getItem(position);

                Intent intent = new Intent(PraiseStickerChoice.this,
                        KidsPraiseRegActivity.class);

                intent.putExtra("parent_hpkl_id",str_hpkl_id);
                intent.putExtra("child_hpkl_id",str_hpkl_child_id);
                intent.putExtra("praise_sticker_name",item.getName());

                Toast.makeText(getApplicationContext(), "선택 : " + item.getName(), Toast.LENGTH_LONG).show();
                startActivity(intent);

            }
        });

    }

class SingerAdapter extends BaseAdapter {
    ArrayList<PraiseStickerItem> items = new ArrayList<PraiseStickerItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(PraiseStickerItem item) {
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
        PraiseStickerItemView view = new PraiseStickerItemView(getApplicationContext());

        PraiseStickerItem item = items.get(position);
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
