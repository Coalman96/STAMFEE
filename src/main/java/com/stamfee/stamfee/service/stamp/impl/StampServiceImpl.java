package com.stamfee.stamfee.service.stamp.impl;

import com.stamfee.stamfee.dto.MemberDTO;
import com.stamfee.stamfee.dto.cafe.CafeDto;
import com.stamfee.stamfee.dto.stamp.StampWalletDto;
import com.stamfee.stamfee.entity.*;
import com.stamfee.stamfee.mapper.MemberMapper;
import com.stamfee.stamfee.service.cafe.CafeService;
import com.stamfee.stamfee.service.member.MemberService;
import com.stamfee.stamfee.service.stamp.StampRepository;
import com.stamfee.stamfee.service.stamp.StampService;
import com.stamfee.stamfee.service.stamp.StampWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StampServiceImpl implements StampService {

    private final StampRepository stampRepository;
    private final StampWalletRepository stampWalletRepository;
    private final CafeService cafeService;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public Long createStampWallet(String cellPhone, Long cafeId) throws Exception {

        // 회원 조회
        MemberDTO memberDto = memberService.getMember(cellPhone);

        // memberDto -> Member Entity로 전환
        Member member = memberMapper.memberDTOToMember(memberDto);

        // 카페 조회
        CafeDto cafeDto = cafeService.getCafe(cafeId);

        //CafeDto -> Cafe Entity로 전환
        Cafe cafe = cafeDto.toEntity();

        // StampWallet 정보 입력
        StampWallet stampWallet = StampWallet.createStampWallet(cafe.getName(), "orange", cafe, member);

        // 스탬프 지갑 생성
        StampWallet savedWallet = stampWalletRepository.save(stampWallet);


        return savedWallet.getId();
    }

    @Override
    public Long addStamp(String cellPhone, Long cafeId, int count) throws Exception {

        // 회원 조회
        MemberDTO memberDto = memberService.getMember(cellPhone);

        // memberDto -> Member Entity로 전환
        Member member = memberMapper.memberDTOToMember(memberDto);

        // 카페 조회
        CafeDto cafeDto = cafeService.getCafe(cafeId);

        //CafeDto -> Cafe Entity로 전환
        Cafe cafe = cafeDto.toEntity();

        // 스탬프 지갑이 없을 경우 스탬프 지갑 생성
        if(!stampWalletRepository.existsByMemberAndCafe(member, cafe)){
            createStampWallet(cellPhone, cafeId);
        }

        //월렛 조회
        StampWallet stampWallet = stampWalletRepository.findByMemberAndCafe(member, cafe);

        // 스탬프 생성 메서드
        Stamp stamp = Stamp.addStamp(cafe.getName(), count, StampHistoryType.STAMP_ADDED, member, stampWallet);

        // 스탬프 적립
        Stamp savedStamp = stampRepository.save(stamp);

        // 월렛 카운트 추가
        stampWallet.updateStampCount(count);

        return savedStamp.getId();
    }

    public StampWalletDto getStampWallet(Long stampWalletId){

        // 카페 및 회원 조회도 이뤄줘야함
        StampWallet stampWallet = stampWalletRepository.findById(stampWalletId)
                .orElseThrow(() -> new IllegalArgumentException("스탬프가 없습니다. "));


        return StampWalletDto.fromEntity(stampWallet);
    }

    public List<StampWalletDto> listStampWallet(String cellPhone) throws Exception {

        // 회원 조회
        MemberDTO memberDto = memberService.getMember(cellPhone);

        // memberDto -> Member Entity로 전환
        Member member = memberMapper.memberDTOToMember(memberDto);

        List<StampWallet> stampWallets = stampWalletRepository.findByMember(member);

        return stampWallets.stream()
                .map(StampWalletDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void addCoupon(Long stampWalletId, int count){

        StampWallet stampWallet = stampWalletRepository.findById(stampWalletId)
                .orElseThrow(() -> new IllegalArgumentException("스탬프가 없습니다. "));

        stampWallet.updateCouponCount(count);
    }

}
