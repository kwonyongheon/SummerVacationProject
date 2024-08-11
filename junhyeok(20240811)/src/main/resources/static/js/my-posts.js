// 게시물 검색 기능
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

// 게시물 삭제 기능
function deletePost(postId) {
    if (confirm('정말로 이 게시물을 삭제하시겠습니까?')) {
        fetch(`/gallery/post/${postId}/delete`, {
            method: 'POST', // 'DELETE' 대신 'POST'로 변경
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('게시물이 삭제되었습니다.');
                location.reload(); // 삭제 후 페이지 새로고침
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

// 특정 갤러리 내 내 게시물 전체 삭제 기능
function deleteMyPostsInGallery(galleryId) {
    if (confirm('정말로 이 갤러리 내 모든 게시물을 삭제하시겠습니까?')) {
        fetch(`/gallery/${galleryId}/delete-my-posts`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('갤러리 내 모든 게시물이 삭제되었습니다.');
                location.reload(); // 삭제 후 페이지 새로고침
            } else {
                alert('갤러리 내 모든 게시물 삭제에 실패하였습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('갤러리 내 모든 게시물 삭제에 실패하였습니다.');
        });
    }
}


// 내 게시물들 전체 삭제 기능
function deleteMyPosts() {
    if (confirm('정말로 모든 게시물을 삭제하시겠습니까?')) {
        fetch(`/gallery/delete-my-posts`, {
            method: 'POST', // POST 요청으로 변경
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('모든 게시물이 삭제되었습니다.');
                location.reload(); // 삭제 후 페이지 새로고침
            } else {
                alert('모든 게시물 삭제에 실패하였습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('모든 게시물 삭제에 실패하였습니다.');
        });
    }
}


// 전체 게시물 삭제 기능
function deleteAllPosts() {
    if (confirm('정말로 모든 게시물을 삭제하시겠습니까?')) {
        fetch(`/gallery/delete-all-posts`, {
            method: 'POST', // 'DELETE' 대신 'POST'로 변경
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('모든 게시물이 삭제되었습니다.');
                location.reload(); // 삭제 후 페이지 새로고침
            } else {
                alert('모든 게시물 삭제에 실패하였습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('모든 게시물 삭제에 실패하였습니다.');
        });
    }
}
