package com.store.technology.Service;

import com.store.technology.Entity.Image;
import com.store.technology.Entity.Product;
import com.store.technology.Repository.ImageRepository;
import com.store.technology.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Upload nhiều ảnh cho product
    public List<Image> uploadImages(List<MultipartFile> files, Long productId) throws IOException {
        List<Image> uploadedImages = new ArrayList<>();
        boolean firstImage = true;

        for (MultipartFile file : files) {
            Map uploadResult = cloudinaryService.upload(file, "store_technology/products");

            Image image = new Image();
            image.setImageUrl((String) uploadResult.get("secure_url"));
            image.setPublicId((String) uploadResult.get("public_id"));
            image.setIsMain(firstImage);
            firstImage = false;

            // Gán product nếu productId tồn tại
            if (productId != null) {
                image.setProduct(productRepository.findById(productId).orElse(null));
            }

            uploadedImages.add(imageRepository.save(image));
        }

        return uploadedImages;
    }

    // Lấy tất cả ảnh
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    // Xóa ảnh theo id (xóa luôn trên Cloudinary)
    public void deleteImage(Long id) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        cloudinaryService.delete(image.getPublicId());
        imageRepository.delete(image);
    }
}
