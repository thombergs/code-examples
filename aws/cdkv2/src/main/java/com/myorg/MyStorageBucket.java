/**
 * 
 */
package com.myorg;



import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.LifecycleRule;
import software.constructs.Construct;

/**
 * @author pratikdas
 *
 */
public class MyStorageBucket extends Construct{

	public MyStorageBucket(final Construct scope, final String id) {
		super(scope, id);
		Bucket bucket = new Bucket(this, "mybucket");
		
		LifecycleRule lifecycleRule = LifecycleRule.builder()
		         .abortIncompleteMultipartUploadAfter(Duration.minutes(30))
		         .enabled(false)
		         .expiration(Duration.minutes(30))
		         .expiredObjectDeleteMarker(false)
		         .id("id").build();
		
		bucket.addLifecycleRule(lifecycleRule);
		
	}

}
