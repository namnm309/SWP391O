# Exercise 1-3. Users
class User:
    def __init__(self, first_name, last_name, email):
        self.first_name = first_name
        self.last_name = last_name 
        self.email = email

    def describe_user(self):
        print(f"User: {self.first_name} {self.last_name}") 
        print(f"Email: {self.email}")

    def greet_user(self):
        print(f"Hello, {self.first_name} {self.last_name}!")

# Create instances of User
user1 = User("John", "Doe",  "john.doe@example.com")
user2 = User("Jane", "Smith", "jane.smith@example.com")

user1.describe_user()
user1.greet_user()

user2.describe_user()
user2.greet_user()
 