document.getElementById('galleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.getElementById('memberGalleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.addEventListener('DOMContentLoaded', function () {
    var editGalleryModal = document.getElementById('editGalleryModal');

    editGalleryModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget; // 버튼
        var galleryId = button.getAttribute('data-gallery-id');
        var galleryName = button.getAttribute('data-gallery-name');
        var galleryDetails = button.getAttribute('data-gallery-details');
        var galleryNameInput = editGalleryModal.querySelector('#galleryName');
        var galleryDetailsInput = editGalleryModal.querySelector('#galleryDetails');
        var InputGalleryId = editGalleryModal.querySelector('#galleryId');

        InputGalleryId.value = galleryId;
        galleryNameInput.value = galleryName;
        galleryDetailsInput.value = galleryDetails;
    });
});

document.getElementById('editGallery').addEventListener('click', function() {
    const galleryId = document.getElementById('galleryId').value;
    const galleryName = document.getElementById('galleryName').value;
    const galleryDetails = document.getElementById('galleryDetails').value;

    fetch(`/MinorGallery/modify/${galleryId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: galleryName,
            description: galleryDetails
        })
    })
        .then(response => {
            if (response.ok) {
                return response.text();  // 서버에서 보낸 메시지(성공 또는 실패 메시지)를 텍스트로 반환
            } else {
                return Promise.reject('갤러리 수정에 실패했습니다.');
            }
        })
        .then(data => {
            // 성공적으로 수정된 경우
            alert(data);
            location.reload();  // 페이지를 새로고침하여 갱신된 내용을 반영
        })
        .catch(error => {
            // 수정 실패한 경우
            alert(error);
        });
});

document.querySelectorAll('#delete').forEach(function(button) {
    button.addEventListener('click', function() {
        // gallery ID 가져오기
        const galleryId = this.getAttribute('data-gallery-id');

        // 확인 창
        if (confirm("정말로 이 갤러리를 삭제하시겠습니까?")) {
            // DELETE 요청 보내기
            fetch(`/MinorGallery/delete/${galleryId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert("갤러리가 성공적으로 삭제되었습니다.");
                        location.reload(); // 페이지 새로고침
                    } else {
                        alert("갤러리 삭제에 실패했습니다.");
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("갤러리 삭제 중 오류가 발생했습니다.");
                });
        }
    });
});





