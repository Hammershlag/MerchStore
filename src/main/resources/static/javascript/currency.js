// JavaScript code to fetch currencies and populate dropdown
window.onload = function() {
    fetch('/api/currencies') // Replace with your server endpoint that returns the list of currencies
        .then(response => response.json())
        .then(currencies => {
            const select = document.getElementById('currencySelect');
            currencies.forEach(currency => {
                const option = document.createElement('option');
                option.value = currency.shortName;
                option.text = currency.shortName + ' - ' + currency.name;
                select.appendChild(option);
            });

            // Check if a 'currency' cookie exists
            const currencyCookie = document.cookie.split('; ').find(row => row.startsWith('currency'));
            if (currencyCookie) {
                // If it does, set the selected value of the dropdown to the value of the 'currency' cookie
                const currencyValue = currencyCookie.split('=')[1];
                select.value = currencyValue;
            }
        });

    // Event listener for currency selection change
    document.getElementById('currencySelect').addEventListener('change', function() {
        document.cookie = `currency=${this.value}; path=/`;
        location.reload();
    });
}