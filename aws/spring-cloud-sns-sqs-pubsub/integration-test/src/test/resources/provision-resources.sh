#!/bin/bash
topic_name="user-account-created"
queue_name="dispatch-email-notification"

sns_arn_prefix="arn:aws:sns:us-east-1:000000000000"
sqs_arn_prefix="arn:aws:sqs:us-east-1:000000000000"

awslocal sns create-topic --name $topic_name
echo "SNS topic '$topic_name' created successfully"

awslocal sqs create-queue --queue-name $queue_name
echo "SQS queue '$queue_name' created successfully"

awslocal sns subscribe --topic-arn "$sns_arn_prefix:$topic_name" --protocol sqs --notification-endpoint "$sqs_arn_prefix:$queue_name"
echo "Subscribed SQS queue '$queue_name' to SNS topic '$topic_name' successfully"

echo "Successfully provisioned resources"