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
public class CafeMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String content;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;


    /*
    * 카페 메뉴 추가 로직
    * */
    public static CafeMenu addMenu(String name, String content, int price,
                                   Cafe cafe){
        CafeMenu cafeMenu = new CafeMenu();
        cafeMenu.name = name;
        cafeMenu.content = content;
        cafeMenu.price = price;
        cafeMenu.cafe = cafe;

        return cafeMenu;
    }


    /*
    * 카페 메뉴 수정 로직
    * */
    public void updateCafeMenu(String name, String content, int price){
        this.name = name;
        this.content = content;
        this.price = price;
    }

}
