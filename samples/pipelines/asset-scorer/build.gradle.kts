plugins {
    id("com.atlan.kotlin-custom-package")
}

dependencies {
    implementation(project(":package-toolkit:events"))
    // In your own project, you would use this in place of the 1 dependency above:
    // implementation("com.atlan:package-toolkit-events:+")
}
