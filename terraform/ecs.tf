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

resource "aws_iam_role" "ecs-recipe-api-task-role" {
  name               = "ecs-recipe-api-task-role"
  assume_role_policy = "${data.aws_iam_policy_document.ecs-task-policy.json}"
}

resource "aws_iam_role_policy_attachment" "ecs-recipe-api-task-role-policy" {
  role = "${aws_iam_role.ecs-recipe-api-task-role.id}"
  policy_arn = "${aws_iam_policy.dynamodb-recipe-table-access.arn}"
}


# Traffic to the ECS Cluster should only come from the ALB
resource "aws_security_group" "ecs-recipe" {
  name        = "secgroup-ecs-recipe"
  description = "Allow inbound access tp ECS from the ALB only"
  vpc_id      = "${aws_vpc.recipe.id}"

  ingress {
    protocol        = "tcp"
    from_port       = "8080"
    to_port         = "8080"
    security_groups = ["${aws_security_group.public_recipe.id}"]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ECR repository that stores our Docker images
data "aws_ecr_repository" "recipe-api" {
  name = "recipe-api"
}

resource "aws_ecs_cluster" "recipe" {
  name = "ecs-cluster-recipe"
}

# ECS task definition for recipe-api
data "template_file" "recipe-api-task" {
  template = "${file("${path.module}/tasks/recipe_api_task_definition.json")}"

  vars {
    name   = "recipe-api"
    image  = "${data.aws_ecr_repository.recipe-api.repository_url}"
  }
}

resource "aws_ecs_task_definition" "recipe-api" {
  family                   = "recipe-api"
  container_definitions    = "${data.template_file.recipe-api-task.rendered}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = "${aws_iam_role.ecs-execution-role.arn}"
  task_role_arn            = "${aws_iam_role.ecs-recipe-api-task-role.arn}"
  depends_on = [
    "aws_iam_role.ecs-execution-role"
  ]
}

resource "aws_ecs_service" "recipe-api" {
  name            = "ecs-service-recipe-api"
  cluster         = "${aws_ecs_cluster.recipe.id}"
  task_definition = "${aws_ecs_task_definition.recipe-api.arn}"
  desired_count   = "2"
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = ["${aws_security_group.ecs-recipe.id}"]
    subnets         = ["${aws_subnet.private-1-recipe-vpc.id}", "${aws_subnet.private-3-recipe-vpc.id}"]
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.recipe-8080.id}"
    container_name   = "recipe-api"
    container_port   = "8080"
  }

  depends_on = [
    "aws_alb_listener.alb-listener-recipe-8080",
    "aws_alb_target_group.recipe-8080",
    "aws_ecs_task_definition.recipe-api"
  ]
}

# Autoscaling of the ECS cluster
resource "aws_appautoscaling_target" "target" {
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.recipe.name}/${aws_ecs_service.recipe-api.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  role_arn           = "arn:aws:iam::274158739358:role/aws-service-role/ecs.application-autoscaling.amazonaws.com/AWSServiceRoleForApplicationAutoScaling_ECSService"
  min_capacity       = 1
  max_capacity       = 3
}

resource "aws_appautoscaling_policy" "up" {
  name                    = "recipe-api-scale-up"
  service_namespace       = "ecs"
  resource_id             = "service/${aws_ecs_cluster.recipe.name}/${aws_ecs_service.recipe-api.name}"
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
  name                    = "recipe-api-scale-down"
  service_namespace       = "ecs"
  resource_id             = "service/${aws_ecs_cluster.recipe.name}/${aws_ecs_service.recipe-api.name}"
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
resource "aws_cloudwatch_metric_alarm" "recipe-api-cpu-utilization-high" {
  alarm_name          = "recipe-api-cpu-utilization-high"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = "85"

  dimensions {
    ClusterName = "${aws_ecs_cluster.recipe.name}"
    ServiceName = "${aws_ecs_service.recipe-api.name}"
  }

  alarm_actions = ["${aws_appautoscaling_policy.up.arn}"]
  ok_actions    = ["${aws_appautoscaling_policy.down.arn}"]
}