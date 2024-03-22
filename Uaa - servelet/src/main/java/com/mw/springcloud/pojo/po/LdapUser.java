package com.mw.springcloud.pojo.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@Entry(objectClasses = {"inetOrgPerson", "posixAccount","top"})
public final class LdapUser {
    @Id
    @JSONField(serialize = false)
    private Name dn;

    @Attribute(name = "cn")
    private String cn;
    @Attribute(name = "ou")
    private String ou;
    /**
     * 账号
     */
    @Attribute(name = "uid")
    private String UserName;
    /**
     * 密码
     */
    @Attribute(name = "userPassword")
    private String Password;
    @Attribute(name = "sn")
    private String sn;
    @Attribute(name="givenName")
    private String givenName;

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(
                this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder
                .reflectionHashCode(this);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(
                this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
