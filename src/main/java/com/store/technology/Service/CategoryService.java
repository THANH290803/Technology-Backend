package com.store.technology.Service;

import com.store.technology.DTO.CategoryRequest;
import com.store.technology.Entity.Brand;
import com.store.technology.Entity.Category;
import com.store.technology.Repository.BrandRepository;
import com.store.technology.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // 🔹 Lấy tất cả chưa bị xóa
    public List<Category> getAllActive() {
        return categoryRepository.findAllActive();
    }

    // 🔹 Lấy tất cả kể cả đã xóa
    public List<Category> getAllWithDeleted() {
        return categoryRepository.findAllIncludingDeleted();
    }

    // 🔹 Lấy theo id chưa bị xóa
    public Optional<Category> getActiveById(Long id) {
        return categoryRepository.findActiveById(id);
    }

    // 🔹 Lấy theo id kể cả đã xóa
    public Optional<Category> getByIdIncludingDeleted(Long id) {
        return categoryRepository.findByIdIncludingDeleted(id);
    }

    // 🔹 Tạo mới hoặc khôi phục nếu đã bị xoá mềm
    public Category createCategory(CategoryRequest request) {
        // Tìm xem có category cùng tên chưa (kể cả đã xoá)
        Optional<Category> existingCategoryOpt = categoryRepository.findByNameIncludingDeleted(request.getName());

        if (existingCategoryOpt.isPresent()) {
            Category existingCategory = existingCategoryOpt.get();

            // Nếu bị xoá mềm → khôi phục lại
            if (existingCategory.getDeletedAt() != null) {
                existingCategory.setDeletedAt(null);
                existingCategory.setDescription(request.getDescription());
                return categoryRepository.save(existingCategory);
            }

            // Nếu chưa bị xoá → vẫn tạo mới, cho phép trùng tên
            // (hoặc bạn có thể chọn throw lỗi nếu muốn không trùng)
        }

        // Nếu chưa tồn tại → tạo mới
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDeletedAt(null);
        return categoryRepository.save(category);
    }

    // 🔹 Cập nhật (bao gồm cả danh mục đã xoá mềm)
    public Category updateCategory(Long id, CategoryRequest request) {
        // Tìm category kể cả đã xoá
        Category category = categoryRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy category với id: " + id));

        // ✅ Cho phép đổi tên, kể cả trùng với category đã bị xoá mềm khác
        if (request.getName() != null) {
            // Kiểm tra trùng tên với category đang hoạt động (deleted_at IS NULL)
            Optional<Category> existingActiveCategory = categoryRepository.findByNameActive(request.getName());
            if (existingActiveCategory.isPresent() && !existingActiveCategory.get().getId().equals(id)) {
                throw new IllegalArgumentException("Tên danh mục này đã tồn tại!");
            }
            category.setName(request.getName());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        return categoryRepository.save(category);
    }

    // 🔹 Soft delete (set deleted_at = now)
    public String softDelete(Long id) {
        Optional<Category> existing = categoryRepository.findActiveById(id);
        if (existing.isPresent()) {
            Category category = existing.get();
            category.setDeletedAt(LocalDateTime.now());
            categoryRepository.save(category);
            return "Đã xóa mềm category có id = " + id;
        }
        return "Không thể xóa: category không tồn tại hoặc đã bị xóa trước đó";
    }

    // 🔹 Restore (set deleted_at = NULL)
    public String restore(Long id) {
        Optional<Category> existing = categoryRepository.findByIdIncludingDeleted(id);
        if (existing.isPresent()) {
            Category category = existing.get();
            if (category.getDeletedAt() == null) {
                return "Category này chưa bị xóa, không cần khôi phục";
            }
            category.setDeletedAt(null);
            categoryRepository.save(category);
            return "Đã khôi phục category có id = " + id;
        }
        return "Không thể khôi phục: category không tồn tại";
    }
}
