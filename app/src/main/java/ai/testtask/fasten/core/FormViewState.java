package ai.testtask.fasten.core;

import android.support.annotation.IntDef;


@IntDef({FormViewState.FORM, FormViewState.LOADING, FormViewState.ERROR, FormViewState.SUCCESS})
public @interface FormViewState {
    int FORM = 0;
    int LOADING = 1;
    int ERROR = 2;
    int SUCCESS = 3;
}
