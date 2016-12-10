package org.burguer.poli.poliburguer.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import org.burguer.poli.poliburguer.models.FirebaseModel;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ListChildEventListener<T> implements ChildEventListener {
    private final Class<T> type;
    private List<T> list;

    public abstract void onUpdate();

    public ListChildEventListener(Class<T> type, List<T> list) {
        this.type = type;
        this.list = list;
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
        list.add(toModel(snapshot));
        onUpdate();
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
        String key = snapshot.getKey();
        for (ListIterator<T> i = list.listIterator(); i.hasNext(); ) {
            FirebaseModel item = (FirebaseModel)i.next();
            if (item.getKey().equals(key)) {
                i.set(toModel(snapshot));
                onUpdate();
                return;
            }
        }
        throw new RuntimeException("Changing nonexistent id");
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
        String key = snapshot.getKey();
        for (Iterator<T> i = list.listIterator(); i.hasNext(); ) {
            FirebaseModel item = (FirebaseModel)i.next();
            if (item.getKey().equals(key)) {
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

    private T toModel(DataSnapshot snapshot) {
        FirebaseModel model = (FirebaseModel)snapshot.getValue(type);
        model.setKey(snapshot.getKey());
        return (T)model;
    }
}
