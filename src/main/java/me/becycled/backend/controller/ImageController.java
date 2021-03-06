package me.becycled.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.becycled.backend.exception.NotFoundException;
import me.becycled.backend.exception.WrongRequestException;
import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.image.Image;
import me.becycled.backend.model.error.ErrorMessages;
import me.becycled.backend.model.utils.ImageUtils;
import me.becycled.backend.service.ImageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @author I1yi4
 */
@RestController
@RequestMapping("/images")
@Api(description = "Файлы изображений")
public class ImageController {

    private final DaoFactory daoFactory;
    private final ImageService imageService;

    @Autowired
    public ImageController(final DaoFactory daoFactory,
                           final ImageService imageService) {
        this.daoFactory = daoFactory;
        this.imageService = imageService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("Выгрузить файл изображения")
    public ResponseEntity<String> uploadImage(
        @RequestParam("file") final MultipartFile file) throws IOException {

        final String imageExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (imageExtension == null) {
            throw new WrongRequestException(ErrorMessages.cannotFindImageExtension());
        }

        final Image image = daoFactory.getImageDao().create(Image.build(
            imageService.buildImageName(imageExtension), file.getBytes()
        ));

        return ResponseEntity.ok(image.getFileName());
    }

    @RequestMapping(value = "/{filename}", method = RequestMethod.GET)
    @ApiOperation("Скачать файл изображения")
    public ResponseEntity<byte[]> downloadImage(
        @PathVariable("filename") final String filename,
        @RequestParam(value = "width", required = false) final Integer width,
        @RequestParam(value = "height", required = false) final Integer height) {

        final Image originImage = daoFactory.getImageDao().getByFileName(filename);
        if (originImage == null) {
            throw new NotFoundException(ErrorMessages.notFound(Image.class));
        }

        final String extension = FilenameUtils.getExtension(originImage.getFileName());
        final byte[] imageData = width != null && height != null
            ? imageService.resizeImage(originImage, width, height, extension)
            : originImage.getData();

        final MediaType mediaType = ImageUtils.findMediaTypeByImageExtension(extension);

        return ResponseEntity.ok().contentType(mediaType).body(imageData);
    }
}
