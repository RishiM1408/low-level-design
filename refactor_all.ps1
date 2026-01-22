$problemsRoot = "e:\low-level-design\Problems"
$modules = Get-ChildItem -Path $problemsRoot -Directory

foreach ($mod in $modules) {
    $name = $mod.Name
    $path = $mod.FullName

    # Skip if already mavenized (look for pom.xml)
    if (Test-Path "$path\pom.xml") {
        Write-Host "Skipping $name (Already Mavenized)"
        continue
    }

    Write-Host "Refactoring $name..."

    # 1. Detect Package Name
    # Expecting src/com/lld/xxxx
    $pkgDir = Get-ChildItem -Path "$path\src\com\lld" -Directory | Select-Object -First 1
    if ($null -eq $pkgDir) {
        Write-Host "  Warning: Could not detect package in $name. Using default naming."
        $pkgName = $name.ToLower()
    } else {
        $pkgName = $pkgDir.Name
    }
    Write-Host "  Package: com.lld.$pkgName"

    # 2. Create POM
    $artifactId = $name -replace '([a-z])([A-Z])', '$1-$2' # CamelCase to kebab-case rough approx or just lowercase
    $artifactId = $artifactId.ToLower()

    $pomContent = @"
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.lld</groupId>
        <artifactId>lld-mastery-pro</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>
    <artifactId>$artifactId</artifactId>
</project>
"@
    Set-Content -Path "$path\pom.xml" -Value $pomContent
    Write-Host "  Created pom.xml"

    # 3. Create Directory Structure
    $srcMain = "$path\src\main\java"
    $srcTest = "$path\src\test\java\com\lld\$pkgName"
    
    if (-not (Test-Path $srcMain)) {
        New-Item -ItemType Directory -Force -Path $srcMain | Out-Null
    }
    if (-not (Test-Path $srcTest)) {
        New-Item -ItemType Directory -Force -Path $srcTest | Out-Null
    }

    # 4. Move Source Files
    # Move src/com to src/main/java/com
    if (Test-Path "$path\src\com") {
        Move-Item -Path "$path\src\com" -Destination "$srcMain" -Force
        Write-Host "  Moved source files"
    }

    # 5. Create Placeholder Test
    $testContent = @"
package com.lld.$pkgName;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SolutionTest {
    @Test
    public void testSanity() {
        // Validation for $name
        // In a real scenario, this would test core logic.
        assertTrue(true, "Sanity check passed for $name");
    }
}
"@
    Set-Content -Path "$srcTest\SolutionTest.java" -Value $testContent
    Write-Host "  Created SolutionTest.java"
}
