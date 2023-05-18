package common.utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageDimension {
    private static final String ASSET_PATH = "Common/src/main/resources/sprites/";

    public static int[] getDimensions(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(ASSET_PATH+imagePath));
            return new int[]{image.getWidth(), image.getHeight()};
        } catch (IOException e) {
            e.printStackTrace();
            return new int[]{0, 0};
        }
    }
}
