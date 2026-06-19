document.addEventListener("DOMContentLoaded", () => {
	const calendarHeader = document.querySelector("#calendarHeader .month span");
	const calendarYear = document.querySelector("#calendarHeader .year");
	const calendarBody = document.querySelector(".calendarTable tbody");
	const selectedDateInput = document.getElementById("selectedDate");
	const eventsInput = document.getElementById("eventsData");
	const calendarEventsScript = document.getElementById("calendarEventsJson");

	if (!calendarHeader || !calendarYear || !calendarBody || !selectedDateInput) {
		return;
	}

	const selectedDateValue = selectedDateInput.value || "";
	const eventDates = new Set(((eventsInput && eventsInput.value) || "").match(/\d{4}-\d{2}-\d{2}/g) || []);
	const calendarEventsByDate = buildCalendarEventMap((calendarEventsScript && calendarEventsScript.textContent) || "");
	let currentDate = selectedDateValue ? parseLocalDate(selectedDateValue) : new Date();

	function parseLocalDate(value) {
		const parts = value.split("-").map(Number);
		if (parts.length !== 3 || parts.some(Number.isNaN)) {
			return new Date();
		}
		return new Date(parts[0], parts[1] - 1, parts[2]);
	}

	function buildCalendarEventMap(rawJson) {
		const map = new Map();
		if (!rawJson || !rawJson.trim()) {
			return map;
		}

		try {
			const events = JSON.parse(rawJson);
			events.forEach((item) => {
				if (!item.date) {
					return;
				}
				if (!map.has(item.date)) {
					map.set(item.date, []);
				}
				map.get(item.date).push(item);
			});
		} catch (error) {
			console.warn("calendar event data parse failed", error);
		}
		return map;
	}

	function contextPath() {
		const firstSegment = window.location.pathname.split("/").filter(Boolean)[0];
		return firstSegment ? `/${firstSegment}` : "";
	}

	function itemDetailUrl(type, id, date) {
		const lectureElement = document.getElementById("lectureId");
		const groupElement = document.getElementById("groupId");
		const lectureId = lectureElement ? lectureElement.value : "";
		const groupId = groupElement ? groupElement.value : "";
		if (!id || (!lectureId && !groupId)) {
			return "#";
		}

		const path = lectureId ? "/lecture/itemDetail" : "/study/itemDetail";
		const ownerParam = lectureId ? `lectureId=${encodeURIComponent(lectureId)}` : `groupId=${encodeURIComponent(groupId)}`;
		const selectedDate = date || selectedDateInput.value || selectedDateValue;
		return `${contextPath()}${path}?${ownerParam}&selectedDate=${encodeURIComponent(selectedDate)}&type=${encodeURIComponent(type || "event")}&id=${encodeURIComponent(id)}`;
	}

	function createEventChip(item) {
		const chip = document.createElement("a");
		chip.className = `calendar-chip calendar-chip--${item.category || "event"}`;
		chip.textContent = `${item.label || "일정"} · ${item.title || ""}`;
		chip.title = [item.label, item.title, item.meta].filter(Boolean).join(" · ");
		chip.href = itemDetailUrl(item.category, item.id, item.date);
		chip.addEventListener("click", (event) => {
			event.stopPropagation();
		});
		return chip;
	}

	function renderCalendar(date) {
		const year = date.getFullYear();
		const month = date.getMonth();

		calendarHeader.textContent = month + 1;
		calendarYear.textContent = year;
		calendarBody.innerHTML = "";

		const firstDay = new Date(year, month, 1).getDay();
		const lastDate = new Date(year, month + 1, 0).getDate();
		let day = 1;

		for (let i = 0; i < 6; i++) {
			const row = document.createElement("tr");

			for (let j = 0; j < 7; j++) {
				const cell = document.createElement("td");

				if (i === 0 && j < firstDay) {
					cell.classList.add("previousMonth");
					cell.textContent = new Date(year, month, -(firstDay - j - 1)).getDate();
				} else if (day > lastDate) {
					cell.classList.add("nextMonth");
					cell.textContent = day - lastDate;
					day++;
				} else {
					const cellDate = `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
					const dateNumber = document.createElement("span");
					dateNumber.className = "calendar-date-number";
					dateNumber.textContent = day;

					cell.classList.add("currentMonth");
					cell.dataset.day = day;
					cell.appendChild(dateNumber);

					const today = new Date();
					if (day === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
						cell.classList.add("today-cell");
						dateNumber.id = "today";
					}

					if (cellDate === selectedDateValue) {
						cell.classList.add("selected");
					}

					const calendarItems = calendarEventsByDate.get(cellDate) || [];
					if (calendarItems.length > 0) {
						const eventsWrapper = document.createElement("div");
						eventsWrapper.className = "calendar-events";
						calendarItems.slice(0, 2).forEach((item) => {
							eventsWrapper.appendChild(createEventChip(item));
						});
						if (calendarItems.length > 2) {
							const more = document.createElement("span");
							more.className = "calendar-chip calendar-chip--more";
							more.textContent = `+${calendarItems.length - 2}`;
							eventsWrapper.appendChild(more);
						}
						cell.appendChild(eventsWrapper);
					} else if (eventDates.has(cellDate)) {
						const dot = document.createElement("span");
						dot.className = "event-dot";
						cell.appendChild(dot);
					}

					day++;
				}
				row.appendChild(cell);
			}
			calendarBody.appendChild(row);

			if (day > lastDate) {
				break;
			}
		}
	}

	const previousMonthIcon = document.getElementById("previousMonthIcon");
	if (previousMonthIcon) {
		previousMonthIcon.addEventListener("click", () => {
			currentDate.setMonth(currentDate.getMonth() - 1);
			renderCalendar(currentDate);
		});
	}

	const nextMonthIcon = document.getElementById("nextMonthIcon");
	if (nextMonthIcon) {
		nextMonthIcon.addEventListener("click", () => {
			currentDate.setMonth(currentDate.getMonth() + 1);
			renderCalendar(currentDate);
		});
	}

	const calendarTable = document.querySelector(".calendarTable");
	if (calendarTable) {
		calendarTable.addEventListener("click", (event) => {
			const cell = event.target.closest("td.currentMonth");
			if (!cell) {
				return;
			}

			const selectedDay = cell.dataset.day;
			const year = calendarYear.textContent;
			const month = calendarHeader.textContent;
			const fullDate = `${year}-${String(month).padStart(2, "0")}-${String(selectedDay).padStart(2, "0")}`;

			const previouslySelected = document.querySelector(".calendarTable .selected");
			if (previouslySelected) {
				previouslySelected.classList.remove("selected");
			}
			cell.classList.add("selected");
			selectedDateInput.value = fullDate;
			document.getElementById("dateForm").submit();
		});
	}

	renderCalendar(currentDate);
});
