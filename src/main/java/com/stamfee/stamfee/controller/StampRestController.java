package com.stamfee.stamfee.controller;

import com.stamfee.stamfee.config.CurrentMember;
import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.stamp.StampAddDto;
import com.stamfee.stamfee.dto.stamp.StampCouponAddDto;
import com.stamfee.stamfee.dto.stamp.StampDto;
import com.stamfee.stamfee.dto.stamp.StampWalletDto;
import com.stamfee.stamfee.service.cafe.CafeService;
import com.stamfee.stamfee.service.stamp.StampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stamfee/stamp")
@RequiredArgsConstructor
@Log4j2
public class StampRestController {

    private final CafeService cafeService;
    private final StampService stampService;


    @GetMapping(path = "/create/wallet/{cafeId}}")
    public ResponseEntity<?>  createStampWallet(@PathVariable Long cafeId, @CurrentMember MemberDTO memberDTO) throws Exception{

        Long stampWalletId = stampService.createStampWallet(memberDTO.getCellphone(), cafeId);

        return ResponseEntity.ok(stampWalletId);
    }

    @PostMapping("/stamp/add")
    public ResponseEntity<?> addStamp(@RequestBody StampAddDto stampAddDto, @CurrentMember MemberDTO memberDTO) throws Exception {


        Long l = stampService.addStamp(memberDTO.getCellphone(), stampAddDto.getCafeId(), stampAddDto.getCount());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/stamp/wallet/{stampWalletId}")
    public ResponseEntity<?> getStampWallet(@PathVariable Long stampWalletId){

        StampWalletDto stampWalletDto = stampService.getStampWallet(stampWalletId);

        return ResponseEntity.ok(stampWalletDto);

    }

    @PostMapping("/stamp/my")
    public ResponseEntity<?> listStampWallet(@CurrentMember MemberDTO memberDTO) throws Exception {

        List<StampWalletDto> stampWalletDtos = stampService.listStampWallet(memberDTO.getCellphone());

        return ResponseEntity.ok(stampWalletDtos);
    }

    @PostMapping("/stamp/coupon/add")
    public ResponseEntity<?> addCoupon(@RequestBody StampCouponAddDto stampCouponAddDto, @CurrentMember MemberDTO memberDTO){


        stampService.addCoupon(stampCouponAddDto.getStampWalletId(), stampCouponAddDto.getCount());

        return null;
    }


}

