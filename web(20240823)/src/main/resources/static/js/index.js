// 갤러리 메뉴 탭 클릭 시 페이지 이동
document.getElementById('navbarDropdownMenuLink').addEventListener('click', function(event) {
    // 기본 드롭다운 동작을 막음
    event.preventDefault();
    // 해당 페이지로 이동
    window.location.href = this.href;
});

// 회원갤러리 메뉴 탭 클릭 시 페이지 이동
document.getElementById('navbarDropdownMenuLink2').addEventListener('click', function(event) {
    // 기본 드롭다운 동작을 막음
    event.preventDefault();
    // 해당 페이지로 이동
    window.location.href = this.href;
});

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