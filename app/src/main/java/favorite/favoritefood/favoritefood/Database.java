package favorite.favoritefood.favoritefood;

/**
 * Created by macintosh on 2016. 9. 4..
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by 202-13 on 2016-08-23.
 */
public class Database extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public Database(Context context) {
        super(context, "STORE", null, 1);
    }
    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE STORE (_id INTEGER PRIMARY KEY AUTOINCREMENT, address TEXT, store TEXT, comment TEXT);");
    }
    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void insert(String address, String store, String comment) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO STORE VALUES(null, '" + address + "', '" + store + "', '" + comment + "');");
        db.close();
    }
    public void update(String address, String store, String comment, int id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE STORE SET address='" + address + "',store='" + store + "',comment='"+comment+"' WHERE _id="+id);
        db.close();
    }
    public void delete(String store) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM STORE WHERE store='" + store + "';");
        db.close();
    }
    public String getAddressResult(int i) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT address FROM STORE WHERE _id="+i, null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        return result;
    }
    public String getStoreResult(int i) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT store FROM STORE WHERE _id="+i, null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        return result;
    }
    public String getCommentResult(int i) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT comment FROM STORE WHERE _id="+i, null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        return result;
    }
    public int getTotalCount(){
        SQLiteDatabase db = getReadableDatabase();
        int count;
        Cursor cursor = db.rawQuery("SELECT * FROM STORE",null);
        count = cursor.getCount();
        return count;
    }
    public int getIDResult(String store) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        int result=0;
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT _id FROM STORE WHERE store='"+store+"'", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0);
        }
        return result;
    }
    public int getLastID(){
        SQLiteDatabase db = getReadableDatabase();
        int result=0;

        Cursor cursor = db.rawQuery("SELECT * FROM STORE ORDER BY _id DESC limit 1", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0);
        }
        return result;
    }
}
