package com.store.technology.Service;

import com.store.technology.DTO.ProductDetailRequest;
import com.store.technology.DTO.ProductRequest;
import com.store.technology.Entity.*;
import com.store.technology.Repository.*;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ConfigurationRepository configurationRepository;
    private final ProductDetailRepository productDetailRepository;

    public ProductService(ProductRepository productRepository,
                          BrandRepository brandRepository,
                          CategoryRepository categoryRepository,
                          ConfigurationRepository configurationRepository,
                          ProductDetailRepository productDetailRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.configurationRepository = configurationRepository;
        this.productDetailRepository = productDetailRepository;
    }

    // Lấy tất cả bao gồm cả bị xóa
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy các sản phẩm chưa bị xóa
    public List<Product> getActiveProducts() {
        return productRepository.findAllByIsDeleted(false);
    }

    // Lấy 1 sản phẩm (cả khi bị xóa)
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Lấy 1 sản phẩm chưa bị xóa
    public Product getActiveProductById(Long id) {
        return productRepository.findByIdAndIsDeleted(id, false).orElse(null);
    }

    // ✅ Tạo Product từ DTO
    public Product createProduct(ProductRequest request) {
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy brandId: " + request.getBrandId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy categoryId: " + request.getCategoryId()));


        Product product = Product.builder()
                .name(request.getName())
                .totalQuality(request.getTotalQuality())
                .description(request.getDescription())
                .brand(brand)
                .category(category)
                .isDeleted(false)
                .build();

        return productRepository.save(product);
    }

    // ✅ Tạo mới Product + ProductDetails
    @Transactional
    public Product createProductWithDetails(ProductRequest request) {
        // Tạo sản phẩm
        Product product = new Product();
        product.setName(request.getName());
        product.setTotalQuality(request.getTotalQuality());
        product.setDescription(request.getDescription());

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        product.setBrand(brand);
        product.setCategory(category);

        Product savedProduct = productRepository.saveAndFlush(product);

        // Thêm product details
        if (request.getProductDetails() != null) {
            for (ProductDetailRequest detailReq : request.getProductDetails()) {
                Configuration config = configurationRepository.findById(detailReq.getConfigurationId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình"));

                ProductDetail detail = new ProductDetail();
                detail.setProduct(savedProduct);
                detail.setConfiguration(config);
                detail.setQuantity(detailReq.getQuantity());
                detail.setPrice(detailReq.getPrice());

                productDetailRepository.save(detail);
            }
        }

        return savedProduct;
    }

    // 🟡 Cập nhật sản phẩm
    @Transactional
    public Product updateProductWithProductDetail(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có ID: " + id));

        // 🟢 Cập nhật thông tin cơ bản
        if (request.getName() != null) existingProduct.setName(request.getName());
        if (request.getTotalQuality() != null) existingProduct.setTotalQuality(request.getTotalQuality());
        if (request.getDescription() != null) existingProduct.setDescription(request.getDescription());

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));
            existingProduct.setBrand(brand);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
            existingProduct.setCategory(category);
        }

        // 🟢 Xóa chi tiết cũ (nếu có)
        productDetailRepository.deleteAllByProductId(existingProduct.getId());

        // 🟢 Thêm chi tiết mới
        if (request.getProductDetails() != null && !request.getProductDetails().isEmpty()) {
            for (ProductDetailRequest detailReq : request.getProductDetails()) {
                Configuration config = configurationRepository.findById(detailReq.getConfigurationId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình"));

                ProductDetail detail = new ProductDetail();
                detail.setProduct(existingProduct);
                detail.setConfiguration(config);
                detail.setQuantity(detailReq.getQuantity());
                detail.setPrice(detailReq.getPrice());

                productDetailRepository.save(detail);
            }
        }

        return productRepository.saveAndFlush(existingProduct);
    }


    // ✅ Cập nhật Product từ DTO
    public Product updateProduct(Long id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm ID: " + id));

        if (request.getName() != null) existing.setName(request.getName());
        if (request.getTotalQuality() != null) existing.setTotalQuality(request.getTotalQuality());
        if (request.getDescription() != null) existing.setDescription(request.getDescription());

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy brandId: " + request.getBrandId()));
            existing.setBrand(brand);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy categoryId: " + request.getCategoryId()));
            existing.setCategory(category);
        }

        return productRepository.save(existing);
    }

    // Xóa mềm
    public boolean softDeleteProduct(Long id) {
        return productRepository.findById(id).map(p -> {
            p.setIsDeleted(true);
            productRepository.save(p);
            return true;
        }).orElse(false);
    }

    // Khôi phục sản phẩm đã bị xóa
    public boolean restoreProduct(Long id) {
        return productRepository.findById(id).map(p -> {
            p.setIsDeleted(false);
            productRepository.save(p);
            return true;
        }).orElse(false);
    }

    // ===================== Import CSV/Excel =====================

    private <T> T findOrCreate(String nameInput, JpaRepository<T, Long> repo, Function<String, T> creator) {
        if (nameInput == null || nameInput.isEmpty()) return null;
        final String nameFinal = nameInput.trim();

        // Exact match case-insensitive
        Optional<T> exact = repo.findAll().stream().filter(e -> {
            try {
                String name = (String) e.getClass().getMethod("getName").invoke(e);
                return name != null && name.equalsIgnoreCase(nameFinal);
            } catch (Exception ex) { return false; }
        }).findFirst();

        if (exact.isPresent()) return exact.get();

        // Fuzzy match <= 2
        LevenshteinDistance distance = new LevenshteinDistance();
        T bestMatch = repo.findAll().stream()
                .min(Comparator.comparingInt(e -> {
                    try {
                        String name = (String) e.getClass().getMethod("getName").invoke(e);
                        return name != null ? distance.apply(name.toLowerCase(), nameFinal.toLowerCase()) : Integer.MAX_VALUE;
                    } catch (Exception ex) { return Integer.MAX_VALUE; }
                }))
                .orElse(null);

        if (bestMatch != null) {
            try {
                String name = (String) bestMatch.getClass().getMethod("getName").invoke(bestMatch);
                if (distance.apply(name.toLowerCase(), nameFinal.toLowerCase()) <= 2) return bestMatch;
            } catch (Exception ignored) {}
        }

        // Không tìm thấy -> tạo mới
        return repo.save(creator.apply(nameFinal));
    }

    @Transactional
    public void importFromExcel(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = row.getCell(0).getStringCellValue().trim();
                String brandName = row.getCell(1).getStringCellValue().trim();
                String categoryName = row.getCell(2).getStringCellValue().trim();
                String configName = row.getCell(3).getStringCellValue().trim();
                int price = (int) row.getCell(4).getNumericCellValue();
                int quantity = (int) row.getCell(5).getNumericCellValue();
                String description = row.getCell(6).getStringCellValue().trim();

                saveProductFromFile(name, brandName, categoryName, configName, price, quantity, description);
            }
            workbook.close();
        }
    }

    @Transactional
    public void importFromCSV(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             InputStreamReader reader = new InputStreamReader(is);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : parser) {
                String name = record.get("name").trim();
                String brandName = record.get("brand_name").trim();
                String categoryName = record.get("category_name").trim();
                String configName = record.get("configuration_name").trim();
                int price = Integer.parseInt(record.get("price").trim());
                int quantity = Integer.parseInt(record.get("quantity").trim());
                String description = record.get("description").trim();

                saveProductFromFile(name, brandName, categoryName, configName, price, quantity, description);
            }
        }
    }

    private void saveProductFromFile(String name, String brandName, String categoryName, String configName, int price, int quantity, String description) {
        Brand brand = findOrCreate(brandName, brandRepository, n -> Brand.builder().name(n).build());
        Category category = findOrCreate(categoryName, categoryRepository, n -> Category.builder().name(n).build());
        Configuration config = findOrCreate(configName, configurationRepository, n -> Configuration.builder().name(n).build());

        Product product = Product.builder()
                .name(name)
                .brand(brand)
                .category(category)
                .totalQuality(quantity)
                .description(description)
                .build();
        productRepository.save(product);

        ProductDetail detail = ProductDetail.builder()
                .product(product)
                .configuration(config)
                .price(price)
                .quantity(quantity)
                .build();
        productDetailRepository.save(detail);
    }
}
