package com.example.pixels.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Endpoint(id = "features")
public class FeatureEndPoint {

    List<String> modals = Arrays.asList("Movie",
            "Theater", "Location", "Address", "Screen", "Seat", "MovieSchedule");

    private final Map<String,Feature> featureMap =
            new ConcurrentHashMap<>();

    public FeatureEndPoint() {
        for (String modal : modals) {
            featureMap.put(modal, Feature.builder().build()); // Default values are false for boolean fields
        }
        featureMap.get("Movie").setCRUDImplemented(true);
        featureMap.get("Movie").setModalValidationImplemented(true);
        // ... add similar lines for other modals as needed
    }

    @ReadOperation
    public Map<String,Feature> features(){
        return featureMap;
    }

    @ReadOperation
    public Feature feature(@Selector String featureName) {
        return featureMap.get(featureName);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class Feature {
        private boolean isCRUDImplemented;
        private boolean isExceptionHandlingImplemented;
        private boolean isControllerValidationImplemented;
        private boolean isServiceValidationImplemented;
        private boolean isRepositoryValidationImplemented;
        private boolean isModalValidationImplemented;
        private boolean isSecurityImplemented;
        private boolean isControllerTestCasesImplemented;
        private boolean isServiceTestCasesImplemented;
        private boolean isRepositoryTestCasesImplemented;
        private boolean isLoggingImplemented;
    }
}
