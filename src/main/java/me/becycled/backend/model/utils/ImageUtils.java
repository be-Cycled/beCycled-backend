package me.becycled.backend.model.utils;

import org.springframework.http.MediaType;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum ImageUtils {;

    public static String buildImageName(final String extension) {
        return DomainUtils.generateUUIDv4() + '.' + extension.toLowerCase();
    }

    public static MediaType findMediaTypeByImageExtension(final String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            default:
                throw new IllegalStateException("Unexpected image extension: " + extension);
        }
    }
}
