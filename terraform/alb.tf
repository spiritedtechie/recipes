resource "aws_alb" "recipes" {
  name                = "alb-recipes"
  security_groups     = ["${aws_security_group.public_recipes.id}"]
  subnets             = [
    "${aws_subnet.public-0-recipes-vpc.id}",
    "${aws_subnet.public-2-recipes-vpc.id}"
  ]
  tags {
    Name = "alb-recipes"
  }
}

resource "aws_alb_target_group" "recipes-8080" {
  port                = "8080"
  protocol            = "HTTP"
  vpc_id              = "${aws_vpc.recipes.id}"
  target_type         = "ip"

  health_check {
    healthy_threshold   = "3"
    unhealthy_threshold = "3"
    interval            = "30"
    matcher             = "200"
    path                = "/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = "5"
  }

  tags {
    Name = "alb-target-group-8080-recipes"
  }
}

# Redirect all traffic from the ALB to the target group
resource "aws_alb_listener" "alb-listener-recipes-8080" {
  load_balancer_arn = "${aws_alb.recipes.id}"
  port              = "8080"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.recipes-8080.id}"
    type             = "forward"
  }
}