# IntelliJ IDEA might be using its bundled Maven. 

To find out the Maven settings in IntelliJ IDEA:

1. Go to File > Settings (or IntelliJ IDEA > Preferences on macOS).
>Navigate to Build, Execution, Deployment > Build Tools > Maven.

Here, you can see the "Maven home directory" that IntelliJ is using, which might be set to a bundled version or a specified path if you've configured a standalone Maven installation.

If you find that Maven is not installed on your system (or not added to your **PATH**), and you rely on IntelliJ IDEA's bundled Maven for your projects, you might consider installing Maven separately for command-line usage. <br> 
The official Maven download page provides binaries and installation instructions. <br> 

2. After downloading and extracting Maven, you'll need to set the **M2_HOME** environment variable (optional on recent Maven versions) and add the Maven bin directory to your system's PATH environment variable.

3. Maven – Download Apache Maven → Binary zip archive
Extract the downloaded archive to a folder on your system. For example, you can extract it to 
>C:\Program Files\Apache\maven or any other location you prefer. In the Environment Variables window, under "System Variables", click "New".

4. For the variable name, enter **MAVEN_HOME**. For the variable value, enter the path to the Maven folder you extracted earlier 
>(e.g., C:\Program Files\Apache\maven).

5. Find the Path variable under **"System Variables"** and select it, then click **"Edit"**.

In the "Edit Environment Variable" window, click "New" and add the path to the Maven bin directory, which will be something like 
>C:\Program Files\Apache\maven\bin.

6. Click **"OK"** to close each window.

7. open CMD and run: 
```bash
    mvn -version
```
