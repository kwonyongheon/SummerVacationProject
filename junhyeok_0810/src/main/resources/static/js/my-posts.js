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

function deletePost(postId) {
    if (confirm('정말로 이 게시물을 삭제하시겠습니까?')) {
        fetch(`/gallery/post/${postId}/delete`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('게시물이 삭제되었습니다.');
                location.reload();
            } else {
                alert('게시물 삭제에 실패하였습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('게시물 삭제에 실패하였습니다.');
        });
    }
}
