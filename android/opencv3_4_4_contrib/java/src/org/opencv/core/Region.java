package org.opencv.core;

import java.util.Objects;

public class Region {
    private Point tl;
    private Point tr;
    private Point bl;
    private Point br;

    public Region(Point tl, Point tr, Point bl, Point br) {
        this.tl = tl;
        this.tr = tr;
        this.bl = bl;
        this.br = br;
    }

    public Boolean contains(Point p) {
        return ((tl.x > p.x) && (p.x > br.x) && (tl.y > p.y) && (p.y > br.y)) ||
                ((tl.x < p.x) && (p.x < br.x) && (tl.y < p.y) && (p.y < br.y)) ||
                ((tl.x < p.x) && (p.x < br.x) && (tl.y > p.y) && (p.y > br.y)) ||
                ((tl.x > p.x) && (p.x > br.x) && (tl.y < p.y) && (p.y < br.y));
    }

    public Point center() {
        return new Point((tl.x + tr.x + bl.x + br.x) / 4, (tl.y + tr.y + bl.y + br.y) / 4);
    }

    public static Region empty() {
        return new Region(zeroPoint(), zeroPoint(), zeroPoint(), zeroPoint());
    }

    public static Region fromCorners(Mat corners) {
        return new Region(
                new Point(corners.get(0, 0)[0], corners.get(0, 0)[1]),
                new Point(corners.get(0, 1)[0], corners.get(0, 1)[1]),
                new Point(corners.get(0, 2)[0], corners.get(0, 2)[1]),
                new Point(corners.get(0, 3)[0], corners.get(0, 3)[1])
        );
    }


    private static Point zeroPoint() {
        return new Point(0.0, 0.0);
    }

    public Point getTl() {
        return tl;
    }

    public Point getTr() {
        return tr;
    }

    public Point getBl() {
        return bl;
    }

    public Point getBr() {
        return br;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return tl.equals(region.tl) &&
                tr.equals(region.tr) &&
                bl.equals(region.bl) &&
                br.equals(region.br);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tl, tr, bl, br);
    }
}
