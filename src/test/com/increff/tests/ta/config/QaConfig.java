package com.increff.tests.ta.config;

import com.increff.ta.spring.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(//
    basePackages = { "com.increff.ta" }, //
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
    @PropertySource(value = "classpath:test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {


}