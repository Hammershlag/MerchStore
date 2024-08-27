function loadAds() {
    $("#ads-container").load("/updateAds");
}

$.get("/api/config?key=ads.refresh.interval", function(refreshInterval) {
    loadAds(); // Initial load
    setInterval(loadAds, parseInt(refreshInterval)); // Refresh every interval specified in messages.properties
});

