 
# Exercise 1-4. Number Served
class Restaurant:
    def __init__(self, restaurant_name, cuisine_type):
        self.restaurant_name = restaurant_name
        self.cuisine_type = cuisine_type
        self.number_served = 0

    def describe_restaurant(self):
        print(f"Restaurant Name: {self.restaurant_name}")
        print(f"Cuisine Type: {self.cuisine_type}")

    def open_restaurant(self):
        print(f"{self.restaurant_name} is now open!")

    def set_number_served(self, number):
        self.number_served = number
        print(f"Number Served: {self.number_served}")

    def increment_number_served(self, number):
        self.number_served += number
        print(f"Number Served: {self.number_served}")

# Create an instance of Restaurant
restaurant = Restaurant("The Food Place", "Italian")
   
restaurant.set_number_served(20)
 
restaurant.increment_number_served(5)
 
 