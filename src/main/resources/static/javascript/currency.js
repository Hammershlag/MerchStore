// JavaScript code to fetch currencies and populate dropdown
window.onload = function() {
    fetch('/api/currencies') // Fetch the list of currencies from the server
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
            const currencyCookie = document.cookie.split('; ').find(row => row.startsWith('currency='));
            if (currencyCookie) {
                const currencyValue = currencyCookie.split('=')[1];
                select.value = currencyValue;
            }

            // After the dropdown is populated, fetch the exchange rate for the selected currency
            fetchExchangeRate();
        });

    // Event listener for currency selection change
    document.getElementById('currencySelect').addEventListener('change', function() {
        document.cookie = `currency=${this.value}; path=/`;
        location.reload();

    });

    // Function to fetch and display the exchange rate
    function fetchExchangeRate() {
        var shortName = document.getElementById('currencySelect').value;

        // Hide exchange rate display if selected currency is PLN (or any base currency)
        if (shortName === 'PLN') {
            document.getElementById('exchangeRate').style.display = 'none'; // Hide if PLN is selected
        } else {
            document.getElementById('exchangeRate').style.display = 'block'; // Show otherwise

            // Fetch exchange rate for the selected currency
            fetch('/api/currencies/' + shortName)
                .then(response => response.json())
                .then(data => {
                    var exchangeRate = data; // Assuming your API returns just the exchange rate
                    // Display the exchange rate in the format "PLN -> USD: 0.26"
                    document.getElementById('exchangeRate').innerHTML = `PLN -> ${shortName}: ${exchangeRate}`;
                })
                .catch(error => console.error('Error fetching exchange rate:', error));
        }
    }
}
