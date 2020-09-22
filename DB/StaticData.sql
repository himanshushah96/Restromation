 use restomation;

INSERT INTO UserType (userTypeID, userType) VALUES (1,'customer'),(2,'manager'),(3,'chef'),(4,'cashier'),(5,'admin');

INSERT INTO MenuCategory(categoryTitle) VALUES ('Appetizer'),('Vegetarian'),('Non Vegetarian'),('Dessert'),('Beverages');

INSERT INTO OrderStatus(orderStatusTitle) VALUES ('Ordered'),('Inprogress'),('Completed'),('Cancelled');


INSERT INTO OrderFlow(currentStatus,nextStatus) VALUES (1,2),(1,4),(2,3),(2,4);

INSERT INTO Tables( capacity, isTableAvailable) VALUES (2,1),(2,1), (4,1), (6,1);

INSERT Into Users(FirstName,lastName,gender,email,password,phoneNo,profileImage,createdDate,updatedDate,deletedDate,userTypeId) VALUES 
('admin','','','admin@gmail.com','123456','1234567890','','','','',5);


insert into menuitems (itemName,itemDescription,price,categoryID,availableQty,itemStatusTitle,createdBy) 
values
('Tomato Soup','Soup with Tomato',4.99,1,20,1,1);
