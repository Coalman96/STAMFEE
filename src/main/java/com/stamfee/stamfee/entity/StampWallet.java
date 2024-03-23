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
public class StampWallet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    private int stampCount;
    private int couponCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /*
    * 스탬프 지갑 생성 로직
    * */
    public static StampWallet createStampWallet(String name, String color, Cafe cafe, Member member){

        StampWallet stampWallet = new StampWallet();
        stampWallet.name = name;
        stampWallet.color = color;
        stampWallet.cafe = cafe;
        stampWallet.member = member;
        return stampWallet;
    }

    /*
    * 스탬프 수정 로직
    * */
    public void updateStampCount(int stampCount){
        this.stampCount += stampCount;

        if(this.stampCount >= 10){
            this.couponCount += 1;
        }
    }

    /*
    * 쿠폰 수정 로직
    * */
    public void updateCouponCount(int coupon){
        this.couponCount = coupon;
    }

}

