window.lambdaButtonListener = (str1, str2) => {
	document.getElementById(str1).addEventListener('click', e => {
		var area = document.getElementById(str2).shadowRoot.querySelector('input');
		var start = area.selectionStart;
		area.value = [area.value.slice(0, start), 'λ', area.value.slice(start)].join('');
		area.selectionStart = ++start;
		area.selectionEnd = start;
		area.focus();
	});
}