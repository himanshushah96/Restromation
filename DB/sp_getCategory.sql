--exec getCategory

use restomation
Go

ALTER PROCEDURE getCategory
AS
BEGIN

	SET NOCOUNT ON;

	
	BEGIN
		
		SELECT
		categoryID as 'CategoryID' 
		,categoryTitle as 'CategoryTitle'
		FROM
		MenuCategory
		ORDER BY
		categoryID
		END
END