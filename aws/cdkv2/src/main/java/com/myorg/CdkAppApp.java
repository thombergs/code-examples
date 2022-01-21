package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class CdkAppApp {
    public static void main(final String[] args) {
        App app = new App();

        new CdkAppStack(app, "CdkAppStack", StackProps.builder()
         
                .env(Environment.builder()
                        .account("**********")
                        .region("us-east-1")
                        .build())
                
                .build());

        app.synth();
    }
}

