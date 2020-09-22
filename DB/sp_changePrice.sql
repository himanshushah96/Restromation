
/*
exec changePrice 3,5.99,1
select * from MenuItems
*/


use restomation
GO

ALTER procedure changePrice
(
	@itemId INT ,
	@price DECIMAL(4,2),
	@updatedBy INT
)
AS 
BEGIN
	SET NOCOUNT ON;
		BEGIN
			DECLARE @_price DECIMAL(4,2) =  @price
			DECLARE @_updatedBy INT =@updatedBy
			DECLARE @_itemId INT = @itemId
		END

	BEGIN

		IF EXISTS(SELECT 1 from MenuItems where	MenuItems.itemId=@_itemId)
			BEGIN
		
				UPDATE MenuItems SET price = @_price , updatedBy = @_updatedBy where  itemId= @_itemId

				SELECT itemId AS 'menuItemId',
				itemName AS 'menuItemName',
				@_price AS 'price' from MenuItems where itemId=@_itemId;
			END

		ELSE 
			BEGIN
				SELECT '004' AS 'ErrorCode' , 'Could not change price, try again later' AS 'ErrorMessage'
			END
	END
END