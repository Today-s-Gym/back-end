package com.gym.tag.config.exception;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    INVALID_USER(false, 2011, "존재하지 않는 사용자입니다."),


    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    DUPLICATED_NICKNAME(false, 2018, "중복된 닉네임입니다."),
    LENGTH_OVER_INTRODUCE(false, 2019, "한 줄 소개는 30글자 이하로 입력해주세요"),

    // report
    REPORT_USER_SELF(false, 2100, "자신을 신고할 수 없습니다."),
    REPORT_POST_SELF(false, 2101, "자신의 게시글을 신고할 수 없습니다."),
    REPORT_COMMENT_SELF(false, 2102, "자신의 댓글을 신고할 수 없습니다."),

    // posts
    EMPTY_CATEGORY(false, 2200, "해당 카테고리에 게시글이 없습니다."),
    INVALID_POST(false, 2201, "유효하지 않은 게시글입니다."),

    // comment
    INVALID_COMMENT(false, 2300, "유효하지 않은 댓글입니다."),

    // record
    INVALID_RECORD(false,2400,"유호하지 않은 기록입니다."),
    RECORD_DATE_EXISTS(false, 2401, "기록 추가는 하루에 한번입니다"),
    EMPTY_RECORD(false, 2402, "기록이 존재하지 않습니다."),


    // category
    INVALID_CATEGORY(false,2500,"유호하지 않은 카테고리입니다."),

    //Tag
    EMPTY_TAG(false, 2602, "최근 사용한 태그가 존재하지 않습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    /**
     * 5000 : 로그인 시 닉네임, 한줄 소개, 운동 설정 시 오류
     */
    NICKNAME_ERROR(false, 5000, "이미 존재하는 닉네임입니다."),
    INTRODUCE_ERROR(false, 5001, "한줄 소개는 0~30자이어야 합니다.");



    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

