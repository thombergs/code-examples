package com.myorg;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IEventSource;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.DeduplicationScope;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class LambdaFromSqsStack extends Stack {
    public LambdaFromSqsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public LambdaFromSqsStack(final Construct scope, final String id, final StackProps props) {
         super(scope, id, props);

         Queue dlq = Queue.Builder.create(this, "mydlq")
					        		.queueName("mydlq")
					        		.build();
        
		 DeadLetterQueue deadLetterQueue = DeadLetterQueue.builder()
													.queue(dlq)
													.maxReceiveCount(10)
													.build();
		
		 Queue queue = Queue.Builder.create(this, "myqueue")
        		.queueName("myqueue")
        		.deadLetterQueue(deadLetterQueue )
        		.visibilityTimeout(Duration.minutes(5))
        		.deliveryDelay(Duration.minutes(1))
        		.maxMessageSizeBytes(1024*100)  // 100 Kb
        		.receiveMessageWaitTime(Duration.seconds(5))
        		.retentionPeriod(Duration.days(1))
        		.build();
		 
		 Queue fifoQueue = Queue.Builder.create(this, "myfifoqueue")
	        		.queueName("myfifoqueue.fifo")
	        		.deduplicationScope(DeduplicationScope.MESSAGE_GROUP)
	        		.contentBasedDeduplication(false)
	        		.fifo(true).build();

        
		
		  Function sqsReceiver = Function.Builder.create(this, "SQSReceiver")
		  .code(AssetCode.fromAsset("resources")) .handler("index.handler") .runtime(
		  Runtime.NODEJS_14_X) .build();
		  
		  IEventSource source =
		  SqsEventSource.Builder.create(queue).batchSize(5).build();
		  sqsReceiver.addEventSource(source);
		  
		  Topic topic = Topic.Builder.create(this, "mytopic").build();
		  
		  
		  topic.addSubscription(
				  SqsSubscription.Builder.create(queue).build());
    }
}
