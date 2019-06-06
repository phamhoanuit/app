package com.example.firebaseexample.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseexample.R;
import com.example.firebaseexample.adapter.StudentAdapter;
import com.example.firebaseexample.adapter.StudentDialogFragment;
import com.example.firebaseexample.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseDatabaseActivity extends AppCompatActivity implements StudentDialogFragment.DataReturn {

    @BindView(R.id.rcvDSSinhVien)
    RecyclerView recyclerView;
    @BindView(R.id.edtNameStudent)
    EditText edtName;
    @BindView(R.id.edtNamSinh)
    EditText edtYear;
    @BindView(R.id.edtQueQuan)
    EditText edQueQuan;
    @BindView(R.id.edtThuongTru)
    EditText edtNoioHienTai;
    @BindView(R.id.ckbNam)
    CheckBox ckNam;
    @BindView(R.id.ckbNu)
    CheckBox ckNu;
    @BindView(R.id.insert_student)
    Button btnInsert;
    @BindView(R.id.no_data)
    TextView txtNoData;
    List<Student> studentList;
    Student student;
    StudentAdapter adapter;
    long maxId = 0;
    long updateMaxId = 0;

    private DatabaseReference mRef;
    private static FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_database);
        ButterKnife.bind(this);
        studentList = new ArrayList<>();
        processCheckBox();
        mRef = FirebaseDatabaseActivity.getINSTANCE().getReference("students");
        getMaxId(FirebaseDatabaseActivity.getINSTANCE().getReference("students"));
        showDataToRecyclerView();
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void processCheckBox() {
        ckNam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ckNu.setChecked(false);
                } else {
                    ckNu.setChecked(true);
                }
            }
        });

        ckNu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ckNam.setChecked(false);
                } else {
                    ckNam.setChecked(true);
                }
            }
        });
    }

    private void getMaxId(DatabaseReference mRefs) {
        mRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxId = dataSnapshot.getChildrenCount();
                    studentList = new ArrayList<>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Student student = noteDataSnapshot.getValue(Student.class);
                        while(maxId < student.getId()){
                            maxId++;
                        }
                        studentList.add(student);
                    }
                    adapter.updateList(studentList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static FirebaseDatabase getINSTANCE() {
        if (database == null) {
            synchronized (FirebaseDatabase.class) {
                if (database == null) {
                    database = FirebaseDatabase.getInstance();
                }
            }
        }
        return database;
    }

    private void insertData() {
        student = new Student();
        if (edtName.getText().length() != 0 || edQueQuan.getText().length() != 0 || edtNoioHienTai.getText().length() != 0 || edtYear.getText().length() != 0) {
            if (ckNam.isChecked()) {
                student.setGioiTinh("Nam");
            } else if (ckNu.isChecked()) {
                student.setGioiTinh("Nữ");
            }
            student.setNamSinh(edtYear.getText().toString());
            student.setNoiThuongTru(edtNoioHienTai.getText().toString());
            student.setQueQuan(edQueQuan.getText().toString());
            student.setHoTen(edtName.getText().toString());
            if(updateMaxId > maxId){
                maxId = updateMaxId;
            }
            student.setId(maxId + 1);
            mRef.child(String.valueOf(maxId + 1)).setValue(student);
            adapter.notifyDataSetChanged();

        } else {
            Toast.makeText(this, "Bạn hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDataToRecyclerView() {
        adapter = new StudentAdapter(studentList, this);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(FirebaseDatabaseActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getDataReturn(Student student) {
        mRef = FirebaseDatabaseActivity.getINSTANCE().getReference("students");
        mRef.child(String.valueOf(student.getId())).setValue(student);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getIdReturn(long id) {
        mRef = FirebaseDatabaseActivity.getINSTANCE().getReference("students");
        mRef.child(String.valueOf(id)).removeValue();
        adapter.notifyDataSetChanged();
    }
}
