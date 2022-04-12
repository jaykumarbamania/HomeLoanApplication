var tbodyRef = document.getElementById('myTable').getElementsByTagName('tbody')[0];

// Insert a row at the end of table
var newRow = tbodyRef.insertRow();

// Insert a cell at the end of the row
var newCell = newRow.insertCell();

// Append a text node to the cell
var newText = document.createTextNode('new row');
newCell.appendChild(newText);