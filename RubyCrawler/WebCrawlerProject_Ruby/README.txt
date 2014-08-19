# Author: Olivia Zhang
# Fri Mar 7, 2014

This program looks for twitter accounts for listed celebrities on wikipedia.
The program consists of two parts:
Part I: Twitter_writeToText.rb
Part II: Twitter_writeToDB.rb

Part I: Twitter_writeToText.rb
Twitter_writeToText will create a new document called “Twitter_writeToText.txt” which writes actress and Twitter URL in the format of “ACTRESS|TWITTER URL” as example shows.

Part II: Twitter_writeToDB.rb
Twitter_writeToDB will insert records into database (Mysql) named “twitter”. To insert data into your database, please change line 68, the value of con into the format of 'db_server', 'username', 'password', 'db_name’. Here I use localhost as server, root database and my password is 5542511 (you might have different setup). 

Screenshot:on the left is real time crawling process after executing ruby Twitter_writeToDB.rb. On the right is 
the last 60 records in database.

Next Step:
1. For further improve the performance, I also collected the year of birth so that twitter search method can ignore dead celebrities.
2. Use Ruby on Rails to create the UI of website based on user requirements. For example, create a search box and submit button in the index page so that user can type in name of celebrity and get her twitter address. 