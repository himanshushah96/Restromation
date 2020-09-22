

CREATE TABLE UserType
(
userTypeID int PRIMARY KEY,
userType varchar(20) NOT NULL UNIQUE
);


CREATE TABLE Users
(
	userId int IDENTITY(1,1) Primary key ,
	FirstName varchar(50) Not null,
	lastName varchar(50) ,
	gender varchar(6),
	email varchar(150) NOT NULL UNIQUE,
	password varchar(64) NOT NULL,
	phoneNo varchar(10) NOT NULL,
	profileImage Varchar(1000),
	createdDate DATETIME NOT NULL DEFAULT GETDATE(),
	updatedDate DATETIME,
	deletedDate DATETIME,
	userTypeId int,
	FOREIGN KEY (userTypeId) references UserType(userTypeID)
);

CREATE TABLE tokens
(
	tokenId int IDENTITY(1,1) PRIMARY KEY,
	token UNIQUEIDENTIFIER NOT NULL UNIQUE,
	createdDate DATETIME NOT NULL DEFAULT GETDATE(),
	expireDate DATETIME NOT NULL,
	userId int 
	FOREIGN KEY (userId) references Users(userId)
);



CREATE TABLE MenuCategory
(
	categoryID int IDENTITY(1,1) PRIMARY KEY,
	categoryTitle varchar(30) NOT NULL UNIQUE 
);


CREATE TABLE MenuItems
(
	itemId int IDENTITY(1,1) PRIMARY KEY,
	itemName varchar(30) NOT NULL,
	itemDescription varchar(150),
	price decimal(4,2) not null,
	categoryID int ,
	availableQty int not null,
	itemStatusTitle bit NOT NULL,
	createdBy int NOT NULL,
	updatedBy int,
	deletedBy int,

	FOREIGN KEY (categoryID) references MenuCategory(categoryID),
	FOREIGN KEY (createdBy) references Users(userId),
	FOREIGN KEY (updatedBy) references Users(userId),
	FOREIGN KEY (deletedBy) references Users(userId),
);




CREATE TABLE Tables
(
	tableID int IDENTITY(1,1) PRIMARY KEY,
	capacity int NOT NULL,
	isTableAvailable bit NOT NULL
);


CREATE TABLE Reservation
(
	reservationId int IDENTITY(1,1) PRIMARY KEY,
	reservationDate DATETIME NOT NULL ,
	reservedBy int,
	numberOfPeople int,
	startTime TIME(7) NOT NULL,
	endTime TIME(7),
	FOREIGN KEY (reservedBy) references Users(userId)
);


CREATE TABLE ReservationDetails
(
	tableID int,
	reservationID int,
	PRIMARY KEY (tableID,reservationID),
	FOREIGN KEY (tableID) references Tables(tableID),
	FOREIGN KEY (reservationID) references Reservation(reservationId)
);

CREATE TABLE Cart
(
	cartId int IDENTITY(1,1) PRIMARY KEY,
	menuItemId int,
	addedBy int,
	quantity int not null,
	isOrdered bit not null,
	FOREIGN KEY(menuItemId) references MenuItems(itemId),
	FOREIGN KEY(addedBy) references Users(userId)
);

CREATE TABLE OrderStatus
(
	orderStatusId int IDENTITY(1,1) PRIMARY KEY,
	orderStatusTitle varchar(20) not null
);

CREATE TABLE OrderFlow
(
	orderFlowId int IDENTITY(1,1) PRIMARY KEY,
	currentStatus int not null,
	nextStatus int,
	FOREIGN KEY (currentStatus) references OrderStatus(orderStatusId),
	FOREIGN KEY (nextStatus) references OrderStatus(orderStatusId)
);




CREATE TABLE Orders
(
	orderId int IDENTITY(1,1) PRIMARY KEY,
	orderDate DATETIME NOT NULL DEFAULT GETDATE(),
	reservationId int NOT NULL,
	idDiningIn bit NOT NULL,
	FOREIGN KEY (reservationId) references Reservation(reservationId)
);

CREATE TABLE OrderHistory
(
	orderId int,
	statusId int,
	createdTime DATETIME NOT NULL DEFAULT GETDATE(),
	PRIMARY KEY(orderId,statusId),
	FOREIGN KEY (orderId) references Orders(orderId),
	FOREIGN KEY (statusId) references OrderStatus(orderStatusId)
);

CREATE TABLE OrderDetails
(
	orderId int,
	itemId int,
	itemQty int not null,
	PRIMARY KEY(orderId,itemId),
	FOREIGN KEY(orderId) references Orders(orderId),
	FOREIGN KEY(itemId) references MenuItems(itemId)
);



CREATE TABLE Bills
(
	billId int identity(1,1) PRIMARY KEY,
	orderId int,
	billDate DATETIME,
	billingAmount decimal(6,2) not null,
	isCardPayment bit not null,
	FOREIGN KEY (orderId) references Orders(orderId)
);


ALTER TABLE users Alter column phoneNo varchar(10)