provider "aws" {
  profile = var.profile
  region  = var.region
}

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 2.7.0"
    }
  }
}

resource "aws_ecs_cluster" "main" {
  name = "${var.name}-cluster"
}
resource "aws_iam_role" "ecs_task_role" {
  name                = "${var.name}-ecsTaskRole"
  managed_policy_arns = []
  assume_role_policy  = <<EOF
{
 "Version": "2012-10-17",
 "Statement": [
   {
     "Action": "sts:AssumeRole",
     "Principal": {
       "Service": "ecs-tasks.amazonaws.com"
     },
     "Effect": "Allow",
     "Sid": ""
   }
 ]
}
EOF
}


resource "aws_iam_role" "ecs_task_execution_role" {
  name                = "${var.name}-ecsTaskExecutionRole"
  managed_policy_arns = ["arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy", aws_iam_policy.ecr.arn]
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      },
    ]
  })

}

resource "aws_iam_policy" "ecr" {
  name = "ecr_read"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:GetRepositoryPolicy",
          "ecr:DescribeRepositories",
          "ecr:ListImages",
          "ecr:DescribeImages",
          "ecr:BatchGetImage",
          "ecr:GetLifecyclePolicy",
          "ecr:GetLifecyclePolicyPreview",
          "ecr:ListTagsForResource",
          "ecr:DescribeImageScanFindings"
        ],
        Resource = "*"
      }
    ]
  })
}

resource "aws_security_group" "ecs_tasks" {
  name   = "${var.name}-sg-task"
  vpc_id = var.vpc

  ingress {
    protocol        = "tcp"
    from_port       = var.port
    to_port         = var.port
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    protocol  = "tcp"
    from_port = var.port
    to_port   = var.port
    self      = true
  }

  egress {
    protocol         = "-1"
    from_port        = 0
    to_port          = 0
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

resource "aws_ecs_task_definition" "main" {
  family                   = "${var.name}-task-definition"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn
  container_definitions = jsonencode([{
    name      = "app"
    image     = "${var.account}.dkr.ecr.${var.region}.amazonaws.com/${var.repository}:latest"
    essential = true
    portMappings = [{
      protocol      = "tcp"
      containerPort = var.port
      hostPort      = var.port
    }]
    logConfiguration = {
      logDriver = "awslogs",
      options = {
        awslogs-group = "/ecs/spring-task-definition",
        awslogs-region = "us-east-2",
        awslogs-stream-prefix = "ecs"
      }
    }
    cpu         = 0
    environment = []
    mountPoints = []
    volumesFrom = []
  }])
}

resource "aws_ecs_service" "main" {
  name                               = "spring-service"
  cluster                            = aws_ecs_cluster.main.id
  task_definition                    = aws_ecs_task_definition.main.arn
  desired_count                      = 1
  deployment_minimum_healthy_percent = 100
  deployment_maximum_percent         = 200
  launch_type                        = "FARGATE"
  platform_version                   = "1.4.0"
  scheduling_strategy                = "REPLICA"

  network_configuration {
    security_groups  = [aws_security_group.ecs_tasks.id]
    subnets          = var.subnet
    assign_public_ip = true
  }

  lifecycle {
    ignore_changes = [task_definition, desired_count]
  }
}



