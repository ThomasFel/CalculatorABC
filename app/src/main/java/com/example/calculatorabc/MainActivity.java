package com.example.calculatorabc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edit_A, edit_B, edit_C;

    Button button;

    TextView equation, result, resultText;

    double a, b, c, d, x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_A = findViewById(R.id.edit_A);
        edit_B = findViewById(R.id.edit_B);
        edit_C = findViewById(R.id.edit_C);

        button = findViewById(R.id.button);

        equation = findViewById(R.id.equation);
        result = findViewById(R.id.result);
        resultText = findViewById(R.id.resultText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_A.getText().toString().equals("") && !edit_B.getText().toString().equals("") && !edit_C.getText().toString().equals("")) {
                    a = Double.parseDouble(edit_A.getText().toString());
                    b = Double.parseDouble(edit_B.getText().toString());
                    c = Double.parseDouble(edit_C.getText().toString());

                    d = Math.pow(b, 2) - (4 * a * c);

                    equation.setText(a + "x\u00b2 + " + b + "x + " + c + " = 0");
                    resultText.setText("Result:");

                    if (a != 0) {
                        if (d == 0) {
                            x1 = -b / (2 * a);
                            result.setText("d = " + d + "\nx = " + x1);
                        } else if (d < 0) {
                            result.setText("No roots!");
                        } else if (d > 0) {
                            x1 = ((-b + Math.sqrt(d)) / (2 * a));
                            x2 = ((-b - Math.sqrt(d)) / (2 * a));
                            result.setText("d = " + d + "\nx\u2081 = " + x1 + "\nx\u2082 = " + x2);
                        }
                    }

                    else {
                        result.setText("Not a quadratic equation!");
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "Semua field harus terisi", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}