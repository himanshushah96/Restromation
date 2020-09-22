USE [restomation]
GO

select * from users; 
--exec deleteorModifyEmployee 1 ,19
alter PROCEDURE deleteorModifyEmployee
(
	 @isDelete bit,
	@userId	int
)
AS
BEGIN
	
	SET NOCOUNT ON;

	BEGIN -- Variables

		DECLARE @_isdelete		bit = @isDelete	
		DECLARE @_userId	int	 = @userId	
		--DECLARE @_deleted_date datetime = NULL
		DECLARE @_currentTime	datetime = GETDATE()
	END

	BEGIN 
		
		SELECT @_userId = userid FROM users WHERE userId=@_userId
		 
		IF @_isdelete=1
			BEGIN
				UPDATE users 
				SET isDeactive = 1
				, deletedDate = @_currentTime
				where userid = @_userId

				SELECT 
					
				users.userid as 'UserId'
				,users.userTypeId as 'UserTypeId'
				
							
							
				FROM users 
				WHERE users.userid = @_userId
			END
			
			 
			
		ELSE
		BEGIN
			SELECT '003' AS 'ErrorCode', 'Couldnt delete' AS 'ErrorMessage'
		END

	END

END
GO