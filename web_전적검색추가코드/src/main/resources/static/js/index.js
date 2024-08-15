    document.getElementById('searchForm').addEventListener('submit', function(event) {
        const searchValue = document.getElementById('search').value;
        const [gameName, tagLine] = searchValue.split('#');

        if (!gameName || !tagLine) {
            alert('플레이어 이름과 태그를 올바르게 입력하세요.');
            event.preventDefault();
            return;
        }

        document.getElementById('gameName').value = gameName.trim();
        document.getElementById('tagLine').value = tagLine.trim();
    });