package net.skhu.javaproject_address;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;






public class MainActivity extends AppCompatActivity{
    Intent intent1;
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn1 = (Button)findViewById(R.id.btn_list);
        Button btn2 = (Button)findViewById(R.id.btn_add);
        intent1 = new Intent(this, RecyclerView.class);
        intent2 = new Intent(this, AddAdressActivity.class);

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_list)
                    startActivity(intent1);
                else if(v.getId()==R.id.btn_add)
                    startActivity(intent2);




            }
        };
        btn1.setOnClickListener(listener1);
        btn2.setOnClickListener(listener1);
    }


}
