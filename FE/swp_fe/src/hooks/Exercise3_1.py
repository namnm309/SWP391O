import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Load the mtcars dataset
mtcars_path = './mtcars.csv'
mtcars = pd.read_csv(mtcars_path)

# Create a 1 by 2 plot for the histogram and density plot of 'mpg'
fig, axes = plt.subplots(1, 2, figsize=(12, 5))

# Histogram
axes[0].hist(mtcars['mpg'], bins=10, edgecolor='black', alpha=0.7)
axes[0].set_title('Histogram of MPG')
axes[0].set_xlabel('MPG')
axes[0].set_ylabel('Frequency')

# Density plot
sns.kdeplot(mtcars['mpg'], fill=True, ax=axes[1], alpha=0.7)
axes[1].set_title('Density Plot of MPG')
axes[1].set_xlabel('MPG')
axes[1].set_ylabel('Density')

plt.tight_layout()
plt.show()
