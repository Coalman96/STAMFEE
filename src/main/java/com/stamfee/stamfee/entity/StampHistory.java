package com.stamfee.stamfee.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class StampHistory extends Base{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cafeName;
    private int count;

    @Enumerated(EnumType.STRING)
    private StampHistoryType stampHistoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_wallet_id")
    private StampWallet stampWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
