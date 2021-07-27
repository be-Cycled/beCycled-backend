package me.becycled.backend.service;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.image.Image;
import me.becycled.backend.model.utils.ImageUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author I1yi4
 */
@Service
public class ImageService {

    private final DaoFactory daoFactory;

    @Autowired
    public ImageService(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public String buildImageName(final String imageExtension) {
        String filename;
        do {
            filename = ImageUtils.buildImageName(imageExtension);
        } while (daoFactory.getImageDao().getByFileName(filename) != null);
        return filename;
    }

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    public byte[] resizeImage(final Image image, final int width, final int height, final String imageExtension) {
        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream(image.getData());
            final BufferedImage origin = ImageIO.read(bais);
            final BufferedImage resize = Scalr.resize(origin, width, height);

            final byte[] result;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(resize, imageExtension, baos);
                baos.flush();
                result = baos.toByteArray();
            }

            return result;
        } catch (IOException ex) {
            throw new RuntimeException("Cannot resize image with name " + image.getFileName() + " to size " + width + " : " + height, ex);
        }
    }
}
