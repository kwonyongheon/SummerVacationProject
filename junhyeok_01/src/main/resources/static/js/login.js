const urlParams = new URLSearchParams(window.location.search);
if (urlParams.get('error')) {
    alert('로그인 실패! 아이디나 비밀번호를 확인하세요.');
}