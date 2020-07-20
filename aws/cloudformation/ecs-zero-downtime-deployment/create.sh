# Turning off the AWS pager so that the CLI doesn't open an editor for each command result
export AWS_PAGER=""

aws cloudformation create-stack \
  --stack-name reflectoring-ecs-zero-downtime-deployment-network \
  --template-body file://network.yml \
  --capabilities CAPABILITY_IAM

aws cloudformation wait stack-create-complete --stack-name reflectoring-ecs-zero-downtime-deployment-network

aws cloudformation create-stack \
  --stack-name reflectoring-ecs-zero-downtime-deployment-service \
  --template-body file://service.yml \
  --parameters \
      ParameterKey=StackName,ParameterValue=reflectoring-ecs-zero-downtime-deployment-network \
      ParameterKey=ServiceName,ParameterValue=reflectoring-hello-world \
      ParameterKey=ImageUrl,ParameterValue=docker.io/reflectoring/aws-hello-world:v3 \
      ParameterKey=ContainerPort,ParameterValue=8080 \
      ParameterKey=HealthCheckPath,ParameterValue=/hello \
      ParameterKey=HealthCheckIntervalSeconds,ParameterValue=90

aws cloudformation wait stack-create-complete --stack-name reflectoring-ecs-zero-downtime-deployment-service
