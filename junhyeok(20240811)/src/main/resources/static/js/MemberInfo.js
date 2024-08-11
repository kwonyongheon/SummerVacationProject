const modifyButton = document.getElementById('modifyMemberInfo-btn');

if(modifyButton) {
    modifyButton.addEventListener('click', event => {
        let id = document.getElementById('id').value;

        fetch(`/modify/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body:JSON.stringify({
                nickName: document.getElementById("nickName").value,
                gameName: document.getElementById("gameName").value,
                tagLine: document.getElementById("tagLine").value,
                birth: document.getElementById("birth").value
            })
        })
            .then(response => {
                if (response.ok) {
                    alert("회원 정보가 성공적으로 변경되었습니다.");
                    window.location.href = '/index';
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message || "회원 정보 변경에 실패했습니다.");
                    });
                }
            })
            .catch(error => {
                alert(error.message);
            });
    });
}

document.getElementById('changePW-btn').addEventListener('click', function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작을 막음

    let currentPassword = document.getElementById('currentPassword').value;
    let newPassword = document.getElementById('newPassword').value;
    let confirmPassword = document.getElementById('confirmPassword').value;

    fetch('/change-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            currentPassword: currentPassword,
            newPassword: newPassword,
            confirmPassword: confirmPassword
        })
    })
        .then(response => response.json().then(data => ({ status: response.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                alert(body.message); // 성공 메시지 알림
                window.location.href = '/login'; // 로그인 페이지로 리다이렉트
            } else {
                alert(body.message); // 실패 메시지 알림
            }
        })
        .catch(error => {
            alert("서버와의 통신 중 오류가 발생했습니다."); // 네트워크 오류 처리
        });
});

document.getElementById('withdrawalForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작을 막음

    let checkPassword = document.getElementById('CheckPassword').value;

    // 서버로 비밀번호 확인 요청 보내기
    fetch('/check-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ password: checkPassword })
    })
        .then(response => {
            if (response.status === 200) {
                // 비밀번호 확인 성공 시 안내 메시지와 탈퇴 버튼 표시
                document.getElementById('withdrawal-message').classList.remove('d-none');
                // 기존 폼 및 확인 버튼 숨기기
                document.getElementById('withdrawalForm').classList.add('d-none');
            } else {
                alert("비밀번호를 다시 확인하세요."); // 실패 메시지 알림
            }
        })
        .catch(() => {
            alert("서버와의 통신 중 오류가 발생했습니다."); // 네트워크 오류 처리
        });
});

const Withdrawal = document.getElementById('confirmWithdrawal-btn');

if (Withdrawal) {
    Withdrawal.addEventListener("click", event => {
        let id = document.getElementById('id').value;

        if (confirm("정말로 회원 탈퇴를 진행하시겠습니까?")) {
            fetch(`/withDrawal/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert("회원 탈퇴가 완료되었습니다.");
                        window.location.href = "/login";
                    } else {
                        return response.json().then(error => {
                            alert(`${error.message}`);
                        });
                    }
                })
                .catch(error => {
                    console.error('에러:', error);
                    alert("서버와의 연결에 실패했습니다.");
                });
        }
    });
}








