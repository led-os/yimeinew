package com.chengzi.app.third.fileuplod;

/**
 * @Desc:
 * @ClassName:AliError
 * @PackageName:com.yimei.app.third.fileuplod
 * @Create On 2019/4/28 0028
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class AliError {
    private String code;

    private String message;

    public AliError() {
    }

    public AliError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AliError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
