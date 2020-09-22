USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[getTables]    Script Date: 2020-05-31 3:32:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


ALTER PROCEDURE [dbo].[getTables]
(
	@tableId INT	
)
AS
BEGIN

	SET NOCOUNT ON;

	BEGIN -- Declare Variables
		DECLARE @_tableId    INT = @tableId
	
	END
	
	BEGIN
		
		SELECT
		*
		FROM
		(
		SELECT 
			 Tables.tableId, Tables.capacity, Tables.isTableAvailable as availability			
		FROM 
		Tables 

		WHERE 
		-- (@_userId IS NULL OR (u1.userId=@_userId)) AND
		 (@_tableId = 0 OR (Tables.tableID=@_tableId)) AND
		 (isTableDeactive = 0 )
		) AS t
		
		END
END
