package com.escapecode.escapify.modules.inventory.utils;

import com.escapecode.escapify.modules.inventory.repositories.CategoryRepository;
import com.escapecode.escapify.modules.inventory.repositories.ProductRepository;
import com.escapecode.escapify.modules.inventory.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        while (repository.existsBySkuAndSubcategoryIdAndDeletedFalse(sku, subcategoryId)) {
            sku = subcategorySku + "_" + baseSku + counter;
            counter++;
        }

        return sku;
    }
}
