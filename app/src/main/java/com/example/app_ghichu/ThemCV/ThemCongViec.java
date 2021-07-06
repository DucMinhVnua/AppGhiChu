package com.example.app_ghichu.ThemCV;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.MainActivity;
import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;

import java.util.Calendar;
import java.util.Date;

public class ThemCongViec extends AppCompatActivity {

    EditText edtThemCongViec;
    TextView tenTieuDe, luuCongViec;
    DBGhiChu db;
    FrameLayout Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cong_viec);

        // Animation
        Animation();

        initUI ();

        // Khởi tạo database
        DataBaseGhiChu();

        // Tự hiện bàn phím khi vào thêm công việc
        AutoHienBanPhim();

        // Thêm ghi chú
        ThemGhiChu();

        // Thêm công việc khi lưu vào database
        ThemCongViecDB();

        // Khi click vào nút back
        SuKienOnclickBtnBack ();
    }

    public void initUI () {
        edtThemCongViec = findViewById(R.id.edt_them_CV);
        tenTieuDe = findViewById(R.id.tv_ten_tieu_de);
        luuCongViec = findViewById(R.id.tv_luu_cong_viec);
        Back = findViewById(R.id.frame_back);
    }

    // Khởi tạo database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);
    }

    // Hiện bàn phím
    public void AutoHienBanPhim() {
        // Hiện bàn phím
        BatBanPhim ();
    }

    // Thêm ghi chú
    public void ThemGhiChu () {
        Intent intent = getIntent();
        String valueGhiChu = intent.getStringExtra("ten_tieu_de");
        if (valueGhiChu == "Thư mục") {
            tenTieuDe.setText(valueGhiChu);
        } else {
            tenTieuDe.setText(valueGhiChu);
        }
    }

    // Thêm công việc khi lưu vào database
    public void ThemCongViecDB() {
        luuCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtThemCongViec.getText().toString().trim())) {

                    // Lấy công việc được nhập vào từ edit text
                    String tenCongViec = edtThemCongViec.getText().toString().trim();

                    // Lấy giờ hiện tại
                    String gio = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    String phut = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
                    String ngay = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    String thang = String.valueOf(Calendar.getInstance().get(Calendar.MONTH + 1));

                    String gioPhut = gio + ":" + phut + "" + " (" + ngay + "/" + thang + ")";
                    if (Integer.parseInt(phut) < 10) {
                        gioPhut = gio + ":0" + phut + "" + " (" + ngay + "/" + thang + ")";
                    }
                    System.out.println(tenCongViec + gioPhut);

                    // Thêm công việc vào database
                    try {
                        db.KhongTraVe("INSERT INTO CongViec VALUES (null, '"+ tenCongViec+"', '"+ gioPhut +"')");
                        Toast.makeText(ThemCongViec.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                        // Tắt bàn phím khi lưu xong
                        TatBanPhim ();
                    } catch (Exception e) {
                        Toast.makeText(ThemCongViec.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ThemCongViec.this, "Vui lòng nhập công việc cần thêm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Tắt bàn phím
    public void TatBanPhim () {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtThemCongViec.getWindowToken(), 0);
    }

    // Bật bàn phím
    public void BatBanPhim () {
        edtThemCongViec.setFocusableInTouchMode(true);
        edtThemCongViec.requestFocus();
    }

    // Khi click vào nút back
    public void SuKienOnclickBtnBack () {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = (String) tenTieuDe.getText();

                /*
                * Nếu text bằng ghi chú click sẽ back TongQuatKhiChu
                * Nếu text bằng ghi chú click sẽ back MainActivity.
                */
                switch (ten) {
                    case "Ghi chú":
                        Intent intentGhiChu = new Intent(ThemCongViec.this, TongQuatGhiChu.class);
                        startActivity(intentGhiChu);
                        break;
                    case "Thư mục":
                        Intent intentMain = new Intent(ThemCongViec.this, MainActivity.class);
                        startActivity(intentMain);
                        break;
                }
            }
        });
    }

    // Animation
    public void Animation () {
        YoYo.with(Techniques.Pulse)
                .duration(500)
                .repeat(0)
                .playOn(findViewById(R.id.layout_main));
    }
}