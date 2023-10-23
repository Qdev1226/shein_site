![Main Page](https://i.imgur.com/jYP8ov5.png)

This is the source code to my personal website [sheinhtike.com](http://sheinhtike.com "My Personal Website") where I post short essays, photos, and poetry. I use Adobe Dreamweaver to make each page on the site using the template file "Templates/template.dwt" .

MakeContents.java is a single-class program that scans the writeups directory. Each HTML file inside "writeups/" is pattern matched using regex to find a title and page. Then, a list of links to each HTML page is generated and displayed with the date and title of the page. This table of contents is inserted into contentstemplate.html to make contents.html. The result looks like this:

![Table of Contents](https://i.imgur.com/h8AW8ok.png)
