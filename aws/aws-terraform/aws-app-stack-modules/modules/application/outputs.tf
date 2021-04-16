output "instanceID" {
  description = "ID of ec2 instance"
  value       = aws_instance.vm-web.id
}