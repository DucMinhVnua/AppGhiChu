package com.example.app_ghichu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.TQGhiChu.Search.ObjFlag;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;
import com.example.app_ghichu.TQGhiChuDaXoa.SearchGhiChuDaXoa.ObjFlagGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.TongQuatGhiChuDaXoa;
import com.example.app_ghichu.ThemCV.ThemCongViec;

public class MainActivity extends AppCompatActivity {

    DBGhiChu db;
    ImageView imageThemCongViec;
    CardView cvTongQuatGhiChu, cvDaXoaGanDay;
    TextView soGhiChu, soGhiChuDaXoa, tvThumuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo bên UI
        initUI ();

        // Khởi tạo database
        DataBaseGhiChu ();

        // click vào thêm công việc
        SuKienOnclickThemCV();

        // click vào tổng quát ghi chú
        SuKienOnclickTongQuatGhiChu();

        // click vào đã xóa gần đây
        SuKienOnclickDaXoaGanDay();

        // Đếm số rows trong table CongViec
        CountRows();

        // Đếm số rows trong table CongViecDaXoa
        CountRowsDaXoa();
    }

    // Khởi tạo bên UI
    public void initUI () {
        imageThemCongViec = findViewById(R.id.imgv_them_cong_viec);
        cvTongQuatGhiChu = findViewById(R.id.cv_ghichu);
        cvDaXoaGanDay = findViewById(R.id.cv_daxoaganday);
        soGhiChu = findViewById(R.id.tv_dem_ghi_chu);
        soGhiChuDaXoa = findViewById(R.id.tv_dem_ghi_chu_da_xoa);
        tvThumuc = findViewById(R.id.tv_thumuc);
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
                new ObjFlagGhiChuDaXoa(1);
                Intent intent = new Intent(MainActivity.this, TongQuatGhiChuDaXoa.class);
                startActivity(intent);
            }
        });
    }

    // Đếm số rows trong table CongViec
    public void CountRows () {
        Cursor cursor = db.TraVe("SELECT * FROM CongViec");
        soGhiChu.setText(String.valueOf(cursor.getCount()));
    }

    // Đếm số rows trong table CongViecDaXoa
    public void CountRowsDaXoa () {
        Cursor cursor = db.TraVe("SELECT * FROM CongViecDaXoa");
        soGhiChuDaXoa.setText(String.valueOf(cursor.getCount()));
    }

    // Khởi tạo database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);

        // Tạo bảng cho database
        db.KhongTraVe("CREATE TABLE IF NOT EXISTS CongViec (Id INTEGER PRIMARY KEY AUTOINCREMENT, tenGhiChu VARCHAR(200), gioGhiChu DATE)");

        // Tạo bảng cho database
        db.KhongTraVe("CREATE TABLE IF NOT EXISTS CongViecDaXoa (Id INTEGER PRIMARY KEY AUTOINCREMENT, tenGhiChu VARCHAR(200), gioGhiChu DATE)");
    }
}