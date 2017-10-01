package ai.testtask.fasten.weather.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import ai.testtask.fasten.R;
import ai.testtask.fasten.core.FormViewState;
import ai.testtask.fasten.core.PresenterManager;
import ai.testtask.fasten.databinding.SearchWeatherActivityBinding;
import ai.testtask.fasten.weather.current.RetryHandler;
import ai.testtask.fasten.weather.current.WeatherAdapter;
import ai.testtask.fasten.weather.model.weather.Weather;

public final class SearchWeatherActivity extends AppCompatActivity implements ISearchWeatherView, RetryHandler {

    private static final String TAG = SearchWeatherActivity.class.getSimpleName();

    private int mPresenterId;
    private SearchWeatherPresenter mPresenter;
    private WeatherAdapter mRecyclerAdapter;
    private SearchWeatherActivityBinding mBinding;
    private ArrayAdapter<String> mAutocompleteAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, SearchWeatherActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.search_weather_activity);

        mPresenterId = SearchWeatherActivity.class.getSimpleName().hashCode();
        mPresenter = PresenterManager.getPresenter(mPresenterId, new PresenterManager.PresenterFactory<SearchWeatherPresenter>() {
            @Override
            public SearchWeatherPresenter createPresenter() {
                return new SearchWeatherPresenter();
            }
        });

        mBinding.setRetryHandler(this);

        mAutocompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        mBinding.searchView.setAdapter(mAutocompleteAdapter);
        mBinding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.searchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.searchWeatherForCity(position);
            }
        });

        mRecyclerAdapter = new WeatherAdapter();
        mBinding.recycler.setAdapter(mRecyclerAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attachView(this);
    }

    @Override
    protected void onStop() {
        mPresenter.detachView(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            PresenterManager.removePresenter(mPresenterId);
        }
        super.onDestroy();
    }


    @Override
    public void showSuggestionsLoading() {
        mBinding.setSuggestionsState(FormViewState.LOADING);
    }

    @Override
    public void showSuggestions(List<String> cities) {
        mBinding.setSuggestionsState(FormViewState.SUCCESS);
        mAutocompleteAdapter.addAll(cities);
        mAutocompleteAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSuggestionsError(String msg) {
        mBinding.setSuggestionsState(FormViewState.ERROR);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        mBinding.setWeatherState(FormViewState.LOADING);
    }

    @Override
    public void showError(String msg) {
        mBinding.setWeatherState(FormViewState.ERROR);
        mBinding.setErrorMessage(msg);
    }

    @Override
    public void showWeather(Weather weather) {
        mBinding.setWeatherState(FormViewState.SUCCESS);
        String cityName = weather.getLocation().getCountryName()
                .concat(", ")
                .concat(weather.getLocation().getCityName());
        mBinding.setCityName(cityName);

        mRecyclerAdapter.setData(weather.getForecastDay());
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRetryClick(View view) {
        mPresenter.retry();
    }
}
