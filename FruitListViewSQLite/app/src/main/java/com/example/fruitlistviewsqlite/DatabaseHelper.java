package com.example.fruitlistviewsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "fruit_db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE fruits(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, image TEXT, summary TEXT, detail TEXT)");

        insertFruit(db, "Chuối", "banana",
                "Nguồn năng lượng vàng cho ngày bận rộn.",
                "Chuối là loại trái cây giàu kali và đường tự nhiên. Chuối giúp cung cấp năng lượng nhanh, hỗ trợ hoạt động của cơ bắp và rất phù hợp để ăn trước khi học tập, làm việc hoặc chơi thể thao.");

        insertFruit(db, "Cam", "orange",
                "Kho tàng vitamin C nhỏ xinh.",
                "Cam chứa nhiều vitamin C, giúp tăng cường sức đề kháng, hỗ trợ làm đẹp da và giúp cơ thể cảm thấy tươi tỉnh hơn. Đây là loại trái cây rất phù hợp khi bạn cần bổ sung vitamin hằng ngày.");

        insertFruit(db, "Dưa hấu", "watermelon",
                "Máy cấp nước mùa hè phiên bản trái cây.",
                "Dưa hấu chứa rất nhiều nước, giúp giải khát và làm mát cơ thể. Ngoài ra, dưa hấu còn cung cấp một số vitamin cần thiết, rất thích hợp dùng trong những ngày nóng.");

        insertFruit(db, "Táo", "apple",
                "Ăn mỗi ngày, khỏe mỗi ngày.",
                "Táo chứa chất xơ và chất chống oxy hóa. Ăn táo có thể hỗ trợ tiêu hóa, tốt cho tim mạch và là một món ăn nhẹ lành mạnh cho học sinh, sinh viên.");

        insertFruit(db, "Xoài", "mango",
                "Ngọt ngào nhưng vẫn đầy vitamin.",
                "Xoài giàu vitamin A và vitamin C. Loại trái cây này giúp hỗ trợ thị lực, tăng sức đề kháng và cung cấp năng lượng tự nhiên cho cơ thể.");

        insertFruit(db, "Nho", "grape",
                "Viên kẹo tự nhiên có chất chống oxy hóa.",
                "Nho có vị ngọt tự nhiên và chứa nhiều chất chống oxy hóa. Nho giúp bổ sung năng lượng nhẹ nhàng, hỗ trợ sức khỏe tim mạch và rất dễ ăn.");

        insertFruit(db, "Dứa", "pineapple",
                "Chua chua ngọt ngọt, hỗ trợ tiêu hóa.",
                "Dứa chứa vitamin C và enzyme bromelain. Loại trái cây này có thể hỗ trợ tiêu hóa, giúp bữa ăn nhẹ nhàng hơn và mang lại cảm giác tươi mát.");

        insertFruit(db, "Dâu tây", "strawberry",
                "Nhỏ mà có võ, đẹp da vui miệng.",
                "Dâu tây chứa nhiều vitamin C và chất chống oxy hóa. Dâu tây hỗ trợ sức khỏe làn da, tăng sức đề kháng và thường được dùng trong các món tráng miệng.");

        insertFruit(db, "Bơ", "avocado",
                "Trái cây béo lành mạnh cho não và tim.",
                "Bơ chứa chất béo tốt và chất xơ. Bơ giúp tạo cảm giác no lâu, hỗ trợ sức khỏe tim mạch và cung cấp năng lượng lành mạnh cho cơ thể.");

        insertFruit(db, "Dừa", "coconut",
                "Nước giải khát tự nhiên từ miền nhiệt đới.",
                "Dừa giúp bổ sung nước và khoáng chất cho cơ thể. Nước dừa rất phù hợp để giải khát, đặc biệt sau khi vận động hoặc trong thời tiết nóng.");
    }

    private void insertFruit(SQLiteDatabase db, String name, String image, String summary, String detail) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("summary", summary);
        values.put("detail", detail);
        db.insert("fruits", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS fruits");
        onCreate(db);
    }

    public ArrayList<Fruit> getAllFruits() {
        ArrayList<Fruit> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM fruits", null);

        while (c.moveToNext()) {
            list.add(new Fruit(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4)
            ));
        }

        c.close();
        return list;
    }

    public Fruit getFruitById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM fruits WHERE id = ?", new String[]{String.valueOf(id)});

        Fruit fruit = null;
        if (c.moveToFirst()) {
            fruit = new Fruit(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4)
            );
        }

        c.close();
        return fruit;
    }
}