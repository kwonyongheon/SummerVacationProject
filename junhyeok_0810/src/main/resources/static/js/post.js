function toggleReplyForm(commentId) {
    const replyForm = document.getElementById(`reply-form-${commentId}`);
    replyForm.classList.toggle('d-none');
}

function submitComment() {
    // 서버에 댓글 작성 요청을 보냅니다
}

function submitReply(commentId) {
    const content = document.getElementById(`reply-content-${commentId}`).value;
    if (content.trim()) {
        $.ajax({
            type: 'POST',
            url: `/gallery/post/comment/${commentId}/reply`,
            data: { content: content },
            success: function (response) {
                // 성공적으로 대댓글을 작성한 후 페이지를 새로고침하거나 목록을 업데이트합니다.
                location.reload();
            },
            error: function (error) {
                console.error('대댓글 작성 실패:', error);
            }
        });
    }
}

function recommendComment(commentId) {
    $.ajax({
        type: 'POST',
        url: `/gallery/post/comment/${commentId}/recommend`,
        success: function (response) {
            // 추천수 업데이트
            location.reload();
        },
        error: function (error) {
            console.error('댓글 추천 실패:', error);
        }
    });
}

function recommendReply(replyId) {
    $.ajax({
        type: 'POST',
        url: `/gallery/post/reply/${replyId}/recommend`,
        success: function (response) {
            // 추천수 업데이트
            location.reload();
        },
        error: function (error) {
            console.error('대댓글 추천 실패:', error);
        }
    });
}

function deleteComment(commentId) {
    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
        $.ajax({
            type: 'POST',
            url: `/gallery/post/comment/${commentId}/delete`,
            success: function (response) {
                // 댓글 삭제 후 페이지를 새로고침하거나 목록을 업데이트합니다.
                location.reload();
            },
            error: function (error) {
                console.error('댓글 삭제 실패:', error);
            }
        });
    }
}

function deleteReply(replyId) {
    if (confirm('정말로 이 대댓글을 삭제하시겠습니까?')) {
        $.ajax({
            type: 'POST',
            url: `/gallery/post/reply/${replyId}/delete`,
            success: function (response) {
                // 대댓글 삭제 후 페이지를 새로고침하거나 목록을 업데이트합니다.
                location.reload();
            },
            error: function (error) {
                console.error('대댓글 삭제 실패:', error);
            }
        });
    }
}
