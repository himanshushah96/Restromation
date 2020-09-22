USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[registerEmployee]    Script Date: 2020-05-29 10:17:27 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


ALTER PROCEDURE [dbo].[registerEmployee]
(
	@email varchar(150),
	@password varchar(64),
	@fname varchar(50),
	@lname varchar(50),
	@phone varchar(10),
	@userTypeId int,
	@gender varchar(6)
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
		DECLARE @_userTypeId int = @userTypeId
		DECLARE @_gender varchar(6) = @gender

		SET @_password = CONVERT(VARCHAR(64),HASHBYTES('SHA2_256', @_password), 2)
		DECLARE @_createdDate datetime = GETDATE()
		DECLARE @_updatedDate datetime = NULL
		DECLARE @_deletedDate datetime = NULL
	END

	BEGIN

		

		IF EXISTS(SELECT 1 FROM users where email = @_email )
		BEGIN
			SELECT '004' AS 'ErrorCode' , 'EMAIL ID IS ALREADY REGISTERED' AS 'ErrorMessage'
		END
		ELSE
		BEGIN

			INSERT INTO users(FirstName, lastName, gender, email, password, phoneNo,createdDate,userTypeId)
			VALUES (@_fname, @_lname, @_gender, @_email, @_password, @_phone, @_createdDate, @_userTypeId)

			select @_email AS 'email', userId   from users where email=@_email
		END
	END
END
