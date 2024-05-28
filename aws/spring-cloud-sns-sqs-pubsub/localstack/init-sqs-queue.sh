#!/bin/bash
queue_name="dispatch-email-notification"

awslocal sqs create-queue --queue-name $queue_name

echo "SQS queue '$queue_name' created successfully"
echo "Executed init-sqs-queue.sh"