document.getElementById('create-btn').addEventListener("click", function (event){
    event.preventDefault();

    let name = document.getElementById('name').value;
    let description = document.getElementById('description').value;

    fetch('/MinorGallery/create', {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: name,
            description: description
        })
    })
        .then(response => response.text())  // 서버에서 받은 응답을 텍스트로 변환
        .then(result => {
            if (result === "ok") {
                alert("갤러리가 성공적으로 생성되었습니다.");
                window.location.href = '/MinorGallery/list'
            } else {
                alert(result);
            }
        })
        .catch(error => {
            alert("갤러리 생성 중 오류가 발생했습니다.");
        });

})

document.getElementById('galleryDropdown').addEventListener('click', function(event) {
    event.preventDefault();
    window.location.href = this.href;
});

document.getElementById('memberGalleryDropdown').addEventListener('click', function(event) {
    event.preventDefault();
    window.location.href = this.href;
});