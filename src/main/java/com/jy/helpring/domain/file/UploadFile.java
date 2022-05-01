package com.jy.helpring.domain.file;

import lombok.Getter;
import lombok.Setter;

/** 고객이 업로드한 파일 정보 보관 **/
@Setter
@Getter
public class UploadFile {

    private String uploadFileName; // 고객이 업로드한 파일명
    private String storeFileName; // 서버가 관리하는 파일명

    public UploadFile(String uploadFileName, String storeFileName){
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

}
