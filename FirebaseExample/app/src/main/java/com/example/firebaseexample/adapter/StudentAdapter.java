package com.example.firebaseexample.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firebaseexample.R;
import com.example.firebaseexample.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> students;
    private Context context;

    public StudentAdapter(List<Student> students, Context context) {
        this.students = students;
        this.context = context;
    }

    public void updateList(List notes) {
        // Allow recyclerview animations to complete normally if we already know about data changes
        if (notes.size() != this.students.size() || !this.students.containsAll(notes)) {
            this.students = notes;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item, null, false);
        return new StudentViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, final int i) {
        studentViewHolder.txtHome.setText(students.get(i).getNoiThuongTru());
        studentViewHolder.txtPlaceOfBird.setText(students.get(i).getQueQuan());
        studentViewHolder.txtGender.setText(students.get(i).getGioiTinh());
        studentViewHolder.txtAge.setText(students.get(i).getNamSinh());
        studentViewHolder.txtName.setText(students.get(i).getHoTen());
        studentViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", students.get(i).getHoTen());
                bundle.putString("place", students.get(i).getQueQuan());
                bundle.putString("home", students.get(i).getNoiThuongTru());
                bundle.putString("year", students.get(i).getNamSinh());
                bundle.putString("gender", students.get(i).getGioiTinh());
                bundle.putLong("id", students.get(i).getId());

                FragmentActivity activity = (FragmentActivity) context;
                StudentDialogFragment dialogFragment = new StudentDialogFragment();
                FragmentManager fm = activity.getSupportFragmentManager();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, "StudentDialogFragment");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAge, txtGender, txtPlaceOfBird, txtHome;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.hoten_item);
            txtAge = itemView.findViewById(R.id.tuoi_item);
            txtGender = itemView.findViewById(R.id.gioitinh_item);
            txtPlaceOfBird = itemView.findViewById(R.id.noisinh_item);
            txtHome = itemView.findViewById(R.id.diachi_item);
        }
    }
}
