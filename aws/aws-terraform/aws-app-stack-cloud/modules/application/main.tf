resource "aws_instance" "vm-web" {
  ami           = var.ami
  instance_type = var.ec2_instance_type
  tags = var.tags
}
