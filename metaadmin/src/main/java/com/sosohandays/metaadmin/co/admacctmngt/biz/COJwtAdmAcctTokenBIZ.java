package com.sosohandays.metaadmin.co.admacctmngt.biz; // 실제 패키지 경로에 맞게 조정

import com.sosohandays.common.exception.SshdException;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COJwtAdmAcctTokenDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 관리를 위해 추가


@Service
@RequiredArgsConstructor // Lombok 어노테이션으로 final 필드에 대한 생성자 자동 생성
public class COJwtAdmAcctTokenBIZ {

    private final COJwtAdmAcctTokenMapper cOJwtAdmAcctTokenMapper;

    /**
     * 리프레시 토큰을 저장하거나 갱신합니다.
     * ACCT_ID와 DEV_ID에 대해 SEQ를 MAX + 1로 자동 부여합니다.
     * 동일 PK(ACCT_ID, DEV_ID, SEQ)가 존재하면 토큰 정보만 갱신합니다.
     */
    @Transactional // 이 메서드 내의 DB 작업들이 하나의 트랜잭션으로 묶이도록 합니다.
    public void saveToken(COJwtAdmAcctTokenDTO cOJwtAdmAcctTokenDTO) {
        int cnt = cOJwtAdmAcctTokenMapper.insertToken(cOJwtAdmAcctTokenDTO);

        if (cnt <= 0) {
            throw new SshdException("발급 토큰 저장 중 오류가 발생하였습니다.");
        }
    }

    /**
     * 계정 ID와 기기 ID로 토큰 정보를 조회합니다.
     * 가장 최근의 토큰을 가져오기 위해 SEQ DESC, LIMIT 1을 사용하도록 Mapper에 구현하는 것이 일반적입니다.
     */
    public COJwtAdmAcctTokenDTO getByAcctIdAndDevId(COJwtAdmAcctTokenDTO cOJwtAdmAcctTokenDTO) {
        // 이 메서드는 기존대로 유지하며, Mapper에서 적절한 SEQ 값을 가진 토큰을 조회하도록 구현합니다.
        // 예를 들어, 해당 계정/기기의 최신 토큰을 가져오도록 Mapper 쿼리를 구성할 수 있습니다.
        return cOJwtAdmAcctTokenMapper.selectByAcctIdAndDevId(cOJwtAdmAcctTokenDTO);
    }

    /**
     * 계정 ID와 기기 ID로 토큰 정보를 삭제합니다.
     */
    @Transactional // 삭제 작업도 트랜잭션으로 묶습니다.
    public void deleteTokenByAcctAndDevice(COJwtAdmAcctTokenDTO cOJwtAdmAcctTokenDTO) {
        int affectedRows = cOJwtAdmAcctTokenMapper.deleteByAcctIdAndDevId(cOJwtAdmAcctTokenDTO);
        if (affectedRows <= 0) {
            // 토큰이 없어서 삭제되지 않은 경우도 있을 수 있으므로,
            // '오류'보다는 '토큰이 존재하지 않습니다'와 같은 메시지가 더 적절할 수 있습니다.
            // 여기서는 원본 코드의 에러 로직을 따릅니다.
            throw new SshdException("발급 토큰 삭제 중 오류가 발생하였습니다.");
        }
    }
}