#!/usr/bin/env bash
./terraform apply -target=aws_dynamodb_table.recipe \
                  -target=aws_iam_policy.dynamodb-recipe-table-access \
                  -target=aws_iam_user.recipe-api \
                  -target=aws_iam_user_policy_attachment.dynamodb-recipe-table-access
