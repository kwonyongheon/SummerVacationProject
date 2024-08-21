document.addEventListener('DOMContentLoaded', function () {
    // 갤러리 생성 폼 제출 처리
    document.getElementById('createGalleryForm').addEventListener('submit', function (e) {
        e.preventDefault();
        var title = document.getElementById('galleryTitle').value.trim();
        var description = document.getElementById('galleryDescription').value.trim();

        if (title.length === 0 || description.length === 0) {
            alert('제목과 설명을 모두 입력해주세요.');
            return;
        }

        if (title.length > 20) {
            alert('제목은 20글자 이내로 입력해주세요.');
            return;
        }

        if (description.length > 20) {
            alert('설명은 20글자 이내로 입력해주세요.');
            return;
        }

        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/member-galleries/create', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    alert(xhr.responseText);
                    if (xhr.responseText === '갤러리를 생성하였습니다.') {
                        window.location.reload();
                    }
                } else {
                    alert('갤러리 생성 중 오류가 발생하였습니다.');
                }
            }
        };
        var formData = 'title=' + encodeURIComponent(title) + '&description=' + encodeURIComponent(description);
        xhr.send(formData);
    });

    // 갤러리 삭제 버튼 클릭 처리
    document.querySelectorAll('.delete-gallery-btn').forEach(function(button) {
        button.addEventListener('click', function() {
            var galleryId = this.getAttribute('data-id');
            document.getElementById('deleteGalleryButton').setAttribute('data-id', galleryId);
        });
    });

    // 갤러리 삭제 처리
    document.getElementById('deleteGalleryButton').addEventListener('click', function () {
        var galleryId = this.getAttribute('data-id');
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/member-galleries/delete/' + galleryId, true); // DELETE 대신 POST 사용
        xhr.setRequestHeader('Content-Type', 'application/json'); // POST 요청에 Content-Type 추가
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    alert(xhr.responseText);
                    if (xhr.responseText === '갤러리를 삭제하였습니다.') {
                        window.location.reload();
                    }
                } else {
                    alert('갤러리 삭제 중 오류가 발생하였습니다.');
                }
            }
        };
        xhr.send(); // 데이터는 필요하지 않음
    });
});
