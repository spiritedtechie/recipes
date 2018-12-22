data "aws_iam_policy_document" "dynamodb-recipes-table-access" {
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
      "${aws_dynamodb_table.recipes.arn}",
    ]
  }

  statement {
    actions = [
      "dynamodb:Query"
    ]

    resources = [
      "${aws_dynamodb_table.recipes.arn}/index/*",
    ]
  }
}

resource "aws_iam_policy" "dynamodb-recipes-table-access" {
  name        = "policy-dynamodb-recipes-rw"
  path        = "/recipe/"
  description = "Read/write to Recipe table"
  policy      = "${data.aws_iam_policy_document.dynamodb-recipes-table-access.json}"
}