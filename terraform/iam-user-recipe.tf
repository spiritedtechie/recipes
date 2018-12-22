# Create user
resource "aws_iam_user" "recipes-api" {
  name = "recipes-api"
  path = "/recipe/"
}

resource "aws_iam_user_policy_attachment" "dynamodb-recipes-table-access" {
  user       = "${aws_iam_user.recipes-api.name}"
  policy_arn = "${aws_iam_policy.dynamodb-recipes-table-access.arn}"
}