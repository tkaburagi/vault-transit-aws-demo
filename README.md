```shell script
vault secrets enable transit
vault write transit/keys/aes256
```

GUIで暗号化

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:ListBucket"
      ],
      "Resource": "arn:aws:s3:::playground-kabu"
    }
  ]
}
```

```hcl
path "aws/creds/read-s3" {
  capabilities = [ "read" ]
}

path "aws/roles/*" {
  capabilities = [ "list", "read" ]
}

path "transit/*" {
  capabilities = [ "read", "create", "list", "update", "delete" ]
}
```