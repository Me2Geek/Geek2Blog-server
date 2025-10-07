curl -s http://localhost:8080/v3/api-docs -o openapi.json && npx redoc-cli bundle openapi.json -o index.html
pause