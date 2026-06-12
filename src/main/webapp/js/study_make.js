document.addEventListener('DOMContentLoaded', () => {
	// 전역 변수 selectedCategory는 JSP에서 전달됨
	if (typeof selectedCategory !== 'undefined' && selectedCategory) {
		const buttonToSelect = document.querySelector(`.category[data-index="${selectedCategory}"]`);
		const hiddenInput = document.getElementById('categories');

		// 해당 버튼이 존재하면 선택 상태로 설정
		if (buttonToSelect) {
			buttonToSelect.classList.add('selected');
			hiddenInput.value = selectedCategory;
		}
	}
});

// 단일 선택을 처리하는 함수
function selectLectureCategory(button) {
	const hiddenInput = document.getElementById('categories');
	const selectedButton = document.querySelector('.category.selected');

	// 이전 선택 해제
	if (selectedButton) {
		selectedButton.classList.remove('selected');
	}

	// 선택한 버튼 스타일 추가
	button.classList.add('selected');

	// 숨겨진 input 값 업데이트
	hiddenInput.value = button.getAttribute('data-index');
}

document.addEventListener('DOMContentLoaded', () => {
	const categoryButtons = document.querySelectorAll('.category');

	categoryButtons.forEach(button => {
		button.addEventListener('click', () => {
			selectLectureCategory(button);
		});
	});
});
let scheduleCount;

function addSchedule() {
	scheduleCount = document.getElementById('scheduleCountInput').value;
	// 기존 article 요소를 복제
	const originalArticle = document.querySelector('.schedule');
	const newArticle = originalArticle.cloneNode(true);

	// 새로운 article 요소 안의 input 태그들을 찾아 값을 비움
	const inputs = newArticle.querySelectorAll('input');
	inputs.forEach((input) => {
		input.value = ''; // input 값을 비움
	});

	// 새로운 select의 name 속성도 동적으로 변경
	const select = newArticle.querySelector('select');
	select.name = `schedule[${scheduleCount}][day]`;

	const timeInputs = newArticle.querySelectorAll('input[type="time"]');
	timeInputs[0].name = `schedule[${scheduleCount}][startTime]`;
	timeInputs[1].name = `schedule[${scheduleCount}][endTime]`;

	// scheduleCount 증가
	scheduleCount++;
	// hidden input에 scheduleCount 값을 반영
	document.getElementById('scheduleCountInput').value = scheduleCount;

	// 새로운 article 요소를 section 밑에 추가
	const section = document.getElementById('schedule');
	const plusButton = document.getElementById('plus_btn');

	// 버튼 앞에 새 article을 추가
	section.insertBefore(newArticle, plusButton);
}

// 일정 삭제 함수
function deleteSchedule(button) {
	scheduleCount = document.getElementById('scheduleCountInput').value;

	const articles = document.querySelectorAll('.schedule');
	if (articles.length > 1) {
		const article = button.parentElement;
		article.remove();

		// 삭제된 항목의 인덱스를 재조정
		adjustScheduleNames();
	} else {
		alert("최소 한 개의 일정은 필요합니다.");
	}
}

// 일정 이름 재조정 함수
function adjustScheduleNames() {
	const articles = document.querySelectorAll('.schedule');

	articles.forEach((article, index) => {
		const select = article.querySelector('select');
		select.name = `schedule[${index}][day]`;

		const timeInputs = article.querySelectorAll('input[type="time"]');
		timeInputs[0].name = `schedule[${index}][startTime]`;
		timeInputs[1].name = `schedule[${index}][endTime]`;
	});

	// scheduleCount는 현재의 일정 개수로 갱신
	scheduleCount = articles.length;
	document.getElementById('scheduleCountInput').value = scheduleCount;

}
