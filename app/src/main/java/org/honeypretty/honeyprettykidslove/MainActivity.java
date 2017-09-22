package org.honeypretty.honeyprettykidslove;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_go = (Button) findViewById(R.id.btn_regchild);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SubActivity로 가는 인텐트를 생성
                Intent intent = new Intent(MainActivity.this, KidsRegActivity.class);
                //액티비티 시작!
                startActivity(intent);

            }
        });

    }
}
