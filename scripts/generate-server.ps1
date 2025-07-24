$currentPath = (Get-Location).Path
docker run --rm `
    -v "${currentPath}:/local" `
    openapitools/openapi-generator-cli generate `
    -i /local/api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml `
    -g spring `
    -o /local/out/spring/spi