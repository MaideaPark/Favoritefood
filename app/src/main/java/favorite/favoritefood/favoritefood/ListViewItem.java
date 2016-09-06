package favorite.favoritefood.favoritefood;

/**
 * Created by macintosh on 2016. 9. 4..
 */
import android.graphics.drawable.Drawable;

/**
 * Created by 202-13 on 2016-08-24.
 */
public class ListViewItem {
    private String storeName;
    private String addressName;

    public ListViewItem(String storeName, String addressName){
        this.storeName=storeName;
        this.addressName=addressName;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getAddressName() {
        return addressName;
    }
    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

}

