# Budget Planner

*What will the application do?*

This budget planner will **keep track of your finances**, from spending to savings!

It can be used to:

- **record pay cheques and income** to proudly display your hard-earned money.
- **monitor your purchases** and sort them into categories of spending, so you know which toilet your money is being flushed down.
- And most importantly... **set savings goals**!

This application is meant to help you keep track of the money in your account.
By inputting your pay cheques and purchases, the budget planner will provide
a log to display your records, making it easy to scroll through 
your financial history. It calculates and displays the total amount of money in your account,
and also separately displays your income and the total spending of purchases in several categories, 
including bills, necessities, shopping, dining out, and recreation.
These amounts will be graphically displayed so you can visually understand where your money is going.
The budget planner also includes a feature to set a goal,
allowing you to responsibly save for a future purchase.
Set the price of the purchase and a minimum safety net you wish to keep in your account,
and the budget planner displays how close you are to that dream purchase.
Being financially responsible has never been easier!

*Who will use it? Why is this project of interest to me?*

This budget planner is aimed to help those that struggle with spending or saving.
One key concept in being responsible with money is being **organized**, 
and this budget planner helps exactly in that way. 
As someone who is not particularly financially smart,
I wanted to be able to create an application that 
displayed the health of my bank account in a simple and clear way,
tracked my spending habits, taught me how to save money with patience,
and helped me achieve my financial goals.
I hope that this application will help people save and
understand their financial situation a little bit better.



### User Stories

> #### Phase 1
> 
> - As a user, I want to be able to add a record
> (income or purchase) to my records log.
> - As a user, I want to be able to view all the records
>in my records log.
> - As a user, I want to be able to edit or remove a record
> (income or purchase) from my records log in case
> I make a mistake.
> - As a user, I want to be able to calculate
> the total amount of money currently in my account.

> #### Phase 2
> 
> - As a user, I want an option in the application menu that,
> when chosen, allows me to save my account 
> (which holds my records and records log) to file.
> - As a user, when the program starts, 
> I want the program to automatically load my account
> (which holds my records and records log) from file if it exists;
>  otherwise, if it doesn't exist, I want the program to start a new account.
> - As a user, I want to be able to calculate
> the total amount of money spent in a certain category.

> #### Phase 3
> 
> - As a user, I want to be able to access all features
> with a simple graphical user interface.

## Instructions for Grader

> - You can generate the first required event by ... **adding a record to the log**.
> This is done by selecting the "Add, Edit, or Remove Records" button.
> Then in the record menu, select the "Add Record" button.
> In the add record menu, fill out the text field with the title of the record
> (note that the title cannot contain a comma [ , ] character;
> if a comma is added, the application will not add the record until it is fixed).
> Then, choose the category of the record in the drop-down box
> (note that to add money, select the "income" option).
> Lastly, fill out the text field with the associated value of the record
> (note that the value has to be >= 0.01;
> if not a numerical value >= 0.01, the application will not add the record until it is fixed).
> Once done, click the "Add Record" button at the bottom. 
> You will be redirected to the main menu, and will hear a "ding" noise confirming the record has been added to the log.
> At the main menu, you will see the bar graph update with the newly added record taken into account.
> To view the log, click the "Display Log and Summary" button.
> This will open a new window that will display the log and a summary of your account.
> Note that the record is now added to the log.
> 
> - You can generate the second required event by ... **removing a record from the log**.
> This is done by selecting the "Add, Edit, or Remove Records" button.
> Then in the record menu, select the "Remove Record" button.
> In the remove record menu, fill out the text field with the index of the record you wish to remove.
> The index of the record can be found in the log window;
> it is the integer displayed within the square brackets to the left-hand side of the record in the log.
> Once done, confirm the "Remove Record" button at the bottom
> (note that the integer inputted as the index must be assigned to a record;
> if not, the application will not remove the record until it is fixed).
> You will be redirected to the main menu.
> At the main menu, you will see the bar graph update with the removed record taken into account.
> To view the log, click the "Display Log and Summary" button.
> This will open a new window that will display the log and a summary of your account.
> Note that the record is now removed from the log.
> 
> - You can trigger my audio component by ... **adding a record**.
> A "ding" will sound once a record is confirmed to be added.
> 
> - You can locate my visual component by ... **adding a record**.
> Once a record is added, the bar graph on the main page will display the account's summary.
> This bar graph will change every time a record is added, edited, or removed to accurately display the account's information.
> 
> - You can save the state of my application by ... **selecting the "Save Log" button** in the main menu.
> 
> - You can reload the state of my application by ... opening the project after it has been saved.
> The application **automatically loads the saved data**.
> If there is no data, it will create a new account.


## Phase 4: Task 2

> I have used the **Map interface within the <s>Account class</s> CategoryInfo class**
> (as of Phase 4: Task 3).
> A Hash Map was used to store category total (i.e. values = Double)
> according to the category title (i.e. keys = String).


## Phase 4: Task 3

> ###### Problem 1:
> There was **poor cohesion within the Account class**.
> The Account class managed both records and category methods,
> when really it should only focus managing the overall account
> (i.e. records log, balance, overall category information).
> ###### Solution:
> I split the Account class into two classes:
>
> - Account class: 
> This class holds the records log, balance, and category information.
> It is responsible for methods regarding the overall account
> (i.e. records log, balance, retrieving category sum totals).
> - CategoryInfo class:
> This class holds the list of category names and category sum totals of the account.
> It is primarily responsible for implementation of methods regarding the category information
> (i.e. category names, storing and updating category sum totals).

> ###### Problem 2:
> There was **bad coupling between the BudgetPlannerApp and CategoryInfo class**.
> If I wanted to change the name of the categories,
> I would have to change and edit these two classes separately,
> once in the categorySelector method of the BudgetPlannerApp class and
> again in the createCategoryNames method of the CategoryInfo class.
> ###### Solution:
> I created an iterator in the CategoryInfo class.
> I then changed the categorySelector method of the BudgetPlannerApp
> to create the categories using the CategoryInfo class.
> This provided a single point of control for category names.
> This also helped improve cohesion as category-related methods belong in the CategoryInfo class.
