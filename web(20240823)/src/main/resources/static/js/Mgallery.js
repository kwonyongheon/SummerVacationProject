document.getElementById('galleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.getElementById('memberGalleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.addEventListener('DOMContentLoaded', function () {
    var moreLink = document.querySelector('.more-link');
    var container = document.querySelector('.gallery-description-container');

    moreLink.addEventListener('click', function (event) {
        event.preventDefault();
        container.classList.toggle('expanded');
        if (container.classList.contains('expanded')) {
            moreLink.textContent
                = '간단히 줄이기';
        } else {
            moreLink.textContent = '...더보기';
        }
    });
});

function searchPosts() {
    const query = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('#postTableBody tr');
    rows.forEach(row => {
        const title = row.querySelector('td:nth-child(2) a').textContent.toLowerCase();
        if (title.includes(query)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}


