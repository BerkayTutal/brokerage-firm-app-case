package com.brokerage.brokeragefirm.repository.entity;


import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "assetName", nullable = false)
    private String assetName;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderSide", nullable = false)
    private Side orderSide;

    @Column(name = "size", nullable = false)
    private BigDecimal size;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "createDate")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "updateDate")
    private LocalDateTime updateDate;
}
