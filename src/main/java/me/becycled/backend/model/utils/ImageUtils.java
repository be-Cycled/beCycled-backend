package me.becycled.backend.model.utils;

import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author I1yi4
 */
@SuppressWarnings("WhitespaceAround")
public enum ImageUtils {;

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    public static byte[] compressBytes(final byte[] data) {
        final Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        final byte[] result;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            final byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                final int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }

            result = outputStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    public static byte[] decompressBytes(final byte[] data) {
        final Inflater inflater = new Inflater();
        inflater.setInput(data);

        final byte[] result;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            final byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                final int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }

            result = outputStream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    public static String buildImageName(final String extension) {
        return DomainUtils.generateUUIDv4() + '.' + extension.toLowerCase();
    }

    public static MediaType findMediaTypeByImageExtension(final String extension) {
        switch (extension.toLowerCase()){
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
