package com.yimeng.jpush;

import java.util.Set;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/1/12 0012 上午 11:57.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class TagAliasBean {
    private int action;
    private Set<String> tags;
    private String alias;
    private boolean isAliasAction;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getAlias() {
        return alias == null ? "" : alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAliasAction() {
        return isAliasAction;
    }

    public void setAliasAction(boolean aliasAction) {
        isAliasAction = aliasAction;
    }

    @Override
    public String toString() {
        return "TagAliasBean{" +
                "action=" + action +
                ", tags=" + tags +
                ", alias='" + alias + '\'' +
                ", isAliasAction=" + isAliasAction +
                '}';
    }
}
