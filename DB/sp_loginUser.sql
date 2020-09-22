USE [restomation]
GO



--exec loginUser 'admin@gmail.com','123456'
ALTER PROCEDURE loginUser
(
	 @email		varchar(150),
	@password	varchar(16)
)
AS
BEGIN
	
	SET NOCOUNT ON;

	BEGIN -- Variables

		DECLARE @_email		varchar(150) = @email	
		DECLARE @_password	varchar(64)	 = @password	

		SET @_password = CONVERT(VARCHAR(64),HASHBYTES('SHA2_256', @_password), 2)

		DECLARE @_userId int = NULL
		DECLARE @_deleted_date datetime = NULL
		DECLARE @_currentTime	datetime = GETDATE()
		DECLARE @_token UNIQUEIDENTIFIER = NEWID()
		DECLARE @_isDeactive bit = NULL
	END

	BEGIN -- login user
		
		SELECT @_userId = userid, @_deleted_date = deletedDate,@_isDeactive=isDeactive FROM users WHERE email = @_email AND password = @_password
		
		--IF ISNULL(@_deleted_date, '') = '' 
		IF @_isDeactive=0
		BEGIN
			IF EXISTS (select 1 from users where users.userId=@_userId) 
			
				BEGIN

					IF NOT EXISTS (SELECT 1 FROM tokens WHERE userid = @_userId)
						BEGIN
							INSERT INTO tokens ( token, createdDate,userId,EXPIREdATE)
							VALUES ( @_token, @_currentTime,@_userId,DATEADD(DAY,30,@_currentTime))

							SELECT 
							concat(users.firstname, ' ', users.lastname) as 'Name'
							,email as 'Email'
							,users.userid as 'UserId'
							,@_token as 'Token'
							,userTypeId as 'UserTypeId'
							,tokens.createdDate as 'TokenCreatedDate'
							,tokens.expireDate as 'ExpireDate'
							
							FROM users inner join tokens
							ON users.userId = tokens.userId
							WHERE users.userid = @_userId
						END

					ELSE 
						BEGIN
							UPDATE tokens 
							SET token = @_token
							, createdDate = @_currentTime
							, expireDate = DATEADD(DAY,30,@_currentTime)
							where userid = @_userId

							SELECT 
							concat(users.firstname, ' ', users.lastname) as 'Name'
							,email as 'Email'
							,users.userid as 'UserId'
							,@_token as 'Token'
							,userTypeId as 'UserTypeId'
							,tokens.createdDate as 'TokenCreatedDate'
							,tokens.expireDate as 'ExpireDate'
							
							FROM users inner join tokens
							ON users.userId = tokens.userId
							WHERE users.userid = @_userId
						END
				END
			ELSE 
			BEGIN
				SELECT '002' AS 'ErrorCode', 'Email id or password is wrong' AS 'ErrorMessage'	
			END
		END
		--ELSE IF ISNULL(@_deleted_date,'') != ''
		ELSE IF @_isDeactive=1
			BEGIN
				SELECT '001' AS 'ErrorCode', 'Sorry, Your account is deactivated. Please send us an email with your registered account' AS 'ErrorMessage'	
			END
		ELSE
		BEGIN
			SELECT '003' AS 'ErrorCode', 'Sorry, we didn''t find your email. Make sure you login with your registered email id' AS 'ErrorMessage'
		END

	END

END
GO