document.addEventListener('DOMContentLoaded', function () {
    const slider = document.querySelector('.recently-viewed-panel');
    const prevButton = document.getElementById('prevButtonRecentlyViewed');
    const nextButton = document.getElementById('nextButtonRecentlyViewed');
    const deleteHistoryElement = document.getElementById('deleteHistory');
    const itemWidth = slider.querySelector('.item-mini-container').offsetWidth + 20; // Assumes a 20px total margin (10px on each side)
    const itemsToScroll = 3;
    const scrollAmount = itemWidth * itemsToScroll;

    updateButtons(); // Initial button state check

    prevButton.addEventListener('click', () => {
        slider.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
        setTimeout(updateButtons, 300); // Update buttons after scrolling
    });

    nextButton.addEventListener('click', () => {
        slider.scrollBy({ left: scrollAmount, behavior: 'smooth' });
        setTimeout(updateButtons, 300); // Update buttons after scrolling
    });

    deleteHistoryElement.addEventListener('click', function() {
        fetch('/cookie/delete?key=allHistory', {
            method: 'GET',
        })
            .then(response => response.text())
            .then(data => {
                console.log('Response from /delete:', data);

                // Reload the page to update the recently viewed panel
                location.reload();
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    });

    slider.addEventListener('scroll', updateButtons);

    function updateButtons() {
        const maxScrollLeft = slider.scrollWidth - slider.clientWidth;

        // If there are not enough items to scroll
        if (slider.scrollWidth <= slider.clientWidth) {
            prevButton.style.display = 'none';
            nextButton.style.display = 'none';
            return; // Exit the function
        } else {
            prevButton.style.display = '';
            nextButton.style.display = '';
        }

        if (slider.scrollLeft <= 10) {
            prevButton.disabled = true;
        } else {
            prevButton.disabled = false;
        }

        if (slider.scrollLeft >= maxScrollLeft - 10) {
            nextButton.disabled = true;
        } else {
            nextButton.disabled = false;
        }
    }
});
