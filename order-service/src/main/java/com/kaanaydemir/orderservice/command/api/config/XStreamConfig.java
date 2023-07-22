package com.kaanaydemir.orderservice.command.api.config;

import com.kaanaydemir.commonservice.query.GetUserPaymentDetailsQuery;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.WildcardTypePermission;
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
        xstream.addPermission(new WildcardTypePermission(new String[] {"**"}));

        return xstream;
    }
}

