--exec getUsers
use restomation
Go

ALTER PROCEDURE getUsers
(
	 @userTypeId	INT				= NULL

)
AS
BEGIN

	SET NOCOUNT ON;

	BEGIN -- Decalre Variables
		
		DECLARE @_userType		INT				= @userTypeId
		
		/*DECLARE @_email			VARCHAR(150)	= @email*/

	
		IF @_userType = 0
		BEGIN
			SET @_userType = NULL
		END

	END
	
	BEGIN
		
		SELECT
		*
		FROM
		(
		SELECT 
			 userId AS 'Id'
			,FirstName AS 'FirstName'
			,lastName AS 'LastName'
			,email AS 'Email'
			,phoneNo AS 'Phone'
			,profileImage AS 'Image'
			,users.userTypeId As 'UserType'
			,gender AS 'Gender'
			,userType AS 'UserTypeTitle'

			
		FROM USERS
		JOIN USERTYPE
		on users.userTypeId = userType.userTypeID
		WHERE 
			--(@_userType IS NULL OR (userTypeId=@_userType))
			users.userTypeId IN (2,3,4,5) AND
			Users.isDeactive = 0
		
		) AS t
		
		END

END