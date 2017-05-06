package classact.com.xprize.activity.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by hyunchanjeong on 2017/05/06.
 */

public abstract class ItemCollection implements Item {

    protected String name;
    protected List<Item> itemList;
    protected LinkedHashMap<String, Item> itemMap;
    private LinkedHashMap<Item, String> itemRefMap;

    protected ItemCollection(String name) {
        this.name = name;
        this.itemList = new ArrayList<>();
        this.itemMap = new LinkedHashMap<>();
        this.itemRefMap = new LinkedHashMap<>();
    }

    public boolean addItem(Item item) {
        if (!itemList.contains(item)) {
            itemList.add(item);
            itemMap.put(item.getName(), item);
            itemRefMap.put(item, item.getName());
            return true;
        }
        return false;
    }

    public boolean removeItem(Item item) {
        if (itemList.contains(item)) {
            itemList.remove(item);
            String key = itemRefMap.get(item);
            itemMap.remove(key);
            itemRefMap.remove(item);
            return true;
        }
        return false;
    }

    public Item getItem(int index) {
        Item item = null;
        if (itemList.size() > index) {
            item = itemList.get(index);
        }
        return item;
    }

    public Item getItem(String key) {
        Item item = null;
        if (itemMap.containsKey(key)) {
            item = itemMap.get(key);
        }
        return item;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public LinkedHashMap<String, Item> getItemMap() {
        return itemMap;
    }

    public String getName() {
        return name;
    }
}