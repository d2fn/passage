package io.measures.passage.geo;

import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.Projectable2D;

import java.awt.*;

import static processing.core.PApplet.*;

/**
 * LinearGeoProjection
 * @author Dietrich Featherston
 */
public class LinearGeoProjection implements GeoProjection {

    private final Projectable2D ulScreen, lrScreen, ulLL, lrLL;

    public LinearGeoProjection(Projectable2D ulScreen, Projectable2D lrScreen, Projectable2D ulLL, Projectable2D lrLL) {
        this.ulScreen = ulScreen;
        this.lrScreen = lrScreen;
        this.ulLL = ulLL;
        this.lrLL = lrLL;
    }

    public Projectable2D screen(float lat, float lng) {
        float x = map(lng+180, ulLL.x(), lrLL.x(), ulScreen.x(), lrScreen.x());
        float y = map(lat+90 , ulLL.y(), lrLL.y(), lrScreen.y(), ulScreen.y());
        return new Point2D(x, y);
    }

    public GeoProjection grow(float xamt, float yamt) {
        Rectangle bounds = new Rectangle(round(ulLL.x()), round(ulLL.y()), round(lrLL.x() - ulLL.x()), round(lrLL.y() - ulLL.y()));
        return new LinearGeoProjection(ulScreen, lrScreen, new Point2D(bounds.x, bounds.y), new Point2D(bounds.x + bounds.width, bounds.y + bounds.width));
    }
}
