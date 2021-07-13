package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.image.Image;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.model.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/images")
@Api(description = "Картинки")
public class ImageController {

    private final DaoFactory daoFactory;

    @Autowired
    public ImageController(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(@RequestParam("imageFile") final MultipartFile file) throws IOException {

        daoFactory.getImageDao().create(new Image(ImageUtils.compressBytes((file.getBytes()))));

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/get/{image-uuid}", method = RequestMethod.GET, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image-uuid") final String imageUUID) {

        final Image image = daoFactory.getImageDao().getById(imageUUID);
        if (image == null) {
            throw new NotFoundException(ErrorMessages.notFound(Image.class));
        }

        return ResponseEntity.ok(ImageUtils.decompressBytes(image.getData()));
    }
}
