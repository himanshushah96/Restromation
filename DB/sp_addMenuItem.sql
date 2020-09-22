/*
exec addMenuItem 2,'Vegan Momos','Veg Momos',7.99,2,29,1
select * from menuItems
*/
use restomation
GO

ALTER procedure addMenuItem
(
	@createdBy int,
	@itemName varchar(30),
	@itemDescription varchar(150),
	@price decimal(4,2),
	@categoryId int,
	@availableQty int,
	@itemStatus bit = 1,
	@itemImage varchar(max)=NULL
)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN
		DECLARE @_createdBy	varchar(150) = @createdBy
		DECLARE @_itemName	varchar(64) = @itemName
		DECLARE @_itemDescription	varchar(50) = @itemDescription
		DECLARE @_price		varchar(50) = @price
		DECLARE @_categoryId	INT = @categoryId
		DECLARE @_availableQty int = @availableQty
		DECLARE @_itemStatus   bit = @itemStatus
		DECLARE @_itemImage varchar(max) = @itemImage
		DECLARE @_isItemDeactive bit = 0
	END

	BEGIN
	IF EXISTS(SELECT 1 FROM MenuItems where itemName=@_itemName AND categoryID=@_categoryId )

		BEGIN
			SELECT '004' AS 'ErrorCode' , 'ITEM ALREADY EXISTS IN THIS CATEGORY' AS 'ErrorMessage'
		END
		ELSE
		BEGIN

			INSERT INTO MenuItems(itemName,itemDescription,price,categoryID,availableQty,itemStatusTitle,createdBy,itemImage,isItemDeactive)
			VALUES (@_itemName,@_itemDescription,@_price,@_categoryId,@_availableQty,@_itemStatus,@_createdBy,@_itemImage,@_isItemDeactive)

			select 
			 itemId AS 'ItemId'
			,itemName AS 'ItemName'
			,itemDescription AS 'Description'
			,price AS 'Price'
			,availableQty AS 'QuantityAvailable'
			,itemStatusTitle AS 'ItemStatus'
			,MenuItems.categoryID As 'CategoryId'
			,MenuCategory.categoryTitle As 'CategoryTitle'
			,MenuItems.createdBy As 'CreatedById'
			,MenuItems.itemImage As 'ItemImage'
			from MenuItems 
			INNER JOIN MenuCategory on MenuItems.categoryID=MenuCategory.categoryID 
			where MenuItems.itemId = @@Identity
		END
	END
END
