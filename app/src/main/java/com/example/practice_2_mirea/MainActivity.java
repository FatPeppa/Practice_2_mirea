package com.example.practice_2_mirea;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice_2_mirea.databinding.ActivityMainBinding;

import java.util.ArrayList;

enum GoodParameterType {
    GOOD_AMOUNT,
    GOOD_NAME
}

public class MainActivity extends AppCompatActivity {

    MainActivity this_obj = this;
    TextView order_congratulations_warning_view;
    TextView order_list;
    EditText customer_introduction_input_text;
    Button choose_the_amount_button;
    Button clean_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        order_congratulations_warning_view = (TextView) findViewById(R.id.order_congratulations_warning);
        order_list = (TextView) findViewById(R.id.order_list);
        customer_introduction_input_text = (EditText) findViewById(R.id.customer_introduction_input);
        choose_the_amount_button = (Button) findViewById(R.id.choose_the_amount_button);
        clean_button = (Button) findViewById(R.id.clean_button);

        choose_the_amount_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer_introduction_input_text.getText().toString().length() > 0
                        && !isNumeric(customer_introduction_input_text.getText().toString())) {

                    Intent intent = new Intent(this_obj, SecondActivity.class);
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(customer_introduction_input_text.getText().toString());
                    intent.putStringArrayListExtra("intent_from_first_activity", (ArrayList<String>) arrayList);

                    mStartForResult.launch(intent);
                } else {
                    customer_introduction_input_text.setText("");
                    customer_introduction_input_text.setHint(R.string.str_customer_introduction_hint);
                    customer_introduction_input_text.setHintTextColor(getColor(R.color.red));
                }
            }
        });

        clean_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_list.setVisibility(View.INVISIBLE);

                customer_introduction_input_text.setText("");
                customer_introduction_input_text.setHint(R.string.str_customer_introduction_hint);
            }
        });
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();

                        if (intent != null) {
                            ArrayList<String> inputs = intent.getStringArrayListExtra("goods");

                            if (inputs != null && checkInputs(inputs)){
                                String goods_amount = parseInputsGoods(inputs, GoodParameterType.GOOD_AMOUNT);
                                String good_name = parseInputsGoods(inputs, GoodParameterType.GOOD_NAME);

                                order_list.setText(goods_amount);
                                customer_introduction_input_text.setText(good_name);
                            } else {
                                Toast.makeText(this_obj, "An intent error occurred", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Intent error occurred!!!");
                            }
                        }
                    }
                    else{
                        Toast.makeText(this_obj, "An intent error occurred", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Intent error occurred!!!");
                    }
                }
            });

    private String parseInputsGoods(ArrayList<String> array, GoodParameterType type) {
        switch (type) {
            case GOOD_AMOUNT:
                return array.get(0);
            case GOOD_NAME:
                return array.get(1);
            default:
                return null;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean checkInputs(ArrayList<String> array) {
        if (array.size() != 2) return false;

        String g_a = array.get(0);
        String g_n = array.get(1);

        if (!isNumeric(g_a) || isNumeric(g_n) || g_n.length() == 0 || g_a.length() == 0) return false;
        return true;
    }
}