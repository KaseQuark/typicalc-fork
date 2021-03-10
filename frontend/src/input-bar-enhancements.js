function changeEvent(element) {
	// notify Vaadin
	document.getElementById("inputField").__dataValue.value = element.value;
	const evt = new Event("change");
	element.dispatchEvent(evt, { 'bubbles': true });
}

window.lambdaButtonListener = (buttonID, inputID) => {
	document.getElementById(buttonID).addEventListener('click', e => {
		const area = document.getElementById(inputID).shadowRoot.querySelector('input');
		let start = area.selectionStart;
		area.value = [area.value.slice(0, start), 'λ', area.value.slice(start)].join('');
		area.selectionStart = ++start;
		area.selectionEnd = start;
		area.focus();
		changeEvent(area);
	});
}
window.backslashListener = (inputID) => {
	document.getElementById(inputID).addEventListener('keyup', e => {
		const area = e.target.shadowRoot.querySelector('input');
		if (area.value.indexOf('\\') >= 0) {
			const start = area.selectionStart;
			const end = area.selectionEnd;
			area.value = area.value.replaceAll('\\', 'λ');
			area.selectionStart = start;
			area.selectionEnd = end;
			changeEvent(area);
		}
	});
}
