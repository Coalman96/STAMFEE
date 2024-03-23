package com.stamfee.stamfee.controller;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.cafe.CafeDto;
import com.stamfee.stamfee.dto.cafe.CafeMenuSaveDto;
import com.stamfee.stamfee.dto.cafe.CafeSaveDto;
import com.stamfee.stamfee.dto.cafe.CafeUpdateDto;
import com.stamfee.stamfee.service.cafe.CafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stamfee/cafe")
@RequiredArgsConstructor
@Log4j2
public class CafeRestController {

    private final CafeService cafeService;

    @PostMapping(path = "/add")
    public ResponseEntity<Long> addCafe(@RequestBody CafeSaveDto cafeSaveDto, @RequestBody MemberDTO memberDTO) throws Exception {


        Long savedCafeId = cafeService.addCafe(memberDTO.getCellphone(), cafeSaveDto);


        return ResponseEntity.ok(savedCafeId);
    }

    @GetMapping(path = "/{cafeId}")
    public ResponseEntity<CafeDto> getCafe(@PathVariable Long cafeId){

        CafeDto cafeDto = cafeService.getCafe(cafeId);

        return ResponseEntity.ok(cafeDto);

    }

    @PostMapping(path = "/update")
    public ResponseEntity<?> updateCafe(@RequestBody CafeUpdateDto cafeUpdateDto){

        cafeService.updateCafe(cafeUpdateDto);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<?> listCafe(){
        return null;
    }

    @PostMapping(path = "/menu/add")
    public ResponseEntity<?> addCafeMenu(@RequestBody CafeMenuSaveDto cafeMenuSaveDto){
        return null;
    }

    @GetMapping(path = "/menus/{cafeId}")
    public ResponseEntity<?> listCafeMenu(@PathVariable Long cafeId){
        return null;
    }
}
