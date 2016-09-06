package favorite.favoritefood.favoritefood;

/**
 * Created by macintosh on 2016. 9. 4..
 */
public class Data {
    private String address;
    private String store;
    private String comment;
    public Data(String address, String store, String comment){
        this.address=address;
        this.store=store;
        this.comment=comment;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getStore() {
        return store;
    }
    public void setStore(String store) {
        this.store = store;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
