# Turning off the AWS pager so that the CLI doesn't open an editor for each command result
export AWS_PAGER=""

IMAGE_URL=$1

aws cloudformation update-stack \
  --stack-name reflectoring-ecs-zero-downtime-deployment-service \
  --use-previous-template \
  --parameters \
      ParameterKey=StackName,ParameterValue=reflectoring-ecs-zero-downtime-deployment-network \
      ParameterKey=ServiceName,ParameterValue=reflectoring-hello-world \
      ParameterKey=ImageUrl,ParameterValue=$IMAGE_URL \
      ParameterKey=ContainerPort,ParameterValue=8080 \
      ParameterKey=HealthCheckPath,ParameterValue=/hello \
      ParameterKey=HealthCheckIntervalSeconds,ParameterValue=90

aws cloudformation wait stack-update-complete --stack-name reflectoring-ecs-zero-downtime-deployment-service
