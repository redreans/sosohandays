# Metaadmin
## 개요
> 메타데이터와 관리자 계정을 관리한다.

## 구성
패키지 : com.sosohandays.metaadmin

## 설계
> 단어, 용어, 도메인을 관리

### 단어
|칼럼명|설명|비고|
|:-----:|:--|:---|
|단어ID|단어를 관리하는 고유ID|
|단어명|단어의 약어|
|단어물리명|단어 한글명|
|단어논리명|단어 영문명|
|단어설명|단어의 설명|
|사용여부|사용여부|
---

### 도메인
> 단어 또는 단어 + 단어로 도메인 생성하고 도메인의 type와 크기를 결정  
> 도메인의 고유한 코드값을 가짐
> 
#### 예제) 
|도메인ID|도메인명|코드ID|코드명|
|-------|------|------|-----|
|000001|STAT_CD|01|대기|
|000001|STAT_CD|02|승인|
|000001|STAT_CD|03|취소|

### 용어
> 단어 + 단어 로 구성하여 용어를 생성  
> 단어가 없을 경우 등록 불가능  
> 용어의 type와 크기를 결정하는 도메인 연결  


### 테이블 설계
> https://dbdiagram.io/d/SOSOHANDAYS-6854ed24f039ec6d361690c1
```sql
TABLE TB_CO0001M_WORD [NOTE: '공통_단어기본'] {
  WORD_ID         VARCHAR(30)   [PK, NOTE: '단어 ID (예: 000001)']
  WORD_NM         VARCHAR(100)  [NOTE: '단어명']
  WORD_PSC_NM     VARCHAR(100)  [NOTE: '단어 물리명']
  WORD_LGC_NM     VARCHAR(100)  [NOTE: '단어 논리명']
  DESC_CTEN       VARCHAR(1000) [NOTE: '설명']
  USE_YN          VARCHAR(1)    [NOTE: '사용 여부']
  FR_RGST_DTTM    DATETIME      [NOTE: '최초 등록일시']
  FR_RGST_ID      VARCHAR(30)   [NOTE: '최초 등록자 ID']
  FINL_CHG_DTTM   DATETIME      [NOTE: '최종 변경일시']
  FINL_CHG_ID     VARCHAR(30)   [NOTE: '최종 변경자 ID']
}

TABLE TB_CO1001M_ADMACCT [NOTE: '공통_관리자계정'] {
  ACCT_ID         VARCHAR(30)   [PK, NOTE: '계정ID']
  ACCT_PWD        VARCHAR(100)  [NOT NULL, NOTE: '계정비밀번호']
  ACCT_NM         VARCHAR(50)   [NOT NULL, NOTE: '계정명']
  ACC_ROLE_CD     VARCHAR(2)    [NOT NULL, NOTE: '계정권한코드']
  USE_YN          VARCHAR(1)    [NOT NULL, NOTE: '사용여부']
  FR_RGST_DTTM    DATETIME      [NOT NULL, NOTE: '최초등록일시']
  FR_RGST_ID      VARCHAR(30)   [NOT NULL, NOTE: '최초등록자ID']
  FINL_CHG_DTTM   DATETIME      [NOT NULL, NOTE: '최종변경일시']
  FINL_CHG_ID     VARCHAR(30)   [NOT NULL, NOTE: '최종변경자ID']
}

TABLE TB_CO1002L_JWTADMACCTTOKEN [NOTE: '공통_토큰'] {
  ACCT_ID         VARCHAR(30)   [PK, NOTE: '계정ID']
  DEV_ID          VARCHAR(100)  [PK, NOTE: '기기ID']
  SEQ             INT           [PK, NOTE: '일련번호']
  REF_TKN         TEXT          [NOT NULL, NOTE: '리프레시토큰']
  REF_EXP_DTTM    DATETIME      [NOT NULL, NOTE: '리프레시토큰 만료일시']
  REG_DTTM        DATETIME      [NOT NULL, NOTE: '등록일시']
}
```