package com.escapecode.escapify.modules.inventory.entities;

import com.escapecode.escapify.shared.model.Audit;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "attributes")
@Data
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 50, nullable = false)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Embedded
    private Audit audit = new Audit();

}
