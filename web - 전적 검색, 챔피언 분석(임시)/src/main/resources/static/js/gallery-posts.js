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
