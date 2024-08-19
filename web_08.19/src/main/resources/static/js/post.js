
ClassicEditor
    .create(document.querySelector('#editor'), {
        // 이미지 업로드
        ckfinder: {
            uploadUrl: '/upload-image' // 이미지 업로드를 처리할 서버 측 URL
        },
        // 글씨체 선택
        toolbar: [
            'heading', '|', 'bold', 'italic', 'link', 'bulletedList', 'numberedList', 'blockQuote',
            '|', 'undo', 'redo', '|', 'textColor', 'textBackgroundColor'
        ],
        // 텍스트 색상을 검은색으로 기본 설정
        fontColor: {
            colors: [
                {
                    color: '#000000',
                    label: 'Black'
                }
            ]
        },
    })
    .catch(error => {
    console.error(error);
});




