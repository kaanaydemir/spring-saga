package com.kaanaydemir.userservice.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.kaanaydemir.commonservice.query.GetUserPaymentDetailsQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XStreamConfig {

    @Bean
    public XStream xStream() {
        XStream xstream = new XStream();
        // clear out existing permissions and set own ones
        xstream.addPermission(NoTypePermission.NONE);
        // allow some specific classes
        xstream.allowTypes(new Class[]{GetUserPaymentDetailsQuery.class});

        return xstream;
    }
}

