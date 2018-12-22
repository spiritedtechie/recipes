#!/usr/bin/env bash
./terraform apply -target=aws_dynamodb_table.recipe \
                  -target=aws_iam_policy.dynamodb-recipes-table-access \
                  -target=aws_iam_user.recipes-api \
                  -target=aws_iam_user_policy_attachment.dynamodb-recipes-table-access
