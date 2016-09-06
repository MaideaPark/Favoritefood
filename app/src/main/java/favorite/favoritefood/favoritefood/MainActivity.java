package favorite.favoritefood.favoritefood;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button add,del;
    private ListView list;
    //private ArrayAdapter<ListViewItem> adapter;
    private ListViewAdapter adapter = new ListViewAdapter();;
    private Database Mydb= new Database(this);;
    private SQLiteDatabase db;
    private Button deleteBtn;
    public Database getMydb() {
        return Mydb;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this,Splash.class));
        startActivity(new Intent(this, Splash.class));
        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"추가하기",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(intent);
            }
        });
        del = (Button)findViewById(R.id.del);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"삭제하기",Toast.LENGTH_SHORT).show();
                //deleteBtn.setVisibility(View.VISIBLE);
                boolean check = adapter.getDeleteCheck();
                if(check){
                    adapter.setDeleteCheck(false);
                    adapter.notifyDataSetChanged();
                }
                else{
                    adapter.setDeleteCheck(true);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        list = (ListView)findViewById(R.id.storelist);

        //adapter = new ArrayAdapter<ListViewItem>(getApplicationContext(),android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        db = Mydb.getReadableDatabase();
        //System.out.println(Mydb.getTotalCount());
        // Toast.makeText(getApplicationContext(),Mydb.getTotalCount()+"",Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),Mydb.getLastID()+"",Toast.LENGTH_SHORT).show();
        printList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),"리스트"+adapter.getItem(i)+","+l,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),InfoActivity.class);
                intent.putExtra("store",adapter.getItem(i));
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),"RESUME됨",Toast.LENGTH_SHORT).show();
        adapter.clear();
        printList();
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(getApplicationContext(),"RESTART",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(getApplicationContext(),"STOP",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"PAUSE",Toast.LENGTH_SHORT).show();
    }
    public void printList(){
        //Toast.makeText(getApplicationContext(),Mydb.getLastID(),Toast.LENGTH_SHORT).show();
        for(int i=1;i<=Mydb.getLastID();i++){
            if(Mydb.getStoreResult(i).equals("")){
                continue;
            }
            else{
                adapter.addItem(Mydb.getStoreResult(i),Mydb.getAddressResult(i));
            }
        }
    }
}
