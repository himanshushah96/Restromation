USE [restomation]
GO
/****** Object:  StoredProcedure [dbo].[reserveTable]    Script Date: 2020-05-29 10:18:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO





ALTER PROCEDURE [dbo].[reserveTable]
(
	@tableId int,
	@reservationDate	dateTime,
	@reservedBy int,
	@numberOfPeople int, 
	@startTime time(7),
	@endTime time(7)
)
AS
BEGIN
	
	SET NOCOUNT ON;

	BEGIN -- Variables

		DECLARE @_tableId		int = @tableId	
		DECLARE @_reservationDate	datetime	 = @reservationDate	

		DECLARE @_reservedBy int = @reservedBy
		DECLARE @_numberOfPeople int = @numberOfPeople
		DECLARE @_startTime	time(7) = @startTime
		DECLARE @_endTime time(7) = @endTime
		DECLARE @_reservationId table(ID int)
		DECLARE @_resId int = null
	END

	BEGIN -- reserve Table

	Select @_resId = Reservation.reservationId from Reservation inner join ReservationDetails on ReservationDetails.reservationID = Reservation.reservationId inner join Tables on Tables.tableID =ReservationDetails.tableID where (Reservation.endTime is null or Reservation.endTime = '') and ReservationDetails.tableID = 1 and Tables.isTableAvailable = 0
	if(@_resId is not null)	
		begin
			if(@_endTime is not null or @_endTime != '')
				begin
					Update Reservation Set endTime = @_endTime where Reservation.reservationId = @_resId;
					Update Tables Set isTableAvailable = 1 where tableID = @_tableId;
					Select '2' as 'StatusCode', 'Successfully unallocated Table' as 'StatusMessage';
				end
			else
				begin
					Select '003' as 'ErrorCode', 'Please specify end time for this reservation' as 'ErrorMessage'
				end
		end
	else
		begin
			Insert into Reservation(reservationDate, reservedBy, numberOfPeople, startTime) output inserted.reservationId into @_reservationId
			values(@_reservationDate, @_reservedBy, @_numberOfPeople, @_startTime);
			if @@ROWCOUNT > 0
				begin
					Select @_resId = id from @_reservationId;
					Insert into ReservationDetails(tableID, reservationID) values(@_tableId, @_resId);
					if(@@ROWCOUNT > 0)
						begin
							Update Tables set isTableAvailable = 0 where tableID = @_tableId;
							Select '1' as 'StatusCode', 'Table Reserved Successfully' as 'StatusMessage';
						end
					else
						begin
							Select '0' As 'StatusCode', 'Failed to assign Reservation to the table!!!' as 'StatusMessage';
							Delete from Reservation where Reservation.reservationId = @_resId;
						end
				end
			else
				begin
					Select '002' as 'ErrorCode', 'Failed to Create reservation' as 'ErrorMessage'
				end
		end
	END
END
