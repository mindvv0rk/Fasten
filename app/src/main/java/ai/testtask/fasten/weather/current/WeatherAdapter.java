package ai.testtask.fasten.weather.current;

import com.squareup.picasso.Picasso;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ai.testtask.fasten.R;
import ai.testtask.fasten.databinding.CurrentDayWeatherViewBinding;
import ai.testtask.fasten.databinding.ForecastDayWeatherViewBinding;
import ai.testtask.fasten.weather.model.response.weather.ForecastDay;

public final class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CURRENT_DAY_VIEW_TYPE = 0;
    private static final int FORECAST_DAY_VIEW_TYPE = 1;

    private List<ForecastDay> mForecastDay;


    public WeatherAdapter() {
        mForecastDay = new ArrayList<>();
    }

    public void setData(List<ForecastDay> forecastDays) {
        mForecastDay.clear();
        mForecastDay.addAll(forecastDays);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CURRENT_DAY_VIEW_TYPE;
        } else {
            return FORECAST_DAY_VIEW_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == CURRENT_DAY_VIEW_TYPE) {
            CurrentDayWeatherViewBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.current_day_weather_view,
                    parent,
                    false);
            return new CurrentDayViewHolder(binding.getRoot());
        } else if (viewType == FORECAST_DAY_VIEW_TYPE) {
            ForecastDayWeatherViewBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.forecast_day_weather_view,
                    parent,
                    false);
            return new ForecastDayViewHolder(binding.getRoot());
        } else {
            throw new RuntimeException("Invalid view type!");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CurrentDayViewHolder) {
            ((CurrentDayViewHolder) holder).setData(mForecastDay.get(position));
        } else if (holder instanceof ForecastDayViewHolder) {
            ((ForecastDayViewHolder) holder).setData(mForecastDay.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mForecastDay.size();
    }

    private class CurrentDayViewHolder extends RecyclerView.ViewHolder {

        private CurrentDayWeatherViewBinding mBinding;

        public CurrentDayViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void setData(ForecastDay forecastDay) {
            WeatherFormatter formatter = new WeatherFormatter();

            String temperature = formatter.formatTemperature(forecastDay.getLow().getCelsius(),
                    forecastDay.getHigh().getCelsius());

            String wind = formatter.formatWind(forecastDay.getWind().getSpeedInKph(),
                    forecastDay.getWind().getDirection());
            String humidity = formatter.formatHumidity(forecastDay.getAverageHumidity());

            mBinding.setTemperature(temperature);
            mBinding.setWind(wind);
            mBinding.setHumidity(humidity);

            Picasso.with(mBinding.getRoot().getContext())
                    .load(forecastDay.getIconUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mBinding.weatherIcon);
        }
    }

    private class ForecastDayViewHolder extends RecyclerView.ViewHolder {

        private ForecastDayWeatherViewBinding mBinding;

        public ForecastDayViewHolder(View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);
        }

        public void setData(ForecastDay forecastDay) {
            WeatherFormatter formatter = new WeatherFormatter();
            String date = formatter.formatDate(forecastDay.getDate().getWeekDayShort(),
                    forecastDay.getDate().getDay(),
                    forecastDay.getDate().getMonthName());


            String temperature = formatter.formatShortTemperature(forecastDay.getLow().getCelsius(),
                    forecastDay.getHigh().getCelsius());

            mBinding.setTemperature(temperature);
            mBinding.setDate(date);

            Picasso.with(mBinding.getRoot().getContext())
                    .load(forecastDay.getIconUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mBinding.icon);
        }
    }
}
