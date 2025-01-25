package com.editions.sqlite_android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText edName, edAge, edGender, edAddress;
    Button Add_btn, show_btn, update_dataBtn, deleteBtn;

    MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //View Binding
        edName = findViewById(R.id.edName);
        edAge = findViewById(R.id.edAge);
        edGender = findViewById(R.id.edGender);
        Add_btn = findViewById(R.id.Add_btn);
        edAddress = findViewById(R.id.edAddress);
        show_btn = findViewById(R.id.show_btn);
        update_dataBtn = findViewById(R.id.update_dataBtn);
        deleteBtn = findViewById(R.id.DeleteBtn);

        //Database Creation & Insert Data
        myDBHelper = new MyDBHelper(this);



        // Add_btn & show_btn Click Listener Here
        Add_btn.setOnClickListener(v -> Add_btn_Click());
        show_btn.setOnClickListener(v -> Show_btn_Click());
        update_dataBtn.setOnClickListener(v -> Update_btn_Click());
        update_dataBtn.setOnClickListener(v -> Delete_btn_Click());


    }//onCreate Method

    //Show Data Button Click Listener
    private void Add_btn_Click() {

        if (edName.getText().toString().isEmpty() || edAddress.getText().toString().isEmpty() || edAge.getText().toString().isEmpty() || edGender.getText().toString().isEmpty()) {
            edName.setError("Please Enter Name");
            edAddress.setError("Please Enter Address");
            edAge.setError("Please Enter Age");
            edGender.setError("Please Enter Gender");
        }else {
            String name = edName.getText().toString();
            String address = edAddress.getText().toString();
            String age = edAge.getText().toString();
            String gender = edGender.getText().toString();

            //Insert Data to Database & Check Result
            long result = myDBHelper.insertData(name, address, Integer.parseInt(age), gender);
            if (result == -1){
                Toast.makeText(this, "Insert Failed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Insert Successfully", Toast.LENGTH_SHORT).show();
            }
            //Clear Edit Text After Insert Data
            edName.setText("");
            edAddress.setText("");
            edAge.setText("");
            edGender.setText("");
            edName.requestFocus();
        }
    }// END
    //Show Data Button Click Listener
    public void Show_btn_Click() {

        Cursor cursor = myDBHelper.FetchData();

        if (cursor.getCount() ==0){
            //Show message Here
            Show_Data("Error", "No Data Found");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            stringBuffer.append("ID : "+cursor.getString(0)+"\n");
            stringBuffer.append("NAME : "+cursor.getString(1)+"\n");
            stringBuffer.append("ADDRESS : "+cursor.getString(2)+"\n");
            stringBuffer.append("AGE : "+cursor.getString(3)+"\n");
            stringBuffer.append("GENDER : "+cursor.getString(4)+"\n \n");
        }
        Show_Data("All Data", stringBuffer.toString());
    }//END

    public void Show_Data(String title, String data){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(data);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    public void Update_btn_Click(){
        //Create LayoutInfiltrator
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.update_layout, null);

        //View Binding
        EditText edID = view.findViewById(R.id.edID);
        EditText edName = view.findViewById(R.id.edName);
        EditText edAddress = view.findViewById(R.id.edAddress);
        EditText edAge = view.findViewById(R.id.edAge);
        EditText edGender = view.findViewById(R.id.edGender);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Update Data");
        builder.setPositiveButton("Update", (dialog, which) -> {
            String id = edID.getText().toString();
            String name = edName.getText().toString();
            String address = edAddress.getText().toString();
            String age = edAge.getText().toString();
            String gender = edGender.getText().toString();

            if (id.isEmpty() || name.isEmpty() || address.isEmpty() || age.isEmpty() || gender.isEmpty()){
                Toast.makeText(this, "All Field Required", Toast.LENGTH_SHORT).show();
            }else {

                SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                sqLiteDatabase.execSQL("UPDATE Student_table SET name = '"+name+"', address = '"+address+"', age = '"+age+"', gender = '"+gender+"' WHERE id = '"+id+"'");

                edID.setText("");
                edName.setText("");
                edAddress.setText("");
                edAge.setText("");
                edGender.setText("");
                Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();



            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }//END
    public void Delete_btn_Click(){
        //Create LayoutInfiltrator
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.delete_layout, null);

        EditText delete_id = view.findViewById(R.id.edDelete_id);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Delete Data");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            String id = delete_id.getText().toString();
            if (id.isEmpty()){
                Toast.makeText(this, "All Field Required", Toast.LENGTH_SHORT).show();
            }else {
                SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                sqLiteDatabase.execSQL("DELETE FROM Student_table WHERE id = '"+id+"'");
                delete_id.setText("");
                Toast.makeText(this, "Delete Successfully", Toast.LENGTH_SHORT).show();
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            delete_id.setText("");
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}//Main Class
