function openTab(tabName) {
	document.querySelectorAll('.tab, .tabs').forEach(function(el) {
		el.classList.remove('on');
	});
	document.getElementById(tabName).classList.add('on');
	document.querySelector('.tabs[onclick="openTab(\'' + tabName + '\')"]').classList.add('on');

	const tabs = document.querySelectorAll('.tabs');
	tabs.forEach(tab => tab.classList.remove('on'));

	if (tabName === 'Tab1') {
		tabs[0].classList.add('on');
		document.getElementById('myStudyGroups_hr1').style.display = 'block';
		document.getElementById('myStudyGroups_hr2').style.display = 'none';
	} else if (tabName === 'Tab2') {
		tabs[1].classList.add('on');
		document.getElementById('myStudyGroups_hr2').style.display = 'block';
		document.getElementById('myStudyGroups_hr1').style.display = 'none';
	}
}
