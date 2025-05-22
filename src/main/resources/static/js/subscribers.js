$(document).ready(function() {
    const subscribersList = $('#subscribersList');

    // Load subscribers on page load
    loadSubscribers();

    function loadSubscribers() {
        $.ajax({
            url: '/api/booking/subscribers',
            type: 'GET',
            success: function(subscribers) {
                displaySubscribers(subscribers);
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
                    <a href="subscriber-details.html?email=${encodeURIComponent(subscriber.email)}" class="subscriber-link">
                        <div class="subscriber-info">
                            <span class="subscriber-email">${subscriber.email}</span>
                        </div>
                    </a>
                    <div class="subscriber-actions">
                        <button class="btn btn-danger btn-sm" onclick="deleteAllSubscriptions('${subscriber.email}')">Delete All Subscriptions</button>
                    </div>
                </div>
            `);
            subscribersList.append(subscriberElement);
        });
    }

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

// Global function for deleting all subscriptions
function deleteAllSubscriptions(email) {
    if (confirm(`Are you sure you want to delete all subscriptions for ${email}?`)) {
        $.ajax({
            url: `/api/booking/subscribers/batch/${encodeURIComponent(email)}`,
            type: 'DELETE',
            success: function() {
                showSuccess('All subscriptions deleted successfully');
                loadSubscribers();
            },
            error: function(xhr, status, error) {
                showError('Error deleting subscriptions: ' + error);
            }
        });
    }
}
