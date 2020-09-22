USE [restomation]
GO

--select * from cart;
--exec getReadyForPayment
Create procedure [dbo].[getReadyForPayment]
AS
BEGIN

	SET NOCOUNT ON;

	
	BEGIN
		
		SELECT
		billId,orderId,billDate,billingAmount,isCardPayment,isPaid,isReadyToPay
		FROM
		Bills
		WHERE
		isReadyToPay = 1
		ORDER BY
		billDate
		END
END