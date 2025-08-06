package com.escapecode.escapify.modules.inventory.utils;

import com.escapecode.escapify.modules.inventory.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SkuGenerator {

    @Autowired
    private CategoryRepository categoryRepository;

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
}
