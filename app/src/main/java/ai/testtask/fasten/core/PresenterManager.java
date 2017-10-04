package ai.testtask.fasten.core;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Iterator;

public final class PresenterManager {

    private static final int MAX_PRESENTERS = 20;

    private static final HashMap<Integer, AbstractPresenter> mPresenters = new HashMap<>();

    public interface PresenterFactory<T extends AbstractPresenter> {
        T createPresenter();
    }

    protected PresenterManager() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractPresenter> T getPresenter(int presenterId, PresenterFactory<T> factory) {

        AbstractPresenter presenter = mPresenters.get(presenterId);

        if (presenter == null) {

            presenter = factory.createPresenter();

            if (presenter == null) throw new RuntimeException("Presenter can't be null");

            presenter.onCreate();

            if (mPresenters.size() > MAX_PRESENTERS) {
                removeObsoletePresenters();
            }

            mPresenters.put(presenterId, presenter);
        }
        return (T) presenter;
    }

    public static boolean hasPresenterWithKey(int presenterId) {
        return mPresenters.containsKey(presenterId);
    }

    private static void removeObsoletePresenters() {

        while (mPresenters.size() > MAX_PRESENTERS) {

            long oldestDetachTime = Long.MAX_VALUE;
            Integer idToRemove = null;

            for (int presenterId : mPresenters.keySet()) {

                AbstractPresenter presenter = mPresenters.get(presenterId);

                if (presenter != null && !presenter.isAttached() && presenter.getDetachedTime() != null
                        && presenter.getDetachedTime() < oldestDetachTime) {
                    idToRemove = presenterId;
                }
            }

            if (idToRemove != null) {
                removePresenter(idToRemove);
            }
        }
    }

    public static void removeAllPresenters() {
        Iterator<Integer> it = mPresenters.keySet().iterator();
        while (it.hasNext()) {
            Integer id = it.next();
            mPresenters.get(id).onDestroy();
            it.remove();
        }
    }

    public static void removePresenter(int id) {
        if (mPresenters.containsKey(id)) {
            mPresenters.get(id).onDestroy();
            mPresenters.remove(id);
        }
    }
}
