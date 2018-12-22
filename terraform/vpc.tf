// range 10.0.0.1 to 10.0.255.254
resource "aws_vpc" "recipes" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  tags {
    Name = "recipes-vpc"
  }
}

resource "aws_default_security_group" "recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"

  tags {
    Name = "sg-default-recipes-vpc"
  }
}

// range 10.0.10.1 to 10.0.10.254
resource "aws_subnet" "public-0-recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"
  cidr_block = "10.0.10.0/24"
  availability_zone = "eu-west-1a"
  tags {
    Name = "subnet-public-0-recipes-vpc"
  }
}

// range 10.0.1.1 to 10.0.1.254
resource "aws_subnet" "private-1-recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"
  cidr_block = "10.0.1.0/24"
  availability_zone = "eu-west-1a"
  tags {
    Name = "subnet-private-1-recipes-vpc"
  }
}

// range 10.0.20.1 to 10.0.20.254
resource "aws_subnet" "public-2-recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"
  cidr_block = "10.0.20.0/24"
  availability_zone = "eu-west-1b"
  tags {
    Name = "subnet-public-2-recipes-vpc"
  }
}

// range 10.0.2.1 to 10.0.2.254
resource "aws_subnet" "private-3-recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"
  cidr_block = "10.0.2.0/24"
  availability_zone = "eu-west-1b"
  tags {
    Name = "subnet-private-3-recipes-vpc"
  }
}

resource "aws_internet_gateway" "recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"
  tags {
    Name = "igw-recipes-vpc"
  }
}

resource "aws_route_table" "recipes-public-subnets" {
  vpc_id = "${aws_vpc.recipes.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.recipes-vpc.id}"
  }
  tags {
    Name = "route-table-public-subnets-recipes-vpc"
  }
}

resource "aws_route_table_association" "public-rt-to-subnet0" {
  subnet_id = "${aws_subnet.public-0-recipes-vpc.id}"
  route_table_id = "${aws_route_table.recipes-public-subnets.id}"
}

resource "aws_route_table_association" "public-rt-to-subnet2" {
  subnet_id = "${aws_subnet.public-2-recipes-vpc.id}"
  route_table_id = "${aws_route_table.recipes-public-subnets.id}"
}

resource "aws_route_table" "private-subnet-1-recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"

  tags {
    Name = "route-table-private-subnet-1-recipes-vpc"
  }
}

resource "aws_route_table" "private-subnet-3-recipes-vpc" {
  vpc_id = "${aws_vpc.recipes.id}"

  tags {
    Name = "route-table-private-subnet-3-recipes-vpc"
  }
}

resource "aws_vpc_endpoint" "dynamodb" {
  vpc_id       = "${aws_vpc.recipes.id}"
  service_name = "com.amazonaws.eu-west-1.dynamodb"
  route_table_ids = [
    "${aws_route_table.private-subnet-1-recipes-vpc.id}",
    "${aws_route_table.private-subnet-3-recipes-vpc.id}",
    "${aws_route_table.recipes-public-subnets.id}",
  ]
}

resource "aws_route_table_association" "private-rt-to-subnet1" {
  subnet_id = "${aws_subnet.private-1-recipes-vpc.id}"
  route_table_id = "${aws_route_table.private-subnet-1-recipes-vpc.id}"
}

resource "aws_route_table_association" "private-rt-to-subnet3" {
  subnet_id = "${aws_subnet.private-3-recipes-vpc.id}"
  route_table_id = "${aws_route_table.private-subnet-3-recipes-vpc.id}"
}


