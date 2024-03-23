package com.stamfee.stamfee.entity;

import com.stamfee.stamfee.dto.cafe.CafeDto;
import com.stamfee.stamfee.dto.cafe.CafeUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Cafe {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String content;

    @Column(name = "tel_number")
    private String telNumber;

    private Double longitude;

    private Double latitude;

    private String dong;

    private Boolean stampFlag;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    // 생성자 메서드
    @Builder
    public Cafe(Long id, String name, String content, String telNumber, Double longitude, Double latitude, String dong, boolean stampFlag){
        this.id = id;
        this.name = name;
        this.content = content;
        this.telNumber = telNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dong = dong;
        this.stampFlag = stampFlag;

    }


    /*
    * 카페 생성 로직
    * */
    public static Cafe createCafe(String name, String content, String telNumber,
                                  Double longitude, Double latitude, String dong, Member member){
        Cafe cafe = new Cafe();
        cafe.name = name;
        cafe.content = content;
        cafe.telNumber = telNumber;
        cafe.longitude = longitude;
        cafe.latitude = latitude;
        cafe.dong = dong;
        cafe.member = member;

        return cafe;
    }

    /*
    * 카페 업데이트 로직
    * */
    public void updateCafe(CafeUpdateDto cafeUpdateDto){
        this.name = cafeUpdateDto.getName();
        this.content = cafeUpdateDto.getContent();
        this.telNumber = cafeUpdateDto.getTelNumber();
        this.longitude = cafeUpdateDto.getLongitude();
        this.latitude = cafeUpdateDto.getLatitude();
        this.dong = cafeUpdateDto.getDong();
    }

}
