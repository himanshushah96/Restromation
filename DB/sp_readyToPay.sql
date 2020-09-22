USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[deleteorModifyCartItems]    Script Date: 2020-06-11 4:06:02 PM ******/

 -- select * from cart
--exec readyToPay 23
Alter PROCEDURE [dbo].[readyToPay]
(
	@orderId int
)
AS
BEGIN
	
	SET NOCOUNT ON;

	BEGIN -- Variables

		DECLARE @_orderId	int  = @orderId	
		
	END

	BEGIN 
		
		--SELECT 1 FROM Bills WHERE orderId=@_orderId
		
				UPDATE Bills 
				SET Bills.isReadyToPay = 1
				where orderId=@_orderId

		
				SELECT billId FROM Bills WHERE bills.orderId = @_orderId
			if(@@ROWCOUNT > 0)
			begin
				--select @_itemTotal= price*@_quantity from MenuItems where itemId = @_menuID
				Select '1' as 'StatusCode', 'Sit tight, our employee will reach you for the payment' as 'StatusMessage';
			end
			else
			begin
				Select '0' as 'StatusCode', 'Failed ' as 'StatusMessage';
			end

			
			END

		

END
