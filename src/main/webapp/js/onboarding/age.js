function selectAge(ageGroup) {
    // 버튼 스타일 업데이트
    const buttons = document.querySelectorAll('.age-button');
    buttons.forEach(button => button.classList.remove('selected'));
    
    const selectedButton = document.getElementById(`${ageGroup}Label`);
    if (selectedButton) {
        selectedButton.classList.add('selected');
   }
}