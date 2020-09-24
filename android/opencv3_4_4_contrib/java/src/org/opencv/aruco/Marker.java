package org.opencv.aruco;

import org.opencv.core.Region;

import java.util.Objects;

public class Marker {
    private Integer id;
    private Region region;

    public Marker(Integer id, Region region) {
        this.id = id;
        this.region = region;
    }

    public Integer getId() {
        return id;
    }

    public Region getRegion() {
        return region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marker marker = (Marker) o;
        return Objects.equals(id, marker.id) &&
                Objects.equals(region, marker.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, region);
    }
}
