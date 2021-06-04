terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }
}

provider "aws" {
  profile = "default"
  region  = "us-west-2"
}

resource "aws_instance" "vm-web" {
  ami           = "ami-830c94e3"
  instance_type = var.ec2_instance_type

  tags = {
    Name = "server for web"
    Env = "dev"
  }
}

