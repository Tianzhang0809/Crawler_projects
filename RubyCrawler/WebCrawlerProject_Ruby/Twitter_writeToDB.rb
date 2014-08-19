# Twitter_writeToDB.rb
# Author: Olivia Zhang
# Fri Mar 7, 2014

require 'nokogiri'
require 'net/http'
require 'open-uri'
require 'mysql'

@names=Hash.new

# check if the celebrity has verified twitter account
def checkTwitterID(name)
	#url = "https://twitter.com/search?q=Taylor%20Swift&src=typd"
	account="None"
	c_name=name.gsub(" ","%20")
    name = name.downcase
	url = "https://twitter.com/search?q="+c_name+"&src=typd"
	encoded_url = URI.encode(url)
	uri=URI(encoded_url)
	response = Net::HTTP.get(uri)

	peoplepage = Nokogiri::HTML(response)
	accounts = peoplepage.css("span.account-group-inner")
	if !accounts.nil? 
		(0..accounts.length-1).each do |n|
			verify = accounts[n].css("span.icon")
			#1. check if the account is verified
			#2. check if user name is full name is the same as celebrity's name
			if !verify.nil? and verify.text == "Verified account" and accounts[n].css("strong").text.downcase == name
				id = accounts[n].css("small").text
				account="https://twitter.com/"+id[1..id.length]
			end
		end
	end
	return account
end

# crawl celebrity name from wiki page
def crawlNameFromWiki()
	base_url = "http://en.wikipedia.org"
	page = "/wiki/List_of_American_film_actresses"
	remote_full_url = base_url + page
	page = Nokogiri::HTML(open(remote_full_url))
	divs = page.css("div#mw-content-text")
	links = divs.css("div.div-col").css("li")
	people = Hash.new(0)
	head ="Name         ID\n"

	(0..links.length - 1).each do |n|
		data = links[n].text # get the info of each celebrity
		name = links[n].css("a")[0]["title"] # get name
		# <Optional_lines> --> using birthday to optimize crawling performance
		birthD = "" 
		if !data.nil?
			regexpression = data[/[^-]\d{4}$/]
			if !regexpression.nil? and !regexpression.include?"-"
				birthD = data[-4..-1]
			end
		end
		#</Optional_lines> 
		people[name] = birthD # map person name to birthday
	end
	verifiedTwitter(people)
end

# put verified twitter profile page URL into database
def verifiedTwitter(people)
	begin
	account="None"
	# create a connection 'db_server', 'username', 'password', 'db_name'
    con = Mysql.new 'localhost', 'root', '5542511','twitter'
    # if table not exist, create a new table
    con.query("CREATE TABLE IF NOT EXISTS \
    Twitter1(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(120) UNIQUE,Account VARCHAR(200))")
	people.each{|key, value|
	    account=checkTwitterID(key)	    
	    puts key,account # you can see progress from command line ^_^
	    # put records into db
	    pst = con.prepare "INSERT INTO Twitter1(Name,Account) VALUES(?,?) ON DUPLICATE KEY UPDATE id=id "
	    pst.execute key,account
	}
	rescue Mysql::Error => e
    	puts e.errno
    	puts e.error
	ensure
    	con.close if con
	end
end

crawlNameFromWiki()
