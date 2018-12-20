package net.skhu.javaproject_address;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddAdressActivity extends AppCompatActivity {

    private List<Person> list ;
    private String googleEmail;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    DatabaseReference databaseReference;

    public boolean nullCheck(String s){
        if(s==null)
            return false;
        return s.toString().trim().length()==0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("myData01");
        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Person>> typeIndicator = new GenericTypeIndicator<ArrayList<Person>>() {};
                ArrayList<Person> temp = dataSnapshot.getValue(typeIndicator);
                if(temp!=null){
                    list.clear();
                    list.addAll(temp);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
    public void onClick_add(View view){

        String name,age,email,number;
        EditText eName = (EditText)findViewById(R.id.editText_name);
        EditText eAge = (EditText)findViewById(R.id.editText_age);
        EditText eEmail = (EditText)findViewById(R.id.editText_email);
        EditText eNumber = (EditText)findViewById(R.id.editText_number);



        name = eName.getText().toString();
        age = eAge.getText().toString();
        email = eEmail.getText().toString();
        number = eNumber.getText().toString();


        if(nullCheck(name)){ //추후 밑에 텍스트 모두 string 요소로 전환 필요
            eName.setError("이름을 입력하세요");
        }
        if(nullCheck(age)){
            eAge.setError("나이를 입력하세요");
        }
        if(nullCheck(email)){
            eEmail.setError("이메일을 입력하세요");
        }
        if(nullCheck(number)){
            eNumber.setError("연락처를 입력하세요");
        }
        if(nullCheck(name)==false){
            if(nullCheck(age)==false){
                if(nullCheck(email)==false){
                    if(nullCheck(number)==false) {
                        //리스트 받아오는거 해야됨
                        Person person =new Person(name,age,email,number);

                        sendToDB(person);
                        //데이터베이스 연동 구현
                        AlertDialog.Builder bd = new AlertDialog.Builder(this);
                        bd.setTitle(R.string.saveInfo);
                        //다이얼로그 타이틀작성
                        bd.setMessage(name+"\n"+age+"\n"+email+"\n"+number);
                        //setMessage는 변수로 string을 받지 못하는데 이걸 해결할 방법이 없을까?
                        bd.setNeutralButton(R.string.close,null);
                        AlertDialog dialog  = bd.create();
                        dialog.show();//다이얼로그를 띄워 입력한 정보를 확인시켜줌

                        eName.setText(null);
                        eAge.setText(null);
                        eEmail.setText(null);
                        eNumber.setText(null);
                        //다음 입력을 위한 초기화 작업진행완료
                    }
                }
            }
        }
    }//onClick_add 메소드 종료
    public void sendToDB(final Person p){
        Intent intent = getIntent();
        googleEmail = (String)intent.getStringExtra("google_email");
        int idEndIndex = googleEmail.indexOf("@");
        String googleId = googleEmail.substring(0,idEndIndex);
        list.add(p);
        databaseReference.setValue(list);
//        databaseReference.child(googleId).setValue(list);
        Intent intent2 = new Intent(this,MainActivity.class);

    }

}
