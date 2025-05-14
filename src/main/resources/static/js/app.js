$(document).ready(function() {
    // Cache DOM elements
    const startBtn = $('#startBtn');
    const stopBtn = $('#stopBtn');
    const subscribersList = $('#subscribersList');
    const subscriberForm = $('#subscriberForm');
    const addSubscriberForm = $('.add-subscriber-form');
    const subscriberListDiv = $('.subscriber-list');
    
    // Function to check status and update buttons
    function checkStatus() {
        $.ajax({
            url: '/api/background/status',
            type: 'GET',
            success: function(response) {
                if (response.status) {
                    startBtn.prop('disabled', true);
                    stopBtn.prop('disabled', false);
                } else {
                    startBtn.prop('disabled', false);
                    stopBtn.prop('disabled', true);
                }
            },
            error: function(xhr, status, error) {
                showError('Error checking status: ' + error);
            }
        });
    }
    
    // Check status initially
    checkStatus();
    
    // Background Process Control
    startBtn.on('click', function() {
        $.ajax({
            url: '/api/background/start',
            type: 'POST',
            success: function() {
                checkStatus();
            },
            error: function(xhr, status, error) {
                showError('Error starting background process: ' + error);
            }
        });
    });
    
    stopBtn.on('click', function() {
        $.ajax({
            url: '/api/background/stop',
            type: 'POST',
            success: function() {
                checkStatus();
            },
            error: function(xhr, status, error) {
                showError('Error stopping background process: ' + error);
            }
        });
    });

    // Subscriber Management
    $('#listSubscribersBtn').on('click', function() {
        loadSubscribers();
    });

    $('#deleteAllBtn').on('click', function() {
        if (confirm('Are you sure you want to delete all subscribers?')) {
            $.ajax({
                url: '/api/booking/subscribers/batch',
                type: 'DELETE',
                success: function() {
                    showSuccess('All subscribers deleted successfully');
                    loadSubscribers();
                },
                error: function(xhr, status, error) {
                    showError('Error deleting subscribers: ' + error);
                }
            });
        }
    });

    $('#addSubscriberBtn').on('click', function() {
        subscriberListDiv.hide();
        addSubscriberForm.show();
        loadServiceTypes();
    });

    $('#cancelAddSubscriber').on('click', function() {
        addSubscriberForm.hide();
        subscriberForm[0].reset();
    });

    $('#cancelEditBooking').on('click', function() {
        $('.edit-booking-form').hide();
        $('#editBookingForm')[0].reset();
    });

    // Edit Booking Form Submit Handler
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
                $('.edit-booking-form').hide();
                loadSubscribers();
            },
            error: function(xhr, status, error) {
                showError('Error updating booking: ' + error);
            }
        });
    });

    // Service Type Change Handler
    $('#serviceType').on('change', function() {
        const serviceKey = $(this).val();
        if (serviceKey) {
            loadInstitutions(serviceKey);
        } else {
            $('#institution').prop('disabled', true).empty();
        }
    });

    // Form Submit Handler
    subscriberForm.on('submit', function(e) {
        e.preventDefault();
        const email = $('#email').val();
        const serviceType = $('#serviceType').val();
        const institution = $('#institution').val();
        const bookingDate = $('#bookingDate').val();
        const serviceDescription = $('#serviceType option:selected').text();
        const institutionDescription = $('#institution option:selected').text();

        $.ajax({
            url: '/api/booking/subscribers',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                serviceKey: serviceType,
                institutionKey: institution,
                bookingDate: bookingDate,
                serviceDescription: serviceDescription,
                institutionDescription: institutionDescription
            }),
            success: function(response) {
                showSuccess('Subscriber added successfully');
                subscriberForm[0].reset();
                addSubscriberForm.hide();
                loadSubscribers();
            },
            error: function(xhr, status, error) {
                showError('Error adding subscriber: ' + error);
            }
        });
    });

    // Helper Functions
    function loadSubscribers() {
        $.ajax({
            url: '/api/booking/subscribers',
            type: 'GET',
            success: function(subscribers) {
                displaySubscribers(subscribers);
                subscriberListDiv.show();
                addSubscriberForm.hide();
            },
            error: function(xhr, status, error) {
                showError('Error loading subscribers: ' + error);
            }
        });
    }

    function displaySubscribers(subscribers) {
        subscribersList.empty();
        if (subscribers.length === 0) {
            subscribersList.html('<p>No subscribers found</p>');
            return;
        }

        subscribers.forEach(function(subscriber) {
            const subscriberElement = $(`
                <div class="subscriber-item">
                    <div class="booking-info">
                        <span>${subscriber.email}</span>
                        <span class="booking-date">Date: ${subscriber.bookingDate}</span>
                        <span>${subscriber.serviceDescription} at ${subscriber.institutionDescription}</span>
                    </div>
                    <div class="actions">
                        <button class="btn btn-primary btn-sm" onclick="editBooking('${subscriber.id}', '${subscriber.email}', '${subscriber.serviceDescription}', '${subscriber.institutionDescription}', '${subscriber.bookingDate}', '${subscriber.serviceKey}', '${subscriber.institutionKey}')">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteSubscriber('${subscriber.email}')">Delete</button>
                    </div>
                </div>
            `);
            subscribersList.append(subscriberElement);
        });
    }

    function loadServiceTypes() {
        $.ajax({
            url: '/api/booking/info',
            type: 'GET',
            success: function(services) {
                const serviceSelect = $('#serviceType');
                serviceSelect.empty();
                serviceSelect.append('<option value="">Select a service</option>');
                
                Object.entries(services).forEach(([key, value]) => {
                    serviceSelect.append(`<option value="${key}">${value}</option>`);
                });
            },
            error: function(xhr, status, error) {
                showError('Error loading service types: ' + error);
            }
        });
    }

    function loadInstitutions(serviceKey) {
        $.ajax({
            url: `/api/booking/info/${serviceKey}`,
            type: 'GET',
            success: function(institutions) {
                const institutionSelect = $('#institution');
                institutionSelect.empty();
                institutionSelect.append('<option value="">Select an institution</option>');
                
                Object.entries(institutions).forEach(([key, value]) => {
                    institutionSelect.append(`<option value="${key}">${value}</option>`);
                });
                
                institutionSelect.prop('disabled', false);
            },
            error: function(xhr, status, error) {
                showError('Error loading institutions: ' + error);
            }
        });
    }

    function showError(message) {
        const errorDiv = $('<div class="error-message"></div>')
            .text(message)
            .insertAfter('.controls')
            .delay(5000)
            .fadeOut(function() { $(this).remove(); });
    }

    function showSuccess(message) {
        const successDiv = $('<div class="success-message"></div>')
            .text(message)
            .insertAfter('.controls')
            .delay(5000)
            .fadeOut(function() { $(this).remove(); });
    }
});

// Global function for deleting individual subscribers
function deleteSubscriber(email) {
    if (confirm(`Are you sure you want to delete subscriber ${email}?`)) {
        $.ajax({
            url: `/api/booking/subscribers/batch/${email}`,
            type: 'DELETE',
            success: function() {
                showSuccess('Subscriber deleted successfully');
                $('#listSubscribersBtn').click();
            },
            error: function(xhr, status, error) {
                showError('Error deleting subscriber: ' + error);
            }
        });
    }
}

function editBooking(id, email, service, institution, date, serviceKey, institutionKey) {
    // Hide other forms and show edit form
    $('.add-subscriber-form').hide();
    $('.edit-booking-form').show();
    
    // Populate the form
    $('#editBookingId').val(id);
    $('#editEmail').text(email);
    $('#editService').text(service);
    $('#editInstitution').text(institution);
    $('#editBookingDate').val(date);
}
