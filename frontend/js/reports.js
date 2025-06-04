const REPORTS_API_URL = 'http://localhost:8080/access'; //

document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.endsWith('dashboard.html')) {
        checkAuth();
    }

    const reportByDateForm = document.getElementById('reportByDateForm');
    const reportByDateResult = document.getElementById('reportByDateResult');
    const reportByEmployeeForm = document.getElementById('reportByEmployeeForm');
    const reportByEmployeeResult = document.getElementById('reportByEmployeeResult');

    if (reportByDateForm) {
        reportByDateForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const date = document.getElementById('reportDate').value;
            if (!date) {
                reportByDateResult.innerHTML = '<p class="message error">Date is required.</p>';
                return;
            }
            try {
                const token = getToken();
                if (!token) {
                    reportByDateResult.innerHTML = '<p class="message error">Authentication token not found.</p>';
                    return;
                }
                //
                const response = await fetch(`${REPORTS_API_URL}/allemployeesbydate?date=${date}`, {
                    method: 'GET', //
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                if (response.ok) {
                    const reportData = await response.json(); //
                    renderReportByDate(reportData);
                } else {
                    const error = await response.json();
                    reportByDateResult.innerHTML = `<p class="message error">Error generating report: ${error.message || response.statusText}</p>`; //
                }
            } catch (error) {
                reportByDateResult.innerHTML = `<p class="message error">Network error: ${error.message}</p>`;
            }
        });
    }

    if (reportByEmployeeForm) {
        reportByEmployeeForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const employeeId = document.getElementById('reportEmployeeId').value;
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;

            if (!employeeId || !startDate || !endDate) {
                reportByEmployeeResult.innerHTML = '<p class="message error">All fields are required.</p>';
                return;
            }
            try {
                const token = getToken();
                if (!token) {
                    reportByEmployeeResult.innerHTML = '<p class="message error">Authentication token not found.</p>';
                    return;
                }
                //
                const queryParams = `employeeId=${employeeId}&startDate=${startDate}&endDate=${endDate}`;
                const response = await fetch(`${REPORTS_API_URL}/employeebydates?${queryParams}`, { //
                    method: 'GET', //
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                if (response.ok) {
                    const reportData = await response.json(); //
                    renderReportByEmployee(reportData);
                } else {
                    const error = await response.json();
                    reportByEmployeeResult.innerHTML = `<p class="message error">Error generating report: ${error.message || response.statusText}</p>`; //
                }
            } catch (error) {
                reportByEmployeeResult.innerHTML = `<p class="message error">Network error: ${error.message}</p>`;
            }
        });
    }

    function renderReportByDate(data) {
        if (!data || data.length === 0) {
            reportByDateResult.innerHTML = '<p>No data found for the selected date.</p>';
            return;
        }

        const date = document.getElementById('reportDate').value;
        let html = '<table><thead><tr><th>Employee ID</th><th>Name</th><th>Entry Time</th><th>Exit Time</th><th>Duration</th></tr></thead><tbody>';
        let text = `Reporte de accesos - Fecha: ${date}\n\n`;

        data.forEach(item => {
            const entry = item.entryTime ? new Date(item.entryTime).toLocaleString() : 'N/A';
            const exit = item.exitTime ? new Date(item.exitTime).toLocaleString() : 'N/A';
            html += `<tr>
                    <td>${item.employeeId}</td>
                    <td>${item.employeeName}</td>
                    <td>${entry}</td>
                    <td>${exit}</td>
                    <td>${item.duration}</td>
                </tr>`;
            text += `ID: ${item.employeeId} | Nombre: ${item.employeeName} | Entrada: ${entry} | Salida: ${exit} | Duración: ${item.duration}\n`;
        });

        html += '</tbody></table>';
        reportByDateResult.innerHTML = html;
        createDownloadButton('reportByDateResult', `report_${date}.txt`, text);
    }

    function renderReportByEmployee(data) {
        if (!data || data.length === 0) {
            reportByEmployeeResult.innerHTML = '<p>No data found for the selected employee and date range.</p>';
            return;
        }

        const employeeId = document.getElementById('reportEmployeeId').value;
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        let html = '';
        let text = `Reporte del empleado ${employeeId} desde ${startDate} hasta ${endDate}\n\n`;

        data.forEach(report => {
            html += `<h3>Report for ${report.employeeName} (ID: ${report.employeeId})</h3>`;
            html += `<p><strong>Total Duration: ${report.totalDuration}</strong></p>`;
            text += `Nombre: ${report.employeeName} | ID: ${report.employeeId} | Total: ${report.totalDuration}\n`;

            if (report.accessRecords && report.accessRecords.length > 0) {
                html += '<table><thead><tr><th>Entry Time</th><th>Exit Time</th><th>Duration</th></tr></thead><tbody>';
                text += 'Entradas:\n';
                report.accessRecords.forEach(record => {
                    const entry = record.entryTime ? new Date(record.entryTime).toLocaleString() : 'N/A';
                    const exit = record.exitTime ? new Date(record.exitTime).toLocaleString() : 'N/A';
                    html += `<tr>
                            <td>${entry}</td>
                            <td>${exit}</td>
                            <td>${record.duration}</td>
                        </tr>`;
                    text += `  Entrada: ${entry} | Salida: ${exit} | Duración: ${record.duration}\n`;
                });
                html += '</tbody></table>';
            } else {
                html += '<p>No access records found for this period.</p>';
                text += 'No se encontraron registros de acceso.\n';
            }
            text += '\n--------------------------------------\n';
        });

        reportByEmployeeResult.innerHTML = html;
        createDownloadButton('reportByEmployeeResult', `${employeeId}_${startDate}_${endDate}_report.txt`, text);
    }
});

function createDownloadButton(containerId, filename, textContent) {
    const container = document.getElementById(containerId);
    const downloadBtn = document.createElement('button');
    downloadBtn.textContent = 'Guardar como TXT';
    downloadBtn.className = 'download-button';
    downloadBtn.addEventListener('click', () => {
        const blob = new Blob([textContent], { type: 'text/plain' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
        URL.revokeObjectURL(url);
    });
    container.appendChild(downloadBtn);
}