/*
* IAM ECS roles
*/
data "aws_iam_policy_document" "ecs-task-policy" {
  statement {
    effect = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      type = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ecs-execution-role" {
  name               = "ecs-task-execution-role"
  assume_role_policy = "${data.aws_iam_policy_document.ecs-task-policy.json}"
}

data "aws_iam_policy_document" "ecs-execution-policy" {
  statement {
    effect = "Allow"
    resources = ["*"]
    actions = [
      "ecr:GetAuthorizationToken",
      "ecr:BatchCheckLayerAvailability",
      "ecr:GetDownloadUrlForLayer",
      "ecr:BatchGetImage",
      "logs:CreateLogStream",
      "logs:PutLogEvents"
    ]
  }
}

resource "aws_iam_role_policy" "ecs-execution-role-ecs-policy" {
  name   = "ecs-execution-role-ecs-policy"
  role   = "${aws_iam_role.ecs-execution-role.id}"
  policy = "${data.aws_iam_policy_document.ecs-execution-policy.json}"

}

resource "aws_iam_role" "ecs-recipes-api-task-role" {
  name               = "ecs-recipes-api-task-role"
  assume_role_policy = "${data.aws_iam_policy_document.ecs-task-policy.json}"
}

resource "aws_iam_role_policy_attachment" "ecs-recipes-api-task-role-policy" {
  role = "${aws_iam_role.ecs-recipes-api-task-role.id}"
  policy_arn = "${aws_iam_policy.dynamodb-recipes-table-access.arn}"
}


# Traffic to the ECS Cluster should only come from the ALB
resource "aws_security_group" "ecs-recipes" {
  name        = "secgroup-ecs-recipes"
  description = "Allow inbound access tp ECS from the ALB only"
  vpc_id      = "${aws_vpc.recipes.id}"

  ingress {
    protocol        = "tcp"
    from_port       = "8080"
    to_port         = "8080"
    security_groups = ["${aws_security_group.public_recipes.id}"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags {
    Name = "sg-ecs-recipes"
  }
}

# ECR repository that stores our Docker images
data "aws_ecr_repository" "recipes_api" {
  name = "recipes_api"
}

resource "aws_ecs_cluster" "recipes" {
  name = "ecs-cluster-recipes"
}

# ECS task definition for recipes-api
data "template_file" "recipes-api-task" {
  template = "${file("${path.module}/tasks/recipe_api_task_definition.json")}"

  vars {
    name   = "recipes-api"
    image  = "${data.aws_ecr_repository.recipes_api.repository_url}"
  }
}

resource "aws_ecs_task_definition" "recipes-api" {
  family                   = "recipes-api"
  container_definitions    = "${data.template_file.recipes-api-task.rendered}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 1024
  memory                   = 2048
  execution_role_arn       = "${aws_iam_role.ecs-execution-role.arn}"
  task_role_arn            = "${aws_iam_role.ecs-recipes-api-task-role.arn}"
  depends_on = [
    "aws_iam_role.ecs-execution-role"
  ]
}

resource "aws_ecs_service" "recipes-api" {
  name            = "ecs-service-recipes-api"
  cluster         = "${aws_ecs_cluster.recipes.id}"
  task_definition = "${aws_ecs_task_definition.recipes-api.arn}"
  desired_count   = "2"
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = ["${aws_security_group.ecs-recipes.id}"]
    subnets         = ["${aws_subnet.public-0-recipes-vpc.id}", "${aws_subnet.public-2-recipes-vpc.id}"]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.recipes-8080.id}"
    container_name   = "recipes-api"
    container_port   = "8080"
  }

  depends_on = [
    "aws_alb_listener.alb-listener-recipes-8080",
    "aws_alb_target_group.recipes-8080",
    "aws_ecs_task_definition.recipes-api"
  ]
}

# Autoscaling of the ECS cluster
resource "aws_appautoscaling_target" "target" {
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.recipes.name}/${aws_ecs_service.recipes-api.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  role_arn           = "arn:aws:iam::274158739358:role/aws-service-role/ecs.application-autoscaling.amazonaws.com/AWSServiceRoleForApplicationAutoScaling_ECSService"
  min_capacity       = 1
  max_capacity       = 3
}

resource "aws_appautoscaling_policy" "up" {
  name                    = "recipes-api-scale-up"
  service_namespace       = "ecs"
  resource_id             = "service/${aws_ecs_cluster.recipes.name}/${aws_ecs_service.recipes-api.name}"
  scalable_dimension      = "ecs:service:DesiredCount"

  step_scaling_policy_configuration {
    adjustment_type         = "ChangeInCapacity"
    cooldown                = 60
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = 1
    }
  }

  depends_on = ["aws_appautoscaling_target.target"]
}

resource "aws_appautoscaling_policy" "down" {
  name                    = "recipes-api-scale-down"
  service_namespace       = "ecs"
  resource_id             = "service/${aws_ecs_cluster.recipes.name}/${aws_ecs_service.recipes-api.name}"
  scalable_dimension      = "ecs:service:DesiredCount"

  step_scaling_policy_configuration {
    adjustment_type         = "ChangeInCapacity"
    cooldown                = 60
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = -1
    }
  }

  depends_on = ["aws_appautoscaling_target.target"]
}

/* metric used for auto scale */
resource "aws_cloudwatch_metric_alarm" "recipes-api-cpu-utilization-high" {
  alarm_name          = "recipes-api-cpu-utilization-high"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = "85"

  dimensions {
    ClusterName = "${aws_ecs_cluster.recipes.name}"
    ServiceName = "${aws_ecs_service.recipes-api.name}"
  }

  alarm_actions = ["${aws_appautoscaling_policy.up.arn}"]
  ok_actions    = ["${aws_appautoscaling_policy.down.arn}"]
}