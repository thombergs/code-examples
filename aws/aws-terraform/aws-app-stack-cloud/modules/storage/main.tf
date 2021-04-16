resource "aws_s3_bucket" "s3_bucket" {
  bucket = format("%s-%s",var.bucket_name,var.env)
  acl    = "private"

  tags = {
    Name        = var.bucket_name
    Environment = var.env
  }
}