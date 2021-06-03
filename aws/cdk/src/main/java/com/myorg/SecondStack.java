package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.s3.Bucket;

public class SecondStack extends Stack {
    public SecondStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public SecondStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        String bucketName = (String) this.getNode().tryGetContext("bucketName2");

        Bucket bucket = Bucket.Builder.create(this, "bucket")
                .bucketName(bucketName)
                .build();

    }
}
