#!/bin/bash
topic_name="user-account-created"

awslocal sns create-topic --name $topic_name

echo "SNS topic '$topic_name' created successfully"
echo "Executed init-sns-topic.sh"