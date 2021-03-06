package yeseul.kr.hs.emirim.dbex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edit_group_name, edit_group_count, edit_result_name, edit_result_count;
    Button but_init, but_insert, but_select, but_update, but_delete;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_group_name = (EditText)findViewById(R.id.edit_group_name);
        edit_group_count = (EditText)findViewById(R.id.edit_group_count);
        but_init = (Button)findViewById(R.id.but_init);
        but_insert = (Button)findViewById(R.id.but_insert);
        but_insert = (Button)findViewById(R.id.but_insert);
        but_select = (Button)findViewById(R.id.but_select);
        edit_result_name = (EditText)findViewById(R.id.edit_result_name);
        edit_result_count = (EditText)findViewById(R.id.edit_result_count);
        but_update = (Button)findViewById(R.id.but_update);
        but_delete = (Button)findViewById(R.id.but_delete);

        //DB 생성
        myHelper = new MyDBHelper(this);
        //기존의 테이블이 존재하면 삭제하고 테이블을 새로 생성한다.
        but_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDb, 1, 2);
                sqlDb.close();
            }
        });
        but_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb = myHelper.getWritableDatabase();
                String sql = "insert into idolTable values('"+edit_group_name.getText()+"', " +edit_group_count.getText()+")";
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this, "저장됨", Toast.LENGTH_LONG).show();
            }
        });
        but_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb = myHelper.getReadableDatabase();
                String sql = "Select * from idolTable";
                Cursor cursor = sqlDb.rawQuery(sql, null);
                String names = "Idol 이름" + "\r\n" + "-------------" + "\r\n";
                String counts = "Idol 인원수" + "\r\n" + "-------------" + "\r\n";
                while(cursor.moveToNext()){
                    names += cursor.getString(0)+"\r\n"; //괄호 안의 값은 인덱스
                    counts += cursor.getInt(1)+"\r\n";
                }
                edit_result_name.setText(names);
                edit_result_count.setText(counts);
                cursor.close();
                sqlDb.close();
            }
        });
        but_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb = myHelper.getWritableDatabase();
                String sql = "update idolTable set idolCount="+edit_group_count.getText()+" where idolName='"+edit_group_name.getText()+"'";
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this, "수정됨", Toast.LENGTH_LONG).show();
            }
        });
        but_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb = myHelper.getWritableDatabase();
                String sql = "delete from idolTable where idolName='"+edit_group_name.getText()+"'";
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this, "삭제됨", Toast.LENGTH_LONG).show();
            }
        });
    }

    class MyDBHelper extends SQLiteOpenHelper{
        // idolDB라는 이름의 데이터베이스가 생성된다.
        public MyDBHelper(Context context) {
            super(context, "idolDB", null, 1);
        }
        // idolTable라는 이름의 테이블 생성
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table idolTable(idolName text not null primary key, idolCount integer)";
            db.execSQL(sql);
        }
        // 이미 idolTable이 존재한다면 기존의 테이블을 삭제하고 새로 테이블을 만들 때 호출
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "drop table if exists idolTable";
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
