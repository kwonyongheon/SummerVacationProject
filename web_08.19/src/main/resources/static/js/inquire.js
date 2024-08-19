// ysh
document.addEventListener("DOMContentLoaded", function () {
    const inquiryTypeSelect = document.getElementById("inquiryTypeSelect");
    const galleryRequestFields = document.getElementById("galleryRequestFields");
    const otherInquiryFields = document.getElementById("otherInquiryFields");
    const inquiryForm = document.getElementById("inquiryForm");

    // Inquiry Type Select Event
    inquiryTypeSelect.addEventListener("change", function () {
        if (this.value === "GALLERY_REQUEST") {
            galleryRequestFields.style.display = "block";
            otherInquiryFields.style.display = "none";
        } else if (this.value === "OTHER_INQUIRY") {
            galleryRequestFields.style.display = "none";
            otherInquiryFields.style.display = "block";
        } else {
            galleryRequestFields.style.display = "none";
            otherInquiryFields.style.display = "none";
        }
    });

    // Form Submit Event
    inquiryForm.addEventListener("submit", function (event) {
        const inquiryType = document.querySelector('select[name="inquiryType"]').value;

        if (inquiryType === "GALLERY_REQUEST") {
            const galleryName = document.getElementById("gallery-name").value.trim();
            const requestReason = document.getElementById("request-reason").value.trim();

            if (!galleryName || !requestReason) {
                alert("갤러리 이름과 개설 요청 이유를 모두 입력해 주세요.");
                event.preventDefault(); // 폼 제출 방지
            } else {
                const confirmation = confirm("갤러리 개설을 요청하시겠습니까?");
                if (!confirmation) {
                    event.preventDefault(); // 폼 제출 방지
                }
            }
        } else if (inquiryType === "OTHER_INQUIRY") {
            const inquiryTitle = document.getElementById("inquiry-title").value.trim();
            const inquiryContent = document.getElementById("inquiry-content").value.trim();

            if (!inquiryTitle || !inquiryContent) {
                alert("문의 제목과 내용을 모두 입력해 주세요.");
                event.preventDefault(); // 폼 제출 방지
            } else {
                const confirmation = confirm("문의 접수를 요청하시겠습니까?");
                if (!confirmation) {
                    event.preventDefault(); // 폼 제출 방지
                }
            }
        }
    });
});

// 답변 내역 로드
// Load Responses Function
function loadResponses() {
    fetch('/inquiries/reply/passing')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.length === 0) {
                responsesList.innerHTML = '<p>답변이 없습니다.</p>';
            } else {
                let html = '';
                data.forEach(answer => {
                    html += `
                            <div class="list-group-item list-group-item-action custom-bg-white mb-2">
                                <h5 class="mb-1 custom-red">${answer.title}</h5>
                                <p class="mb-1">${answer.content}</p>
                                <small>작성일: ${new Date(answer.createdAt).toLocaleDateString()}</small>
                            </div>`;
                });
                responsesList.innerHTML = html;
            }
        })
        .catch(error => {
            console.error('Error loading responses:', error);
            responsesList.innerHTML = '<p>답변을 로드하는 중 오류가 발생했습니다.</p>';
        });

}

// Button Click Event to Load Responses
if (loadResponsesButton) {
    loadResponsesButton.addEventListener("click", function() {
        loadResponses();
    })
}


