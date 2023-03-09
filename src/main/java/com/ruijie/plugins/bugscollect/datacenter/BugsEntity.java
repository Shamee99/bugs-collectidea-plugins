package com.ruijie.plugins.bugscollect.datacenter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 彭耀煌
 */
public class BugsEntity implements Serializable {
    private String title;
    private String code;
    private String suggest;
    private Date createDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
