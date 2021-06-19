package com.example.app_ghichu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.TQGhiChu.Search.ObjFlag;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;
import com.example.app_ghichu.TQGhiChuDaXoa.TongQuatGhiChuDaXoa;
import com.example.app_ghichu.ThemCV.ThemCongViec;

public class MainActivity extends AppCompatActivity {

    DBGhiChu db;
    ImageView imageThemCongViec;
    CardView cvTongQuatGhiChu, cvDaXoaGanDay;
    TextView soGhiChu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI ();

        // Khởi tạo database
        DataBaseGhiChu ();

        // click vào thêm công việc
        SuKienOnclickThemCV();

        // click vào tổng quát ghi chú
        SuKienOnclickTongQuatGhiChu();

        // click vào đã xóa gần đây
        SuKienOnclickDaXoaGanDay();

        // Đếm số rows trong table
        CountRows();
    }

    // Khởi tạo ánh xạ
    public void initUI () {
        imageThemCongViec = findViewById(R.id.imgv_them_cong_viec);
        cvTongQuatGhiChu = findViewById(R.id.cv_ghichu);
        cvDaXoaGanDay = findViewById(R.id.cv_daxoaganday);
        soGhiChu = findViewById(R.id.tv_dem_ghi_chu);
    }

    // click vào thêm công việc
    public void SuKienOnclickThemCV () {
        imageThemCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemCongViec.class);
                intent.putExtra("ten_tieu_de", "Thư mục");
                startActivity(intent);
            }
        });
    }

    // click vào tổng quát ghi chú
    public void SuKienOnclickTongQuatGhiChu () {
        cvTongQuatGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ObjFlag(1);
                Intent intent = new Intent(MainActivity.this, TongQuatGhiChu.class);
                startActivity(intent);
            }
        });
    }

    // click vào đã xóa gần đây
    public void SuKienOnclickDaXoaGanDay () {
        cvDaXoaGanDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TongQuatGhiChuDaXoa.class);
                startActivity(intent);
            }
        });
    }

    // Đếm số rows trong table
    public void CountRows () {
        Cursor cursor = db.TraVe("SELECT * FROM CongViec");
        soGhiChu.setText(String.valueOf(cursor.getCount()));
    }

    // Khởi tạo database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);
    }
}