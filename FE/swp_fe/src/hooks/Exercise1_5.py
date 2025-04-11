
# Exercise 1-5. Login Attempts
class User:
    def __init__(self, first_name, last_name, age, email):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
        self.email = email
        self.login_attempts = 0

    def describe_user(self):
        print(f"User: {self.first_name} {self.last_name}")
        print(f"Age: {self.age}")
        print(f"Email: {self.email}")

    def greet_user(self):
        print(f"Hello, {self.first_name} {self.last_name}!")

    def increment_login_attempts(self):
        self.login_attempts += 1
        print(f"login_attempts:{self.login_attempts}")


    def reset_login_attempts(self):
        self.login_attempts = 0
        print(f"login_attempts:{self.login_attempts}")


# Create an instance of User
user = User("John", "Doe", 30, "john.doe@example.com")
user.increment_login_attempts()
user.increment_login_attempts()
user.increment_login_attempts()
# print(user.login_attempts)
user.reset_login_attempts()
# print(user.login_attempts)