package org.services;

import org.models.Pixel;


import java.util.ArrayList;
import java.util.List;

public class PathUtil {
    public static List<Pixel<Integer>> calculatePath() {
        List<Pixel<Integer>> pixels = new ArrayList<>();
        for (int i = 0; i < 800; i++) {
            pixels.add(new Pixel<>(i, i));
        }
        return pixels;

    }
}
