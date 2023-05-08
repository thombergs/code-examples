provider "aws" {
  profile = "Admin-Account-Access-489455091964"  
  region = "eu-central-1"
}

data "aws_ami" "latest-linux" {
    most_recent = true

    filter {
        name   = "name"
        values = ["*Linux 2023*"]
    }

    filter {
        name   = "virtualization-type"
        values = ["hvm"]
    }
}

resource "aws_key_pair" "tf-key-pair" {
    key_name = "tf-key-pair"
    public_key = tls_private_key.rsa.public_key_openssh
}

resource "tls_private_key" "rsa" {
    algorithm = "RSA"
    rsa_bits  = 4096
}

resource "local_file" "tf-key" {
    content  = tls_private_key.rsa.private_key_pem
    filename = "tf-key-pair.pem"
}

resource "aws_vpc_security_group_ingress_rule" "sg-ssh" {
  security_group_id = aws_security_group.main.id

  cidr_ipv4   = "0.0.0.0/0"
  from_port   = 22
  ip_protocol = "tcp"
  to_port     = 22
}

resource "aws_vpc_security_group_egress_rule" "sg-down" {
  security_group_id = aws_security_group.main.id

  cidr_ipv4   = "0.0.0.0/0"
  from_port   = 443
  ip_protocol = "tcp"
  to_port     = 443
}

resource "aws_cloudwatch_log_group" "accountsAppLogs" {
  name = "accounts"

  tags = {
    Environment = "production"
    Application = "accountsApi"
  }
}

resource "aws_security_group" "main" {

}

resource "aws_instance" "ec2-web1" {
 # ami           =  data.aws_ami.latest-linux.id
  ami           = "ami-0b7fd829e7758b06d"
  instance_type = "t2.micro"
  tags = {
    Name = "app-ec2-server",
    Created_By = "pratik"
  }
  key_name = "tf-key-pair"
  vpc_security_group_ids = [aws_security_group.main.id]
  user_data = <<-EOF
               #!/bin/bash
               echo "installing jdk"
               yum update -y
               wget https://download.java.net/java/GA/jdk20.0.1/b4887098932d415489976708ad6d1a4b/9/GPL/openjdk-20.0.1_linux-x64_bin.tar.gz
               tar xvf openjdk*
               export JAVA_HOME=jdk-20.0.1
               export PATH=$JAVA_HOME/bin:$PATH
               export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
               sudo yum install amazon-cloudwatch-agent
              EOF
}

resource "aws_iam_role" "ec2_role" {
  name = "ec2_role"

  # Terraform's "jsonencode" function converts a
  # Terraform expression result to valid JSON syntax.
  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": [
          "cloudwatch:PutMetricData",
          "ec2:DescribeVolumes",
          "ec2:DescribeTags",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams",
          "logs:DescribeLogGroups",
          "logs:CreateLogStream",
          "logs:CreateLogGroup"
        ],
        "Resource": "*"
      },
      {
        "Effect": "Allow",
        "Action": [
          "ssm:GetParameter"
        ],
        "Resource": "arn:aws:ssm:*:*:parameter/AmazonCloudWatch-*"
      }
    ]
  })

  tags = {
    tag-key = "tag-value"
  }
}
output "server_private_ip" {
value = aws_instance.ec2-web1.private_ip
}
output "server_public_ipv4" {
value = aws_instance.ec2-web1.public_ip
}
output "server_id" {
value = aws_instance.ec2-web1.id
}
