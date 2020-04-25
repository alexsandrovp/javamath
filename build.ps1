param ($jarId = 'mathgists', [Version]$version = '1.0.0.1')

Write-Host "Building $jarId version $version" -ForegroundColor Green

$jarName = $jarId
if (-not $jarName.EndsWith('.jar')) {
	$jarName += '.jar'
}

Set-Alias jar 'C:\jdk\bin\jar.exe'
Set-Alias java 'C:\jdk\bin\java.exe'
Set-Alias javac 'C:\jdk\bin\javac.exe'

$restore = Get-Location

Remove-Item "$PSScriptRoot\$jarName" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "$PSScriptRoot\_build" -Recurse -Force -ErrorAction SilentlyContinue

$proc = Start-Process (Get-Alias javac).Definition " -d _build *.java" -PassThru -Wait -NoNewWindow

if ($proc.ExitCode -eq 0) {
	Set-Location "$PSScriptRoot\_build"

	#Run unit tests
	$proc = Start-Process (Get-Alias java).Definition "TestRunner" -PassThru -Wait -NoNewWindow
	if ($proc.ExitCode -eq 0) {
		Remove-Item TestRunner.class
		Get-ChildItem *UTest.class -Recurse | Remove-Item
	
		Copy-Item ..\META-INF . -Recurse
		$manifestFile = Get-Item .\META-INF\MANIFEST.MF
		$verStr = "$($version.Major).$($version.Minor).$($version.Build)"
		$manifest = Get-Content $manifestFile -Encoding UTF8 -Raw
		$manifest = $manifest.Replace('$id', $jarId)
		$manifest = $manifest.Replace('$version', $verStr)
		$manifest = $manifest.Replace('$revision', $version.Revision)
		#$manifest | Out-File $manifestFile -Encoding UTF8
		[IO.File]::WriteAllText($manifestFile, $manifest)

		$proc = Start-Process (Get-Alias jar).Definition "-cvfM $jarName ." -PassThru -Wait -NoNewWindow
		if ($proc.ExitCode -eq 0) {
			Copy-Item $jarName ..
		} else {
			Write-Error "build failed (jar): code $($proc.ExitCode)"
		}
	} else {
		Write-Error "build failed (unit tests): code $($proc.ExitCode)"
	}

	Set-Location $restore
	Remove-Item "$PSScriptRoot\_build" -Recurse -Force -ErrorAction SilentlyContinue
	
} else {
	Write-Error "build failed (javac): code $($proc.ExitCode)"
}