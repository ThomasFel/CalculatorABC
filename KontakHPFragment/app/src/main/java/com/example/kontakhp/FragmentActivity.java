package com.example.kontakhp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentActivity extends AppCompatActivity {
    Fragment frag1, frag2;
    Button btn1, btn2, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        close = (Button) findViewById(R.id.close);

        btn1.setOnClickListener(view -> {
            frag1 = new DescFragment1();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, frag1);
            ft.addToBackStack(null);
            ft.commit();
        });

        btn2.setOnClickListener(view -> {
            frag2 = new DescFragment2();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, frag2);
            ft.addToBackStack(null);
            ft.commit();
        });

        close.setOnClickListener(view -> finish());
    }
}
