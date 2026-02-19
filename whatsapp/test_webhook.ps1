$headers = @{ "Content-Type" = "application/json" }
$body = @{
    name = "Debug User"
    email = "test@example.com"
    phone = "1234567890"
    facingProblems = "Debug Test"
}
$jsonBody = $body | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/webhook/submit" -Method Post -Headers $headers -Body $jsonBody
    Write-Host "Success! Response: $response"
} catch {
    Write-Host "Error: $_"
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Host "Response Body: $($reader.ReadToEnd())"
    }
}
