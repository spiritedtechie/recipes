data "aws_iam_policy_document" "dynamodb-recipe-table-access" {
  statement {
    actions = [
      "dynamodb:BatchGet*",
      "dynamodb:DescribeStream",
      "dynamodb:DescribeTable",
      "dynamodb:Get*",
      "dynamodb:Query",
      "dynamodb:Scan",
      "dynamodb:BatchWrite*",
      "dynamodb:Delete*",
      "dynamodb:Update*",
      "dynamodb:PutItem"
    ]

    resources = [
      "${aws_dynamodb_table.recipe.arn}",
    ]
  }

  statement {
    actions = [
      "dynamodb:Query"
    ]

    resources = [
      "${aws_dynamodb_table.recipe.arn}/index/*",
    ]
  }
}

resource "aws_iam_policy" "dynamodb-recipe-table-access" {
  name        = "policy-dynamodb-recipe-rw"
  path        = "/recipe/"
  description = "Read/write to Recipe table"
  policy      = "${data.aws_iam_policy_document.dynamodb-recipe-table-access.json}"
}