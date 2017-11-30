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

//        Toast.makeText(getApplicationContext(), "str_hpkl_id : " + str_hpkl_id + ", str_hpkl_child_id =" + str_hpkl_child_id, Toast.LENGTH_LONG).show();

        adapter = new SingerAdapter();

        adapter.addItem(new PraiseStickerItem("이닦기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("일찍 자기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("떼쓰지 않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("세수하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("손씻기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("목욕하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("친구와인사하기", R.drawable.sticker_1));

        adapter.addItem(new PraiseStickerItem("책 읽기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("친구와사이좋게지내기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("동생돌보기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("일찍 일어나기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("등하원 잘하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("질서 지키기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("떠들지 않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("뛰어 다니지 않기", R.drawable.sticker_1));

        adapter.addItem(new PraiseStickerItem("울음 그치기", R.drawable.sticker_1));

        adapter.addItem(new PraiseStickerItem("TV보는 시간 지키기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("방정리하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("이불정리하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("옷입고벗기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("집안일돕기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("남의물건가져오지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("게임하는시간지키기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("거짓말하지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("공부하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("학원가기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("어른께인사하기", R.drawable.sticker_1));

        adapter.addItem(new PraiseStickerItem("감사표현하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("사과하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("바르게앉아서식사하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("어른께존댓말하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("두손으로드리고받기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("식사후그릇치우기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("고운말쓰기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("밥잘먹기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("야채잘먹기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("과일먹기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("약잘먹기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("우유잘마시기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("병원가기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("횡단보도안전하게걷기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("유산균잘먹기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("편식하지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("주사잘맞기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("이뽑기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("위험한장난하지않기", R.drawable.sticker_1));

        adapter.addItem(new PraiseStickerItem("배변하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("코파지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("주워먹지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("손으로음식먹지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("손가락빨지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("손톱물어뜯지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("더러운(속)옷갈아입기", R.drawable.sticker_1));

        adapter.addItem(new PraiseStickerItem("양보하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("음식나눠먹기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("화해하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("친구들과함께놀기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("남을아프게하지않기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("남의의견에동의하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("형제들과사이좋게지내기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("단체생활참여하기", R.drawable.sticker_1));

        adapter.addItem(new PraiseStickerItem("반려동물먹이주기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("심부름하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("옷정리하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("신발정리하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("놀잇감정리하기", R.drawable.sticker_1));
        adapter.addItem(new PraiseStickerItem("준비물챙기기", R.drawable.sticker_1));

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

//                Toast.makeText(getApplicationContext(), "선택 : " + item.getName(), Toast.LENGTH_LONG).show();
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
