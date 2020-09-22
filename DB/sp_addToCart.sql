USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[addToCart]    Script Date: 15-06-2020 03:57:40 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


--exec addToCart 23,23,5
CREATE procedure [dbo].[addToCart]
(
	@itemId int,
	@addedBy int,
	@qty int
)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN
		DECLARE @_cartId int = null
		DECLARE @_avaQTY int = null
		DECLARE @_itemName varchar(30) = null
		DECLARE @_itemId int = @itemId
		DECLARE @_qty int = @qty
		DECLARE @_updatedQTY int= null;
	END

	BEGIN
		Select @_cartId = Cart.cartId, @_avaQTY=availableQty, @_itemName=MenuItems.itemName, @_itemId = MenuItems.itemId from Cart JOIN MenuItems on MenuItems.itemId=Cart.menuItemId where Cart.menuItemId = @itemId and Cart.isOrdered = 'false' and Cart.addedBy = @addedBy;
		if(@_cartId is null)
		begin
			Select @_avaQTY = MenuItems.availableQty from MenuItems where itemId = @_itemId;
				if (@_avaQTY >= @_qty)
				begin
					Insert into Cart(menuItemId, addedBy, quantity, isOrdered) values (@itemId, @addedBy, @qty, 'false');
				
					Select '1' as 'StatusCode', 'Added to cart successfully' as 'StatusMessage';
				end
			    else if (@_avaQTY < @qty and @_avaQTY > 0)
				begin
					select '404' as 'StatusCode', CONCAT('Sorry, You cannot order more than ',@_avaQTY,' ',@_itemName) as 'StatusMessage';
				end
			else if(@_avaQTY = 0)
				begin
					select '404' as 'StatusCode',CONCAT('Sorry we are out of ',@_itemName);
				end
			else
				begin
					Select '01' as 'StatusCode', CONCat('Failed to add to Cart', @_avaQTY,' ',  @_itemId) as 'StatusMessage';
				end
		end
		else
		begin
			Select @_avaQTY = MenuItems.availableQty from MenuItems where itemId = @_itemId;
			Select @_updatedQTY = (quantity + @qty) from Cart where cartId = @_cartId;
			if (@_avaQTY >= @_updatedQTY)
				begin
					Update Cart set quantity = (quantity + @qty), addedBy = @addedBy where cartId = @_cartId;
					if(@@ROWCOUNT > 0)
					begin
						Select '1' as 'StatusCode', 'Added to cart successfully' as 'StatusMessage';
					end
					else
					begin
						Select '0' as 'StatusCode', 'Failed to add to Cart' as 'StatusMessage';
					end
				end
			    else if (@_avaQTY < @_updatedQTY and @_avaQTY > 0)
				begin
					select '404' as 'StatusCode', CONCAT('Sorry, You cannot order more than ',@_avaQTY,' ',@_itemName) as 'StatusMessage';
				end
			else if(@_avaQTY = 0)
				begin
					select '404' as 'StatusCode',CONCAT('Sorry we are out of ',@_itemName);
				end
			else
				begin
					Select '0' as 'StatusCode', 'Failed to add to Cart' as 'StatusMessage';
				end
		end
	END
END
