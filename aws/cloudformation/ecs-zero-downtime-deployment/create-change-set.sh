# Turning off the AWS pager so that the CLI doesn't open an editor for each command result
export AWS_PAGER=""

aws cloudformation create-change-set \
  --change-set-name update-reflectoring-ecs-zero-downtime-deployment-service \
  --stack-name reflectoring-ecs-zero-downtime-deployment-service \
  --use-previous-template \
  --parameters \
      ParameterKey=StackName,ParameterValue=reflectoring-ecs-zero-downtime-deployment-network \
      ParameterKey=ServiceName,ParameterValue=reflectoring-hello-world \
      ParameterKey=ImageUrl,ParameterValue=docker.io/reflectoring/aws-hello-world:v4 \
      ParameterKey=ContainerPort,ParameterValue=8080 \
      ParameterKey=HealthCheckPath,ParameterValue=/hello \
      ParameterKey=HealthCheckIntervalSeconds,ParameterValue=90

aws cloudformation describe-change-set \
  --stack-name reflectoring-ecs-zero-downtime-deployment-service \
  --change-set-name update-reflectoring-ecs-zero-downtime-deployment-service
