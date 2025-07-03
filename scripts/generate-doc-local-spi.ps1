$currentPath = (Get-Location).Path
docker run -it --rm -p 8081:80 `
  -v "${currentPath}\api-definitions\src\main\resources\openapi\ihk-content-provider-spi.yaml:/usr/share/nginx/html/swagger.yaml" `
  -e SPEC_URL=swagger.yaml redocly/redoc


