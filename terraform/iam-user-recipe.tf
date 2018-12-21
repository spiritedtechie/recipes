# Create user
resource "aws_iam_user" "recipe-api" {
  name = "recipe-api"
  path = "/recipe/"
}

resource "aws_iam_user_policy_attachment" "dynamodb-recipe-table-access" {
  user       = "${aws_iam_user.recipe-api.name}"
  policy_arn = "${aws_iam_policy.dynamodb-recipe-table-access.arn}"
}