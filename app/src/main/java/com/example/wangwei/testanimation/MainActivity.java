package com.example.wangwei.testanimation;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button btn;
    private TypingView2 type;
    private RatingView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
//        type = (TypingView2) findViewById(R.id.type);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                type.starta();
////                if(type.getVisibility() == View.GONE)
////                {
////                    type.setVisibility(View.VISIBLE);
////                }else {
////                    type.setVisibility(View.GONE);
////                }
//            }
//        });




        rv = (RatingView) findViewById(R.id.rv);
        rv.show();
//        rv.set
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.clear();
                rv.show();
            }
        });
    }
}
