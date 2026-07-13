document.getElementById('calculateBtn').addEventListener('click', calculate);

// Allow Enter key to calculate
document.getElementById('dividend').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') calculate();
});

document.getElementById('divisor').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') calculate();
});

function calculate() {
    const dividend = parseInt(document.getElementById('dividend').value);
    const divisor = parseInt(document.getElementById('divisor').value);

    // Validation
    if (isNaN(dividend) || isNaN(divisor)) {
        document.getElementById('result').textContent = 'Error';
        document.getElementById('calculation').textContent = 'Please enter valid numbers';
        return;
    }

    if (divisor === 0) {
        document.getElementById('result').textContent = 'Error';
        document.getElementById('calculation').textContent = 'Divisor cannot be zero';
        return;
    }

    // Calculate modulo
    const result = dividend % divisor;

    // Display result
    document.getElementById('result').textContent = result;
    document.getElementById('calculation').textContent = 
        `${dividend} ÷ ${divisor} = ${Math.floor(dividend / divisor)} remainder ${result}`;
}

// Calculate on page load with default values
calculate();