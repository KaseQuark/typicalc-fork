function changeEvent(element: HTMLElement) {
	// notify Vaadin
	// @ts-ignore
	document.getElementById("inputField")!.__dataValue.value = element.value;
	const evt = new Event("change", { bubbles: true });
	element.dispatchEvent(evt);
}

// @ts-ignore
window.lambdaButtonListener = (buttonID: string, inputID: string) => {
	const button = document.getElementById(buttonID)!;
	const input = document.getElementById(inputID)!;
	button.addEventListener('click', () => {
		const area = input.shadowRoot!.querySelector('input')!;
		let start = area.selectionStart!;
		area.value = [area.value.slice(0, start), 'λ', area.value.slice(start)].join('');
		area.selectionStart = ++start;
		area.selectionEnd = start;
		area.focus();
		changeEvent(area);
	});
}

// @ts-ignore
window.backslashListener = (inputID: string) => {
	document.getElementById(inputID)!.addEventListener('keyup', e => {
		const area = (e.target as HTMLElement).shadowRoot!.querySelector('input')!;
		if (area.value.indexOf('\\') >= 0) {
			const start = area.selectionStart;
			const end = area.selectionEnd;
			// @ts-ignore
			area.value = area.value.replaceAll('\\', 'λ');
			area.selectionStart = start;
			area.selectionEnd = end;
			changeEvent(area);
		}
	});
}
