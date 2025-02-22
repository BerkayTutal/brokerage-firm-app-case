package com.brokerage.brokeragefirm.repository.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asset")
public class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "customerId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerEntity customer;

    @Column(name = "assetName")
    private String assetName;

    @Column(name = "size")
    private BigDecimal size;

    @Column(name = "usableSize")
    private BigDecimal usableSize;

}
