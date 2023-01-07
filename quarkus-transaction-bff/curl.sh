#!/bin/bash
curl --location --request POST 'http://localhost:8080/v1/transactions' \
--header 'Content-Type: application/json' \
--data-raw '{
  "beneficiario": {
    "agencia": "1210",
    "codigoBanco": 341,
    "conta": "101214",
    "cpf": 12735354,
    "nomeFavorecido": "JOSÉ DOS SANTOS"
  },
  "tipoTransacao": "PAGAMENTO_TRIBUTOS",
  "valor": 140
}'


curl --location --request GET 'http://localhost:8080/v1/transactions/2de5ba3f-47d4-4636-a5fc-fb04f9cfa21f' \
--header 'Content-Type: application/json'

curl --location --request GET 'http://localhost:8080/v1/limite/' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJDbU9rcWFsd1Q3SnJjVTA5X2ljVkpaQmxOQVpxc3lYUmh4cGFmanAyS3hJIn0.eyJleHAiOjE2MzE4MDU3ODAsImlhdCI6MTYzMTgwNDU4MCwianRpIjoiMGNhNTA3NzMtZGNlYS00NDFhLWFhODYtNTg3MTM5MmM1NWQ4IiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay5jb2ZmZWVhbmRpdC5jb20uYnIvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjE5YjFmMmQzLWViNWMtNGEzOC1hODFhLWE2NGY5YTk5OGQ2NSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImNvZmZlZWFuZGl0Iiwic2Vzc2lvbl9zdGF0ZSI6ImY1ZDcxOTA5LTdkM2ItNDg5Yy1iZDM2LWQ2NTAxYjc0Nzc0NyIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgQ29mZmVlQW5kSVRSb2xlIGNvZmZlZWFuZGl0IGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjb250YSI6Ijc4NTkyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY29mZmVlYW5kaXQiLCJhZ2VuY2lhIjoiMjA5IiwiZW1haWwiOiJjb2ZmZWVhbmRpdEBjb2ZmZWVhbmRpdC5jb20uYnIifQ.RkEVvQCKYKs2MnBkKMpHP922cRkVtnm2w2MhTIx-cfJQmxMW6gd0k6a7xrUAPEXEHtE19ZHuVRXakSJOSvc43gutWwqvQeWFucvr2Yei7iKd9RgZni5SrZa3SnVVr0aSe8jUKKgIRQKO7dienjSVxGG6OfZwRu082rJjLkbLxXCMpWBM1xpGhPMwdMjwW1BxkBpzNpm0QFtUB-g8DaXQGtpjaEr78ZEhfavDfgQcIppmP9FGiuUjJ-3Su_lDHB9eRh9FkZr37NiLC7WHC2AGWIPV-TuUxG-rpUReKBJITh9I4uK58vrq83nQ99CLEUBXwCU9bhA_3ULOZnUbbQbOXQ'