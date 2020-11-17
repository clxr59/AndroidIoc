package com.hankage.hrouter_compiler;

/**
 * Author: cheers
 * Time ： 2020/11/16
 * Description ：
 */
public class GroupInfo {
    private String className;
    private String packageName;

    public GroupInfo() {
    }

    public GroupInfo(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }

    public String getClassName() {
        return className == null ? "" : className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName == null ? "" : packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
