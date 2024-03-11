package com.stamfee.stamfee.controller;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.cafe.CafeSaveDto;
import com.stamfee.stamfee.service.cafe.CafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stamfee/cafe")
@RequiredArgsConstructor
@Log4j2
public class CafeRestController {

    private final CafeService cafeService;

    @PostMapping("/add")
    public ResponseEntity<Long> addClub(@RequestBody CafeSaveDto cafeSaveDto, @RequestBody MemberDTO memberDTO) throws Exception {


        Long savedCafeId = cafeService.addCafe(memberDTO.getCellphone(), cafeSaveDto);


        return ResponseEntity.ok(savedCafeId);
    }
}
