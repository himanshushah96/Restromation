USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[orderCartItem]    Script Date: 15-06-2020 02:26:54 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO






ALTER procedure [dbo].[orderCartItem]
(
	@orderBy int, 
	@isDiningIn bit,
	@isCardPayment bit
)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN
		DECLARE @_cartId int = null
		DECLARE @_menuItemId int = null
		DECLARE @_quantity int = null
		DECLARE @_orderId int = null
		DECLARE @_reservationId int = null
		DECLARE @_billAmount decimal(6, 2) =null
		DECLARE @_resId table(reservationId int);
		DECLARE @_availableQTY int = null
		DECLARE @_itemName varchar(30) = null
		DECLARE @_processedItems table(cartId int);
	END

	BEGIN
		Select @_reservationId = Reservation.reservationId from Reservation where (endTime is null or endTime = '') and reservedBy = @orderBy;

		if(@_reservationId is null and @isDiningIn = 0)
		begin
			Insert into Reservation(reservationDate, reservedBy, startTime) OUTPUT inserted.reservationId into @_resId values (dateadd(HOUR, -4, getdate()), @orderBy, dateadd(HOUR, -4, getdate()));
				Select @_reservationId = reservationId from @_resId;
		end

		if(@_reservationId is not null)
		begin
			Insert into Orders(orderDate, reservationId, isDiningIn, currentOrderStatusId) values (CURRENT_TIMESTAMP, @_reservationId, @isDiningIn, 1);
			Select @_orderId = Orders.orderId from Orders where Orders.reservationId = @_reservationId;
			Insert into OrderHistory(orderId, statusId, createdTime) values (@_orderId, 1, CURRENT_TIMESTAMP);
			if(@@ROWCOUNT > 0)
			begin
				Select * into #tempCart from Cart where Cart.isOrdered = 'false' and Cart.addedBy = @orderBy
				WHILE EXISTS(Select * from #tempCart)
				BEGIN
					SELECT TOP 1 @_cartId =  cartId, @_menuItemId = menuItemId, @_quantity = quantity FROM  #tempCart;
					DECLARE @_quantityTemp int = 0;
					Select @_quantityTemp = MenuItems.availableQty from MenuItems where MenuItems.itemId = @_menuItemId;
					if(@_quantityTemp >= @_quantity)
					begin
						Insert into OrderDetails(orderId, itemId, itemQty) values(@_orderId, @_menuItemId, @_quantity);
					
						Update Cart Set isOrdered = 'true' where cartId = @_cartId;
						insert into @_processedItems(cartId) values(@_cartId);
						delete #tempCart where cartId = @_cartId;
					end
					else
						begin
							Declare @_menuName varchar(100) = null;
							Select @_menuName = MenuItems.itemName, @_availableQTY = availableQty from MenuItems where itemId = @_menuItemId;
							while exists(Select * from @_processedItems)
							begin
								Select Top 1 @_cartId = cartId from @_processedItems;
								update Cart set isOrdered = 'false' where cartId = @_cartId;
								delete from @_processedItems where cartId = @_cartId;
							end
							delete from OrderDetails where orderId = @_orderId;
							delete from OrderHistory where orderId = @_orderId;
							delete from Orders where orderId = @_orderId;
							if(@_availableQTY = 0)
							begin
								Select '404' as 'ErrorCode', CONCAT(@_menuName, ' is out of stock!!!') as 'ErrorMessage';
							end
							else
							begin
								Select '404' as 'ErrorCode', CONCAT('Only ', @_availableQTY, ' quantity of ', @_menuName, ' is available, Please remove it from cart or reduce the quantity!!!') as 'ErrorMessage';
							end
							break;
						end
				END

				if exists(Select * from @_processedItems)
				begin
					Select @_billAmount = SUM(MenuItems.price * OrderDetails.itemQty) from OrderDetails Inner Join MenuItems on MenuItems.itemId = OrderDetails.itemId where OrderDetails.orderId = @_orderId
					Insert into Bills(orderId, billDate, billingAmount, isCardPayment) values(@_orderId, CURRENT_TIMESTAMP, @_billAmount, @isCardPayment);
					Select Bills.BillId as billId, Bills.BillingAmount as billAmount, Bills.BillDate as billDate from Bills where orderId = @_orderId;
					while exists(Select * from @_processedItems)
					begin
						Select @_cartId = cartId from @_processedItems;
						Select @_menuItemId = itemId, @_quantity = Cart.quantity from MenuItems inner join Cart on Cart.menuItemId = MenuItems.itemId where Cart.cartId = @_cartId;
						Update MenuItems set availableQty = availableQty - @_quantity where itemId = @_menuItemId;
						Delete from @_processedItems where cartId = @_cartId;
					end
				end

			end
			else
			begin
				Delete from Orders where orderId = @_orderId;
				Select '404' as 'ErrorCode', 'Failed to add Order' as 'ErrorMessage'
			end
		end
		else
		begin
			Select '404' as 'ErrorCode', 'Failed to Order Cart Items' as 'ErrorMessage'
		end
	END
END
