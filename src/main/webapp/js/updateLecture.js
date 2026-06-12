document.addEventListener('DOMContentLoaded', () => {
	const applyCategoryStyle = (input) => {
		const categorySection = input.closest('.study');
		if (!categorySection) {
			return;
		}

		categorySection.querySelectorAll('.category').forEach(label => {
			label.classList.remove('selected');
			label.classList.remove('selected-category');
		});

		const label = input.closest('.category');
		if (label) {
			label.classList.add('selected');
			label.classList.add('selected-category');
		}
	};

	document.querySelectorAll('input[type="radio"][name="category"]').forEach(input => {
		if (input.checked) {
			applyCategoryStyle(input);
		}

		input.addEventListener('change', () => applyCategoryStyle(input));
	});

	document.querySelectorAll('input[type="checkbox"][name="dayOfWeek"]').forEach(input => {
		const label = input.closest('.category');
		if (label && input.checked) {
			label.classList.add('selected-category');
		}

		input.addEventListener('change', () => {
			const changedLabel = input.closest('.category');
			if (changedLabel) {
				changedLabel.classList.toggle('selected-category', input.checked);
			}
		});
	});

	document.querySelectorAll('form').forEach(form => {
		form.addEventListener('submit', event => {
			const dayInputs = form.querySelectorAll('input[type="checkbox"][name="dayOfWeek"]');
			if (dayInputs.length === 0) {
				return;
			}

			const hasCheckedDay = Array.from(dayInputs).some(input => input.checked);
			if (!hasCheckedDay) {
				event.preventDefault();
				alert('정기 모임 요일을 하나 이상 선택해주세요.');
			}
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

	const scheduleIdInput = newArticle.querySelector('#scheduleId');
	scheduleIdInput.name = `schedule[${scheduleCount}][scheduleId]`;
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

		const scheduleIdInput = article.querySelector('#scheduleId');
		scheduleIdInput.name = `schedule[${index}][scheduleId]`;

	});


	// scheduleCount는 현재의 일정 개수로 갱신
	scheduleCount = articles.length;
	document.getElementById('scheduleCountInput').value = scheduleCount;

}
