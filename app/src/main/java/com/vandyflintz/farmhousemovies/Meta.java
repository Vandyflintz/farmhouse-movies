package com.vandyflintz.farmhousemovies;

public class Meta {

    public Meta(String metaname, String metavalue) {
        this.metaname = metaname;
        this.metavalue = metavalue;
    }

    String metavalue;

    public String getMetavalue() {
        return metavalue;
    }

    public void setMetavalue(String metavalue) {
        this.metavalue = metavalue;
    }

    public String getMetaname() {
        return metaname;
    }

    public void setMetaname(String metaname) {
        this.metaname = metaname;
    }

    String metaname;
}
