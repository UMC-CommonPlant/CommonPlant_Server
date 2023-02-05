package com.commonplant.umc.config.exception;

public enum ErrorResponseStatus {
    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    FAILED_TO_LOGOUT_JWT(false,2004,"로그아웃 실패하였습니다"),
    FAILED_TO_LOGIN_JWT(false,2004,"token을 확인하세요."),


    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),



    //4000 : Database, Server 오류
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    FILE_SAVE_ERROR(false, 4001, "파일 저장이 실패하였습니다."),
    EXIST_USER(false, 4002, "이미 등록된 유저가 있습니다."),
    NOT_FOUND_USER(false, 4003, "등록된 유저가 없습니다."),
    INVALID_PWD(false, 4004, "비밀번호가 올바르지 않습니다."),
    PLACE_CODE_ERROR(false, 4005, "잘못된 place code 입니다."),
    WRONG_PLATFORM(false, 4006, "잘못된 플랫폼입니다."),
    EXPIRED_JWT(false, 4007, "만료된 토큰입니다."),

    //5000 : Server connection 오류
    SERVER_ERROR(false, 5000, "서버와의 연결에 실패하였습니다."),

    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private ErrorResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}