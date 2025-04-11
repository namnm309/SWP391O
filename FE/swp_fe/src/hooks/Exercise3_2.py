import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn import metrics

if __name__ == '__main__':
    file_path = './Ecommerce Customers.csv'
    ecommerce_data = pd.read_csv(file_path)

    print("First 5 rows of the dataset:")
    print(ecommerce_data.head())

    print("\nSummary Statistics:")
    print(ecommerce_data.describe())

    numeric_columns = ecommerce_data.select_dtypes(include=['int64', 'float64']).columns
    correlation_data = ecommerce_data[numeric_columns]

    plt.figure(figsize=(10, 6))
    sns.heatmap(correlation_data.corr(), annot=True, cmap='coolwarm')
    plt.title('Correlation Heatmap')
    plt.show()

    sns.jointplot(x='Time on Website', y='Yearly Amount Spent', data=ecommerce_data)
    plt.show()

    sns.jointplot(x='Time on App', y='Yearly Amount Spent', data=ecommerce_data)
    plt.show()

    sns.jointplot(x='Time on App', y='Length of Membership', kind='hex', data=ecommerce_data)
    plt.show()

    sns.pairplot(ecommerce_data)
    plt.show()

    sns.lmplot(x='Length of Membership', y='Yearly Amount Spent', data=ecommerce_data)
    plt.show()

    X = ecommerce_data[['Avg. Session Length', 'Time on App', 'Time on Website', 'Length of Membership']]
    y = ecommerce_data['Yearly Amount Spent']

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20, random_state=105)

    print("\nShapes of Training and Testing Sets:")
    print(f"X_train: {X_train.shape}, X_test: {X_test.shape}")
    print(f"y_train: {y_train.shape}, y_test: {y_test.shape}")

    lr_model = LinearRegression()

    lr_model.fit(X_train, y_train)

    lr_coefficients = pd.DataFrame(lr_model.coef_, X.columns, columns=['Coefficient'])
    print("\nModel Coefficients:")
    print(lr_coefficients)

    predictions = lr_model.predict(X_test)

    plt.scatter(y_test, predictions)
    plt.xlabel('Actual Values')
    plt.ylabel('Predicted Values')
    plt.title('Actual vs Predicted')
    plt.show()

    mae = metrics.mean_absolute_error(y_test, predictions)
    mse = metrics.mean_squared_error(y_test, predictions)
    rmse = np.sqrt(mse)

    print(f'\nEvaluation Metrics:')
    print(f'MAE: {mae}')
    print(f'MSE: {mse}')
    print(f'RMSE: {rmse}')

    residuals = y_test - predictions
    sns.histplot(residuals, bins=50, kde=True)
    plt.title('Residuals Distribution')
    plt.show()

    coefficients = pd.DataFrame(lr_model.coef_, X.columns, columns=['Coefficient'])
    print("\nFinal Model Coefficients:")
    print(coefficients)


