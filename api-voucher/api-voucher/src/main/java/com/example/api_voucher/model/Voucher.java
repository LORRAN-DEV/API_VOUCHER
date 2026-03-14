package com.example.api_voucher.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;

    private Double discountPercentage;

    private Integer maxUses;

    @Column(columnDefinition = "integer default 0")
    private Integer currentUses = 0;

    @Column(columnDefinition = "boolean default true")
    private Boolean active = true;

    private LocalDateTime expirationDate;

    private LocalDateTime createdAt;
}
