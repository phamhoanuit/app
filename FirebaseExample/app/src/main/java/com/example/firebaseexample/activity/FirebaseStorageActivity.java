package com.example.firebaseexample.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebaseexample.R;
import com.example.firebaseexample.model.Coffee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseStorageActivity extends AppCompatActivity {

    @BindView(R.id.pick_image)
    ImageView imgTesting;
    @BindView(R.id.editPrice)
    EditText edtPrice;
    @BindView(R.id.editName)
    EditText edtName;
    @BindView(R.id.SaveData)
    Button btnSave;
    @BindView(R.id.progress_wait)
    ProgressBar progressBar;
    @BindView(R.id.float_action_button)
    FloatingActionButton fReturn;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://goooglefribase.appspot.com");
    private DatabaseReference mRef = FirebaseDatabaseActivity.getINSTANCE().getReference();

    public static final int SELECT_FILE = 0;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_storage);
        ButterKnife.bind(this);
        imgTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                upload();
            }
        });

        fReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("IntentReset")
    public void loadData() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "select file"), SELECT_FILE);
    }

    private void upload() {
        if (edtName.getText().length() == 0 || edtPrice.getText().length() == 0) {
            Toast.makeText(this, "Bạn hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else {
            uploadPictureToFirebaeStorage();
        }
    }

    private void uploadPictureToFirebaeStorage() {
        Calendar calendar = Calendar.getInstance();
        StorageReference storageReference = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
        imgTesting.setDrawingCacheEnabled(true);
        imgTesting.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgTesting.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(FirebaseStorageActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                uri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadDatatoFirebaseDatabase(uri);
                    }
                });
            }
        });
    }

    private void uploadDatatoFirebaseDatabase(Uri uri) {
        Coffee cafe = new Coffee(edtName.getText().toString(), Double.parseDouble(edtPrice.getText().toString()), String.valueOf(uri.toString()));
        mRef.child("Food").push().setValue(cafe, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FirebaseStorageActivity.this, "Bạn đã thêm thành công dữ liệu lên firebase ", Toast.LENGTH_SHORT).show();
                    edtName.setText("");
                    edtPrice.setText("");
                    imgTesting.setImageResource(R.drawable.image_no);
                } else {
                    Toast.makeText(FirebaseStorageActivity.this, "Thêm dữ liệu firebase không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Lấy image và gán vào imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (resultCode) {
                case SELECT_FILE:
                    Uri uri = data.getData();
                    imgTesting.setImageURI(uri);
                    break;
                case CAMERA_REQUEST:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imgTesting.setImageBitmap(photo);
                    break;
                default:
                    Log.d("AAA", "onActivityResult: error");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
