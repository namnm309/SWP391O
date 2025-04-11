import sqlite3

def create_database():
    conn = sqlite3.connect('doctors.db')
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS doctors (
            id INTEGER PRIMARY KEY,
            first_name TEXT,
            last_name TEXT,
            joining_date TEXT,
            salary REAL,
            address TEXT
        )
    ''')
    conn.commit()
    conn.close()

# Function to check if a record exists
def record_exists(doctor_id):
    conn = sqlite3.connect('doctors.db')
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM doctors WHERE id = ?', (doctor_id,))
    exists = cursor.fetchone() is not None
    conn.close()
    return exists

# Function to perform CRUD operations
def manipulate_database(operation, data=None):
    conn = sqlite3.connect('doctors.db')
    cursor = conn.cursor()

    if operation == "insert":
        if record_exists(data[0]):  # Check if the record with the same ID exists
            print(f"Error: A doctor with ID {data[0]} already exists.")
        else:
            cursor.execute('''
                INSERT INTO doctors (id, first_name, last_name, joining_date, salary, address)
                VALUES (?, ?, ?, ?, ?, ?)
            ''', data)
            print(f"Doctor with ID {data[0]} inserted successfully.")

    elif operation == "update":
        if record_exists(data[-1]):  # Check if the record to be updated exists
            cursor.execute('''
                UPDATE doctors
                SET first_name = ?, last_name = ?, joining_date = ?, salary = ?, address = ?
                WHERE id = ?
            ''', data)
            print(f"Doctor with ID {data[-1]} updated successfully.")
        else:
            print(f"Error: Doctor with ID {data[-1]} does not exist.")

    elif operation == "delete":
        if record_exists(data):
            cursor.execute('DELETE FROM doctors WHERE id = ?', (data,))
            print(f"Doctor with ID {data} deleted successfully.")
        else:
            print(f"Error: Doctor with ID {data} does not exist.")

    elif operation == "select":
        cursor.execute('SELECT * FROM doctors')
        rows = cursor.fetchall()
        if rows:
            for row in rows:
                print(row)
        else:
            print("No records found.")
    else:
        print("Invalid operation")

    conn.commit()
    conn.close()

# Create the database and table
create_database()

# Examples of CRUD operations 
# manipulate_database("insert", (2, "haohao", "van", "2023-01-01", 500000, "viet nam"))  
# manipulate_database("update", ("hao", "ngoc", "2024-03-15", 130000, "456 Avenue", 2))
manipulate_database("delete", 2)   
manipulate_database("select")
