# HITSZ Taste Platform

> See the latest `README` and code at [HITSZ Taste](https://github.com/efJerryYang/hitsz-taste) 
## Database Password Setup

### Requirements

- Install the requirements from the `requirement.txt` file:

    ```shell
    pip install -r requirement.txt
    ```

### Encrypt password

- Run the `encrypted_password.py` script to encrypt your database password and generate a `application.properties` template. The template will be stored in the classpath `src/main/resources/` by running the script:

    ```shell
    python encrypted_password.py
    ```

  **IMPORTANT:** If you have previously generated a `application.properties` file and have replaced it with a new one, make sure to update the `ENCRYPTED_PASSWORD` environment variable with the new encrypted password.

  **WARNING:** If you do not update the `ENCRYPTED_PASSWORD` environment variable, the application will not be able to decrypt your database password and will not be able to connect to the database.

### Set environment variable

- Set the encrypted password as the `ENCRYPTED_PASSWORD` environment variable on your operating system.

- For Windows, open the System Properties window by running `sysdm.cpl` in the command prompt. Go to the "Advanced" tab and click on the "Environment Variables" button. Add a new system variable called `ENCRYPTED_PASSWORD` with the encrypted password as the value.

- For Linux and MacOS, retain the environment variable for future sessions by adding the `export` command to your shell's initialization file, such as `~/.bashrc` for the bash shell like this:

    ```shell
    echo 'export ENCRYPTED_PASSWORD=<your_encrypted_password> >> ~/.bashrc'
    ```

  This will ensure that the environment variable is set every time you open a new shell session.

### Add properties file to classpath

- Place the generated `application.properties` file to the `src/main/resources/` directory in your project, this step has already been done by the script and you don't need to update anything manually. Place the file in the classpath will ensure that the project has access to the encrypted database password.

## References

1. https://projectlombok.org/features/SneakyThrows
2. https://juejin.cn/post/6844903575877861390
