package ai.testtask.fasten.weather.current;

public final class WeatherFormatter {

    private static final String CELSIUS_SYMBOL = " \u2103";
    private static final String TEMPERATURE = "Temperature: ";
    private static final String DIVIDER = " | ";

    private static final String WIND = "Wind: ";
    private static final String COMMA = ", ";
    private static final String MPS = " m/s";
    private static final double ONE_KPH_IN_MPS = 0.27777777777778;

    private static final String HUMIDITY = "Humidity: ";
    private static final String PERCENTAGE = " %";

    public String formatTemperature(String low, String high) {
        StringBuilder builder = new StringBuilder();

        builder.append(TEMPERATURE)
                .append(low)
                .append(CELSIUS_SYMBOL)
                .append(DIVIDER)
                .append(high)
                .append(CELSIUS_SYMBOL);

        return builder.toString();
    }

    public String formatWind(int windSpeed, String direction) {
        StringBuilder builder = new StringBuilder();

        builder.append(WIND)
                .append(convertWindSpeed(windSpeed))
                .append(MPS)
                .append(COMMA)
                .append(direction);
        return builder.toString();
    }

    private String convertWindSpeed(int windSpeed) {
        double result = ((double) windSpeed) * ONE_KPH_IN_MPS;
        return String.valueOf((int) result);
    }

    public String formatHumidity(int humidity) {
        StringBuilder builder = new StringBuilder();

        builder.append(HUMIDITY)
                .append(humidity)
                .append(PERCENTAGE);

        return builder.toString();
    }

    public String formatDate(String weekDay, int day, String month) {
        StringBuilder builder = new StringBuilder();

        builder.append(weekDay)
                .append(COMMA)
                .append(day)
                .append(" ")
                .append(month);
        return builder.toString();
    }

    public String formatShortTemperature(String low, String high) {
        StringBuilder builder = new StringBuilder();

        builder.append(low)
                .append(CELSIUS_SYMBOL)
                .append(DIVIDER)
                .append(high)
                .append(CELSIUS_SYMBOL);

        return builder.toString();
    }

}
