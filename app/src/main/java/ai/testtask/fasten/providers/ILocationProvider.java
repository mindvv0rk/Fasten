package ai.testtask.fasten.providers;

public interface ILocationProvider {
    public void startLocationUpdates();
    public void stopLocationUpdates();
    public void setLocationCallback(LocationProvider.LocationCallback locationCallback);
}
