package com.escapecode.escapify.modules.inventory.utils;

import com.escapecode.escapify.modules.inventory.repositories.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SkuGenerator {

    public String generateCategorySku(String name) {
        // Ejemplo: "Alimentos" -> "ALM" o "ALI"
        return name.trim()
                .replaceAll("[^A-Za-z]", "")
                .substring(0, Math.min(name.length(), 3))
                .toUpperCase();
    }

    public String generateUniqueCategorySku(String baseSku, UUID branchId, CategoryRepository categoryRepository) {
        String sku = baseSku;
        int counter = 1;
        while (categoryRepository.existsBySkuAndBranchId(sku, branchId)) {
            sku = baseSku + counter;
            counter++;
        }
        return sku;
    }

    public String generateSubcategorySku(String subcategoryName) {
        return subcategoryName.trim()
                .replaceAll("[^A-Za-z]", "")
                .substring(0, Math.min(subcategoryName.length(), 3))
                .toUpperCase();
    }

    public String generateUniqueSubcategorySku(String categorySku, String baseSku, UUID branchId, SubcategoryRepository repository) {
        String sku = categorySku + "_" + baseSku;
        int counter = 1;

        while (repository.existsBySkuAndBranchId(sku, branchId)) {
            sku = categorySku + "_" + baseSku + counter;
            counter++;
        }

        return sku;
    }

    public String generateProductSku(String productName) {
        return productName.trim()
                .replaceAll("[^A-Za-z]", "")
                .substring(0, Math.min(productName.length(), 4))
                .toUpperCase();
    }

    public String generateUniqueProductSku(String subcategorySku, String baseSku, UUID subcategoryId, ProductRepository repository) {
        String sku = subcategorySku + "_" + baseSku;
        int counter = 1;

        while (repository.existsBySkuAndSubcategoryId(sku, subcategoryId)) {
            sku = subcategorySku + "_" + baseSku + counter;
            counter++;
        }

        return sku;
    }

    public String generateAttributeSku(String attributeName) {
        return attributeName.trim()
                .replaceAll("[^A-Za-z]", "")
                .substring(0, Math.min(attributeName.length(), 3))
                .toUpperCase();
    }

    public String generateUniqueAttributeSku(String subcategorySku, String baseSku, UUID subcategoryId, AttributeRepository repository) {
        String sku = subcategorySku + "_" + baseSku;
        int counter = 1;

        while (repository.existsBySkuAndSubcategoryId(sku, subcategoryId)) {
            sku = subcategorySku + "_" + baseSku + counter;
            counter++;
        }

        return sku;
    }

    public String generateSequentialVariantSku(int number) {
        return String.format("%03d", number); // 001, 002, ...
    }

    public String generateUniqueVariantSku(String productSku, String attributeSku, UUID productId, VariantRepository repository) {
        int counter = 1;
        String suffix = generateSequentialVariantSku(counter);
        String fullSku = productSku + "-" + attributeSku + "-" + suffix;

        while (repository.existsBySkuAndProductId(fullSku, productId)) {
            counter++;
            suffix = generateSequentialVariantSku(counter);
            fullSku = productSku + "-" + attributeSku + "-" + suffix;
        }

        return fullSku;
    }

}
