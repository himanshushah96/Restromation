USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[getCartItems]    Script Date: 06-06-2020 21:29:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--select * from cart;
--exec getCartItems '22' 
ALTER procedure [dbo].[getCartItems]
(
	@userId INT	
)
AS
BEGIN

	SET NOCOUNT ON;

	BEGIN -- Declare Variables
		DECLARE @_userId    INT = @userId
		DECLARE @_total numeric(10,2) = null
	
	END
	
	BEGIN
		SELECT @_total=sum(cart.quantity*menuitems.price) 
		FROM Cart JOIN MenuItems 
		On 
		Cart.menuItemId=menuItems.itemId
		WHERE addedBy = @_userId
		GROUP BY addedBy


		SELECT
		*
		FROM
		(
		SELECT 
			cart.cartId,cart.menuItemId,cart.quantity,cart.addedBy,MenuItems.itemName,MenuItems.itemDescription,(MenuItems.price*Cart.quantity ) as 'Price',MenuItems.itemImage
			,menuitems.itemStatusTitle,@_total as 'TotalPrice'
		FROM 
		Cart JOIN MenuItems 
		On 
		Cart.menuItemId=menuItems.itemId


		

		WHERE 
		cart.addedBy=@_userId AND
		cart.isOrdered=0
		GROUP BY
		cart.cartId,cart.menuItemId,cart.quantity,cart.addedBy,MenuItems.itemName,MenuItems.itemDescription,Price,MenuItems.itemImage
			,menuitems.itemStatusTitle
		) AS t
		
		END
END
