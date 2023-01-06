package be.ehb.boopmap.data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Pin {
    private String uid;
    private double lat;
    private double lng;
    private String titel;
    private String description;

    public Pin(String uid, double lat, double lng, String titel, String description) {
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.titel = titel;
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
