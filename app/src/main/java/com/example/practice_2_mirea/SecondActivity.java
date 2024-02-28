package com.example.practice_2_mirea;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.practice_2_mirea.MainActivity.isNumeric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        SecondActivity this_obj = this;
        EditText input_the_amount_edit_text = (EditText) findViewById(R.id.input_the_amount_edit_text);
        TextView chosen_good = (TextView) findViewById(R.id.chosen_good);
        Button choose_the_amount_button = (Button) findViewById(R.id.choose_the_amount_button);

        Intent intent = getIntent();
        ArrayList<String> inputs = intent.getStringArrayListExtra("intent_from_first_activity");

        if (inputs != null && checkInputs(inputs)) {
            String good_name = parseInputsGoods(inputs);

            chosen_good.setText(good_name);
        } else {
            Toast.makeText(this, "An intent error occurred", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Intent error occurred!!!");
        }

        choose_the_amount_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String input_amount = input_the_amount_edit_text.getText().toString();
                if (input_amount.length() != 0 && isNumeric(input_amount)) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(input_amount);
                    arrayList.add(chosen_good.getText().toString());

                    Intent data = new Intent();
                    data.putStringArrayListExtra("goods", (ArrayList<String>) arrayList);
                    setResult(RESULT_OK, data);
                    finish();

                } else {
                    input_the_amount_edit_text.setText("");
                    input_the_amount_edit_text.setHint(R.string.str_input_the_amount_hint);
                    input_the_amount_edit_text.setHintTextColor(getColor(R.color.red));
                }
            }
        });
    }

    private String parseInputsGoods(ArrayList<String> array) {
        return array.get(0);
    }

    private boolean checkInputs(ArrayList<String> array) {
        if (array.size() != 1) return false;

        String g_n = array.get(0);

        if (isNumeric(g_n) || g_n.length() == 0) return false;
        return true;
    }
}