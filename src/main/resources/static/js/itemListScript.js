// itemListScript.js
const eventSource = new EventSource('/api/items');
eventSource.onmessage = function(event) {
    const item = JSON.parse(event.data);
    const tableBody = document.getElementById("itemsTable").getElementsByTagName("tbody")[0];
    const existingRow = document.getElementById(item.id);

    if (existingRow) {
        existingRow.cells[0].textContent = item.input;
        existingRow.cells[1].textContent = item.status;
        existingRow.cells[2].textContent = item.result || 'N/A';
    } else {
        const row = tableBody.insertRow();
        row.id = item.id;

        const cellInput = row.insertCell(0);
        const cellStatus = row.insertCell(1);
        const cellResult = row.insertCell(2);

        cellInput.textContent = item.input;
        cellStatus.textContent = item.status;
        cellResult.textContent = item.result || 'N/A';
    }
};
