import pandas as pd
print("Your code and datasets should be in the same directory")
fileinput1 = str(input("Enter the name of CSV file for Bikes and Rain: \n"))
MR_rain = pd.read_csv(fileinput1, header=None)
MR_rain.columns = ['StandName', 'Rain','UtilizedBikes Count']
# To calculate Pearson's Correlation coefficient for Utilized bikes and Rain.
corr_rain = MR_rain.corr(method = 'pearson')
print(corr_rain)
fileinput2 = str(input("Enter the name of CSV file for Bikes and Temperature: \n"))
MR_temp = pd.read_csv(fileinput2, header=None)
MR_temp.columns = ['StandName', 'Temperature','UtilizedBikes Count']
# To calculate Pearson's Correlation coefficient for Utilized bikes and Temperature.
corr_temp = MR_temp.corr(method = 'pearson')
print(corr_temp)

import matplotlib.pyplot as plt
plt.scatter(MR_temp['Temperature'], MR_temp['UtilizedBikes Count'])
plt.xlabel("Temperature")
plt.ylabel("Count of Utilized bikes")
plt.title("Scatter plot")
plt.show()
