# Setup Env

    # By default, listen on port 6379
    docker start redis

# Demo

    # Add a new user first
    curl "http://localhost:8080/register/user?username=hao&password=hao"
    {"code":200,"msg":"ok","data":{"id":1,"username":"hao","password":"$2a$10$RxJDteWl73DFL5DgyXXpuOQfmrU3jWU5fAUP2.JrB9/NLCZBVcCN2"}}
    
    # Login, JWT is returned
    curl -X POST "http://localhost:8080/login/submit?username=hao&password=hao"
    {"code":200,"data":{"token":"Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiaGFvIiwiaWF0IjoxNjI4NDUzODYxLCJpc3MiOiJIYW8iLCJleHAiOjE2Mjg0NTQxNjEsImF1dGhvcml0aWVzIjoibnVsbCJ9.mKbH5leCXsRvFbOeQUvxML3QledXTfHwIxe16d-o15d7rZ9SkE8d4ZF0Gp0Uk5FCs2cIfkdd1HVSUChIIDPo1w"},"msg":"Login ok"}

    # Get users without JWT header, failed
    curl "http://localhost:8080/users"                                   
    {"code":401,"data":"Full authentication is required to access this resource","msg":"User not login"}

    # Get users with JWT header
    curl -H "Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiaGFvIiwiaWF0IjoxNjI4NDUzODYxLCJpc3MiOiJIYW8iLCJleHAiOjE2Mjg0NTQxNjEsImF1dGhvcml0aWVzIjoibnVsbCJ9.mKbH5leCXsRvFbOeQUvxML3QledXTfHwIxe16d-o15d7rZ9SkE8d4ZF0Gp0Uk5FCs2cIfkdd1HVSUChIIDPo1w" "http://localhost:8080/users"
    [{"id":1,"username":"hao","password":"$2a$10$RxJDteWl73DFL5DgyXXpuOQfmrU3jWU5fAUP2.JrB9/NLCZBVcCN2"}]