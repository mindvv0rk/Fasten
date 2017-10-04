package ai.testtask.fasten.core;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractPresenter<V extends IView> {
    private List<V> mViews = new ArrayList<>();
    private Long mDetachedTime = null;

    public void attachView(@NonNull V view) {
        mViews.add(view);
        mDetachedTime = null;
        onViewAttached(view);
    }

    public void detachView(@NonNull V view) {
        onViewDetached(view);
        mDetachedTime = System.currentTimeMillis();
        mViews.remove(view);
    }

    public void detachAllViews() {
        Iterator<V> it = mViews.iterator();
        while (it.hasNext()) {
            V view = it.next();
            onViewDetached(view);
            mDetachedTime = System.currentTimeMillis();
            it.remove();
        }
    }

    public boolean isAttached() {
        return mViews.size() > 0;
    }

    public List<V> getViews() {
        return mViews;
    }

    protected void onCreate() {
    }

    protected void onViewAttached(@NonNull V view) {
    }

    protected void onViewDetached(@NonNull V view) {
    }

    protected void onDestroy() {
    }

    public Long getDetachedTime() {
        return mDetachedTime;
    }

}
