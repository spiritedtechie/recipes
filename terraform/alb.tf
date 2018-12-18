resource "aws_alb" "recipe" {
  name                = "alb-recipe"
  security_groups     = ["${aws_security_group.public_recipe.id}"]
  subnets             = [
    "${aws_subnet.public-0-recipe-vpc.id}",
    "${aws_subnet.public-2-recipe-vpc.id}"
  ]
  tags {
    Name = "alb-recipe"
  }
}

resource "aws_alb_target_group" "recipe-8080" {
  port                = "8080"
  protocol            = "HTTP"
  vpc_id              = "${aws_vpc.recipe.id}"
  target_type         = "ip"

  health_check {
    healthy_threshold   = "5"
    unhealthy_threshold = "2"
    interval            = "30"
    matcher             = "200"
    path                = "/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = "5"
  }

  tags {
    Name = "alb-target-group-8080-recipe"
  }

  lifecycle {
    create_before_destroy = true
  }
}

# Redirect all traffic from the ALB to the target group
resource "aws_alb_listener" "alb-listener-recipe-8080" {
  load_balancer_arn = "${aws_alb.recipe.id}"
  port              = "8080"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.recipe-8080.id}"
    type             = "forward"
  }
}