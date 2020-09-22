
-- exec getMenuItems

use restomation
Go

ALTER PROCEDURE getMenuItems
(
	--@userId INT = NULL
	@categoryId INT			= NULL
	
)
AS
BEGIN

	SET NOCOUNT ON;

	BEGIN -- Decalre Variables
		
		--DECLARE @_userId		INT				= @userId
		DECLARE @_categoryId    INT				= @categoryId
		
		IF @_categoryId = 0
		BEGIN
			SET @_categoryId = NULL
		END	
	END
	
	BEGIN
		
		SELECT
		*
		FROM
		(
		SELECT 
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
			,u1.FirstName + ' ' + u1.lastName  As 'CreatedBy'
			,u2.FirstName + ' ' + u2.lastName As 'UpdatedBy'
			,u3.FirstName + ' ' + u3.lastName As 'DeletedBy'
			
		FROM 
		MenuItems 
		
		JOIN Users u1 on MenuItems.createdBy = u1.userId
		LEFT JOIN Users u2 on MenuItems.updatedBy = u2.userId
		LEFT JOIN Users u3 on MenuItems.deletedBy = u3.userId
		JOIN MenuCategory on MenuItems.categoryID=MenuCategory.categoryID 

		WHERE 
		-- (@_userId IS NULL OR (u1.userId=@_userId)) AND
		 (@_categoryId IS NULL OR (MenuItems.categoryID=@_categoryId)) AND
		 (MenuItems.isItemDeactive=0)
		) AS t
		
		END
END