$(document).ready(function() {
    const bookingsList = $('#bookingsList');
    const subscriberEmail = $('#subscriberEmail');
    const editForm = $('.edit-booking-form');
    
    // Get email from URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');
    
    if (!email) {
        window.location.href = 'subscribers.html';
        return;
    }

    // Display email in header
    subscriberEmail.text(email);

    // Load subscriber's bookings
    loadBookings();

    function loadBookings() {
        $.ajax({
            url: `/api/booking/subscribers/${encodeURIComponent(email)}`,
            type: 'GET',
            success: function(bookings) {
                displayBookings(bookings);
            },
            error: function(xhr, status, error) {
                showError('Error loading bookings: ' + error);
            }
        });
    }

    function displayBookings(bookings) {
        bookingsList.empty();
        if (bookings.length === 0) {
            bookingsList.html('<p>No bookings found for this subscriber</p>');
            return;
        }

        bookings.forEach(function(booking) {
            const bookingElement = $(`
                <div class="booking-item">
                    <div class="booking-info">
                        <div class="booking-service">
                            <strong>Service:</strong> ${booking.serviceDescription}
                        </div>
                        <div class="booking-institution">
                            <strong>Institution:</strong> ${booking.institutionDescription}
                        </div>
                        <div class="booking-date">
                            <strong>Date:</strong> ${booking.subscribedAt}
                        </div>
                    </div>
                    <div class="booking-actions">
                        <button class="btn btn-primary btn-sm" onclick="openEditModal('${booking.id}', '${booking.serviceDescription}', '${booking.institutionDescription}', '${booking.bookingDate}')">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteBooking('${booking.id}')">Delete</button>
                    </div>
                </div>
            `);
            bookingsList.append(bookingElement);
        });
    }

    // Edit Booking Form Handler
    $('#editBookingForm').on('submit', function(e) {
        e.preventDefault();
        const bookingId = $('#editBookingId').val();
        const bookingDate = $('#editBookingDate').val();

        $.ajax({
            url: '/api/booking/subscribers',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                id: bookingId,
                bookingDate: bookingDate
            }),
            success: function() {
                showSuccess('Booking updated successfully');
                editForm.hide();
                loadBookings();
            },
            error: function(xhr, status, error) {
                showError('Error updating booking: ' + error);
            }
        });
    });

    // Cancel Edit Button Handler
    $('#cancelEditBooking').on('click', function() {
        editForm.hide();
        $('#editBookingForm')[0].reset();
    });

    function showError(message) {
        const errorDiv = $('<div class="error-message"></div>')
            .text(message)
            .insertAfter('h2')
            .delay(5000)
            .fadeOut(function() { $(this).remove(); });
    }

    function showSuccess(message) {
        const successDiv = $('<div class="success-message"></div>')
            .text(message)
            .insertAfter('h2')
            .delay(5000)
            .fadeOut(function() { $(this).remove(); });
    }
});

// Global functions for edit and delete
function editBooking(id, service, institution, date) {
    const editForm = $('.edit-booking-form');
    
    // Populate the form
    $('#editBookingId').val(id);
    $('#editService').text(service);
    $('#editInstitution').text(institution);
    $('#editBookingDate').val(date);
    
    // Show the form
    editForm.show();
}

// Modal handling
function openEditModal(id, service, institution, date) {
    $('#editBookingId').val(id);
    $('#editServiceInfo').text(service);
    $('#editInstitutionInfo').text(institution);
    $('#editBookingDate').val(date);
    $('#editBookingModal').show();
}

$('#cancelEdit').on('click', function() {
    $('#editBookingModal').hide();
    $('#editBookingForm')[0].reset();
});

// Close modal when clicking outside
$('#editBookingModal').on('click', function(e) {
    if (e.target === this) {
        $(this).hide();
        $('#editBookingForm')[0].reset();
    }
});

// Handle form submission
$('#editBookingForm').on('submit', function(e) {
    e.preventDefault();
    const bookingId = $('#editBookingId').val();
    const newDate = $('#editBookingDate').val();

    $.ajax({
        url: '/api/booking/subscribers',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: bookingId,
            bookingDate: newDate
        }),
        success: function() {
            showSuccess('Booking updated successfully');
            $('#editBookingModal').hide();
            loadBookings();
        },
        error: function(xhr, status, error) {
            showError('Error updating booking: ' + error);
        }
    });
});

function deleteBooking(id) {
    if (confirm('Are you sure you want to delete this booking?')) {
        $.ajax({
            url: `/api/booking/subscribers/${id}`,
            type: 'DELETE',
            success: function() {
                showSuccess('Booking deleted successfully');
                loadBookings();
            },
            error: function(xhr, status, error) {
                showError('Error deleting booking: ' + error);
            }
        });
    }
}
