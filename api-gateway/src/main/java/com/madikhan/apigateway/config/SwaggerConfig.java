package com.madikhan.apigateway.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary //Override Original swagger implementation with custom swagger resource provider.
public class SwaggerConfig implements SwaggerResourcesProvider {

    @Override
    public List get() {
        List resources = new ArrayList();
        resources.add(swaggerResource("profile-ms", "/profile/v2/api-docs", "2.0"));
        resources.add(swaggerResource("chat-ms", "/chat/v2/api-docs", "2.0"));
        resources.add(swaggerResource("image-ms", "/image/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}