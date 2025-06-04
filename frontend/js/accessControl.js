// ...existing code for check-in/check-out operations...
const ACCESS_API_URL = 'http://localhost:8080/access'; //

document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.endsWith('dashboard.html')) {
        checkAuth();
    }

    const checkInButton = document.getElementById('checkInButton');
    const checkOutButton = document.getElementById('checkOutButton');
    const accessMessage = document.getElementById('accessMessage');

    if (checkInButton) {
        checkInButton.addEventListener('click', () => handleAccess('usercheckin')); //
    }

    if (checkOutButton) {
        checkOutButton.addEventListener('click', () => handleAccess('usercheckout')); //
    }

    async function handleAccess(action) {
        const employeeId = document.getElementById('accessEmployeeId').value;
        const accessDateTimeValue = document.getElementById('accessDateTime').value;
        
        const requestBody = { employeeId }; //
        if (accessDateTimeValue) {
            requestBody.accessDateTime = new Date(accessDateTimeValue).toISOString(); //
        }

        if (!employeeId) {
            accessMessage.textContent = 'Employee ID is required.';
            accessMessage.className = 'message error';
            return;
        }

        try {
            const token = getToken();
            if (!token) {
                accessMessage.textContent = 'Authentication token not found.';
                accessMessage.className = 'message error';
                return;
            }

            const response = await fetch(`${ACCESS_API_URL}/${action}`, {
                method: 'POST', //
                headers: {
                    'Content-Type': 'application/json', //
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(requestBody),
            });

            const result = await response.json(); //

            if (response.ok) {
                accessMessage.textContent = `${result.message || (action === 'usercheckin' ? 'Check-in' : 'Check-out') + ' successful!'}. Employee: ${result.employeeId}, Time: ${new Date(result.accessDateTime).toLocaleString()}`;
                accessMessage.className = 'message success';
                document.getElementById('accessEmployeeId').value = ''; // Clear fields
                document.getElementById('accessDateTime').value = '';
            } else {
                 accessMessage.textContent = `Error: ${result.message || response.statusText}. Status: ${response.status}`; //
                accessMessage.className = 'message error';
            }
        } catch (error) {
            accessMessage.textContent = 'Network error: ' + error.message;
            accessMessage.className = 'message error';
        }
    }
});