# Twitter_writeToText.rb
# Author: Olivia Zhang
# Fri Mar 7, 2014

require "open-uri"
require 'nokogiri' 

base_url = "http://en.wikipedia.org"
page = "/wiki/List_of_American_film_actresses"
remote_full_url = base_url + page

page = Nokogiri::HTML(open(remote_full_url))

# check if the celebrity has verified twitter account
def checkTwitterID(name)
	id = ""
	#url = "https://twitter.com/search?q=Taylor%20Swift&src=typd"
	c_name=name.gsub(" ","%20")
	name = name.downcase
	url = "https://twitter.com/search?q="+c_name+"&src=typd"
	encoded_url = URI.encode(url)
	response = open(encoded_url).read
	page = Nokogiri::HTML(response)
	accounts = page.css("span.account-group-inner")
	if !accounts.nil? 
		(0..accounts.length-1).each do |n|
			#1. check if the account is verified
			#2. check if user name is full name is the same as celebrity's name
			if accounts[n].css("span.icon").text == "Verified account" and accounts[n].css("strong").text.downcase == name
				id = accounts[n].css("small").text
				id = id[1..id.length-1]
			end
		end
	end 
	return id
end

# crawl celebrity name from wiki page
def crawlNameFromWiki(page)
	divs = page.css("div#mw-content-text")
	links = divs.css("div.div-col").css("li")
	people = Hash.new(0)
	out_file = File.new("Twitter_writeToText.txt", "w") # create a file to write in

	(0..links.length - 1).each do |n|
		data = links[n].text # get the info of each celebrity
		name = links[n].css("a")[0]["title"] # get name
		# <Optional_lines> --> using birthday to optimize crawling performance
		birthD = "" # initialize birthday
		if !data.nil?
			regexpression = data[/[^-]\d{4}$/]
			if !regexpression.nil? and !regexpression.include?"-" 
				birthD = data[-4..-1]
			end
		end
		#</Optional_lines>  
		people[name] = birthD # map person name to birthday
	end
	verifiedTwitter(people, out_file)
	out_file.close
end

# put verified twitter profile page URL into database
def verifiedTwitter(people, out_file)
	twitter = Hash.new(0)
	people.each{|key, value|
	    res = key
	    if !value.nil? and value.length > 0  # remove dead celebrity
	        twitter[key] = checkTwitterID(key) # get userID
	        if !twitter[key].nil? and twitter[key].length > 0
	        	res +="   |https://twitter.com/" + twitter[key]
	        end 
	    end
	    out_file.puts(res)
	}
end

crawlNameFromWiki(page)
