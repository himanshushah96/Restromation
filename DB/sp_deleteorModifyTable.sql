USE [restomation]
GO

select * from tables
--exec deleteorModifyTable 1 ,4
alter PROCEDURE deleteorModifyTable
(
	 @isDelete bit,
	@tableId	int
)
AS
BEGIN
	
	SET NOCOUNT ON;

	BEGIN -- Variables

		DECLARE @_isdelete		bit = @isDelete	
		DECLARE @_tableId	int	 = @tableId	
		--DECLARE @_deleted_date datetime = NULL
		--DECLARE @_currentTime	datetime = GETDATE()
	END

	BEGIN 
		
		SELECT @_tableId = tableID FROM tables WHERE tableID=@_tableId
		 
		IF @_isdelete=1
			BEGIN
				UPDATE tables 
				SET isTableDeactive = 1
				--, deletedDate = @_currentTime
				where tableID = @_tableId

				SELECT 
					
				tables.tableID as 'TableId'
				,tables.capacity as 'Capacity'
				,tables.isTableDeactive as 'Table Active'
				
							
							
				FROM Tables 
				WHERE Tables.tableID = @_tableId
			END
			
			 
			
		ELSE
		BEGIN
			SELECT '003' AS 'ErrorCode', 'Couldnt delete' AS 'ErrorMessage'
		END

	END

END
GO