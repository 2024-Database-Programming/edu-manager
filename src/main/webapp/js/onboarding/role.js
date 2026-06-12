let selectedRole = '';

function selectRole(role) {
	selectedRole = role;

	const teacherLabel = document.getElementById('teacherLabel');
	const studentLabel = document.getElementById('studentLabel');

	// Apply selected style
	if (role === 'TEACHER') {
		studentLabel.classList.remove('selected');
		teacherLabel.classList.add('selected');
	} else if (role === 'STUDENT') {
		teacherLabel.classList.remove('selected');
		studentLabel.classList.add('selected');
	}
}
