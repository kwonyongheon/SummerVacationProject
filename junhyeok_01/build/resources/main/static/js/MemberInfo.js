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