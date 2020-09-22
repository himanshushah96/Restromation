USE [restomation]
GO



ALTER procedure [dbo].[addTable]
(
	@capacity int
)
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN
		DECLARE @_capacity	varchar(150) = @capacity
		Declare @_table table(id int);
		Declare @_tableId int = null;
		Declare @_isTableDeactive bit = 0
	END

	BEGIN
		Insert into Tables(capacity, isTableAvailable,isTableDeactive)output inserted.tableID into @_table values (@_capacity, 'true',@_isTableDeactive);
		if (@@ROWCOUNT > 0)
		begin
			Select @_tableId = id from @_table;
			Select tableId, capacity, isTableAvailable as availability from Tables where Tables.tableID = @_tableId;
		end
		else
		begin
			Select '004' as 'ErrorCode', 'Failed to create table' as 'ErrorMessage';
		end
	END
END
