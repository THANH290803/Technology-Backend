package com.store.technology.Controller;

import com.store.technology.Entity.Image;
import com.store.technology.Service.CloudinaryService;
import com.store.technology.Service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Image", description = "CRUD API cho Image")
@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Operation(summary = "📤 Upload nhiều ảnh từ máy tính (có gán ProductId)")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<List<Image>> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "productId", required = false) Long productId
    ) throws IOException {
        List<Image> images = imageService.uploadImages(files, productId);
        return ResponseEntity.ok(images);
    }

    @Operation(summary = "📸 Lấy toàn bộ ảnh (mọi product)")
    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @Operation(summary = "🗑️ Xóa ảnh theo ID (xóa luôn trên Cloudinary)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) throws IOException {
        imageService.deleteImage(id);
        return ResponseEntity.ok("Đã xóa ảnh thành công");
    }
}
