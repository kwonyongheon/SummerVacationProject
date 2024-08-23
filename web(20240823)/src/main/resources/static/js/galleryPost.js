document.getElementById('galleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.getElementById('memberGalleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.addEventListener('DOMContentLoaded', function () {
    let editCommentId = null;

    // 수정 버튼 클릭 시 모달 표시
    document.querySelectorAll('.edit-comment-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            editCommentId = this.getAttribute('data-id');
            document.getElementById('editCommentContent').value = this.getAttribute('data-content');

            const editCommentModal = new bootstrap.Modal(document.getElementById('editCommentModal'));
            editCommentModal.show();
        });
    });

    // 저장 버튼 클릭 시 AJAX 요청
    document.getElementById('saveCommentBtn').addEventListener('click', function () {
        const content = document.getElementById('editCommentContent').value;
        const password = document.getElementById('commentPasswordInput').value; // 비밀번호 입력 필드 가져오기

        if (!password) {
            alert("비밀번호를 입력해 주세요.");
            return;
        }

        console.log('Saving comment with ID:', editCommentId);
        console.log('Content to save:', content);
        console.log('Password entered:', password);

        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/Gallery/post/comment/' + editCommentId + '/edit', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function () {
            console.log('ReadyState:', xhr.readyState);
            console.log('Status:', xhr.status);
            if (xhr.readyState === 4 && xhr.status === 200) {
                console.log('Response:', xhr.responseText);
                location.reload(); // 전체 페이지를 새로고침
            } else if (xhr.readyState === 4 && xhr.status !== 200) {
                console.error('Failed to save comment:', xhr.responseText);
                alert('댓글 수정에 실패했습니다.');
            }
        };
        xhr.send('content=' + encodeURIComponent(content) + '&password=' + encodeURIComponent(password));
    });

    // 댓글 삭제 버튼 클릭 시
    document.querySelectorAll('.delete-comment-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-id');
            const password = prompt("비밀번호를 입력해 주세요:");

            if (!password) {
                alert("비밀번호를 입력해 주세요.");
                return;
            }

            if (!confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
                return;
            }

            console.log('Deleting comment with ID:', commentId);
            console.log('Password entered:', password);

            var xhr = new XMLHttpRequest();
            xhr.open('DELETE', '/Gallery/post/comment/' + commentId + '/delete', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function () {
                console.log('ReadyState:', xhr.readyState);
                console.log('Status:', xhr.status);
                if (xhr.readyState === 4 && xhr.status === 204) {
                    console.log('Response:', xhr.responseText);
                    location.reload(); // 전체 페이지를 새로고침하여 삭제된 댓글 반영
                } else if (xhr.readyState === 4 && xhr.status !== 204) {
                    console.error('Failed to delete comment:', xhr.responseText);
                    alert('댓글 삭제에 실패했습니다.');
                }
            };
            xhr.send('password=' + encodeURIComponent(password));
        });
    });
});



