# Amazon Wishlist crawler
# author Olivia Zhang
# Mar 27, 2014
#
# This program search and print out all wishlist items on amazon given his email address using Ruby

require 'net/http'
require 'net/https'
require 'nokogiri'
require 'open-uri'

def crawWishList(url)
	page = Nokogiri::HTML(open(url))
	fixdiv = page.css("div.a-section").css("div.a-fixed-right-grid")
	nxt = page.css("a").select{|link|link.text.include?"Next"} # check next page
	if !fixdiv.nil?
		(0..fixdiv.length - 1).each do |n|
			title = fixdiv[n].css("div.a-row").css("h5").text.strip
			if !title.nil?
				puts title
			end
		end
	end
	# if there is no next page
	if nxt[0] == nil or nxt[0]['href'] == nil
		return nil
	end
	# if there has a next page
	path = nxt[0]['href']
	return path
end

def mainFunction(email)
	uri = URI('http://www.amazon.com/gp/registry/search')
	res = Net::HTTP.post_form(uri,'field-name'=>email,'submit.search'=>'Submit')
	url = res['location']
	path = crawWishList(url)
	while !path.nil?
		url = "http://www.amazon.com/"+path
		path = crawWishList(url)
	end
end

email = 'tianz@andrew.cmu.edu'
mainFunction(email)
