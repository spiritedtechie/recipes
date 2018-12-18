resource "aws_security_group" "public_recipe" {
  name = "public-recipe"
  description = "Recipe public access security group"
  vpc_id = "${aws_vpc.recipe.id}"

  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  ingress {
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  tags {
    Name = "sg-public-recipe"
  }

  depends_on = [
    "aws_vpc.recipe"
  ]
}