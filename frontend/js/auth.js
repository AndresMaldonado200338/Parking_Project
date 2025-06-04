const API_URL = 'http://localhost:8081'; // Port for LoginService

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const logoutButton = document.getElementById('logoutButton');

    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const userID = document.getElementById('userID').value;
            const password = document.getElementById('password').value;
            const loginMessage = document.getElementById('loginMessage');

            try {
                const response = await fetch(`${API_URL}/login/authuser`, { //
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ userID: parseInt(userID), password }),
                });

                const result = await response.json(); //

                if (response.ok && result.token) {
                    localStorage.setItem('authToken', result.token); // Store the token
                    loginMessage.textContent = `Login successful! Token: ${result.token.substring(0,20)}...`;
                    loginMessage.className = 'message success';
                    window.location.href = 'dashboard.html'; // Redirect to dashboard
                } else {
                    loginMessage.textContent = result.message || 'Login failed. Please check User ID and Password.'; //
                    loginMessage.className = 'message error';
                    localStorage.removeItem('authToken');
                }
            } catch (error) {
                loginMessage.textContent = 'Error during login: ' + error.message;
                loginMessage.className = 'message error';
                localStorage.removeItem('authToken');
            }
        });
    }

    if (logoutButton) {
        logoutButton.addEventListener('click', () => {
            localStorage.removeItem('authToken');
            window.location.href = 'index.html'; // Redirect to login page
        });
    }
});

function getToken() {
    return localStorage.getItem('authToken');
}

// Function to check if user is authenticated (e.g., on dashboard load)
function checkAuth() {
    if (!getToken()) {
        window.location.href = 'index.html';
    }
}