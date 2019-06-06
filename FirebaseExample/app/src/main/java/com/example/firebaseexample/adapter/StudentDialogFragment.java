package com.example.firebaseexample.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.firebaseexample.R;
import com.example.firebaseexample.model.Student;

public class StudentDialogFragment extends DialogFragment {

    public interface DataReturn {
        void getDataReturn(Student student);

        void getIdReturn(long id);
    }

    private DataReturn mData;
    Button btnCancel, btnUpdate, btnDelete;
    String name, place, year, gender, home = "";
    long id = 0;
    EditText edtName, edtPlace, edtYear, edtHome;
    CheckBox chkNam, chkNu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        place = getArguments().getString("place");
        year = getArguments().getString("year");
        gender = getArguments().getString("gender");
        home = getArguments().getString("home");
        id = getArguments().getLong("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_student_item, container, false);
        btnCancel = v.findViewById(R.id.button_back);
        btnDelete = v.findViewById(R.id.button_delete);
        btnUpdate = v.findViewById(R.id.button_update);
        chkNam = v.findViewById(R.id.ckb_nam);
        chkNu = v.findViewById(R.id.ckb_nu);
        edtHome = v.findViewById(R.id.editThuongTru);
        edtName = v.findViewById(R.id.editNameStudent);
        edtPlace = v.findViewById(R.id.editQueQuan);
        edtYear = v.findViewById(R.id.editNamSinh);

        if (gender.equals("Nam")) {
            chkNam.setChecked(true);
            chkNu.setChecked(false);
        } else {
            chkNu.setChecked(true);
            chkNam.setChecked(false);
        }
        processCheckBox();

        edtYear.setText(year);
        edtPlace.setText(place);
        edtName.setText(name);
        edtHome.setText(home);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();
                student.setNamSinh(edtYear.getText().toString());
                if (chkNam.isChecked()) {
                    student.setGioiTinh("Nam");
                } else {
                    student.setGioiTinh("Nữ");
                }
                student.setNoiThuongTru(edtHome.getText().toString());
                student.setQueQuan(edtPlace.getText().toString());
                student.setHoTen(edtName.getText().toString());
                student.setId(id);
                mData.getDataReturn(student);
                getDialog().dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.getIdReturn(id);
                getDialog().dismiss();
            }
        });
        return v;
    }

    private void processCheckBox() {
        chkNam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chkNu.setChecked(false);
                } else {
                    chkNu.setChecked(true);
                }
            }
        });

        chkNu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chkNam.setChecked(false);
                } else {
                    chkNam.setChecked(true);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mData = (DataReturn) getActivity();
        } catch (Exception ex) {

        }
    }
}
