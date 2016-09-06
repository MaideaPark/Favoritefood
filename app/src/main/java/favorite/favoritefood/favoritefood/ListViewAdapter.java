package favorite.favoritefood.favoritefood;

/**
 * Created by macintosh on 2016. 9. 4..
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
/**
 * Created by 202-13 on 2016-08-24.
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private Button deleteBtn;
    private static boolean deleteCheck=false;
    private SQLiteDatabase db;

    TextView storeName;
    String deleteStore=null;
    @Override
    public int getCount() {
        return listViewItemList.size();
    }
    @Override
    public String getItem(int i) {
        return listViewItemList.get(i).getStoreName();
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        final Context context = viewGroup.getContext();
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        storeName = (TextView) view.findViewById(R.id.storeName) ;
        TextView addressName = (TextView) view.findViewById(R.id.addressName) ;
        deleteBtn = (Button)view.findViewById(R.id.deleteBtn);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(pos);
        deleteStore = listViewItem.getStoreName();
        // 아이템 내 각 위젯에 데이터 반영
        storeName.setText(listViewItem.getStoreName());
        addressName.setText(listViewItem.getAddressName());
        //iconImageView.setImageDrawable(listViewItem.getIcon());
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println();
                Database Mydb =new Database(context);
                db=Mydb.getWritableDatabase();
                ListViewItem item = listViewItemList.get(pos);
                Mydb.delete(item.getStoreName());
                clear();
                for(int i=1;i<=Mydb.getLastID();i++){
                    if(Mydb.getStoreResult(i).equals("")){
                        continue;
                    }
                    else{
                        addItem(Mydb.getStoreResult(i),Mydb.getAddressResult(i));
                    }
                }
                notifyDataSetChanged();
                //System.out.println(item.getStoreName());
            }
        });
        System.out.println(deleteCheck);
        if(deleteCheck){
            deleteBtn.setVisibility(View.VISIBLE);
        }
        else{
            deleteBtn.setVisibility(View.GONE);
        }

        return view;
    }
    public void addItem(String storeName, String addressName){
        ListViewItem item = new ListViewItem(storeName, addressName);
        listViewItemList.add(item);
    }
    public void setDeleteCheck(boolean deleteCheck){
        this.deleteCheck = deleteCheck;
    }
    public boolean getDeleteCheck(){
        return deleteCheck;
    }

    public void clear(){
        listViewItemList.clear();
    }

}
