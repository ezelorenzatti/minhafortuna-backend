package br.com.lorenzatti.minhafortuna.backend.shared.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorUtils {

    public static List<String> generateRandomColors(int numColors, List<String> ignoreColors) {
        if (numColors <= 0) {
            throw new IllegalArgumentException("O número de cores deve ser maior que zero.");
        }

        List<String> colors = new ArrayList<>();
        Random random = new Random();

        while (colors.size() < numColors) {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);

            String color = String.format("#%02x%02x%02x", red, green, blue);

            if (!ignoreColors.contains(color) && !colors.contains(color)) {
                colors.add(color);
            }
        }

        return colors;
    }


}
