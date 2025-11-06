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

### 기본단어 목록
> WrodList.md 참고


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
TABLE TB_CO0001M_WORD {
  WORD_ID VARCHAR(30) [pk, note: '단어ID']
  WORD_NM VARCHAR(100) [note: '단어명']
  WORD_PSC_NM VARCHAR(100) [note: '단어물리명']
  WORD_LGC_NM VARCHAR(100) [note: '단어논리명']
  DESC_CTEN VARCHAR(1000) [note: '설명내용']
  USE_YN VARCHAR(1) [note: '사용여부']
  FR_RGST_DTTM DATETIME [note: '최초등록일시']
  FR_RGST_ID VARCHAR(30) [note: '최초등록ID']
  FINL_CHG_DTTM DATETIME [note: '최종변경일시']
  FINL_CHG_ID VARCHAR(30) [note: '최종변경ID']

  note: '공통_단어기본'
}

TABLE TB_CO0002M_DMN {
  DMN_ID VARCHAR(30) [pk, note: '도메인ID']
  DMN_NM VARCHAR(100) [note: '도메인명']
  DMN_PSC_NM VARCHAR(100) [note: '도메인물리명']
  DMN_LGC_NM VARCHAR(100) [note: '도메인논리명']
  DATA_TYPE_CD VARCHAR(2) [note: '데이터타입코드']
  DATA_LEN INT [note: '데이터길이']
  DEC_PL INT [note: '소수자리수']
  DESC_CTEN VARCHAR(1000) [note: '설명내용']
  USE_YN VARCHAR(1) [note: '사용여부']
  FR_RGST_DTTM DATETIME [note: '최초등록일시']
  FR_RGST_ID VARCHAR(30) [note: '최초등록ID']
  FINL_CHG_DTTM DATETIME [note: '최종변경일시']
  FINL_CHG_ID VARCHAR(30) [note: '최종변경ID']

  note: '공통_도메인기본'
}

TABLE TB_CO0003L_DMNCD {
  DMN_ID VARCHAR(30) [pk, note: '도메인ID']
  CD_ID VARCHAR(10) [pk, note: '코드ID']
  CD_NM VARCHAR(100) [note: '코드명']
  CD_DESC VARCHAR(500) [note: '코드설명']
  DATA_TYPE_CD VARCHAR(2) [note: '데이터타입코드']
  SRT_NO INT [note: '정렬번호']
  USE_YN VARCHAR(1) [note: '사용여부']
  FR_RGST_DTTM DATETIME [note: '최초등록일시']
  FR_RGST_ID VARCHAR(30) [note: '최초등록ID']
  FINL_CHG_DTTM DATETIME [note: '최종변경일시']
  FINL_CHG_ID VARCHAR(30) [note: '최종변경ID']

  note: '공통_도메인코드'
}

TABLE TB_CO0004M_TERM {
  TERM_ID VARCHAR(30) [pk, note: '용어ID']
  TERM_NM VARCHAR(100) [note: '용어명']
  TERM_PSC_NM VARCHAR(100) [note: '용어물리명']
  TERM_LGC_NM VARCHAR(100) [note: '용어논리명']
  DMN_ID VARCHAR(30) [note: '도메인ID']
  DESC_CTEN VARCHAR(1000) [note: '설명내용']
  USE_YN VARCHAR(1) [note: '사용여부']
  FR_RGST_DTTM DATETIME [note: '최초등록일시']
  FR_RGST_ID VARCHAR(30) [note: '최초등록ID']
  FINL_CHG_DTTM DATETIME [note: '최종변경일시']
  FINL_CHG_ID VARCHAR(30) [note: '최종변경ID']

  note: '공통_용어기본'
}

TABLE TB_CO0005L_WORDLST {
  OBJ_ID VARCHAR(30) [pk, note: '객체ID (TERM_ID 또는 DMN_ID)']
  OBJ_TYPE_CD VARCHAR(2) [pk, note: '객체타입코드 (01:용어, 02:도메인)']
  WORD_ID VARCHAR(30) [note: '단어ID']
  SRT_NO INT [note: '정렬번호']
  FR_RGST_DTTM DATETIME [note: '최초등록일시']
  FR_RGST_ID VARCHAR(30) [note: '최초등록ID']
  FINL_CHG_DTTM DATETIME [note: '최종변경일시']
  FINL_CHG_ID VARCHAR(30) [note: '최종변경ID']

  note: '공통_단어내역'
}

TABLE TB_CO1001M_ADMACCT {
  ACCT_ID VARCHAR(30) [pk, note: '계정ID']
  ACCT_PWD VARCHAR(100) [not null, note: '계정비밀번호']
  ACCT_NM VARCHAR(50) [not null, note: '계정명']
  ACCT_ROLE_CD VARCHAR(2) [not null, note: '계정권한코드']
  USE_YN VARCHAR(1) [not null, note: '사용여부']
  FR_RGST_DTTM DATETIME [not null, note: '최초등록일시']
  FR_RGST_ID VARCHAR(30) [not null, note: '최초등록ID']
  FINL_CHG_DTTM DATETIME [not null, note: '최종변경일시']
  FINL_CHG_ID VARCHAR(30) [not null, note: '최종변경ID']

  note: '공통_관리자계정'
}

TABLE TB_CO1002L_JWTADMACCTTOKEN {
  ACCT_ID VARCHAR(30) [pk, note: '계정ID']
  DEV_ID VARCHAR(100) [pk, note: '기기ID']
  SEQ INT [pk, note: '일련번호']
  REF_TKN TEXT [not null, note: '리프레시토큰']
  REF_EXP_DTTM DATETIME [not null, note: '리프레시만료일시']
  REG_DTTM DATETIME [not null, note: '등록일시']

  note: '공통_JWT관리자계정토큰'
}
```