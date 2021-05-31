variable "profile" {
  default = "default"
}

variable "region" {
  default = "us-east-2"
}

variable "port" {
  default = 8080
}

variable "name" {
  default = "example-app"
}

variable "account" {
  default = "<YOUR_ACCOUNT>"
}

variable "repository" {
  default = "<YOUR_REPO_NAME>"
}

variable "vpc" {
  default = "<YOUR_VPC>"
}

variable "subnet" {
  default = ["<YOUR_PUBLIC_SUBNETS>"]
}
