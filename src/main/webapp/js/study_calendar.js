document.addEventListener("DOMContentLoaded", () => {
	const calendarHeader = document.querySelector("#calendarHeader .month span");
	const calendarYear = document.querySelector("#calendarHeader .year");
	const calendarBody = document.querySelector(".calendarTable tbody");

	// 서버에서 받아온 selectedDate
	const selectedDateValue = document.getElementById("selectedDate").value;

	// 초기 날짜 설정: selectedDate가 있으면 해당 날짜로 설정, 없으면 현재 날짜
	let currentDate = selectedDateValue
		? new Date(selectedDateValue) // selectedDate를 기준으로 초기 날짜 설정
		: new Date();

	// 달력을 렌더링하는 함수
	function renderCalendar(date) {
		const year = date.getFullYear();
		const month = date.getMonth();

		// 월과 연도 업데이트
		calendarHeader.textContent = month + 1; // JavaScript 월은 0부터 시작
		calendarYear.textContent = year;

		// 달력 테이블 초기화
		calendarBody.innerHTML = "";

		// 첫날과 마지막 날 계산
		const firstDay = new Date(year, month, 1).getDay();
		const lastDate = new Date(year, month + 1, 0).getDate();

		let day = 1; // 일 시작

		for (let i = 0; i < 6; i++) {
			const row = document.createElement("tr");

			for (let j = 0; j < 7; j++) {
				const cell = document.createElement("td");

				// 첫 주: 첫날의 요일부터 시작
				if (i === 0 && j < firstDay) {
					cell.classList.add("previousMonth");
					cell.textContent = new Date(year, month, -(firstDay - j - 1)).getDate();
				} else if (day > lastDate) {
					// 마지막 날짜 이후의 날짜 표시
					cell.classList.add("nextMonth");
					cell.textContent = day - lastDate;
					day++;
				} else {
					// 현재 월 날짜 표시
					cell.textContent = day;

					// 오늘 날짜 강조
					if (
						day === new Date().getDate() &&
						month === new Date().getMonth() &&
						year === new Date().getFullYear()
					) {
						cell.id = "today";
					}

					cell.classList.add("currentMonth");

					// selectedDate와 일치하는 날짜에 'selected' 클래스 추가
					const cellDate = `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
					if (cellDate === selectedDateValue) {
						cell.classList.add("selected");
					}

					day++;
				}
				row.appendChild(cell);
			}
			calendarBody.appendChild(row);

			// 마지막 날짜를 넘으면 루프 종료
			if (day > lastDate) break;
		}
	}

	// 이전 달 버튼
	document.getElementById("previousMonthIcon").addEventListener("click", () => {
		currentDate.setMonth(currentDate.getMonth() - 1);
		renderCalendar(currentDate);
	});

	// 다음 달 버튼
	document.getElementById("nextMonthIcon").addEventListener("click", () => {
		currentDate.setMonth(currentDate.getMonth() + 1);
		renderCalendar(currentDate);
	});

	// 날짜 클릭 이벤트 (이벤트 위임 방식)
	document.querySelector(".calendarTable").addEventListener("click", (event) => {
		if (event.target.tagName.toLowerCase() === "td" && event.target.classList.contains("currentMonth")) {
			let selectedDay = event.target.textContent.trim();
			let year = calendarYear.textContent;
			let month = calendarHeader.textContent;

			let fullDate = year + "-" + String(month).padStart(2, "0") + "-" + String(selectedDay).padStart(2, "0");

			// 이전 선택된 날짜의 'selected' 클래스 제거
			const prevSelected = document.querySelector(".calendarTable .selected");
			if (prevSelected) {
				prevSelected.classList.remove("selected");
			}

			// 현재 선택된 날짜에 'selected' 클래스 추가
			event.target.classList.add("selected");

			// 폼의 hidden input에 값 설정
			document.getElementById("selectedDate").value = fullDate;

			// 폼 제출
			document.getElementById("dateForm").submit();
		}
	});

	// 초기 달력 렌더링
	renderCalendar(currentDate);
});
