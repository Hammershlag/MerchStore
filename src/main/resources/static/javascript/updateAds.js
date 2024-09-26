let loadAdsInterval = 0;
let adCycleCounter = 0;
let maxAds = 0;

let urlParams = new URLSearchParams(window.location.search);
let lang = urlParams.get('lang') || 'pl';

$.get("/api/config?key=ads.refresh.interval", function(refreshInterval) {
    loadAds(); // Initial load
    setInterval(loadAds, parseInt(refreshInterval)); // Refresh every interval specified in messages.properties
});

$.get("/api/config?key=ads.load.cycles", function(loadInterval) {
    loadAdsInterval = parseInt(loadInterval);
});

$.get("/api/config?key=ads.load.max", function(maxAdsValue) {
    maxAds = parseInt(maxAdsValue);
});


function loadAds() {
    $("#ads-container").load(`/updateAds?lang=${lang}&maxAds=${maxAds}`, function() {
        // After loading new ads, re-query the ad elements and reset the ad cycle counter
        ads = document.querySelectorAll('.ad-item');
        adCycleCounter = 0;
    });
}

document.addEventListener('DOMContentLoaded', function () {
    let ads = document.querySelectorAll('.ad-item');
    let currentAdIndex = 0;

    function showNextAd() {
        ads[currentAdIndex].style.display = 'none';
        currentAdIndex = (currentAdIndex + 1) % ads.length;
        ads[currentAdIndex].style.display = 'block';

        // Increment the ad cycle counter and load new ads if necessary
        adCycleCounter++;
        if (adCycleCounter >= loadAdsInterval * ads.length) {
            loadAds();
        }
    }

    // Initial ad display
    ads[currentAdIndex].style.display = 'block';

    // Cycle through ads every X seconds (use value from properties file)
    setInterval(showNextAd, 10000); // Adjust the interval (in milliseconds) as needed
});