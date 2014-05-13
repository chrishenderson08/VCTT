            Voodoo Cert Tree Tracker (VCTT)  


INTELLECTUAL PROPERTY NOTICE
----------------------------
The Voodoo Cert Tree Tracker (VCTT) is NOT in any manner (express or implied) 
sponsored by, endorsed by, or associated with ©2013 Sony Online Entertainment (SOE) 
or Planetside 2®.



NOTICE
------
VCTT has been unsupported for some time and is not correctly reporting several certifications.
As always feel free to research and fix these issues, however I will not likely be devoting
much time to this project for a while due to it being largely unused.  If interest were to
surge, this may change.


Description
-----------
The Voodoo Cert Tree Tracker (VCTT) is a simple Windows java program that allows 
Voodoo Shipping Company (VCO) outfit members to easily keep tabs on 
their progress with regards to the outfit specified certification tree.  
This will help ensure that cert points are spent wisely in a way that 
will help the player progress quickly and be an effective contributor 
during outfit operations.



Licensing
---------
Apache License Version 2.0 (See LICENSE.txt and NOTICE.txt)



User Instructions
-----------------
To start the application, simply run the VCTT.jar file (double-click).
The jar file can be ran from any file location.  It will create
a directory in your user AppData folder called "VCTT".  The only file
that will be created in this directory in the "PlayerDat.txt" file.  The
only information that should be in this file is your unique character 
identification number.

When you start the application for the first time, you will be prompted
to select your character from a pop-up list.  If you don't see the list,
minimize other windows (sometimes the list pops up behind open windows...
this will be fixed eventually).  Note that this application is designed to
be used specifically by VCO members.  If you are not a member of VCO, you will
not see your name in the list.

After you select your character, a user interface will display that lists your
completion status for the Voodoo Company Cert Tree (Note: The currently selected user
is displayed in the window title).  You can filter this table
by tier and by class.  To do this, use the drop-down menus above the table.  Below
the table, the application displays the total cost to complete your current selection,
how many more certs you need to earn to complete the selection, and your current
certification point balance.

IMPORTANT:  This application communicates with the Census API provided by
©2013 Sony Online Entertainment.  The outbound communications are simple
web queries that take the form of URLs.  The only information transmitted is
your in-game character's unique id number.  No information related to
you or your machine is communicated.  Ensure your firewall is configured to allow
this application to communicate.

IMPORTANT:  As of now, there is no built-in way to change the character you selected
if you wish to or if you choose the wrong character on accident.  There is an easy
work-around however.  Simply go to the your Windows account's AppData\Roaming directory.
Note that the AppData directory is hidden by default, so you will have to set your
browser to view hidden files.  The location should be something like
C:\Users\<windows account name>\AppData\Roaming.  Once you are in this folder, simply
delete the "VCTT" directory.  The next time you open the application, you will be prompted to
select a new character.



Developer Instructions
----------------------
Source code can be obtained on GitHub at https://github.com/chrishenderson08/VCTT.

I used Eclipse Standard when I developed this.  Whatever IDE you use, ensure that you
add all of the included libraries to your build path or the project won't compile.  Since the
project is small as of now, I am just going to include the libraries in the repository itself
to make things easy for you.

If you would like your changes pulled to the main repo on GitHub, try and maintain
the coding style I have used thus far.  I know that may not be easy since I haven't
included a standards file.  Basically, keep brackets on the same line, pad expressions/parameters
with a space between them and parenthesis, use "m_" before all instance variables, use all-caps
for constants, and use the typical java conventions.  If you come remotely close, I won't be too
up-tight about it.



Current Version
---------------
Version 1.0



Release Notes
-------------
Version 1.0 (Initial Release)
		-Added ability to select outfit/character data from API.  User chooses their
			character and then the application saves their info locally.  This
			way they only need to do this once.
		-Added cert tree config file in JSON format.  File is written up to specification
			of outfit.  This file is parsed at run-time, giving the application the
			structure of the certification tree.
		-Added baseline structure back-end for querying the API and parsing the results.
		-Added GUI that displays a table laying out the completion status of the cert tree 
			for the user.  This table can be filtered by tier and by class.
			
			
			
Known Bugs
----------
-2 certifications (advanced foregrip for the SAW and the Anti-Vehicle turret) are missing from
	the current tree structure.  This is due to the API failing to present the data or presenting
	the data in an odd manner that has yet to be dissected.  Until this is fixed, remain cognizant
	of the absense.
	
	
	
Planned Features (to-do list)
-----------------------------
-Add in-application functionality to change character.
-Add functionality to filter the completion table (e.g. - Alphabetical order, by cert cost,
	by completion status, etc).
-Add capability to search character name out of the outfit member list.
	
	
Contact Info / Bug Reporting
----------------------------
VCTT is brought to you by the Voodoo Shipping Company Development Team.
The project lead is Chris Henderson.  For questions, concerns, suggestions,
or bug reports, contact Chris at chrishenderson08@gmail.com.