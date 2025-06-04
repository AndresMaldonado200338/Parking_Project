// ...existing code for employee CRUD operations...
const EMPLOYEE_API_URL = 'http://localhost:8080/employee'; //

document.addEventListener('DOMContentLoaded', () => {
    // Check authentication on pages that require it
    if (window.location.pathname.endsWith('dashboard.html')) {
        checkAuth(); // Assumes checkAuth is globally available or imported
    }

    const employeeForm = document.getElementById('employeeForm');
    const loadEmployeesButton = document.getElementById('loadEmployeesButton');
    const employeesTableBody = document.querySelector('#employeesTable tbody');
    const employeeMessage = document.getElementById('employeeMessage');
    const employeeDocumentHidden = document.getElementById('employeeDocumentHidden');
    const saveEmployeeButton = document.getElementById('saveEmployeeButton');
    const clearEmployeeFormButton = document.getElementById('clearEmployeeFormButton');


    // CREATE/UPDATE Employee
    if (employeeForm) {
        employeeForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const documentId = employeeDocumentHidden.value; // For updates
            const isUpdate = !!documentId;

            const employeeData = {
                document: document.getElementById('document').value,
                firstname: document.getElementById('firstname').value,
                lastname: document.getElementById('lastname').value,
                email: document.getElementById('email').value,
                phone: document.getElementById('phone').value,
            };

            const url = isUpdate ? `${EMPLOYEE_API_URL}/updateemployee/${documentId}` : `${EMPLOYEE_API_URL}/createemployee`; //
            const method = isUpdate ? 'PUT' : 'POST'; //

            try {
                const token = getToken();
                if (!token) {
                    employeeMessage.textContent = 'Authentication token not found. Please login again.';
                    employeeMessage.className = 'message error';
                    return;
                }

                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(employeeData),
                });

                if (response.ok) {
                    const result = await response.json();
                    employeeMessage.textContent = isUpdate ? 'Employee updated successfully!' : 'Employee created successfully!'; //
                    employeeMessage.className = 'message success';
                    employeeForm.reset();
                    employeeDocumentHidden.value = '';
                    saveEmployeeButton.textContent = 'Save Employee';
                    document.getElementById('document').disabled = false;
                    if (loadEmployeesButton) loadEmployeesButton.click(); // Refresh list
                } else {
                    const errorResult = await response.json();
                    employeeMessage.textContent = `Error: ${errorResult.message || response.statusText}`;
                    employeeMessage.className = 'message error';
                }
            } catch (error) {
                employeeMessage.textContent = 'Network error: ' + error.message;
                employeeMessage.className = 'message error';
            }
        });
    }
    
    if (clearEmployeeFormButton) {
        clearEmployeeFormButton.addEventListener('click', () => {
            employeeForm.reset();
            employeeDocumentHidden.value = '';
            saveEmployeeButton.textContent = 'Save Employee';
            document.getElementById('document').disabled = false;
            employeeMessage.textContent = '';
            employeeMessage.className = 'message';
        });
    }

    // READ All Employees
    if (loadEmployeesButton) {
        loadEmployeesButton.addEventListener('click', async () => {
            try {
                const token = getToken();
                 if (!token) {
                    employeeMessage.textContent = 'Authentication token not found. Please login again.';
                    employeeMessage.className = 'message error';
                    return;
                }
                const response = await fetch(`${EMPLOYEE_API_URL}/findallemployees`, { //
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.ok) {
                    const employees = await response.json(); //
                    renderEmployees(employees);
                    employeeMessage.textContent = 'Employees loaded.';
                    employeeMessage.className = 'message success';
                } else {
                    employeeMessage.textContent = `Error loading employees: ${response.statusText}`;
                    employeeMessage.className = 'message error';
                }
            } catch (error) {
                employeeMessage.textContent = 'Network error: ' + error.message;
                employeeMessage.className = 'message error';
            }
        });
    }

    function renderEmployees(employees) {
        if (!employeesTableBody) return;
        employeesTableBody.innerHTML = ''; // Clear existing rows
        employees.forEach(emp => {
            const row = employeesTableBody.insertRow();
            row.insertCell().textContent = emp.document;
            row.insertCell().textContent = emp.firstname;
            row.insertCell().textContent = emp.lastname;
            row.insertCell().textContent = emp.email;
            row.insertCell().textContent = emp.phone;
            row.insertCell().textContent = emp.status ? 'Active' : 'Inactive'; 

            const actionsCell = row.insertCell();
            const editButton = document.createElement('button');
            editButton.textContent = 'Edit';
            editButton.onclick = () => loadEmployeeForEdit(emp);
            actionsCell.appendChild(editButton);

            const toggleStatusButton = document.createElement('button');
            toggleStatusButton.textContent = emp.status ? 'Disable' : 'Enable (Logic not shown)'; // Enable logic not in backend specs
            if (emp.status) { // Only add disable functionality if active
                 toggleStatusButton.onclick = () => disableEmployee(emp.document); 
            } else {
                toggleStatusButton.disabled = true; // Or hide, as per requirements
            }
            actionsCell.appendChild(toggleStatusButton);
        });
    }

    function loadEmployeeForEdit(employee) {
        employeeDocumentHidden.value = employee.document;
        document.getElementById('document').value = employee.document;
        document.getElementById('document').disabled = true; // Document is not editable 
        document.getElementById('firstname').value = employee.firstname;
        document.getElementById('lastname').value = employee.lastname;
        document.getElementById('email').value = employee.email;
        document.getElementById('phone').value = employee.phone;
        saveEmployeeButton.textContent = 'Update Employee';
        window.scrollTo(0, 0); // Scroll to top to see form
    }

    // DISABLE Employee
    async function disableEmployee(documentId) {
        if (!confirm(`Are you sure you want to disable employee ${documentId}?`)) return;
        try {
            const token = getToken();
            if (!token) {
                employeeMessage.textContent = 'Authentication token not found.';
                employeeMessage.className = 'message error';
                return;
            }
            const response = await fetch(`${EMPLOYEE_API_URL}/disableemployee/${documentId}`, { //
                method: 'PATCH', //
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            if (response.ok) {
                const result = await response.json(); //
                employeeMessage.textContent = `Employee ${documentId} status: ${result.status ? 'Active' : 'Inactive'}. Message: ${result.message}`;
                employeeMessage.className = 'message success';
                if (loadEmployeesButton) loadEmployeesButton.click(); // Refresh list
            } else {
                const errorResult = await response.json();
                employeeMessage.textContent = `Error disabling employee: ${errorResult.message || response.statusText}`;
                employeeMessage.className = 'message error';
            }
        } catch (error) {
            employeeMessage.textContent = 'Network error: ' + error.message;
            employeeMessage.className = 'message error';
        }
    }
});