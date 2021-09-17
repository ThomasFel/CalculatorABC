package com.example.calculatorabc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    EditText editNum1, editNum2;

    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNum1 = findViewById(R.id.editNum1);
        editNum2 = findViewById(R.id.editNum2);

        textResult = findViewById(R.id.textResult);
    }

    public void operation (View v) {
        float num1, num2, result = 0;

        num1 = Float.parseFloat(editNum1.getText().toString());
        num2 = Float.parseFloat(editNum2.getText().toString());

        Button bt = (Button) v;

        switch (v.getId()) {
            case R.id.tambah: result = num1 + num2; break;
            case R.id.kurang: result = num1 - num2; break;
            case R.id.kali: result = num1 * num2; break;
            case R.id.bagi: result = num1 / num2; break;
        }

        textResult.setText(num1 + " " + bt.getText() + " " + num2 + " = " + result);
    }
}