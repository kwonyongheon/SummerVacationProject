//ysh

let inquiries = []; // 전체 문의 데이터를 저장할 변수

// 사용자 삭제 -ysh
async function deleteUser(memberId) {
    if (confirm('정말로 이 사용자를 삭제하시겠습니까?')) {
        try {
            const response = await fetch(`/admin/delete/${memberId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            const result = await response.text();
            alert(result); // 서버로부터의 응답 메시지를 사용자에게 알림
            location.reload(); // 페이지를 새로 고쳐서 사용자 목록을 갱신합니다.
        } catch (error) {
            console.error('Error:', error);
            alert('삭제 처리 중 오류가 발생했습니다.');
        }
    }
}

// 사용자 검색 -ysh
function searchUsers() {
    const input = document.getElementById('searchInput').value.trim();  // 검색어를 가져옵니다.
    const rows = document.querySelectorAll('#userTableBody .user-row');  // 사용자 행들을 가져옵니다.

    rows.forEach(row => {
        const nickNameCell = row.cells[2];  // 닉네임 열의 인덱스가 2
        const nickNameText = nickNameCell.innerText.trim();  // 닉네임을 가져옵니다.

        // 검색어가 닉네임에 포함되어 있는지 확인합니다.
        if (nickNameText.includes(input)) {
            row.style.display = '';  // 포함되면 행을 표시합니다.
        } else {
            row.style.display = 'none';  // 포함되지 않으면 행을 숨깁니다.
        }
    });
}

// 검색 입력란에서 Enter 키 입력 이벤트 리스너
document.getElementById('searchInput').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault();  // 기본 Enter 키 동작을 방지합니다 (폼 제출 방지)
        searchUsers();  // 검색 실행
    }
});

// 문의 검색 입력란에서 Enter 키 입력 이벤트 리스너 추가
document.getElementById('inquiry-search').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault();  // 기본 Enter 키 동작을 방지합니다 (폼 제출 방지)
        searchInquiries();  // 검색 실행
    }
});

// 사용자 화면 표시(데이터 영어 -> 한글로 변환)
function mapTypeToKorean(type) {
    switch(type) {
        case 'GALLERY_REQUEST':
            return '갤러리 개설 요청';
        case 'OTHER_INQUIRY':
            return '기타 문의';
        default:
            return '알 수 없음';
    }
}

// 특정 닉네임 문의 검색
function searchInquiries() {
    const nickname = document.getElementById('inquiry-search').value;
    fetch(`/admin/inquiries/search?nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(data => {
            inquiries = data; // 검색된 데이터 저장
            displayInquiries(data); // 검색 결과를 표시
        })
        .catch(error => console.error('Error:', error));
}

// 모든 문의 조회
function getAllInquiries() {
    fetch('/admin/inquiries/all')
        .then(response => response.json())
        .then(data => {
            inquiries = data; // 전체 데이터 저장
            displayInquiries(data); // 전체 데이터를 표시
        })
        .catch(error => console.error('Error:', error));
}

// 문의 종류 필터
function filterInquiries(filter) {
    let filteredData = inquiries; // 저장된 전체/검색된 데이터로 필터링
    if (filter === 'GALLERY_REQUEST') {
        filteredData = inquiries.filter(inquiry => inquiry.type === 'GALLERY_REQUEST');
    } else if (filter === 'OTHER_INQUIRY') {
        filteredData = inquiries.filter(inquiry => inquiry.type === 'OTHER_INQUIRY');
    }
    displayInquiries(filteredData); // 필터링된 데이터를 표시
}

// 문의 목록을 테이블에 표시하는 함수
function displayInquiries(data) {
    const inquiryTableBody = document.getElementById('inquiryTableBody');
    inquiryTableBody.innerHTML = ''; // 기존 내용 초기화

    data.forEach(inquiry => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${inquiry.id}</td>
            <td>${inquiry.member.nickName}</td>
            <td>${mapTypeToKorean(inquiry.type)}</td>
            <td>${inquiry.galleryBoardType || ''}</td>
            <td>${inquiry.title}</td>
            <td>${inquiry.content}</td>
            <td><button class="btn btn-primary" onclick="viewInquiryDetail(${inquiry.id})" data-bs-toggle="modal" data-bs-target="#inquiryDetailModal">답변 달기</button></td>
        `;
        inquiryTableBody.appendChild(row);
    });
}

// 모든 문의 초기화
getAllInquiries(); // 초기 로드 시 모든 문의 조회

// 버튼 클릭 이벤트 리스너
document.getElementById('filter-gallery').addEventListener('click', () => filterInquiries('GALLERY_REQUEST'));
document.getElementById('filter-other').addEventListener('click', () => filterInquiries('OTHER_INQUIRY'));

// 모달 데이터 표시 기능
function viewInquiryDetail(id) {
    fetch(`/admin/inquiry/${id}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('inquiryTitle').innerText = data.title;
            document.getElementById('inquiryContent').innerText = data.content;

            // 히든 필드에 문의 ID 설정
            document.getElementById('inquiryId').value = data.id;

            // 갤러리 타입 섹션 처리
            document.getElementById('galleryType').innerText = data.galleryBoardType;


            if (document.getElementById('galleryType') != null) {
                document.getElementById('galleryTypeSection').style.display = 'block'; // 갤러리 타입이 있을 때 섹션을 표시
            } else {
                document.getElementById('galleryTypeSection').style.display = 'none'; // 갤러리 타입이 없을 때 섹션을 숨김
            }

            // 관리자 답변 초기화
            document.getElementById('adminReplyTitle').value = '';
            document.getElementById('adminReplyContent').value = '';
        })
        .catch(error => console.error('Error:', error));
}



//문의 답변 제출
function submitReply() {
    const title = document.getElementById('adminReplyTitle').value.trim();
    const content = document.getElementById('adminReplyContent').value.trim();
    const inquiryId = document.getElementById('inquiryId').value;

    // 제목과 내용이 비어 있으면 알림을 표시하고 전송하지 않음
    if (!title || !content) {
        alert('답변 제목과 내용을 모두 입력해주세요.');
        return;
    }

    fetch(`/admin/inquiries/${inquiryId}/reply`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ title, content, inquiryId }) // inquiryId를 포함
    })
        .then(response => {
            if (response.ok) {
                return response.text(); // 응답 본문을 텍스트로 반환
            } else {
                return response.text().then(text => { // 응답 본문을 읽어 오류 메시지를 반환
                    throw new Error(text);
                });
            }
        })
        .then(result => {
            alert(result); // 서버에서 받은 메시지를 표시
            closeModal();  // 모달 닫기 함수 호출
            activateTab('inquiry-management-tab'); // 탭 전환 처리
        })
        .catch(error => {
            console.error('Error:', error);
            alert('답변 전송 중 오류가 발생했습니다: ' + error.message); // 상세 오류 메시지 표시
        });
}


// 특정 탭을 활성화하고 나머지 탭을 비활성화하는 함수
function activateTab(tabId) {
    const tabIds = [
        'member-management-tab',
        'gallery-management-tab',
        'minor_gallery-management-tab',
        'board-management-tab',
        'post-management-tab',
        'comment-management-tab',
        'inquiry-management-tab'
    ];

    tabIds.forEach(id => {
        const button = document.getElementById(id);
        if (button) {
            button.setAttribute('aria-selected', id === tabId ? 'true' : 'false');
            button.classList.toggle('active', id === tabId); // Bootstrap의 active 클래스 처리
        }
    });

    // Bootstrap 탭 전환 처리
    const tabTrigger = document.querySelector(`#${tabId}`);
    if (tabTrigger) {
        const tab = new bootstrap.Tab(tabTrigger);
        tab.show();
    }
}

// 모달 닫기 함수
function closeModal() {
    const inquiryDetailModal = document.getElementById('inquiryDetailModal');
    if (inquiryDetailModal) {
        const modalInstance = bootstrap.Modal.getInstance(inquiryDetailModal);
        if (modalInstance) {
            modalInstance.hide(); // Bootstrap의 모달 닫기 메서드 사용
        }
    }
}
