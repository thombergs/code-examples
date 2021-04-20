variable "ec2_instance_type" {
  description = "Instance type"
  type        = string
}

variable "ami" {
  description = "ami id"
  type = string
}

variable "tags" {
  description = "Tags to set on the bucket."
  type        = map(string)
  default     = {Name = "server for web"
                 Env = "dev"}
}