function changeEvent(element: HTMLElement, inputID: string) {
	// notify Vaadin about the text value change
	// @ts-ignore
	document.getElementById(inputID)!.__dataValue.value = element.value;
	const evt = new Event("change", { bubbles: true });
	element.dispatchEvent(evt);
}

// @ts-ignore
window.buttonListener = (buttonID: string, inputID: string) => {
	// this function is called when the lambda button or the ∀ button is pressed
	// the requested character is inserted at the cursor position
	let replacement = (buttonID === "lambda-button") ? 'λ' : '∀';
	const button = document.getElementById(buttonID)!;
	const input = document.getElementById(inputID)!;
	button.addEventListener('click', () => {
		const area = input.shadowRoot!.querySelector('input')!;
		let start = area.selectionStart!;
		area.value = [area.value.slice(0, start), replacement, area.value.slice(start)].join('');
		area.selectionStart = ++start;
		area.selectionEnd = start;
		area.focus();
		changeEvent(area, inputID);
	});
}

// @ts-ignore
window.characterListener = (inputID: string) => {
	// called when the user types in one of the input fields (see InputBar.java)
	let toReplace = (inputID === "term-input-field") ? '\\' : '!';
	let replacement = (inputID === "term-input-field") ? 'λ' : '∀';
	document.getElementById(inputID)!.addEventListener('keyup', e => {
		const area = (e.target as HTMLElement).shadowRoot!.querySelector('input')!;
		if (area.value.indexOf(toReplace) >= 0) {
			const start = area.selectionStart;
			const end = area.selectionEnd;
			// @ts-ignore
			area.value = area.value.replaceAll(toReplace, replacement);
			area.selectionStart = start;
			area.selectionEnd = end;
			changeEvent(area, inputID);
		}
	});
}