package com.example.app_ghichu.TQGhiChu.UpdateGhiChu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.MainActivity;
import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.Search.ObjFlag;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;

import java.util.Calendar;

public class UpdateGhiChu extends AppCompatActivity {

    FrameLayout Back;
    EditText edtGhiChu;
    TextView luuGhiChu;
    DBGhiChu db;

    // String ghi chú mới
    String value;

    // String giờ phút hiện tại
    String gioPhut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ghi_chu);

        // Khởi tạo bên UI
        initUI();

        // Khởi tạo DataBase
        DataBaseGhiChu();

        // Sự kiện khi click vào button back
        SuKienOnclickBtnBack();

        // Sửa ghi chú
        SuaGhiChu ();

        // Bật bàn phím
        BatBanPhim ();
    }

    // Khởi tạo bên UI
    public void initUI () {
        Back = findViewById(R.id.frame_back);
        edtGhiChu = findViewById(R.id.edt_search_ghichu);
        luuGhiChu = findViewById(R.id.tv_luu_ghichu);
    }

    // Sự kiện khi click vào button back
    public void SuKienOnclickBtnBack () {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjFlag.getFlag() == 0) {
                    new ObjFlag(1);
                } else {
                    new ObjFlag(1);
                }
                Intent intent = new Intent(UpdateGhiChu.this, TongQuatGhiChu.class);
                startActivity(intent);
            }
        });
    }

    // Sửa ghi chú
    public void SuaGhiChu () {

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String ghiChu = intent.getStringExtra("ghichu");
        String ngaythang = intent.getStringExtra("ngaythang");

        // Set text của ghi chú hiện tại cho sửa ghi chú
        edtGhiChu.setText(ghiChu);

        // Lấy value khi thay đổi ghi chú
        if (!TextUtils.isEmpty(edtGhiChu.getText().toString().trim())) {

            // Khi click button xong bắt đầu lấy value về
            luuGhiChu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Tắt bàn phím ảo
                    TatBanPhim();

                    // Lấy ghi chú mới
                    value = edtGhiChu.getText().toString();

                    // Lấy giờ phút hiện tại
                    GioPhutHienTai();

                    // Update ghi chú
                    Update(id, value, gioPhut);
                }
            });
        }
    }

    // Lấy giờ và phút hiện tại
    public void GioPhutHienTai () {
        String gio = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        String phut = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        gioPhut = gio + ":" + phut;
        if (Integer.parseInt(phut) < 10) {
            gioPhut = gio + ":0" + phut;
        }
    }

    // Tắt bàn phím
    public void TatBanPhim () {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtGhiChu.getWindowToken(), 0);
    }

    // Bật bàn phím
    public void BatBanPhim () {
        edtGhiChu.setFocusableInTouchMode(true);
        edtGhiChu.requestFocus();
    }

    //Update ghi chú
    public void Update (int id, String value, String gioPhut) {
        try {
            db.KhongTraVe("UPDATE CongViec SET tenGhiChu = '"+ value +"', gioGhiChu = '"+ gioPhut +"' WHERE id = '"+ id +"'");
            Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Update thất bại" + e, Toast.LENGTH_SHORT).show();
        }
    }

    // Khởi tạo database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);
    }
}