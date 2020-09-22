USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[deleteorModifyCartItems]    Script Date: 2020-06-11 4:06:02 PM ******/

--exec changePaymentStatus 10


Create PROCEDURE changePaymentStatus
(
	@billId int
)
AS
BEGIN
	
	SET NOCOUNT ON;

	BEGIN -- Variables

		DECLARE @_billId	int  = @billId
		DECLARE @_endDate   datetime = GETDATE()
		DECLARE @_reservationId int = NULL
		
	END

	BEGIN 
		
		SELECT 1 FROM Bills WHERE billId=@_billId 
		
				UPDATE Bills 
				SET Bills.isPaid = 1
				FROM BILLS
				where billId=@_billId

				if(@@ROWCOUNT > 0)
					BEGIN
					Select @_reservationId= Reservation.reservationId From Bills Inner Join Orders on bills.orderId=Orders.orderId inner join Reservation on Orders.reservationId
					=Reservation.reservationId where bills.billId=@_billId and bills.isPaid=1
					Update Reservation 
					SET endTime=@_endDate
					where reservationId=@_reservationId


					END

		
				SELECT billId,billingAmount
				FROM Bills
				WHERE billId=@_billId
			if(@@ROWCOUNT > 0)
			begin
				--select @_itemTotal= price*@_quantity from MenuItems where itemId = @_menuID
				Select '1' as 'StatusCode', 'Payment Status changed' as 'StatusMessage';
			end
			else
			begin
				Select '0' as 'StatusCode', 'Failed ' as 'StatusMessage';
			end

			
			END

		

END
