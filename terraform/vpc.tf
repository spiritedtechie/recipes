// range 10.0.0.1 to 10.0.255.254
resource "aws_vpc" "recipe" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  tags {
    Name = "recipe-vpc"
  }
}

resource "aws_default_security_group" "recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"
  tags {
    Name = "sg-default-recipe-vpc"
  }
}

// range 10.0.10.1 to 10.0.10.254
resource "aws_subnet" "public-0-recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"
  cidr_block = "10.0.10.0/24"
  availability_zone = "eu-west-1a"
  tags {
    Name = "subnet-public-0-recipe-vpc"
  }
}

// range 10.0.1.1 to 10.0.1.254
resource "aws_subnet" "private-1-recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"
  cidr_block = "10.0.1.0/24"
  availability_zone = "eu-west-1a"
  tags {
    Name = "subnet-private-1-recipe-vpc"
  }
}

// range 10.0.20.1 to 10.0.20.254
resource "aws_subnet" "public-2-recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"
  cidr_block = "10.0.20.0/24"
  availability_zone = "eu-west-1b"
  tags {
    Name = "subnet-public-2-recipe-vpc"
  }
}

// range 10.0.2.1 to 10.0.2.254
resource "aws_subnet" "private-3-recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"
  cidr_block = "10.0.2.0/24"
  availability_zone = "eu-west-1b"
  tags {
    Name = "subnet-private-3-recipe-vpc"
  }
}

resource "aws_internet_gateway" "recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"
  tags {
    Name = "igw-recipe-vpc"
  }
}

# Nat gateway to allow private subnets to communicate out to the internet
resource "aws_eip" "subnet-public-0" {
  vpc = true
  depends_on = [
    "aws_internet_gateway.recipe-vpc"
  ]
  tags {
    Name = "eip-subnet-public-0-recipe-vpc"
  }
}

resource "aws_eip" "subnet-public-2" {
  vpc = true
  depends_on = [
    "aws_internet_gateway.recipe-vpc"
  ]
  tags {
    Name = "eip-subnet-public-2-recipe-vpc"
  }
}

resource "aws_nat_gateway" "in-subnet-public-0-recipe-vpc" {
  allocation_id = "${aws_eip.subnet-public-0.id}"
  subnet_id = "${aws_subnet.public-0-recipe-vpc.id}"
  depends_on = [
    "aws_internet_gateway.recipe-vpc"
  ]
  tags {
    Name = "nat-gw-in-subnet-public-0-recipe-vpc"
  }
}

resource "aws_nat_gateway" "in-subnet-public-2-recipe-vpc" {
  allocation_id = "${aws_eip.subnet-public-2.id}"
  subnet_id = "${aws_subnet.public-2-recipe-vpc.id}"
  depends_on = [
    "aws_internet_gateway.recipe-vpc"
  ]
  tags {
    Name = "nat-gw-in-subnet-public-2-recipe-vpc"
  }
}

resource "aws_route_table" "recipe-public-subnets" {
  vpc_id = "${aws_vpc.recipe.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.recipe-vpc.id}"
  }
  tags {
    Name = "route-table-public-recipe-vpc"
  }
}

resource "aws_route_table_association" "public-rt-to-subnet0" {
  subnet_id = "${aws_subnet.public-0-recipe-vpc.id}"
  route_table_id = "${aws_route_table.recipe-public-subnets.id}"
}

resource "aws_route_table_association" "public-rt-to-subnet2" {
  subnet_id = "${aws_subnet.public-2-recipe-vpc.id}"
  route_table_id = "${aws_route_table.recipe-public-subnets.id}"
}

resource "aws_route_table" "private-subnet-1-recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"

  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = "${aws_nat_gateway.in-subnet-public-0-recipe-vpc.id}"
  }

  tags {
    Name = "route_table_private-subnet-1-recipe-vpc"
  }
}

resource "aws_route_table" "private-subnet-3-recipe-vpc" {
  vpc_id = "${aws_vpc.recipe.id}"

  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = "${aws_nat_gateway.in-subnet-public-2-recipe-vpc.id}"
  }

  tags {
    Name = "route_table_private-subnet-3-recipe-vpc"
  }
}

resource "aws_route_table_association" "private-rt-to-subnet1" {
  subnet_id = "${aws_subnet.private-1-recipe-vpc.id}"
  route_table_id = "${aws_route_table.private-subnet-1-recipe-vpc.id}"
}

resource "aws_route_table_association" "private-rt-to-subnet3" {
  subnet_id = "${aws_subnet.private-3-recipe-vpc.id}"
  route_table_id = "${aws_route_table.private-subnet-3-recipe-vpc.id}"
}
