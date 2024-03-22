package com.mw.springcloud.utill;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.util.Hashtable;

public class LdapUtil {

    /**
     *  初始化LdapTemplate
     * @return
     */
    public static LdapTemplate getLdapTemplate(){
        LdapTemplate template = null;
        try {
            System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");
            System.setProperty("java.naming.ldap.attributes.binary", "objectSid");
            LdapContextSource contextSource = new LdapContextSource();

            String url = "ldaps://127.0.0.1:636";
            String base = "DC=domain,DC=com";
            String userDn = "CN=Administrator,CN=Users,DC=domain,DC=com";
            String password = "Letmein123";

            contextSource.setUrl(url);
            contextSource.setBase(base);
            contextSource.setUserDn(userDn);
            contextSource.setPassword(password);
            contextSource.setPooled(false);
            contextSource.afterPropertiesSet(); // important
            Hashtable<String, Object> baseEnvMaps = new Hashtable<String, Object>();
            baseEnvMaps.put("java.naming.ldap.attributes.binary", "objectSid");
            contextSource.setBaseEnvironmentProperties(baseEnvMaps);
            template = new LdapTemplate(contextSource);
            template.setIgnorePartialResultException(true);

        }catch (Exception e){
            e.printStackTrace();
        }
        return template;
    }

    static String getObjectSid(byte[] SID) {
        StringBuilder strSID = new StringBuilder("S-");
        strSID.append(SID[0]).append('-');
        StringBuilder tmpBuff = new StringBuilder();
        for (int t = 2; t <= 7; t++) {
            String hexString = Integer.toHexString(SID[t] & 0xFF);
            tmpBuff.append(hexString);
        }
        strSID.append(Long.parseLong(tmpBuff.toString(), 16));
        int count = SID[1];
        for (int i = 0; i < count; i++) {
            int currSubAuthOffset = i * 4;
            tmpBuff.setLength(0);
            tmpBuff.append(String.format("%02X%02X%02X%02X",
                    (SID[11 + currSubAuthOffset] & 0xFF),
                    (SID[10 + currSubAuthOffset] & 0xFF),
                    (SID[9 + currSubAuthOffset] & 0xFF),
                    (SID[8 + currSubAuthOffset] & 0xFF)));
            strSID.append('-').append(
                    Long.parseLong(tmpBuff.toString(), 16));

        }
        return strSID.toString();
    }
}

