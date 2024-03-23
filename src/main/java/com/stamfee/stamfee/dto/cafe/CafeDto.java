package com.stamfee.stamfee.dto.cafe;

import com.stamfee.stamfee.entity.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeDto {

    private Long id;
    private String name;
    private String telNumber;
    private String content;
    private Double longitude;
    private Double latitude;
    private String dong;
    private boolean stampFlag;
//
//    public Cafe toEntity(CafeDto cafeDto){
//        return Cafe.builder()
//                .
//    }

    public static CafeDto fromEntity(Cafe cafe){
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .telNumber(cafe.getTelNumber())
                .content(cafe.getContent())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .dong(cafe.getDong())
                .build();
    }

    public Cafe toEntity(){
        return Cafe.builder()
                .id(this.id)
                .name(this.name)
                .content(this.content)
                .telNumber(this.telNumber)
                .dong(this.dong)
                .stampFlag(this.stampFlag)
                .build();
    }

}
