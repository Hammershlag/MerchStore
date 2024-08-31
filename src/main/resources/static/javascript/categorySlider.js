document.addEventListener('DOMContentLoaded', function () {
    const slider = document.querySelector('.category-slider');
    const prevButton = document.getElementById('prevButton');
    const nextButton = document.getElementById('nextButton');
    const itemWidth = slider.querySelector('.category-container').offsetWidth + 20; // 150px + 20px margin (10px on each side)
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

    slider.addEventListener('scroll', updateButtons);

    function updateButtons() {
        const maxScrollLeft = slider.scrollWidth - slider.clientWidth;

        // If there are not enough categories to scroll
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

        if (slider.scrollLeft >= maxScrollLeft-10) {
            nextButton.disabled = true;
        } else {
            nextButton.disabled = false;
        }
    }
});

