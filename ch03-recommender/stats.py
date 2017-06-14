#!/usr/bin/ipython
import pandas as pd
import matplotlib.pyplot as plt


artists_per_user = pd.read_csv('all_artists_per_user.csv',header=None,names=['num_artists','num_users'])
%matplotlib
artists_per_user.sort_values('num_artists',ascending=False,inplace=True)
# cumulative meaningless , since num of unique artists per user!!
artists_per_user['cum_artists']=artists_per_user.num_artists.cumsum()
artists_per_user['cum_users']=artists_per_user.num_users.cumsum()
artists_per_user.plot(x='cum_users',y='cum_artists')


fig, ax = plt.subplots()
artists_per_user.hist('num_artists_log10',ax = ax,bins=100)
ax.set_xticklabels(np.power(10,ax.get_xticks()))
