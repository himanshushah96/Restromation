
use restomation
GO





Alter PROCEDURE [dbo].[registerUser]
	
(
	@email varchar(150),
	@password varchar(64),
	@fname varchar(50),
	@lname varchar(50),
	@phone varchar(10) = NULL,
	@userTypeId int
)

AS
BEGIN


	SET NOCOUNT ON;
	BEGIN

		DECLARE @_email		varchar(150) = @email
		DECLARE @_password	varchar(64) = @password
		DECLARE @_fname		varchar(50) = @fname
		DECLARE @_lname		varchar(50) = @lname
		DECLARE @_phone		varchar(10) = @phone

		SET @_password = CONVERT(VARCHAR(64),HASHBYTES('SHA2_256', @_password), 2)
		DECLARE @_createdDate datetime = GETDATE()
		DECLARE @_updatedDate datetime = NULL
		DECLARE @_deletedDate datetime = NULL
		DECLARE @_userTypeId  int = @userTypeId
		DECLARE @_isDeactive bit = 0
	END

	BEGIN

	
		IF EXISTS(SELECT 1 FROM users where email = @_email )

		BEGIN
			SELECT '004' AS 'ErrorCode' , 'EMAIL ID IS ALREADY REGISTERED' AS 'ErrorMessage'
		END
		ELSE
		BEGIN

			INSERT INTO users(FirstName,lastName,email,password,phoneNo,createdDate,userTypeId,isDeactive)
			VALUES (@_fname,@_lname,@_email,@_password,@_phone,@_createdDate,@_userTypeId,@_isDeactive)

			select @_email AS 'email',userId   from users where email=@_email
		END
	END
END
