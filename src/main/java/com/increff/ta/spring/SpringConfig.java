package com.increff.ta.spring;

import org.springframework.context.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@ComponentScan("com.increff.ta")
@PropertySources({ //
        @PropertySource(value = "file:./employee.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {

    private int maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(maxUploadSizeInMb * 2);
        cmr.setMaxUploadSizePerFile(maxUploadSizeInMb); //bytes
        return cmr;

    }

}
