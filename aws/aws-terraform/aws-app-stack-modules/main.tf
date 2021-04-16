terraform {

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.36"
    }
  }
}

provider "aws" {
  profile = "default"
  region  = "us-west-2"
}

module "app_server" {
  source = "./modules/application"

  ec2_instance_type    = "t2.micro"
  ami = "ami-830c94e3"
  tags = {
    Name = "server for web"
    Env = "dev"
  }
}

module "app_storage" {
  source = "./modules/storage/"

  bucket_name     = "io.pratik.tf-example-bucket"
  env = "dev"
}

