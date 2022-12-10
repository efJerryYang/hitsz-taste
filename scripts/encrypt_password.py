import os
import sys
import getpass
import base64
import random
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad

# Prompt the user for their database password
password = getpass.getpass("Enter your database password: ")

# Confirm the password by asking the user to enter it again
password_confirm = getpass.getpass("Confirm your password: ")

# Make sure the two passwords match
if password != password_confirm:
    print("Passwords do not match. Exiting.")
    sys.exit()

# Generate a random 16-byte key for encrypting the password
key = os.urandom(16)
iv = os.urandom(16)
# Use the key to encrypt the password with AES
cipher = AES.new(key, mode=AES.MODE_CBC, iv=iv)
encrypted_password = base64.b64encode(cipher.encrypt(pad(password.encode("utf-8"), 16, 'pkcs7')))

# Print the key and encrypted password to the terminal
print(f"Key: {base64.b64encode(key).decode('utf-8')}")
print(f"Encrypted password: {encrypted_password.decode('utf-8')}")

# Store the encrypted password in an environment variable
os.environ["ENCRYPTED_PASSWORD"] = encrypted_password.decode('utf-8')

# Create a template for the application.properties file
template = """
server.port=8080
server.servlet.context-path=/hitsz-taste

# Database connection settings
spring.datasource.url=jdbc:mysql://localhost:3306/hitsz_taste
spring.datasource.username=root
spring.datasource.password=ENC(#{env.ENCRYPTED_PASSWORD})
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# MySQL JDBC driver settings
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=none
""" + f"spring.datasource.key={base64.b64encode(key).decode('utf-8')}\n" + f"spring.datasource.iv={base64.b64encode(iv).decode('utf-8')}\n"

# Write the template to the application.properties file
with open("application.properties", "w") as f:
    f.write(template)

