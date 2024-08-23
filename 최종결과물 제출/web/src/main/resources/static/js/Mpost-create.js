document.getElementById('galleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.getElementById('memberGalleryDropdown').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.addEventListener("DOMContentLoaded", function() {
    // URL에서 쿼리 파라미터를 확인합니다.
    const urlParams = new URLSearchParams(window.location.search);

    if (urlParams.has('error') && urlParams.get('error') === 'true') {
        alert("게시물 생성에 실패했습니다. 다시 시도해주세요.");
    }

    if (urlParams.has('create') && urlParams.get('create') === 'true') {
        alert("게시물이 성공적으로 생성되었습니다.");
        const galleryId = urlParams.get('galleryId');
        if (galleryId) {
            window.location.href = `/MinorGallery/${galleryId}`;
        }
    }
});