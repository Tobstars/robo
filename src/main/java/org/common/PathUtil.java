package org.common;

import javafx.scene.image.ImageView;
import org.models.Pixel;


import java.util.ArrayList;
import java.util.List;

public class PathUtil {
    public static List<Pixel> calculatePath(ImageView env) {
        List<Pixel> pixels = new ArrayList();
        pixels.add(new Pixel(2,3));
        pixels.add(new Pixel(5,4));
        pixels.add(new Pixel(8,50));
        pixels.add(new Pixel(34,80));
        pixels.add(new Pixel(50,100));
        pixels.add(new Pixel(100,102));
        pixels.add(new Pixel(200,103));
        pixels.add(new Pixel(201,104));
        pixels.add(new Pixel(202,300));
        pixels.add(new Pixel(500,600));
        pixels.add(new Pixel(502,620));
        return pixels;

    }
}
