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
    public Cafe(CafeDto cafeDto){
        this.id = cafeDto.getId();
        this.name = cafeDto.getName();
        this.content = cafeDto.getContent();
        this.telNumber = cafeDto.getTelNumber();
        this.longitude = cafeDto.getLongitude();
        this.latitude = cafeDto.getLatitude();
        this.dong = cafeDto.getDong();

    }


    /*
    * 카페 생성 로직
    * */
    public static Cafe createClub(String name, String content, String telNumber,
                                  Double longitude, Double latitude, String dong){
        Cafe cafe = new Cafe();
        cafe.name = name;
        cafe.content = content;
        cafe.telNumber = telNumber;
        cafe.longitude = longitude;
        cafe.latitude = latitude;
        cafe.dong = dong;

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
