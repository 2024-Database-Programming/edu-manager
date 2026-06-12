// 선택된 체크박스 개수를 추적하는 변수
let selectedCount = 0;

function updateCategoryStyle(checkbox) {
	const label = checkbox.closest('label'); // 체크박스를 감싸고 있는 label을 찾기



	// 체크박스가 선택되었을 때
	if (checkbox.checked) {
		// 선택된 체크박스가 3개 이상이면 추가 선택을 막기
		if (selectedCount >= 3) {
			alert("3개 이하로만 선택 가능합니다.");
			checkbox.checked = false; // 체크박스 해제
			return;
		}
		selectedCount++; // 선택된 체크박스 개수 증가
		label.classList.add('selected');
		/*label.style.backgroundColor = '#1E2A7C'; // 체크된 상태 배경색
		label.style.color = 'white'; // 체크된 상태 텍스트 색상*/
	} else {
		selectedCount--; // 선택 해제된 체크박스 개수 감소
		label.classList.remove('selected');
		/*label.style.backgroundColor = 'white'; // 체크되지 않은 상태 배경색
		label.style.color = '#a8a7a7'; // 체크되지 않은 상태 텍스트 색상*/
	}


}