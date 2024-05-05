#!/bin/bash
topic_name="user-account-created"
queue_name="dispatch-email-notification"

awslocal sns subscribe --topic-arn "arn:aws:sns:us-east-1:000000000000:$topic_name" --protocol sqs --notification-endpoint "arn:aws:sqs:us-east-1:000000000000:$queue_name"

echo "Subscribed SQS queue '$queue_name' to SNS topic '$topic_name' successfully"
echo "Executed subscribe-sqs-to-sns.sh"