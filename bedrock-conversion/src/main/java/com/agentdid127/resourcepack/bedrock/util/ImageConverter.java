package com.agentdid127.resourcepack.bedrock.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class ImageConverter extends com.agentdid127.resourcepack.library.utilities.ImageConverter {
    public ImageConverter(int defaultWIn, int defaultHIn, Path locationIn) throws IOException {
        super(defaultWIn, defaultHIn, locationIn);

    }

    public void backgroundImage(Path background) throws IOException {
        BufferedImage image2 = ImageIO.read(background.toFile());
        //BufferedImage newImage2 = new BufferedImage(newImage.getWidth(), newImage.getHeight(), BufferedImage.TYPE_INT_ARGB);


        this.newImage(defaultW, defaultH);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.01f));
        g2d.drawImage(image2, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2d.drawImage(image, 0, 0, null);


       // newImage2.getGraphics().drawImage(newImage, 0, 0, null);


    }

    public boolean storeTGA() throws IOException {
        return ImageIO.write(newImage, "tga", location.toFile());
    }
}
