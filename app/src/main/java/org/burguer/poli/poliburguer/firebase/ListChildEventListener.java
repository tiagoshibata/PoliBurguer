package org.burguer.poli.poliburguer.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class ListChildEventListener implements ChildEventListener {
    private List<Map<String, String>> list;

    public abstract void onUpdate();
    public abstract Map<String, String> snapshotToMap(DataSnapshot snapshot);

    public ListChildEventListener(List<Map<String, String>> list) {
        this.list = list;
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
        list.add(snapshotToMap(snapshot));
        onUpdate();
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
        String key = snapshot.getKey();
        for (Map<String, String> m : list) {
            if (m.get("key").equals(key)) {
                m.putAll(snapshotToMap(snapshot));
                onUpdate();
                return;
            }
        }
        throw new RuntimeException("Changing nonexistent id");
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
        String key = snapshot.getKey();
        for (Iterator<Map<String, String>> i = list.listIterator(); i.hasNext(); ) {
            Map<String, String> m = i.next();
            if (m.get("key").equals(key)) {
                i.remove();
                onUpdate();
                return;
            }
        }
        throw new RuntimeException("Removing nonexistent id");
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
        String key = snapshot.getKey();
        Log.d("PoliBurguer", "onChildMoved ignored: key = " + key);
    }

    private Map<String, String> toMap(DataSnapshot snapshot) {
        Map<String, String> map = snapshotToMap(snapshot);
        map.put("key", snapshot.getKey());
        return map;
    }
}
