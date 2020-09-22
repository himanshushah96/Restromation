USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[getOrders]    Script Date: 08-06-2020 12:33:02 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--exec getTables '0','0'

CREATE PROCEDURE [dbo].[getOrders]
(
	@customer_id int = null,
	@fromDate DateTime = null,
	@toDate DateTime = null,
	@email varchar(100) = null
)
AS
BEGIN
	if(@customer_id = 0)
	begin
		set @customer_id = null;
	end
	Select Orders.orderId, Orders.orderDate, Orders.isDiningIn, OrderStatus.orderStatusTitle, OrderDetails.itemQty, Bills.billingAmount, Bills.isCardPayment, MenuItems.itemName,
		MenuItems.itemDescription, MenuItems.price, MenuCategory.categoryTitle, Users.FirstName, Users.lastName

		from Orders Inner Join OrderDetails on OrderDetails.orderId = Orders.orderId
		inner join OrderStatus on OrderStatus.orderStatusId = Orders.currentOrderStatusId
		inner join Bills on Orders.orderId = Bills.orderId
		inner join  MenuItems on OrderDetails.itemId = MenuItems.itemId
		inner join MenuCategory on MenuCategory.categoryID = MenuItems.categoryID
		inner join Reservation on Reservation.reservationId = Orders.reservationId
		inner join Users on Users.userId = Reservation.reservedBy

		where ((@fromDate is not null and @toDate is not null and Orders.orderDate between @fromDate and @toDate)
		or (@customer_id is not null and Reservation.reservedBy = @customer_id) or (@email is not null and Users.email = @email)) or(@fromDate is null and @toDate is null or @customer_id is null or @email is null)

		order by Orders.orderId, Orders.orderDate
END
