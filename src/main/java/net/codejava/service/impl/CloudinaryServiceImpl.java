package net.codejava.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import net.codejava.service.CloudinaryService;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder.default}")
    private String defaultFolder;

    @Override
    public Map uploadFileBase64(String base64String, String folder) throws IOException {
        if (folder == null) folder = defaultFolder;
        if (folder == null) folder = defaultFolder;
        Map result = cloudinary.uploader().upload(base64String, ObjectUtils.asMap("folder", folder));
        return result;
    }

    @Override
    public Map uploadFile(MultipartFile multipartFile, String folder) throws IOException {
        File file = convert(multipartFile);
        if (folder == null) folder = defaultFolder;
        Map result = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", folder));
        if (!Files.deleteIfExists(file.toPath())) {
            throw new RuntimeException("Failed to delete temporary file: " + file.getAbsolutePath());
        }
        return result;
    }

    @Override
    public List<Map> uploadFiles(List<MultipartFile> files, String folder) throws IOException {
        if (files == null) System.out.println("Null or Empty");
        if (folder == null) folder = defaultFolder;
        List<Map> uploadResults = new ArrayList<>();
        for (MultipartFile file : files) {
            File fileConvert = convert(file);
            Map result = cloudinary.uploader().upload(fileConvert, ObjectUtils.asMap("folder", folder));
            if (!Files.deleteIfExists(fileConvert.toPath())) {
                throw new RuntimeException("Failed to delete temporary file: " + fileConvert.getAbsolutePath());
            }
            uploadResults.add(result);
        }
        return uploadResults;
    }

    @Override
    public Map deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    @Override
    public Map deleteFiles(List<Integer> ids) {
        return null;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
