package com.escapecode.escapify.modules.stock.entities;

import com.escapecode.escapify.modules.enterprise.entities.Branch;
import com.escapecode.escapify.shared.model.Audit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "stock_items")
@Data
public class StockItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 10, nullable = false)
    private String typeItem; // "PRODUCT" o "VARIANT"

    @Column(nullable = false, unique = true)
    private UUID itemId;

    @Column(nullable = false)
    private Integer quantity; // Cantidad total de productos en stock disponible

    @Column(nullable = false)
    private Integer reserved; // Cantidad de productos reservados para ventas pendientes (Carritos, órdenes, etc.)

    @Column(nullable = false)
    private Boolean enabled; // Activar el control de stock

    @Column(nullable = false)
    private Integer oversold = 0; // Cantidad vendida por encima del stock (sin stock disponible)

    @Embedded
    private Audit audit = new Audit();

}