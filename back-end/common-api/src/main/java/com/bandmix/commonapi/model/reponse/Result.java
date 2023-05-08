package com.bandmix.commonapi.model.reponse;

import lombok.Data;

import java.util.List;

@Data
public class Result {

    private boolean isSuccess;

    private Object data;

    private String errMsg;

    private List<String> checkList;

    public static Result success(Object obj){
        Result fsdResult = new Result();
        fsdResult.isSuccess = true;
        fsdResult.setData(obj);
        return fsdResult;
    }

    public static Result error(String errMsg){
        Result fsdResult = new Result();
        fsdResult.isSuccess = false;
        fsdResult.setErrMsg(errMsg);
        return fsdResult;
    }

    public static Result checkError(List<String> checkList){
        Result fsdResult = new Result();
        fsdResult.isSuccess = false;
        fsdResult.setCheckList(checkList);
        return fsdResult;
    }
}
