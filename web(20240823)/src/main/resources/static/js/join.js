document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get('success');
    const error = urlParams.get('error');

    if (success) {
        alert('회원 가입이 완료되었습니다! 로그인 페이지로 이동합니다.');
        window.location.href = '/login';
    } else if (error) {
        alert(error);
    }
});